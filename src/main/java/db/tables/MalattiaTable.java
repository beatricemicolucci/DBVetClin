package db.tables;

import db.Table;
import model.Malattia;
import utils.Utils;

import java.sql.*;
import java.time.LocalTime;
import java.util.*;
import java.util.Date;

public class MalattiaTable implements Table<Malattia, String> {

    public static final String TABLE_NAME = "malattia";

    private final Connection connection;

    public MalattiaTable(final Connection connection) {
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
    public Optional<Malattia> findByPrimaryKey(final String description) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Descrizione = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setString(1, description);
            // 4. Execute the query, this operation returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readMalattieFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Malattia> readMalattieFromResultSet(final ResultSet resultSet) {
        final List<Malattia> malattie = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final String description = resultSet.getString("Descrizione");
                final Optional<Integer> idVet =Optional.ofNullable(resultSet.getInt("CodVeterinario"));
                final Optional<Date> checkUpDay = Optional.ofNullable(resultSet.getDate("GiornoControllo"));
                final Optional<LocalTime> checkUpTime = Optional.ofNullable(Utils.sqlTimeToTime(resultSet.getTime("OraInizioControllo")));
                final Optional<Integer> idExam = Optional.ofNullable(resultSet.getInt("CodEsame"));
                // After retrieving all the data we create a Student object
                final Malattia malattia = new Malattia(description, idVet, checkUpDay, checkUpTime, idExam);
                malattie.add(malattia);
            }
        } catch (final SQLException e) {}
        return malattie;
    }

    @Override
    public List<Malattia> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readMalattieFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Malattia malattia) {
        final String query = "INSERT INTO " + TABLE_NAME + "(Descrizione, CodVeterinario, GiornoControllo, OraInizioControllo, CodEsame) VALUES (?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, malattia.getDescription());
            if (malattia.getIdVet().isEmpty()) {
                statement.setNull(2, Types.INTEGER);
            } else {
                statement.setInt(2, malattia.getIdVet().get());
            }
            statement.setDate(3, malattia.getCheckUpDay().map(Utils::dateToSqlDate).orElse(null));
            statement.setTime(4, malattia.getCheckUpTime().map(Utils::timeToSqlTime).orElse(null));
            if (malattia.getCodExam().isEmpty()) {
                statement.setNull(5, Types.INTEGER);
            } else {
                statement.setInt(5, malattia.getCodExam().get());
            }
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Malattia malattia) {
        final String query = "UPDATE " + TABLE_NAME + " SET " +
                "CodVeterinario = ?," +
                "GiornoControllo = ?," +
                "OraInizioControllo = ?, " +
                "CodEsame = ?" +
                "WHERE Descrizione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(5, malattia.getDescription());
            if (malattia.getIdVet().isEmpty()) {
                statement.setNull(1, Types.INTEGER);
            } else {
                statement.setInt(1, malattia.getIdVet().get());
            }
            statement.setDate(2, malattia.getCheckUpDay().map(Utils::dateToSqlDate).orElse(null));
            statement.setTime(3, malattia.getCheckUpTime().map(Utils::timeToSqlTime).orElse(null));
            if (malattia.getCodExam().isEmpty()) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(4, malattia.getCodExam().get());
            }
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final String description) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE Descrizione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, description);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
