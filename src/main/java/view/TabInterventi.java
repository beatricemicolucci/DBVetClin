package view;

import db.ConnectionProvider;
import db.tables.CartellaClinicaTable;
import db.tables.FatturaTable;
import db.tables.InterventoTable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import model.Fattura;
import model.Intervento;
import utils.Utils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TabInterventi extends TabController {

    private ConnectionProvider connectionProvider;
    private InterventoTable interventoTable;
    private CartellaClinicaTable cartellaClinicaTable;
    private FatturaTable fatturaTable;
    private List<Intervento> operationsList;

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
    private TableView<List<Intervento>> operationsTable;

    public void init() {
        connectionProvider = new ConnectionProvider();
        interventoTable = new InterventoTable(connectionProvider.getMySQLConnection());
        cartellaClinicaTable = new CartellaClinicaTable(connectionProvider.getMySQLConnection());
        fatturaTable = new FatturaTable(connectionProvider.getMySQLConnection());
        operationsList = new ArrayList<>();
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
    }

    public void onInsertOperationClick(final ActionEvent e) {
        int operationRoom = Integer.valueOf(operatingRoomInsert.getText());
        String type = typeInsert.getText();
        Date operationDate = Utils.buildDate(dayInsert.getValue().getDayOfMonth(), dayInsert.getValue().getMonthValue(), dayInsert.getValue().getYear()).get();
        int invoice = Integer.valueOf(invoiceOperation.getText());
        int medRecord = Integer.valueOf(medRecordInsert.getText());
        int vet = Integer.valueOf(vetInsert.getText());
        LocalTime startTime = LocalTime.of((int) startHour.getValue() ,(int) startMin.getValue());
        LocalTime endTime = LocalTime.of((int) endHour.getValue(), (int) endMin.getValue());
        Date invoiceDate = Utils.buildDate(dateInvoice.getValue().getDayOfMonth(), dateInvoice.getValue().getMonthValue(), dateInvoice.getValue().getYear()).get();
        float amount = Float.valueOf(amountInsert.getText());
        String services = servicesInsert.getText();
        String cf = cfInvoice.getText();

        Fattura fattura = new Fattura(invoice, invoiceDate, amount, services, cf);
        fatturaTable.save(fattura);

        Intervento intervento = new Intervento(operationRoom, type, operationDate, startTime, invoice, endTime, medRecord, vet);
        interventoTable.save(intervento);
        operationsList = interventoTable.findAll();
        operationsTable.getItems().setAll(operationsList);
    }

    public void onShowOperationsClick(final ActionEvent e) {
        operationsList = cartellaClinicaTable.showAnimalOperations(Integer.valueOf(microchipOperation.getText()));
        operationsTable.getItems().setAll(operationsList);
    }


}
