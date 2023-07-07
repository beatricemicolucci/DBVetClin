package db.tables;

import db.Table;
import model.*;
import utils.Utils;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartellaClinicaTable implements Table<CartellaClinica, Integer> {

    public static final String TABLE_NAME = "cartella_clinica";

    private final Connection connection;

    public CartellaClinicaTable(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public boolean dropTable() {
        // 1. Create the statement from the open connection inside a try-with-resources
        try (final Statement statement = this.connection.createStatement()) {
            // 2. Execute the statement with the given query
            statement.executeUpdate("DROP TABLE " + TABLE_NAME);
            return true;
        } catch (final SQLException e) {
            // 3. Handle possible SQLExceptions
            return false;
        }
    }

    @Override
    public Optional<CartellaClinica> findByPrimaryKey(Integer id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CodiceCartella = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readCartelleFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<CartellaClinica> readCartelleFromResultSet(final ResultSet resultSet) {
        final List<CartellaClinica> cartelle = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final int id = resultSet.getInt("CodiceCartella");
                final int idAnimal = resultSet.getInt("CodCartella");
                final Date creationDate = (Date) Utils.sqlDateToDate(resultSet.getDate("DataCreazione"));
                // After retrieving all the data we create a Student object
                final CartellaClinica cartellaClinica = new CartellaClinica(id, idAnimal, creationDate);
                cartelle.add(cartellaClinica);
            }
        } catch (final SQLException e) {}
        return cartelle;
    }

    @Override
    public List<CartellaClinica> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readCartelleFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(CartellaClinica cartellaClinica) {
        if (this.animalExists(cartellaClinica.getCodAnimal())) {
            return false;
        }
        final String query = "INSERT INTO " + TABLE_NAME + "(CodiceCartella, CodAnimale, DataCreazione) VALUES (?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, cartellaClinica.getCodMedicalRec());
            statement.setInt(2, cartellaClinica.getCodAnimal());
            statement.setDate(3, Utils.dateToSqlDate(cartellaClinica.getCreationDate()));
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean animalExists(final int idAnimal) {
        final AnimaleTable animaleTable = new AnimaleTable(connection);
        return animaleTable.findByPrimaryKey(idAnimal).isPresent();
    }

    @Override
    public boolean update(CartellaClinica cartellaClinica) {
        final String query = "UPDATE " + TABLE_NAME + " SET " +
                "CodAnimale = ?," +
                "DataCreazione = ? " +
                "WHERE CodiceCartella = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, cartellaClinica.getCodAnimal());
            statement.setDate(2, Utils.dateToSqlDate(cartellaClinica.getCreationDate()));
            statement.setInt(3, cartellaClinica.getCodMedicalRec());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Integer id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE CodiceCartella = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Controllo> findControlliByAnimale(final int idAnimal) {
        final String query = "SELECT cc.CodiceCartella, c.* FROM " + TABLE_NAME + " AS cc " +
                "LEFT JOIN controllo AS c ON cc.CodiceCartella = c.CodiceCartella " +
                "WHERE cc.CodAnimale = ?";

        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, idAnimal);
            final ResultSet resultSet = statement.executeQuery();
            return readControlliFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Controllo> readControlliFromResultSet(ResultSet resultSet) {
        final List<Controllo> controlli = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final int idVet =  resultSet.getInt("CodVeterinario");
                final java.util.Date day = Utils.sqlDateToDate(resultSet.getDate("Giorno"));
                final LocalTime startTime = Utils.sqlTimeToTime(resultSet.getTime("OraInizio"));
                final int idInvoice = resultSet.getInt("NumeroFattura");
                final LocalTime endTime = Utils.sqlTimeToTime(resultSet.getTime("OraFine"));
                final int idMedRec = resultSet.getInt("CodiceCartella");
                // After retrieving all the data we create a Student object
                final Controllo controllo = new Controllo(idVet, day, startTime, idInvoice, endTime, idMedRec);
                controlli.add(controllo);
            }
        } catch (final SQLException e) {}
        return controlli;
    }

    public List<Intervento> findInterventiByAnimale(final int idAnimal) {
        final String query = "SELECT cc.CodiceCartella, i.* FROM " + TABLE_NAME + " AS cc " +
                "LEFT JOIN intervento AS i ON cc.CodiceCartella = i.CodiceCartella " +
                "WHERE cc.CodAnimale = ?";

        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, idAnimal);
            final ResultSet resultSet = statement.executeQuery();
            return readInterventiFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Intervento> readInterventiFromResultSet(ResultSet resultSet) {
        final List<Intervento> interventi = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final int idOperatingRoom =  resultSet.getInt("CodiceSala");
                final String type = resultSet.getString("Tipo");
                final java.util.Date day = Utils.sqlDateToDate(resultSet.getDate("Giorno"));
                final LocalTime startTime = Utils.sqlTimeToTime(resultSet.getTime("OraInizio"));
                final int idInvoice = resultSet.getInt("NumeroFattura");
                final LocalTime endTime = Utils.sqlTimeToTime(resultSet.getTime("OraFine"));
                final int idVet = resultSet.getInt("CodVeterinario");
                final int idMedRec = resultSet.getInt("CodiceCartella");
                // After retrieving all the data we create a Student object
                final Intervento intervento = new Intervento(idOperatingRoom, type, day, startTime, idInvoice, endTime, idVet, idMedRec);
                interventi.add(intervento);
            }
        } catch (final SQLException e) {}
        return interventi;
    }

    public List<Vaccinazione> findVaccinazioniByAnimale(final int idAnimal) {
        final String query = "SELECT cc.CodiceCartella, v.* FROM " + TABLE_NAME + " AS cc " +
                "LEFT JOIN vaccinazione AS v ON cc.CodiceCartella = v.CodiceCartella " +
                "WHERE cc.CodAnimale = ?";

        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, idAnimal);
            final ResultSet resultSet = statement.executeQuery();
            return readVaccinazioniFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Vaccinazione> readVaccinazioniFromResultSet(ResultSet resultSet) {
        final List<Vaccinazione> vaccinazioni = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final int idVet = resultSet.getInt("CodVeterinario");
                final java.util.Date day = Utils.sqlDateToDate(resultSet.getDate("Giorno"));
                final LocalTime time = Utils.sqlTimeToTime(resultSet.getTime("OraInizio"));
                final int idInvoice = resultSet.getInt("NumeroFattura");
                final LocalTime endTime= Utils.sqlTimeToTime(resultSet.getTime("OraFine"));
                final String disease = resultSet.getString("Malattia");
                final int idMedRecord = resultSet.getInt("CodiceCartella");
                // After retrieving all the data we create a Student object
                final Vaccinazione vaccinazione = new Vaccinazione(idVet, day, time, idInvoice, endTime, disease, idMedRecord);
            }
        } catch (final SQLException e) {}
        return vaccinazioni;
    }

    public List<Esame> findEsamiByAnimale(final int idAnimal) {
        final String query = "SELECT cc.CodiceCartella, e.* FROM " + TABLE_NAME + " AS cc " +
                "LEFT JOIN esame AS e ON cc.CodiceCartella = e.CodiceCartella " +
                "WHERE cc.CodAnimale = ?";

        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, idAnimal);
            final ResultSet resultSet = statement.executeQuery();
            return readEsamiFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Esame> readEsamiFromResultSet(ResultSet resultSet) {
        final List<Esame> esami = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final int id = resultSet.getInt("CodEsame");
                final int idInvoice = resultSet.getInt("NumeroFattura");
                final String type = resultSet.getString("Tipologia");
                final Date day = (Date) Utils.sqlDateToDate(resultSet.getDate("Giorno"));
                final LocalTime startTime = (LocalTime) Utils.sqlTimeToTime(resultSet.getTime("OraInizio"));
                final LocalTime endTime = (LocalTime) Utils.sqlTimeToTime(resultSet.getTime("OraFine"));
                final int idVet = resultSet.getInt("CodVeterinario");
                final int idMedRecord = resultSet.getInt("CodiceCartella");
                // After retrieving all the data we create a Student object
                final Esame esame = new Esame(id, idInvoice, type, day, startTime, endTime, idVet, idMedRecord);
                esami.add(esame);
            }
        } catch (final SQLException e) {}
        return esami;
    }

    public List<Intervento> showAnimalOperations(final int microchip) {
        final String query = "SELECT *" +
                "FROM " + TABLE_NAME +  " cc " +
                "LEFT JOIN intervento i ON cc.CodiceCartella = i.CodiceCartella " +
                "WHERE cc.CodAnimale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, microchip);
            final ResultSet resultSet = statement.executeQuery();
            return readInterventiFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public java.util.Date showNextVisit(final Integer microchip) {
        final String query = "SELECT MIN(Giorno) AS ProssimaVisita " +
                "FROM " +
                "(SELECT Giorno " +
                " FROM controllo" +
                " WHERE CodiceCartella IN (" +
                "      SELECT CodiceCartella" +
                "      FROM " + TABLE_NAME +
                "      WHERE CodAnimale = (SELECT CodAnimale FROM animale WHERE Microchip = ?)" +
                ")" +
                " UNION" +
                " SELECT Giorno" +
                " FROM intervento" +
                " WHERE CodiceCartella IN (" +
                "       SELECT CodiceCartella" +
                "       FROM " + TABLE_NAME +
                "       WHERE CodAnimale = (SELECT CodAnimale FROM animale WHERE Microchip = ?)" +
                " )" +
                " UNION" +
                " SELECT Giorno " +
                " FROM esame" +
                " WHERE CodiceCartella IN (" +
                "       SELECT CodiceCartella" +
                "       FROM " + TABLE_NAME +
                "       WHERE CodAnimale = (SELECT Microchip FROM animale WHERE Microchip = ?)" +
                " )" +
                " UNION" +
                " SELECT Giorno " +
                " FROM vaccinazione " +
                " WHERE CodiceCartella IN (" +
                "       SELECT CodiceCartella"+
                "       FROM " + TABLE_NAME +
                "        WHERE CodAnimale = (SELECT Microchip FROM animale WHERE Microchip = ?)" +
                " ) " +
                ") AS visite " +
                "WHERE visite.Giorno > CURDATE();";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, microchip);
            statement.setInt(2, microchip);
            statement.setInt(3, microchip);
            statement.setInt(4, microchip);
            final ResultSet resultSet = statement.executeQuery();
            return readNextVisit(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private java.util.Date readNextVisit(final ResultSet resultSet) {
        java.util.Date nextVisit = null;
        try {
            while (resultSet.next()) {
                nextVisit = Utils.sqlDateToDate(resultSet.getDate("ProssimaVisita"));
            }
        } catch (final SQLException e) {}
        return nextVisit;
    }


}
