package view;

import db.ConnectionProvider;
import db.tables.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Fattura;
import model.Intervento;
import model.SalaOperatoria;
import utils.ThreeKeys;
import utils.Utils;

import java.time.LocalTime;
import java.util.Date;


public class TabInterventi extends TabController {

    private InterventoTable interventoTable;
    private CartellaClinicaTable cartellaClinicaTable;
    private FatturaTable fatturaTable;
    private PadroneTable padroneTable;
    private SalaOperatoriaTable salaOperatoriaTable;
    private AnimaleTable animaleTable;
    private ObservableList<Intervento> operationsList;

    @FXML
    private TextField operatingRoomInsert;

    @FXML
    private TextField typeInsert;

    @FXML
    private DatePicker dayInsert;

    @FXML
    private TextField invoiceOperation;

    @FXML
    private TextField medRecordInsert;

    @FXML
    private TextField vetInsert;

    @FXML
    private Spinner<Integer> startHour;

    @FXML
    private Spinner<Integer> startMin;

    @FXML
    private Spinner<Integer> endHour;

    @FXML
    private Spinner<Integer> endMin;

    @FXML
    private TextField microchipOperation;

    @FXML
    private DatePicker dateInvoice;

    @FXML
    private TextField amountInsert;

    @FXML
    private TextField servicesInsert;

    @FXML
    private TextField cfInvoice;

    @FXML
    private TableView<Intervento> operationsTable;

    @FXML
    private TableColumn<Intervento, Integer> operatingRoomCol;

    @FXML
    private TableColumn<Intervento, String> typeCol;

    @FXML
    private TableColumn<Intervento, Date> dateCol;

    @FXML
    private TableColumn<Intervento, Integer> invoiceCol;

    @FXML
    private TableColumn<Intervento, Integer> medRecordCol;

    @FXML
    private TableColumn<Intervento, Integer> vetCol;

    @FXML
    private TableColumn<Intervento, LocalTime> startTimeCol;

    @FXML
    private TableColumn<Intervento, LocalTime> endTimeCol;

