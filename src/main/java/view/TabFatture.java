package view;

import db.ConnectionProvider;
import db.tables.FatturaTable;
import db.tables.PadroneTable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Fattura;
import model.Intervento;
import utils.Utils;

import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TabFatture extends TabController {

    private FatturaTable fatturaTable;

    private PadroneTable padroneTable;

    private ObservableList<Fattura> invoiceList;

    @FXML
    private TextField cfField;

    @FXML
    private DatePicker invoiceDateField;

    @FXML
    private ChoiceBox<Integer> yearField;

    @FXML
    private TableView<Fattura> invoiceTable;

    @FXML
    private TableColumn<Fattura, Integer> idInvoice;

    @FXML
    private TableColumn<Fattura, Date> dateInvoice;

    @FXML
    private TableColumn<Fattura, Float> amount;

    @FXML
    private TableColumn<Fattura, String> services;

    @FXML
    private TableColumn<Fattura, String> cf;

    @FXML

    @Override
    void init() {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        fatturaTable = new FatturaTable(connectionProvider.getMySQLConnection());
        padroneTable = new PadroneTable(connectionProvider.getMySQLConnection());
        ObservableList<Integer> yearList = IntStream.rangeClosed(1970, 2023)
                .boxed()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        yearField.setItems(yearList);
        yearField.setValue(1970);

        idInvoice.setCellValueFactory(new PropertyValueFactory<>("idInvoice"));
        dateInvoice.setCellValueFactory(cellData -> {
            Fattura fattura = cellData.getValue();
            Date date = fattura.getDate();
            return new SimpleObjectProperty<>(Utils.dateToSqlDate(date));
        });
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        services.setCellValueFactory(new PropertyValueFactory<>("services"));
        cf.setCellValueFactory(new PropertyValueFactory<>("cfOwner"));
        invoiceList = FXCollections.observableArrayList(fatturaTable.findAll());
        invoiceTable.getItems().setAll(invoiceList);
    }

    public void onYearResearchClick(ActionEvent actionEvent) {

        if (invoiceDateField.getValue() == null) {
            showPopUp("Inserisci tutti i campi!", null, Alert.AlertType.ERROR);
        } else {
            invoiceList = FXCollections.observableArrayList(fatturaTable.findByDate(Utils.buildDate(invoiceDateField.getValue().getDayOfMonth(), invoiceDateField.getValue().getMonthValue(), invoiceDateField.getValue().getYear()).get()));
            if (invoiceList.isEmpty()) {
                showPopUp("Non ci sono fatture emesse in questa data.", null, Alert.AlertType.INFORMATION);
            }
            invoiceTable.getItems().setAll(invoiceList);
        }
    }

    public void onCFResearchClick(ActionEvent actionEvent) {

        if (cfField.getText().isEmpty()) {
            showPopUp("Inserisci tutti i campi!", null, Alert.AlertType.ERROR);
        } else {
            invoiceList = FXCollections.observableArrayList(fatturaTable.findByCF(cfField.getText()));
            if (invoiceList.isEmpty()) {
                showPopUp("Non ci sono fatture relative a questo padrone.", null, Alert.AlertType.INFORMATION);
            }
            invoiceTable.getItems().setAll(invoiceList);
        }
    }

    public void onShowClick(ActionEvent actionEvent) {
        float expense = fatturaTable.calculateAverageExpense(yearField.getValue());
        showPopUp("La spesa media relativa all'anno scelto e' di: " + expense, null, Alert.AlertType.INFORMATION);
    }
}
