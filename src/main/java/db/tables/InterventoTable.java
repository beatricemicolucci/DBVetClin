package db.tables;

import db.Table;
import model.Intervento;
import utils.ThreeKeys;
import utils.Utils;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class InterventoTable implements Table<Intervento, ThreeKeys<Date, LocalTime, Integer>> {

    public static final String TABLE_NAME = "intervento";

    private final Connection connection;

    public InterventoTable(Connection connection) {
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
    public Optional<Intervento> findByPrimaryKey(final ThreeKeys<Date, LocalTime, Integer> id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Giorno = ? AND OraInizio = ? AND CodiceSala = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setDate(1, Utils.dateToSqlDate(id.getX()));
            statement.setTime(2, Utils.timeToSqlTime(id.getY()));
            statement.setInt(3, id.getZ());
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readInterventiFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Intervento> readInterventiFromResultSet(ResultSet resultSet) {
        final List<Intervento> interventi = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final int idOperatingRoom =  resultSet.getInt("CodiceSala");
                final String type = resultSet.getString("Tipo");
                final Date day = Utils.sqlDateToDate(resultSet.getDate("Giorno"));
                final LocalTime startTime = Utils.sqlTimeToTime(resultSet.getTime("OraInizio"));
                final int idInvoice = resultSet.getInt("NumeroFattura");
                final LocalTime endTime = Utils.sqlTimeToTime(resultSet.getTime("OraFine"));
                final int idVet = resultSet.getInt("CodVeterinario");
                final int idMedRec = resultSet.getInt("CodiceCartella");
                // After retrieving all the data we create a Student object
                final Intervento intervento = new Intervento(idOperatingRoom, type, day, startTime, idInvoice, endTime, idVet, idMedRec);
                interventi.add(intervento);
            }
        } catch (final SQLException e) {}
        return interventi;
    }

    @Override
    public List<Intervento> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readInterventiFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Intervento intervento) {
        final String query = "INSERT INTO " + TABLE_NAME + "(CodiceSala, Tipo, Giorno, OraInizio, NumeroFattura, OraFine, CodVeterinario, CodiceCartella) VALUES (?,?,?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, intervento.getOperatingRoom());
            statement.setString(2, intervento.getType());
            statement.setDate(3, Utils.dateToSqlDate(intervento.getDate()));
            statement.setTime(4, Utils.timeToSqlTime(intervento.getStartTime()));
            statement.setInt(5, intervento.getIdInvoice());
            statement.setTime(6, Utils.timeToSqlTime(intervento.getEndTime()));
            statement.setInt(7, intervento.getCodVet());
            statement.setInt(8, intervento.getCodMedicalRecord());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Intervento intervento) {
        final String query = "UPDATE " + TABLE_NAME + " SET " +
                "Tipo = ?," +
                "NumeroFattura = ?," +
                "OraFine = ?," +
                "CodiceCartella = ?," +
                "CodVeterinario = ?" +
                "WHERE CodiceSala = ? AND Giorno = ? AND OraInizio = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, intervento.getType());
            statement.setInt(2, intervento.getIdInvoice());
            statement.setTime(3, Utils.timeToSqlTime(intervento.getEndTime()));
            statement.setInt(4, intervento.getCodMedicalRecord());
            statement.setInt(5, intervento.getCodVet());
            statement.setInt(6, intervento.getOperatingRoom());
            statement.setDate(7, Utils.dateToSqlDate(intervento.getDate()));
            statement.setTime(8, Utils.timeToSqlTime(intervento.getStartTime()));
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final ThreeKeys<Date, LocalTime, Integer> id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE CodiceSala = ? AND Giorno = ? AND OraInizio = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id.getZ());
            statement.setDate(2, Utils.dateToSqlDate(id.getX()));
            statement.setTime(3, Utils.timeToSqlTime(id.getY()));
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

}
