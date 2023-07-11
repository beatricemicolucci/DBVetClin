package view;

import db.ConnectionProvider;
import db.tables.CartellaClinicaTable;
import db.tables.TerapiaTable;
import db.tables.VeterinarioTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Terapia;


public class TabTerapie extends TabController {

    private TerapiaTable terapiaTable;
    private VeterinarioTable veterinarioTable;
    private CartellaClinicaTable cartellaClinicaTable;
    private ObservableList<Terapia> therapiesList;

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
    private TableView<Terapia> therapiesTable;

    @FXML
    private TableColumn<Terapia, Integer> idTherapyCol;

    @FXML
    private TableColumn<Terapia, String> diseaseCol;

    @FXML
    private TableColumn<Terapia, Integer> idVetCol;

    @FXML
    private TableColumn<Terapia, Integer> idMedRecCol;

    public void init() {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        terapiaTable = new TerapiaTable(connectionProvider.getMySQLConnection());
        veterinarioTable = new VeterinarioTable(connectionProvider.getMySQLConnection());
        cartellaClinicaTable = new CartellaClinicaTable(connectionProvider.getMySQLConnection());

        idTherapyCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        diseaseCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        idVetCol.setCellValueFactory(new PropertyValueFactory<>("idVet"));
        idMedRecCol.setCellValueFactory(new PropertyValueFactory<>("idMedicalRecord"));
        therapiesList = FXCollections.observableArrayList();
    }

    public void onInsertClick(final ActionEvent actionEvent) {

        if (idTherapyField.getText().isEmpty() || diseaseField.getText().isEmpty() || idVetField.getText().isEmpty() || idMedRecField.getText().isEmpty()) {
            showPopUp("Inserisci tutti i campi!", null, Alert.AlertType.WARNING);
        } else {
            int idTherapy = Integer.parseInt(idTherapyField.getText());
            String disease = diseaseField.getText();
            int idVet = Integer.parseInt(idVetField.getText());
            int idMedRecord = Integer.parseInt(idMedRecField.getText());

            if (veterinarioTable.findByPrimaryKey(idVet).isEmpty()) {
                showPopUp("Veterinario non esistente!", null, Alert.AlertType.WARNING);
            } else if (cartellaClinicaTable.findByPrimaryKey(idMedRecord).isEmpty()) {
                showPopUp("Cartella clinica non esistente!", null, Alert.AlertType.WARNING);
            } else {
                Terapia terapia = new Terapia(idTherapy, disease, idVet, idMedRecord);
                terapiaTable.save(terapia);
                therapiesList = FXCollections.observableArrayList(terapiaTable.findAll());
                therapiesTable.getItems().setAll(therapiesList);
            }
        }

    }

    public void onShowTherapiesClick(final ActionEvent actionEvent) {

        if (microchipField.getText().isEmpty()) {
            showPopUp("Inserisci tutti i campi!", null, Alert.AlertType.WARNING);
        } else {
            int microchip = Integer.parseInt(microchipField.getText());
            String disease = diseaseShowField.getText();

            therapiesList = FXCollections.observableArrayList(terapiaTable.findTerapieByAnimaleAndMalattia(microchip, disease));
            therapiesTable.getItems().setAll(therapiesList);

        }
    }

}
