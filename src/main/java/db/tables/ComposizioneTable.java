package db.tables;

import db.Table;
import model.Composizione;
import utils.TwoKeys;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComposizioneTable implements Table<Composizione, TwoKeys<Integer, Integer>> {

    public static final String TABLE_NAME = "composizione";

    private final Connection connection;

    public ComposizioneTable(Connection connection) {
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
    public Optional<Composizione> findByPrimaryKey(final TwoKeys<Integer, Integer> id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CodFarmaco = ? AND CodiceTerapia = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id.getX());
            statement.setInt(2, id.getY());
            // 4. Execute the query, this operation returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readComposizioniFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Composizione> readComposizioniFromResultSet(ResultSet resultSet) {
        final List<Composizione> composizioni = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final int idMed = resultSet.getInt("CodFarmaco");
                final int idTherapy = resultSet.getInt("CodTerapia");
                // After retrieving all the data we create a Student object
                final Composizione composizione = new Composizione(idMed, idTherapy);
                composizioni.add(composizione);
            }
        } catch (final SQLException e) {}
        return composizioni;
    }

    @Override
    public List<Composizione> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readComposizioniFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Composizione composizione) {
        final String query = "INSERT INTO " + TABLE_NAME + "(CodFarmaco, CodiceTerapia) VALUES (?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, composizione.getCodMed());
            statement.setInt(2, composizione.getCodTherapy());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(Composizione updatedValue) {
        return false;
    }

    @Override
    public boolean delete(final TwoKeys<Integer, Integer> id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE CodFarmaco = ? AND CodiceTerapia = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id.getX());
            statement.setInt(2, id.getY());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
