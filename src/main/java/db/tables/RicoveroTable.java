package db.tables;

import db.Table;
import model.Ricovero;
import utils.Utils;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Date;

public class RicoveroTable implements Table<Ricovero, Integer> {

    public static final String TABLE_NAME = "ricovero";

    private final Connection connection;

    public RicoveroTable(Connection connection) {
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
    public Optional<Ricovero> findByPrimaryKey(final Integer id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CodiceRicovero = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id);
            // 4. Execute the query, this operation returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readRicoveriFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Ricovero> readRicoveriFromResultSet(final ResultSet resultSet) {
        final List<Ricovero> ricoveri = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final Date startDay = Utils.sqlDateToDate(resultSet.getDate("DataInizio"));
                final Date endDay = Utils.sqlDateToDate(resultSet.getDate("DataFine"));
                final int id = resultSet.getInt("CodiceRicovero");
                final Date operationDay = Utils.sqlDateToDate(resultSet.getDate("GiornoIntervento"));
                final LocalTime operationTime = Utils.sqlTimeToTime(resultSet.getTime("OraInizioIntervento"));
                final int idOperatingRoom = resultSet.getInt("CodiceSalaInt");
                // After retrieving all the data we create a Student object
                final Ricovero ricovero = new Ricovero(startDay, endDay, id, operationDay, operationTime, idOperatingRoom);
                ricoveri.add(ricovero);
            }
        } catch (final SQLException e) {}
        return ricoveri;
    }

    @Override
    public List<Ricovero> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readRicoveriFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Ricovero ricovero) {
        final String query = "INSERT INTO " + TABLE_NAME + "(DataInizio, DataFine, CodiceRicovero, GiornoIntervento, OraInizioIntervento, CodiceSalaINt) VALUES (?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, Utils.dateToSqlDate(ricovero.getStartDate()));
            statement.setDate(2, Utils.dateToSqlDate(ricovero.getEndDate()));
            statement.setInt(3, ricovero.getIdHospitalization());
            statement.setDate(4, Utils.dateToSqlDate(ricovero.getOperationDay()));
            statement.setTime(5, Utils.timeToSqlTime(ricovero.getOperationTime()));
            statement.setInt(6, ricovero.getIdOperatingRoom());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override //DataInizio, DataFine, CodiceRicovero, GiornoIntervento, OraInizioIntervento, CodiceSalaInt
    public boolean update(final Ricovero ricovero) {
        final String query = "UPDATE " + TABLE_NAME + " SET " +
                "DataInizio = ?," +
                "DataFine = ?," +
                "GiornoIntervento = ?, " +
                "OraInizioIntervento = ?," +
                "CodiceSalaInt = ?" +
                "WHERE CodiceRicovero = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, Utils.dateToSqlDate(ricovero.getStartDate()));
            statement.setDate(2, Utils.dateToSqlDate(ricovero.getEndDate()));
            statement.setDate(3, Utils.dateToSqlDate(ricovero.getOperationDay()));
            statement.setTime(4, Utils.timeToSqlTime(ricovero.getOperationTime()));
            statement.setInt(5, ricovero.getIdOperatingRoom());
            statement.setInt(6, ricovero.getIdHospitalization());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Integer id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE CodiceRicovero = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
