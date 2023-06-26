package db.tables;

import db.Table;
import model.Competenza;
import utils.TwoKeys;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompetenzaTable implements Table<Competenza, TwoKeys<String, Integer>> {

    public static final String TABLE_NAME = "competenza";

    private final Connection connection;

    public CompetenzaTable(Connection connection) {
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
    public Optional<Competenza> findByPrimaryKey(final TwoKeys<String, Integer> id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE AmbitoSpecializzazione = ? AND CodVeterinario = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1, id.getX());
            statement.setInt(2, id.getY());
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readCompetenzeFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Competenza> readCompetenzeFromResultSet(ResultSet resultSet) {
        final List<Competenza> competenze = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final String field = resultSet.getString("AmbitoSpecializzazione");
                final int idVet = resultSet.getInt("CodVeterinario");
                // After retrieving all the data we create a Student object
                final Competenza competenza = new Competenza(field, idVet);
                competenze.add(competenza);
            }
        } catch (final SQLException e) {}
        return competenze;
    }

    @Override
    public List<Competenza> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readCompetenzeFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Competenza competenza) {
        final String query = "INSERT INTO " + TABLE_NAME + "(AmbitoSpecializzazione, CodVeterinario) VALUES (?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, competenza.getField());
            statement.setInt(2, competenza.getCodVet());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Competenza competenza) {
        return false;
    }

    @Override
    public boolean delete(final TwoKeys<String, Integer> id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE AmbitoSpecializzazione = ? AND CodVeterinario = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, id.getX());
            statement.setInt(2, id.getY());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
