package db.tables;

import db.Table;
import model.Assistenza;
import utils.TwoKeys;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AssistenzaTable implements Table<Assistenza, TwoKeys<Integer,Integer>> {

    public static final String TABLE_NAME = "assistenza";

    private final Connection connection;

    public AssistenzaTable(Connection connection) {
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
    public Optional<Assistenza> findByPrimaryKey(final TwoKeys<Integer, Integer> id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CodTecnico = ? AND CodVeterinario = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id.getX());
            statement.setInt(2, id.getY());
            // 4. Execute the query, this operation returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readAssistenzeFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Assistenza> readAssistenzeFromResultSet(ResultSet resultSet) {
        final List<Assistenza> assistenze = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final int idTec = resultSet.getInt("CodTecnico");
                final int idVet =  resultSet.getInt("CodVeterinario");
                // After retrieving all the data we create a Student object
                final Assistenza assistenza = new Assistenza(idTec, idVet);
                assistenze.add(assistenza);
            }
        } catch (final SQLException e) {}
        return assistenze;
    }

    @Override
    public List<Assistenza> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readAssistenzeFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Assistenza assistenza) {
        final String query = "INSERT INTO " + TABLE_NAME + "(CodTecnico, CodVeterinario) VALUES (?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, assistenza.getCodTec());
            statement.setInt(2, assistenza.getCodVet());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Assistenza assistenza) {
        return false;
    }

    @Override
    public boolean delete(final TwoKeys<Integer, Integer> id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE CodTecnico = ? AND CodVeterinario = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id.getX());
            statement.setInt(2, id.getY());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
