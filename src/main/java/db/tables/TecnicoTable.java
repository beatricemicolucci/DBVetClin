package db.tables;

import db.Table;
import model.Tecnico;
import utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TecnicoTable implements Table<Tecnico, Integer> {

    public static final String TABLE_NAME = "tecnico";

    private final Connection connection;

    public TecnicoTable(Connection connection) {
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
    public Optional<Tecnico> findByPrimaryKey(final Integer id) {
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
            return readTecniciFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Tecnico> readTecniciFromResultSet(ResultSet resultSet) {
        final List<Tecnico> tecnici = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final int id = resultSet.getInt("CodImpiegato");
                final String cf = resultSet.getString("CF");
                final String firstName = resultSet.getString("Nome");
                final String lastName = resultSet.getString("Cognome");
                final Date birthdayDate = (Date) Utils.sqlDateToDate(resultSet.getDate("DataNascita"));
                final String address = resultSet.getString("Indirizzo");
                final String telephone = resultSet.getString("Telefono");
                final Optional<String> email = Optional.ofNullable(resultSet.getString("IndirizzoEmail"));
                // After retrieving all the data we create a Student object
                final Tecnico tecnico = new Tecnico(id, cf, firstName, lastName, birthdayDate, address, telephone, email);
                tecnici.add(tecnico);
            }
        } catch (final SQLException e) {}
        return tecnici;
    }

    @Override
    public List<Tecnico> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readTecniciFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Tecnico tecnico) {
        final String query = "INSERT INTO " + TABLE_NAME + "(CodImpiegato, CF, Nome, Cognome, DataNascita, Indirizzo, Telefono, IndirizzoEmail) VALUES (?,?,?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, tecnico.getIdTec());
            statement.setString(2, tecnico.getCf());
            statement.setString(3, tecnico.getName());
            statement.setString(4, tecnico.getLastName());
            statement.setDate(5, Utils.dateToSqlDate(tecnico.getBirthDate()));
            statement.setString(6, tecnico.getAddress());
            statement.setString(7, tecnico.getTelephone());
            statement.setString(8, tecnico.getEmail().orElse(null));
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Tecnico tecnico) {
        final String query = "UPDATE " + TABLE_NAME + " SET " +
                "CF = ?," +
                "Nome = ?," +
                "Cognome = ?," +
                "DataNascita = ?, " +
                "Indirizzo = ?," +
                "Telefono = ?," +
                "IndirizzoEmail = ?" +
                "WHERE CodImpiegato = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, tecnico.getCf());
            statement.setString(2, tecnico.getName());
            statement.setString(3, tecnico.getLastName());
            statement.setDate(4, Utils.dateToSqlDate(tecnico.getBirthDate()));
            statement.setString(5, tecnico.getAddress());
            statement.setString(6, tecnico.getTelephone());
            statement.setString(7, tecnico.getEmail().orElse(null));
            statement.setInt(8, tecnico.getIdTec());
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
