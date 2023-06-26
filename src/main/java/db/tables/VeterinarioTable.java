package db.tables;

import db.Table;
import model.Veterinario;
import utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            // 4. Execute the query, this operations returns a ResultSet
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
                final Date birthdayDate = (Date) Utils.sqlDateToDate(resultSet.getDate("DataNascita"));
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
            statement.setString(8, veterinario.getEmail().orElse(null));
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
}
