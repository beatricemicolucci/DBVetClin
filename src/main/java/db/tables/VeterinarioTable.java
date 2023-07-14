package db.tables;

import db.Table;
import model.Specializzazione;
import model.Veterinario;
import model.Visita;
import utils.Utils;

import java.sql.*;
import java.sql.Date;
import java.time.LocalTime;
import java.util.*;

public class VeterinarioTable implements Table<Veterinario, Integer> {

    public static final String TABLE_NAME = "veterinario";

    private final Connection connection;

    public VeterinarioTable(Connection connection) {
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
    public Optional<Veterinario> findByPrimaryKey(final Integer id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CodImpiegato = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id);
            // 4. Execute the query, this operation returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readVeterinariFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Veterinario> readVeterinariFromResultSet(ResultSet resultSet) {
        final List<Veterinario> veterinari = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final int id = resultSet.getInt("CodImpiegato");
                final String office = resultSet.getString("Ambulatorio");
                final String cf = resultSet.getString("CF");
                final String firstName = resultSet.getString("Nome");
                final String lastName = resultSet.getString("Cognome");
                final java.util.Date birthdayDate = Utils.sqlDateToDate(resultSet.getDate("DataNascita"));
                final String address = resultSet.getString("Indirizzo");
                final String telephone = resultSet.getString("Telefono");
                final Optional<String> email = Optional.ofNullable(resultSet.getString("IndirizzoEmail"));
                // After retrieving all the data we create a Student object
                final Veterinario veterinario = new Veterinario(id, office, cf, firstName, lastName, birthdayDate, address, telephone, email);
                veterinari.add(veterinario);
            }
        } catch (final SQLException e) {}
        return veterinari;
    }

    @Override
    public List<Veterinario> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readVeterinariFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(Veterinario veterinario) {
        final String query = "INSERT INTO " + TABLE_NAME + "(CodImpiegato, Ambulatorio, CF, Nome, Cognome, DataNascita, Indirizzo, Telefono, IndirizzoEmail) VALUES (?,?,?,?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, veterinario.getId());
            statement.setString(2, veterinario.getOffice());
            statement.setString(3, veterinario.getCf());
            statement.setString(4, veterinario.getName());
            statement.setString(5, veterinario.getLastName());
            statement.setDate(6, Utils.dateToSqlDate(veterinario.getBirthDate()));
            statement.setString(7, veterinario.getAddress());
            statement.setString(8, veterinario.getTelephone());
            statement.setString(9, veterinario.getEmail().orElse(null));
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(Veterinario veterinario) {
        final String query = "UPDATE " + TABLE_NAME + " SET " +
                "Ambulatorio = ?," +
                "CF = ?," +
                "Nome = ?," +
                "Cognome = ?," +
                "DataNascita = ?, " +
                "Indirizzo = ?," +
                "Telefono = ?," +
                "IndirizzoEmail = ?" +
                "WHERE CodImpiegato = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, veterinario.getOffice());
            statement.setString(2, veterinario.getCf());
            statement.setString(3, veterinario.getName());
            statement.setString(4, veterinario.getLastName());
            statement.setDate(5, Utils.dateToSqlDate(veterinario.getBirthDate()));
            statement.setString(6, veterinario.getAddress());
            statement.setString(7, veterinario.getTelephone());
            statement.setString(8, veterinario.getEmail().orElse(null));
            statement.setInt(9, veterinario.getId());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Integer id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE CodImpiegato = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Visita> showVetVisits(final int idVet, final java.util.Date day) {
        final String query = "SELECT * " +
                "FROM (" +
                "    SELECT Giorno, OraInizio, OraFine, 'Intervento' AS TipoVisita " +
                "    FROM intervento " +
                "    WHERE Giorno = ? AND CodVeterinario = ? " +
                "    UNION ALL " +
                "    SELECT Giorno, OraInizio, OraFine, 'Esame' AS TipoVisita " +
                "    FROM esame " +
                "    WHERE Giorno = ? AND CodVeterinario = ? " +
                "    UNION ALL " +
                "    SELECT Giorno, OraInizio, OraFine, 'Vaccinazione' AS TipoVisita " +
                "    FROM vaccinazione " +
                "    WHERE Giorno = ? AND CodVeterinario = ? " +
                "    UNION ALL " +
                "    SELECT Giorno, OraInizio, OraFine, 'Controllo' AS TipoVisita " +
                "    FROM controllo " +
                "    WHERE Giorno = ? AND CodVeterinario = ? " +
                "    ) AS Visite " +
                " ORDER BY OraInizio;";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, Utils.dateToSqlDate(day));
            statement.setInt(2, idVet);
            statement.setDate(3, Utils.dateToSqlDate(day));
            statement.setInt(4, idVet);
            statement.setDate(5, Utils.dateToSqlDate(day));
            statement.setInt(6, idVet);
            statement.setDate(7, Utils.dateToSqlDate(day));
            statement.setInt(8, idVet);

            final ResultSet resultSet = statement.executeQuery();
            return readVisits(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Visita> readVisits(ResultSet resultSet) {
        List<Visita> visite = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final java.util.Date day = Utils.sqlDateToDate(resultSet.getDate("Giorno"));
                final LocalTime startTime = Utils.sqlTimeToTime(resultSet.getTime("oraInizio"));
                final LocalTime endTime = Utils.sqlTimeToTime(resultSet.getTime("oraFine"));
                final String type = resultSet.getString("TipoVisita");
                // After retrieving all the data we create a Student object
                final Visita visita = new Visita(day, startTime, endTime, type);
                visite.add(visita);
            }
        } catch (final SQLException e) {}
        return visite;
    }

    public List<Veterinario> getVetBySpecialization(final String specialization) {

        final String query = "SELECT v.*" +
                             "FROM " + TABLE_NAME + " v, specializzazione s, competenza c " +
                             "WHERE v.CodImpiegato = c.CodVeterinario " +
                             "AND s.Ambito = c.AmbitoSpecializzazione " +
                             "AND s.Ambito = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, specialization);
            final ResultSet resultSet = statement.executeQuery();
            return readVeterinariFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

}
