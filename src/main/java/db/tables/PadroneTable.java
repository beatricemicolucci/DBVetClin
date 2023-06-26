package db.tables;

import db.Table;
import model.Padrone;
import utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PadroneTable implements Table<Padrone, String> {

    public static final String TABLE_NAME = "padrone";

    private final Connection connection;

    public PadroneTable(final Connection connection) {
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
    public Optional<Padrone> findByPrimaryKey(final String cf) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CF = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1, cf);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readPadroniFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Padrone> readPadroniFromResultSet(final ResultSet resultSet) {
        final List<Padrone> padroni = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final String cf = resultSet.getString("CF");
                final String firstName = resultSet.getString("Nome");
                final String lastName = resultSet.getString("Cognome");
                final Date birthdayDate = (Date) Utils.sqlDateToDate(resultSet.getDate("DataNascita"));
                final String address = resultSet.getString("Indirizzo");
                final String telephone = resultSet.getString("Telefono");
                final Optional<String> email = Optional.ofNullable(resultSet.getString("IndirizzoEmail"));
                // After retrieving all the data we create a Student object
                final Padrone padrone = new Padrone(cf, firstName, lastName, birthdayDate, address, telephone, email);
                padroni.add(padrone);
            }
        } catch (final SQLException e) {}
        return padroni;
    }

    @Override
    public List<Padrone> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readPadroniFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Padrone padrone) {
        final String query = "INSERT INTO " + TABLE_NAME + "(CF, Nome, Cognome, DataNascita, Indirizzo, Telefono, IndirizzoEmail) VALUES (?,?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, padrone.getCf());
            statement.setString(2, padrone.getName());
            statement.setString(3, padrone.getLastName());
            statement.setDate(4, Utils.dateToSqlDate(padrone.getBirthDate()));
            statement.setString(5, padrone.getAddress());
            statement.setString(6, padrone.getTelephone());
            statement.setString(7, padrone.getEmail().orElse(null));
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Padrone padrone) {
        final String query = "UPDATE " + TABLE_NAME + " SET " +
                        "Nome = ?," +
                        "Cognome = ?," +
                        "DataNascita = ?, " +
                        "Indirizzo = ?," +
                        "Telefono = ?," +
                        "IndirizzoEmail = ?" +
                        "WHERE CF = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, padrone.getName());
            statement.setString(2, padrone.getLastName());
            statement.setDate(3, Utils.dateToSqlDate(padrone.getBirthDate()));
            statement.setString(4, padrone.getAddress());
            statement.setString(5, padrone.getTelephone());
            statement.setString(6, padrone.getEmail().orElse(null));
            statement.setString(7, padrone.getCf());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final String cf) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE CF = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, cf);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
