package db.tables;

import db.Table;
import model.Animale;
import model.CartellaClinica;
import model.Esame;
import utils.Utils;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class EsameTable implements Table<Esame, Integer> {

    public static final String TABLE_NAME = "Esame";

    private final Connection connection;

    public EsameTable(Connection connection) {
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
    public Optional<Esame> findByPrimaryKey(final Integer id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CodEsame = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readEsamiFromResultSet(resultSet).stream().findFirst();
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

    @Override
    public List<Esame> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readEsamiFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(Esame esame) {
        if (!vetAndMedRecordExist(esame.getCodVet(), esame.getCodMedicalRecord())) {
            return false;
        }
        final String query = "INSERT INTO " + TABLE_NAME + "(CodEsame, NumeroFattura, Tipologia, Giorno, OraInizio, OraFine, CodVeterinario, CodCartella) VALUES (?,?,?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, esame.getCodExam());
            statement.setInt(2, esame.getCodMedicalRecord());
            statement.setString(3, esame.getType());
            statement.setDate(4, Utils.dateToSqlDate(esame.getDay()));
            statement.setTime(5, Utils.timeToSqlTime(esame.getStartTime()));
            statement.setTime(6, Utils.timeToSqlTime(esame.getEndTime()));
            statement.setInt(7, esame.getCodVet());
            statement.setInt(8, esame.getCodMedicalRecord());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean vetAndMedRecordExist(final int idVet, final int idMedRecord) {
        VeterinarioTable vet = new VeterinarioTable(connection);
        CartellaClinicaTable medRecord = new CartellaClinicaTable(connection);
        return (medRecord.findByPrimaryKey(idMedRecord).isPresent() && vet.findByPrimaryKey(idVet).isPresent());
    }

    @Override
    public boolean update(final Esame esame) {
        final String query = "UPDATE " + TABLE_NAME + " SET " +
                "NumeroFattura = ?," +
                "Tipologia = ?," +
                "Giorno = ?, " +
                "OraInizio = ?," +
                "OraFine = ?," +
                "CodVeterinario = ?," +
                "CodCartella = ?" +
                "WHERE CodEsame = ?";
        if (!vetAndMedRecordExist(esame.getCodVet(), esame.getCodMedicalRecord())) {
            return false;
        }
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, esame.getIdInvoice());
            statement.setString(2, esame.getType());
            statement.setDate(3, Utils.dateToSqlDate(esame.getDay()));
            statement.setTime(4, Utils.timeToSqlTime(esame.getStartTime()));
            statement.setTime(5, Utils.timeToSqlTime(esame.getEndTime()));
            statement.setInt(6, esame.getCodVet());
            statement.setInt(7, esame.getCodMedicalRecord());
            statement.setInt(8, esame.getCodExam());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Integer id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE CodEsame = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
