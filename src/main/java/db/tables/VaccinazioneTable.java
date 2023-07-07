package db.tables;

import db.Table;
import model.Vaccinazione;
import utils.ThreeKeys;
import utils.Utils;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class VaccinazioneTable implements Table<Vaccinazione, ThreeKeys<Integer, Date, LocalTime>> {

    public static final String TABLE_NAME = "vaccinazione";

    private final Connection connection;

    public VaccinazioneTable(Connection connection) {
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
    public Optional<Vaccinazione> findByPrimaryKey(final ThreeKeys<Integer, Date, LocalTime> id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CodVeterinario = ? AND Giorno = ? AND OraInizio = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id.getX());
            statement.setDate(2, Utils.dateToSqlDate(id.getY()));
            statement.setTime(3, Utils.timeToSqlTime(id.getZ()));
            // 4. Execute the query, this operation returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readVaccinazioniFromResultSet(resultSet).stream().findFirst();
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
                final Date day = Utils.sqlDateToDate(resultSet.getDate("Giorno"));
                final LocalTime time = Utils.sqlTimeToTime(resultSet.getTime("OraInizio"));
                final int idInvoice = resultSet.getInt("NumeroFattura");
                final LocalTime endTime= Utils.sqlTimeToTime(resultSet.getTime("OraFine"));
                final String disease = resultSet.getString("Malattia");
                final int idMedRecord = resultSet.getInt("CodiceCartella");
                // After retrieving all the data we create a Student object
                final Vaccinazione vaccinazione = new Vaccinazione(idVet, day, time, idInvoice, endTime, disease, idMedRecord);
                vaccinazioni.add(vaccinazione);
            }
        } catch (final SQLException e) {}
        return vaccinazioni;
    }

    @Override
    public List<Vaccinazione> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readVaccinazioniFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Vaccinazione vaccinazione) {
        final String query = "INSERT INTO " + TABLE_NAME + "(CodVeterinario, Giorno, OraInizio, NumeroFattura, OraFine, Malattia, CodiceCartella) VALUES (?,?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, vaccinazione.getIdVet());
            statement.setDate(2, Utils.dateToSqlDate(vaccinazione.getDay()));
            statement.setTime(3, Utils.timeToSqlTime(vaccinazione.getStartTime()));
            statement.setInt(4, vaccinazione.getIdInvoice());
            statement.setTime(5, Utils.timeToSqlTime(vaccinazione.getEndTime()));
            statement.setString(6, vaccinazione.getDisease());
            statement.setInt(7, vaccinazione.getIdMedicalRecord());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Vaccinazione vaccinazione) {
        final String query = "UPDATE " + TABLE_NAME + " SET " +
                "NumeroFattura = ?," +
                "OraFine = ?," +
                "CodiceCartella = ?," +
                "Malattia = ?" +
                "WHERE CodVeterinario = ? AND Giorno = ? AND OraInizio = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, vaccinazione.getIdInvoice());
            statement.setTime(2, Utils.timeToSqlTime(vaccinazione.getEndTime()));
            statement.setInt(3, vaccinazione.getIdMedicalRecord());
            statement.setString(4, vaccinazione.getDisease());
            statement.setInt(5, vaccinazione.getIdVet());
            statement.setDate(6, Utils.dateToSqlDate(vaccinazione.getDay()));
            statement.setTime(7, Utils.timeToSqlTime(vaccinazione.getStartTime()));
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final ThreeKeys<Integer, Date, LocalTime> id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE CodVeterinario = ? AND Giorno = ? AND OraInizio = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id.getX());
            statement.setDate(2, Utils.dateToSqlDate(id.getY()));
            statement.setTime(3, Utils.timeToSqlTime(id.getZ()));
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
