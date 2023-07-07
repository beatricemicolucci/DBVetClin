package db.tables;

import db.Table;
import model.Fattura;
import utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Date;

public class FatturaTable implements Table<Fattura, Integer> {

    public static final String TABLE_NAME = "fattura";

    private final Connection connection;

    public FatturaTable(Connection connection) {
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
    public Optional<Fattura> findByPrimaryKey(final Integer id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE NumeroFattura = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readFattureFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Fattura> readFattureFromResultSet(final ResultSet resultSet) {
        final List<Fattura> fatture = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final int id = resultSet.getInt("NumeroFattura");
                final Date date = Utils.sqlDateToDate(resultSet.getDate("DataFattura"));
                final Float price = resultSet.getFloat("Spesa");
                final String services = resultSet.getString("Servizi");
                final String cf = resultSet.getString("CF_Padrone");
                // After retrieving all the data we create a Student object
                final Fattura fattura = new Fattura(id, date, price, services, cf);
                fatture.add(fattura);
            }
        } catch (final SQLException e) {}
        return fatture;
    }

    @Override
    public List<Fattura> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readFattureFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Fattura fattura) {
        if (!ownerExists(fattura.getCfOwner())) {
            return false;
        }
        final String query = "INSERT INTO " + TABLE_NAME + "(NumeroFattura, DataFattura, Spesa, Servizi, CF_Padrone) VALUES (?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, fattura.getIdInvoice());
            statement.setDate(2, Utils.dateToSqlDate(fattura.getDate()));
            statement.setFloat(3, fattura.getPrice());
            statement.setString(4, fattura.getServices());
            statement.setString(5, fattura.getCfOwner());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean ownerExists(final String cf) {
        final PadroneTable padroneTable = new PadroneTable(connection);
        return padroneTable.findByPrimaryKey(cf).isPresent();
    }

    @Override
    public boolean update(final Fattura fattura) {
        final String query = "UPDATE " + TABLE_NAME + " SET " +
                "DataFattura = ?," +
                "Spesa = ?," +
                "Servizi = ?, " +
                "CF_PADRONE = ?" +
                "WHERE NumeroFattura = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, Utils.dateToSqlDate(fattura.getDate()));
            statement.setFloat(2, fattura.getPrice());
            statement.setString(3, fattura.getServices());
            statement.setString(4, fattura.getCfOwner());
            statement.setInt(5, fattura.getIdInvoice());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Integer id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE NumeroFattura = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
