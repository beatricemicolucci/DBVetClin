package view;

import db.ConnectionProvider;
import db.tables.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import utils.ThreeKeys;
import utils.Utils;

import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;


public class TabTerapie extends TabController {

    private TerapiaTable terapiaTable;
    private VeterinarioTable veterinarioTable;
    private CartellaClinicaTable cartellaClinicaTable;
    private MalattiaTable malattiaTable;
    private ControlloTable controlloTable;
    private EsameTable esameTable;
    private FatturaTable fatturaTable;
    private PadroneTable padroneTable;
    private FarmacoTable farmacoTable;
    private ComposizioneTable composizioneTable;
    private ObservableList<Terapia> therapiesList;

    @FXML
    private TextField idVetFieldCheckUp;

    @FXML
    private TextField idInvoiceField;

    @FXML
    private TextField idMedRecFieldCheckUp;

    @FXML
    private TextField amountField;

    @FXML
    private TextField servicesField;

    @FXML
    private TextField cfField;

    @FXML
    private TextField diseaseFieldCheckUp;

    @FXML
    private DatePicker checkUpDate;

    @FXML
    private DatePicker invoiceDate;

    @FXML
    private Spinner<Integer> startHourCheckUp;

    @FXML
    private Spinner<Integer> startMinCheckUp;

    @FXML
    private Spinner<Integer> endHourCheckUp;

    @FXML
    private Spinner<Integer> endMinCheckUp;

    @FXML
    private TextField idVetFieldExam;

    @FXML
    private TextField idInvoiceField2;

    @FXML
    private TextField idMedRecordFieldExam;

    @FXML
    private TextField amountField2;

    @FXML
    private TextField servicesField2;

    @FXML
    private TextField cfField2;

    @FXML
    private TextField diseaseFieldExam;

    @FXML
    private TextField idExamField;

    @FXML
    private TextField typeField;

    @FXML
    private DatePicker examDate;

    @FXML
    private DatePicker invoiceDate2;

    @FXML
    private Spinner<Integer> startHourExam;

    @FXML
    private Spinner<Integer> startMinExam;

    @FXML
    private Spinner<Integer> endHourExam;

    @FXML
    private Spinner<Integer> endMinExam;

    @FXML
    private TextField idTherapyField;

    @FXML
    private TextField diseaseField;

    @FXML
    private TextField idVetField;

    @FXML
    private TextField idMedRecField;

    @FXML
    private TextField idMedField;

    @FXML
    private TextField idMedField2;

    @FXML
    private TextField idMedField3;

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
        malattiaTable = new MalattiaTable(connectionProvider.getMySQLConnection());
        controlloTable = new ControlloTable(connectionProvider.getMySQLConnection());
        esameTable = new EsameTable(connectionProvider.getMySQLConnection());
        fatturaTable = new FatturaTable(connectionProvider.getMySQLConnection());
        padroneTable = new PadroneTable(connectionProvider.getMySQLConnection());
        farmacoTable = new FarmacoTable(connectionProvider.getMySQLConnection());
        composizioneTable = new ComposizioneTable(connectionProvider.getMySQLConnection());