    public void init() {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        interventoTable = new InterventoTable(connectionProvider.getMySQLConnection());
        cartellaClinicaTable = new CartellaClinicaTable(connectionProvider.getMySQLConnection());
        fatturaTable = new FatturaTable(connectionProvider.getMySQLConnection());
        padroneTable = new PadroneTable(connectionProvider.getMySQLConnection());
        salaOperatoriaTable = new SalaOperatoriaTable(connectionProvider.getMySQLConnection());
        animaleTable = new AnimaleTable(connectionProvider.getMySQLConnection());

        SpinnerValueFactory<Integer> valueStartHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24);
        SpinnerValueFactory<Integer> valueEndHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24);
        SpinnerValueFactory<Integer> valueStartMinutesFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        SpinnerValueFactory<Integer> valueEndMinutesFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        valueStartHourFactory.setValue(0);
        valueStartMinutesFactory.setValue(0);
        valueEndHourFactory.setValue(0);
        valueEndMinutesFactory.setValue(0);
        startHour.setValueFactory(valueStartHourFactory);
        startMin.setValueFactory(valueStartMinutesFactory);
        endHour.setValueFactory(valueEndHourFactory);
        endMin.setValueFactory(valueEndMinutesFactory);

        operationsList = FXCollections.observableArrayList();
        operatingRoomCol.setCellValueFactory(new PropertyValueFactory<>("operatingRoom"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateCol.setCellValueFactory(cellData -> {
            Intervento intervento = cellData.getValue();
            Date date = intervento.getDate();
            return new SimpleObjectProperty<>(Utils.dateToSqlDate(date));
        });
        invoiceCol.setCellValueFactory(new PropertyValueFactory<>("idInvoice"));
        medRecordCol.setCellValueFactory(new PropertyValueFactory<>("codMedicalRecord"));
        vetCol.setCellValueFactory(new PropertyValueFactory<>("codVet"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        operationsList = FXCollections.observableArrayList(interventoTable.findAll());
        operationsTable.getItems().setAll(operationsList);

    }

    public void onInsertOperationClick(final ActionEvent e) {

        String type = typeInsert.getText();
        LocalTime startTime = LocalTime.of(startHour.getValue(), startMin.getValue());
        LocalTime endTime = LocalTime.of(endHour.getValue(), endMin.getValue());
        String services = servicesInsert.getText();
        String cf = cfInvoice.getText();

        if (operatingRoomInsert.getText().isEmpty() || type.isEmpty() || invoiceOperation.getText().isEmpty() || medRecordInsert.getText().isEmpty()
            || vetInsert.getText().isEmpty() || amountInsert.getText().isEmpty() || services.isEmpty() || cf.isEmpty() || dayInsert.getValue() == null
            || dateInvoice.getValue() == null) {
            showPopUp("Inserisci tutti i campi!", null, Alert.AlertType.WARNING);
        } else {
            int operationRoom = Integer.parseInt(operatingRoomInsert.getText());
            int invoice = Integer.parseInt(invoiceOperation.getText());
            int medRecord = Integer.parseInt(medRecordInsert.getText());
            int vet = Integer.parseInt(vetInsert.getText());
            float amount = Float.parseFloat(amountInsert.getText());
            Date operationDate = Utils.buildDate(dayInsert.getValue().getDayOfMonth(), dayInsert.getValue().getMonthValue(), dayInsert.getValue().getYear()).get();
            Date invoiceDate = Utils.buildDate(dateInvoice.getValue().getDayOfMonth(), dateInvoice.getValue().getMonthValue(), dateInvoice.getValue().getYear()).get();

            if (fatturaTable.findByPrimaryKey(invoice).isPresent()) {
                showPopUp("Fattura già registrata", null, Alert.AlertType.WARNING);
            } else {
                if (interventoTable.findByPrimaryKey(new ThreeKeys<>(operationDate, startTime, operationRoom)).isPresent()) {
                    showPopUp("Intervento già registrato!", null, Alert.AlertType.WARNING);
                } else {
                    if (padroneTable.findByPrimaryKey(cf).isEmpty()) {
                        showPopUp("Padrone non esistente!", null, Alert.AlertType.WARNING);
                    } else if (cartellaClinicaTable.findByPrimaryKey(medRecord).isEmpty()) {
                        showPopUp("Cartella Clinica non esistente!", null, Alert.AlertType.WARNING);
                    } else {
                        if (salaOperatoriaTable.findByPrimaryKey(operationRoom).isEmpty()) {
                            SalaOperatoria salaOperatoria = new SalaOperatoria(operationRoom);
                            salaOperatoriaTable.save(salaOperatoria);
                        }
                        Fattura fattura = new Fattura(invoice, invoiceDate, amount, services, cf);
                        fatturaTable.save(fattura);
                        Intervento intervento = new Intervento(operationRoom, type, operationDate, startTime, invoice, endTime, medRecord, vet);
                        interventoTable.save(intervento);
                        operationsList = FXCollections.observableArrayList(interventoTable.findAll());
                        operationsTable.getItems().setAll(operationsList);
                    }
                }
            }
        }
    }

    public void onShowOperationsClick(final ActionEvent e) {

        if (microchipOperation.getText().isEmpty()) {
            showPopUp("Inserisci tutti i campi!", null, Alert.AlertType.WARNING);
        } else {
            int microchip = Integer.parseInt(microchipOperation.getText());
            if (animaleTable.findByPrimaryKey(microchip).isEmpty()) {
                showPopUp("Animale non esistente!", null, Alert.AlertType.WARNING);
            } else {
                operationsList = FXCollections.observableArrayList(interventoTable.showAnimalOperations(microchip));
                if (operationsList.isEmpty()) {
                    showPopUp("Non ci sono interventi!", null, Alert.AlertType.INFORMATION);
                }
                operationsTable.getItems().setAll(operationsList);
            }
        }

    }


}
