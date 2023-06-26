package db.tables;

import db.Table;
import model.AllergieCartella;
import utils.TwoKeys;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AllergieCartellaTable implements Table<AllergieCartella, TwoKeys<Integer, Integer>> {

    public static final String TABLE_NAME = "allergie_cartella";

    private final Connection connection;

    public AllergieCartellaTable(Connection connection) {
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
    public Optional<AllergieCartella> findByPrimaryKey(final TwoKeys<Integer, Integer> id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CodiceCartella = ? AND IdAllergia = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id.getX());
            statement.setInt(2, id.getY());
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readAllergieCartelleFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<AllergieCartella> readAllergieCartelleFromResultSet(ResultSet resultSet) {
        final List<AllergieCartella> allergieCartelle = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final int idMedRec = resultSet.getInt("CodiceCartella");
                final int idAllergy =  resultSet.getInt("IdAllergia");
                // After retrieving all the data we create a Student object
                final AllergieCartella allergiaCartella = new AllergieCartella(idMedRec, idAllergy);
                allergieCartelle.add(allergiaCartella);
            }
        } catch (final SQLException e) {}
        return allergieCartelle;
    }

    @Override
    public List<AllergieCartella> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readAllergieCartelleFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final AllergieCartella allergia) {
        final String query = "INSERT INTO " + TABLE_NAME + "(CodiceCartella, IdAllergia) VALUES (?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, allergia.getIdMedRecord());
            statement.setInt(2, allergia.getIdAllergy());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(AllergieCartella updatedValue) {
        return false;
    }

    @Override
    public boolean delete(final TwoKeys<Integer, Integer> id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE CodiceCartella = ? AND IdAllergia = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id.getX());
            statement.setInt(2, id.getY());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
