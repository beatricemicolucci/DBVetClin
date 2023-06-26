package db.tables;

import db.Table;
import model.Assistenza;
import model.Controllo;
import utils.ThreeKeys;
import utils.Utils;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ControlloTable implements Table<Controllo, ThreeKeys<Integer, Date, LocalTime>> {

    public static final String TABLE_NAME = "assistenza";

    private final Connection connection;

    public ControlloTable(Connection connection) {
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
    public Optional<Controllo> findByPrimaryKey(ThreeKeys<Integer, Date, LocalTime> id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CodVeterinario = ? AND Giorno = ? AND OraInizio = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id.getX());
            statement.setDate(2, Utils.dateToSqlDate(id.getY()));
            statement.setTime(3, Utils.timeToSqlTime(id.getZ()));
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readControlliFromResultSet(resultSet).stream().findFirst();
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
                final Date day = Utils.sqlDateToDate(resultSet.getDate("Giorno"));
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

    @Override
    public List<Controllo> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readControlliFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Controllo controllo) {
        final String query = "INSERT INTO " + TABLE_NAME + "(CodVeterinario, Giorno, OraInizio, NumeroFattura, OraFine, CodiceCartella) VALUES (?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, controllo.getCodVet());
            statement.setDate(2, Utils.dateToSqlDate(controllo.getDay()));
            statement.setTime(3, Utils.timeToSqlTime(controllo.getStartTime()));
            statement.setInt(4, controllo.getIdInvoice());
            statement.setTime(5, Utils.timeToSqlTime(controllo.getEndTime()));
            statement.setInt(6, controllo.getCodMedicalRecord());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Controllo controllo) {
        final String query = "UPDATE " + TABLE_NAME + " SET " +
                "NumeroFattura = ?," +
                "OraFine = ?," +
                "CodiceCartella = ?" +
                "WHERE CodVeterinario = ? AND Giorno = ? AND OraInizio = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, controllo.getIdInvoice());
            statement.setTime(2, Utils.timeToSqlTime(controllo.getEndTime()));
            statement.setInt(3, controllo.getCodMedicalRecord());
            statement.setInt(4, controllo.getCodVet());
            statement.setDate(5, Utils.dateToSqlDate(controllo.getDay()));
            statement.setTime(6, Utils.timeToSqlTime(controllo.getStartTime()));
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
