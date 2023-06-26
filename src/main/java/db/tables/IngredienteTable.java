package db.tables;

import db.Table;
import model.Ingrediente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IngredienteTable implements Table<Ingrediente, String> {

    public static final String TABLE_NAME = "ingrediente";

    private final Connection connection;

    public IngredienteTable(Connection connection) {
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
    public Optional<Ingrediente> findByPrimaryKey(final String name) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE NomeScientifico = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1, name);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readIngredientiFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Ingrediente> readIngredientiFromResultSet(final ResultSet resultSet) {
        final List<Ingrediente> ingredienti = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final String name = resultSet.getString("NomeScientifico");
                final int idMed = resultSet.getInt("CodFarmaco");
                // After retrieving all the data we create a Student object
                final Ingrediente ingrediente = new Ingrediente(name, idMed);
                ingredienti.add(ingrediente);
            }
        } catch (final SQLException e) {}
        return ingredienti;
    }

    @Override
    public List<Ingrediente> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readIngredientiFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Ingrediente ingrediente) {
        final String query = "INSERT INTO " + TABLE_NAME + "(NomeScientifico, CodFarmaco) VALUES (?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, ingrediente.getName());
            statement.setInt(2, ingrediente.getIdMedicine());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Ingrediente ingrediente) {
        return false;
    }

    @Override
    public boolean delete(final String name) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE NomeScientifico = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, name);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
