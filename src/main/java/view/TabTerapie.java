package view;

import db.ConnectionProvider;
import db.tables.TerapiaTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Terapia;

import java.util.ArrayList;
import java.util.List;


public class TabTerapie {

    private ConnectionProvider connectionProvider;
    private TerapiaTable terapiaTable;
    private List<Terapia> therapiesList;

    @FXML
    private TextField idTherapyField;

    @FXML
    private TextField diseaseField;

    @FXML
    private TextField idVetField;

    @FXML
    private TextField idMedRecField;

    @FXML
    private TextField microchipField;

    @FXML
    private TextField diseaseShowField;

    @FXML
    private TableView therapiesTable;

    public void init() {
        connectionProvider = new ConnectionProvider();
        terapiaTable = new TerapiaTable(connectionProvider.getMySQLConnection());
        therapiesList = new ArrayList<>();
    }

    public void onInsertClick(final ActionEvent actionEvent) {
        int idTherapy = Integer.valueOf(idTherapyField.getText());
        String disease = diseaseField.getText();
        int idVet = Integer.valueOf(idVetField.getText());
        int idMedRecord = Integer.valueOf(idMedRecField.getText());

        Terapia terapia = new Terapia(idTherapy, disease, idVet, idMedRecord);
        terapiaTable.save(terapia);
        therapiesList = terapiaTable.findAll();
        therapiesTable.getItems().setAll(therapiesList);
    }

    public void onShowTherapiesClick(final ActionEvent actionEvent) {
        int microchip = Integer.valueOf(microchipField.getText());
        String disease = diseaseShowField.getText();

        therapiesList = terapiaTable.findTerapieByAnimaleAndMalattia(microchip, disease);
        therapiesTable.getItems().setAll(therapiesList);
    }
}
