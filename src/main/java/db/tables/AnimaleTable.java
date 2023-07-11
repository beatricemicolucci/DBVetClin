package db.tables;

import db.Table;
import model.Animale;
import utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Date;

public class AnimaleTable implements Table<Animale, Integer> {

    public static final String TABLE_NAME = "animale";

    private final Connection connection;

    public AnimaleTable(Connection connection) {
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
    public Optional<Animale> findByPrimaryKey(final Integer microchip) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Microchip = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, microchip);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readAnimaliFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Animale> readAnimaliFromResultSet(final ResultSet resultSet) {
        final List<Animale> animali = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final int microchip = resultSet.getInt("Microchip");
                final String name = resultSet.getString("Nome");
                final String race = resultSet.getString("Razza");
                final Date birthdayDate = (Date) Utils.sqlDateToDate(resultSet.getDate("DataNascita"));
                final String gender = resultSet.getString("Sesso");
                final String cfOwner = resultSet.getString("CF_Padrone");
                // After retrieving all the data we create a Student object
                final Animale animale = new Animale(microchip, name, race, birthdayDate, gender, cfOwner);
                animali.add(animale);
            }
        } catch (final SQLException e) {}
        return animali;
    }

    @Override
    public List<Animale> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readAnimaliFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(Animale animale) {
        if (this.ownerExists(animale.getCfOwner())) {
            return false;
        }
        final String query = "INSERT INTO " + TABLE_NAME + "(Microchip, Nome, Razza, DataNascita, Sesso, CF_PADRONE) VALUES (?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, animale.getMicrochip());
            statement.setString(2, animale.getName());
            statement.setString(3, animale.getRace());
            statement.setDate(4, Utils.dateToSqlDate(animale.getBirthDate()));
            statement.setString(5, animale.getGender());
            statement.setString(6, animale.getCfOwner());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean ownerExists(final String cf) {
        final PadroneTable padroneTable = new PadroneTable(connection);
        return padroneTable.findByPrimaryKey(cf).isPresent();
    }

    @Override
    public boolean update(Animale animale) {
        final String query = "UPDATE " + TABLE_NAME + " SET " +
                "Nome = ?," +
                "Razza = ?," +
                "DataNascita = ?, " +
                "Sesso = ?," +
                "CF_PADRONE = ?" +
                "WHERE Microchip = ?";
        if (this.ownerExists(animale.getCfOwner())) {
            return false;
        }
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, animale.getName());
            statement.setString(2, animale.getRace());
            statement.setDate(3, Utils.dateToSqlDate(animale.getBirthDate()));
            statement.setString(4, animale.getGender());
            statement.setString(5, animale.getCfOwner());
            statement.setInt(6, animale.getMicrochip());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Integer microchip) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE Microchip = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, microchip);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Animale> showTopTen() {
        final String query = "SELECT a.Microchip, a.Nome, COUNT(*) AS NumeroVisite " +
                "FROM " + TABLE_NAME + " a " +
                "JOIN cartella_clinica cc ON cc.CodAnimale = a.Microchip " +
                "JOIN (" +
                "  SELECT CodiceCartella FROM controllo " +
                "  UNION ALL" +
                "  SELECT CodiceCartella FROM intervento " +
                "  UNION ALL" +
                "  SELECT CodiceCartella FROM vaccinazione " +
                "  UNION ALL" +
                "  SELECT CodiceCartella FROM esame " +
                ") AS v ON v.CodiceCartella = cc.CodiceCartella " +
                "GROUP BY a.Microchip, a.Nome " +
                "ORDER BY NumeroVisite DESC " +
                "LIMIT 10";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            final ResultSet resultSet = statement.executeQuery();
            return readAnimaliFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
