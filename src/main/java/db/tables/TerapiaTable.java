package db.tables;

import db.Table;
import model.Terapia;


import java.sql.*;
import java.util.ArrayList;;
import java.util.List;
import java.util.Optional;

public class TerapiaTable implements Table<Terapia, Integer> {

    public static final String TABLE_NAME = "terapia";

    private final Connection connection;

    public TerapiaTable(Connection connection) {
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
    public Optional<Terapia> findByPrimaryKey(final Integer id) {
        // 1. Define the query with the "?" placeholder(s)
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE CodiceTerapia = ?";
        // 2. Prepare a statement inside a try-with-resources
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            // 3. Fill in the "?" with actual data
            statement.setInt(1, id);
            // 4. Execute the query, this operations returns a ResultSet
            final ResultSet resultSet = statement.executeQuery();
            // 5. Do something with the result of the query execution;
            //    here we extract the first (and only) student from the ResultSet
            return readTerapieFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Terapia> readTerapieFromResultSet(ResultSet resultSet) {
        final List<Terapia> terapie = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get method
                final int id = resultSet.getInt("CodiceTerapia");
                final String disease = resultSet.getString("DescrizioneMalattia");
                final int idVet = resultSet.getInt("CodVeterinario");
                final int idMedRecord = resultSet.getInt("CodiceCartella");
                // After retrieving all the data we create a Student object
                final Terapia terapia = new Terapia(id, disease, idVet, idMedRecord);
                terapie.add(terapia);
            }
        } catch (final SQLException e) {}
        return terapie;
    }

    @Override
    public List<Terapia> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readTerapieFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Terapia terapia) {
        if (!vetAndMedRecExist(terapia.getIdVet(), terapia.getIdMedicalRecord())) {
            return false;
        }
        final String query = "INSERT INTO " + TABLE_NAME + "(CodiceTerapia, DescrizioneMalattia, CodVeterinario, CodiceCartella) VALUES (?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, terapia.getId());
            statement.setString(2, terapia.getDescription());
            statement.setInt(3, terapia.getIdVet());
            statement.setInt(4, terapia.getIdMedicalRecord());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean vetAndMedRecExist(final int idVet, final int idMedRec) {
        VeterinarioTable vet=  new VeterinarioTable(connection);
        CartellaClinicaTable medRec= new CartellaClinicaTable(connection);
        return vet.findByPrimaryKey(idVet).isPresent() && medRec.findByPrimaryKey(idMedRec).isPresent();

    }

    @Override
    public boolean update(final Terapia terapia) {
        return false;
    }

    @Override
    public boolean delete(final Integer id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE CodiceTerapia = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Terapia> findTerapieByAnimaleAndMalattia(int microchip, String descrizioneMalattia) {
        String query = "SELECT t.* " +
                "FROM animale a " +
                "JOIN cartella_clinica cc ON a.Microchip = cc.CodAnimale " +
                "JOIN terapia t ON cc.CodiceCartella = t.CodiceCartella " +
                "JOIN malattia m ON t.DescrizioneMalattia = m.Descrizione " +
                "WHERE a.Microchip = ? " +
                "AND m.Descrizione = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, microchip);
            statement.setString(2, descrizioneMalattia);

            ResultSet resultSet = statement.executeQuery();
            return readTerapieFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