        SpinnerValueFactory<Integer> valueStartHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24);
        SpinnerValueFactory<Integer> valueEndHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24);
        SpinnerValueFactory<Integer> valueStartMinutesFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        SpinnerValueFactory<Integer> valueEndMinutesFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        valueStartHourFactory.setValue(0);
        valueStartMinutesFactory.setValue(0);
        valueEndHourFactory.setValue(0);
        valueEndMinutesFactory.setValue(0);
        startHourCheckUp.setValueFactory(valueStartHourFactory);
        startMinCheckUp.setValueFactory(valueStartMinutesFactory);
        endHourCheckUp.setValueFactory(valueEndHourFactory);
        endMinCheckUp.setValueFactory(valueEndMinutesFactory);

        SpinnerValueFactory<Integer> valueStartHourExamFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24);
        SpinnerValueFactory<Integer> valueEndHourExamFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24);
        SpinnerValueFactory<Integer> valueStartExamMinutesFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        SpinnerValueFactory<Integer> valueEndMinutesExamFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        valueStartHourExamFactory.setValue(0);
        valueStartExamMinutesFactory.setValue(0);
        valueEndHourExamFactory.setValue(0);
        valueEndMinutesExamFactory.setValue(0);
        startHourExam.setValueFactory(valueStartHourExamFactory);
        startMinExam.setValueFactory(valueStartExamMinutesFactory);
        endHourExam.setValueFactory(valueEndHourExamFactory);
        endMinExam.setValueFactory(valueEndMinutesExamFactory);

        idTherapyCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        diseaseCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        idVetCol.setCellValueFactory(new PropertyValueFactory<>("idVet"));
        idMedRecCol.setCellValueFactory(new PropertyValueFactory<>("idMedicalRecord"));
        therapiesList = FXCollections.observableArrayList();
    }

    public void onInsertClick(final ActionEvent actionEvent) {

        if (idTherapyField.getText().isEmpty() || diseaseField.getText().isEmpty() || idVetField.getText().isEmpty() || idMedRecField.getText().isEmpty()
            || idMedField.getText().isEmpty()) {
            showPopUp("Inserisci tutti i campi!", null, Alert.AlertType.WARNING);
        } else {
            int idTherapy = Integer.parseInt(idTherapyField.getText());
            String disease = diseaseField.getText();
            int idVet = Integer.parseInt(idVetField.getText());
            int idMedRecord = Integer.parseInt(idMedRecField.getText());
            int idMed = Integer.parseInt(idMedField.getText());


            if (veterinarioTable.findByPrimaryKey(idVet).isEmpty()) {
                showPopUp("Veterinario non esistente!", null, Alert.AlertType.WARNING);
            } else if (cartellaClinicaTable.findByPrimaryKey(idMedRecord).isEmpty()) {
                showPopUp("Cartella clinica non esistente!", null, Alert.AlertType.WARNING);
            } else if (malattiaTable.findByPrimaryKey(disease).isEmpty()) {
                showPopUp("Malattia non registrata", null, Alert.AlertType.ERROR);
            } else {

                if (farmacoTable.findByPrimaryKey(idMed).isEmpty()) {
                    Farmaco farmaco = new Farmaco(idMed);
                    farmacoTable.save(farmaco);
                    Composizione composizione = new Composizione(idMed, idTherapy);
                    composizioneTable.save(composizione);
                }

                if (!idMedField2.getText().isEmpty()) {
                    int idMed2 = Integer.parseInt(idMedField2.getText());
                    if (farmacoTable.findByPrimaryKey(idMed2).isEmpty()) {
                        Farmaco farmaco = new Farmaco(idMed2);
                        farmacoTable.save(farmaco);
                        Composizione composizione = new Composizione(idMed, idTherapy);
                        composizioneTable.save(composizione);
                    }
                }

                if (!idMedField3.getText().isEmpty()) {
                    int idMed3 = Integer.parseInt(idMedField3.getText());
                    if (farmacoTable.findByPrimaryKey(idMed3).isEmpty()) {
                        Farmaco farmaco = new Farmaco(idMed3);
                        farmacoTable.save(farmaco);
                        Composizione composizione = new Composizione(idMed, idTherapy);
                        composizioneTable.save(composizione);
                    }
                }

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

    public void onInsertCheckUpClick(ActionEvent actionEvent) {

        if (idVetFieldCheckUp.getText().isEmpty() || checkUpDate.getValue() == null || idInvoiceField.getText().isEmpty() || idMedRecFieldCheckUp.getText().isEmpty()
            || startHourCheckUp.getValue() == null || startMinCheckUp.getValue() == null || endHourCheckUp.getValue() == null || endMinCheckUp.getValue() == null
            || invoiceDate.getValue() == null || amountField.getText().isEmpty() || servicesField.getText().isEmpty() || cfField.getText().isEmpty() || diseaseFieldCheckUp.getText().isEmpty()) {
            showPopUp("Inserisci tutti i campi!", null, Alert.AlertType.WARNING);
        } else {
            int idVet = Integer.parseInt(idVetFieldCheckUp.getText());
            Date visitDay = Utils.buildDate(checkUpDate.getValue().getDayOfMonth(), checkUpDate.getValue().getMonthValue(), checkUpDate.getValue().getYear()).get();
            int idInvoice = Integer.parseInt(idInvoiceField.getText());
            int idMedRecord = Integer.parseInt(idMedRecFieldCheckUp.getText());
            LocalTime startTime = Optional.ofNullable(LocalTime.of(startHourCheckUp.getValue(), startMinCheckUp.getValue())).get();
            LocalTime endTime = LocalTime.of(endHourCheckUp.getValue(), endMinCheckUp.getValue());
            Date invoiceDate = Utils.buildDate(this.invoiceDate.getValue().getDayOfMonth(), this.invoiceDate.getValue().getMonthValue(), this.invoiceDate.getValue().getYear()).get();
            float amount = Float.parseFloat(amountField.getText());
            String services = servicesField.getText();
            String cf = cfField.getText();
            String disease = diseaseFieldCheckUp.getText();
            if (cartellaClinicaTable.findByPrimaryKey(idMedRecord).isEmpty()) {
                showPopUp("Cartella clinica non esistente!", null, Alert.AlertType.ERROR);
            } else if (padroneTable.findByPrimaryKey(cf).isEmpty()) {
                showPopUp("Padrone non esistente!", null, Alert.AlertType.ERROR);
            } else if (fatturaTable.findByPrimaryKey(idInvoice).isPresent()) {
                showPopUp("Fattura già registrata", null, Alert.AlertType.ERROR);
            } else if (controlloTable.findByPrimaryKey(new ThreeKeys<>(idVet, visitDay, startTime)).isPresent()){
                showPopUp("Non è stato possibile registrare la visita!", null, Alert.AlertType.ERROR);
            } else {
                if (malattiaTable.findByPrimaryKey(disease).isEmpty()) {
                    Malattia malattia = new Malattia(disease, idVet, visitDay, startTime);
                    malattiaTable.save(malattia);
                }
                Fattura fattura = new Fattura(idInvoice, invoiceDate, amount, services, cf);
                fatturaTable.save(fattura);

                Controllo controllo = new Controllo(idVet, visitDay, startTime, idInvoice, endTime, idMedRecord);
                controlloTable.save(controllo);
                if (controlloTable.save(controllo)) {
                    showPopUp("Visita registrata!", null, Alert.AlertType.INFORMATION);
                } else {
                    showPopUp("Qualcosa è andato storto!", null, Alert.AlertType.ERROR);
                }
            }
        }

    }

    public void onInsertExamClick(ActionEvent actionEvent) {

        if (idVetFieldExam.getText().isEmpty() || examDate.getValue() == null || idInvoiceField2.getText().isEmpty() || idMedRecordFieldExam.getText().isEmpty()
                || startHourExam.getValue() == null || startMinExam.getValue() == null || endHourExam.getValue() == null || endMinExam.getValue() == null
                || invoiceDate2.getValue() == null || amountField2.getText().isEmpty() || servicesField2.getText().isEmpty() || cfField2.getText().isEmpty() || diseaseFieldExam.getText().isEmpty()
                || idExamField.getText().isEmpty() || typeField.getText().isEmpty()) {
                    showPopUp("Inserisci tutti i campi!", null, Alert.AlertType.WARNING);
        } else {
            int idVet = Integer.parseInt(idVetFieldExam.getText());
            Optional<Date> visitDay = Utils.buildDate(examDate.getValue().getDayOfMonth(), examDate.getValue().getMonthValue(), examDate.getValue().getYear());
            int idInvoice = Integer.parseInt(idInvoiceField2.getText());
            int idMedRecord = Integer.parseInt(idMedRecordFieldExam.getText());
            Optional<LocalTime> startTime = Optional.ofNullable(LocalTime.of(startHourExam.getValue(), startMinExam.getValue()));
            LocalTime endTime = LocalTime.of(endHourExam.getValue(), endMinExam.getValue());
            Date invoiceDate = Utils.buildDate(this.invoiceDate2.getValue().getDayOfMonth(), this.invoiceDate2.getValue().getMonthValue(), this.invoiceDate2.getValue().getYear()).get();
            float amount = Float.parseFloat(amountField2.getText());
            String services = servicesField2.getText();
            String cf = cfField2.getText();
            String disease = diseaseFieldCheckUp.getText();
            int id = Integer.parseInt(idExamField.getText());
            String type = typeField.getText();
            if (cartellaClinicaTable.findByPrimaryKey(idMedRecord).isEmpty()) {
                showPopUp("Cartella clinica non esistente!", null, Alert.AlertType.ERROR);
            } else if (padroneTable.findByPrimaryKey(cf).isEmpty()) {
                showPopUp("Padrone non esistente!", null, Alert.AlertType.ERROR);
            } else if (fatturaTable.findByPrimaryKey(idInvoice).isPresent()) {
                showPopUp("Fattura già registrata", null, Alert.AlertType.ERROR);
            } else if (esameTable.findByPrimaryKey(id).isPresent()){
                showPopUp("Non è stato possibile registrare la visita!", null, Alert.AlertType.ERROR);
            } else {
                if (malattiaTable.findByPrimaryKey(disease).isEmpty()) {
                    Malattia malattia = new Malattia(disease, id);
                    malattiaTable.save(malattia);
                }
                Fattura fattura = new Fattura(idInvoice, invoiceDate, amount, services, cf);
                fatturaTable.save(fattura);

                Esame esame = new Esame(id, idInvoice, type, visitDay.get(), startTime.get(), endTime, idVet, idMedRecord);
                esameTable.save(esame);
                if (esameTable.save(esame)) {
                    showPopUp("Visita registrata!", null, Alert.AlertType.INFORMATION);
                } else {
                    showPopUp("Qualcosa è andato storto!", null, Alert.AlertType.ERROR);
                }
            }
        }

    }
}
