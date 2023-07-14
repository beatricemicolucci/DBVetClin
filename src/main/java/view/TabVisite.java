package view;

import db.ConnectionProvider;
import db.tables.VeterinarioTable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Visita;
import utils.Utils;

import java.time.LocalTime;
import java.util.Date;

public class TabVisite extends TabController {

    private VeterinarioTable veterinarioTable;
    private ObservableList<Visita> visitsList;

    @FXML
    private TextField idVetVisits;

    @FXML
    private DatePicker dateVisits;

    @FXML
    private TableView<Visita> visitsTable;

    @FXML
    private TableColumn<Visita, java.sql.Date> dateCol;

    @FXML
    private TableColumn<Visita, LocalTime> startTimeCol;

    @FXML
    private TableColumn<Visita, LocalTime> endTimeCol;

    @FXML
    private TableColumn<Visita, String> typeCol;

    public void init() {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        veterinarioTable = new VeterinarioTable(connectionProvider.getMySQLConnection());

        dateCol.setCellValueFactory(cellData -> {
            Visita visita = cellData.getValue();
            Date date = visita.getDay();
            return new SimpleObjectProperty<>(Utils.dateToSqlDate(date));
        });
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        visitsList = FXCollections.observableArrayList();
    }

    public void onShowVetVisitsClick(final ActionEvent actionEvent) {

        if (idVetVisits.getText().isEmpty() || dateVisits.getValue() == null) {
            showPopUp("Inserisci tutti i campi!", null, Alert.AlertType.WARNING);
        } else {
            final int idVet = Integer.parseInt(idVetVisits.getText());
            final Date date = Utils.buildDate(dateVisits.getValue().getDayOfMonth(), dateVisits.getValue().getMonthValue(), dateVisits.getValue().getYear()).get();
            if (veterinarioTable.findByPrimaryKey(idVet).isEmpty()) {
                showPopUp("Veterinario non esistente!", null, Alert.AlertType.WARNING);
            } else {
                visitsList = FXCollections.observableArrayList(veterinarioTable.showVetVisits(idVet, date));
                if (visitsList.isEmpty()) {
                    showPopUp("Nessuna visita!", null, Alert.AlertType.INFORMATION);
                }
                visitsTable.getItems().setAll(visitsList);
            }
        }
    }

}
