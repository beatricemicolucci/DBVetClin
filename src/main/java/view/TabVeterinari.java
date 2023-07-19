package view;

import db.ConnectionProvider;
import db.tables.CompetenzaTable;
import db.tables.SpecializzazioneTable;
import db.tables.VeterinarioTable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Competenza;
import model.Specializzazione;
import model.Veterinario;
import utils.Utils;

import java.util.Date;
import java.util.Optional;


public class TabVeterinari extends TabController{

    VeterinarioTable veterinarioTable;
    SpecializzazioneTable specializzazioneTable;
    CompetenzaTable competenzaTable;
    ObservableList<Veterinario> vetList;

    @FXML
    private TextField idVetField;

    @FXML
    private TextField officeField;

    @FXML
    private TextField cfField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private DatePicker birthDateField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField specializationField;

    @FXML
    private TextField specializationField2;

    @FXML
    private TextField specializationField3;

    @FXML
    private TextField specializationInsertField;

    @FXML
    private TableView<Veterinario> vetTable;

    @FXML
    private TableColumn<Veterinario, Integer> idCol;

    @FXML
    private TableColumn<Veterinario, String> nameCol;

    @FXML
    private TableColumn<Veterinario, String> lastNameCol;

    @FXML
    private TableColumn<Veterinario, String> cfCol;

    @FXML
    private TableColumn<Veterinario, String> officeCol;

    @FXML
    private TableColumn<Veterinario, Date> birthDateCol;

    @FXML
    private TableColumn<Veterinario, String> addressCol;

    @FXML
    private TableColumn<Veterinario, String> telephoneCol;

    @FXML
    private TableColumn<Veterinario, String> emailCol;

    @Override
    void init() {

        ConnectionProvider connectionProvider = new ConnectionProvider();
        veterinarioTable = new VeterinarioTable(connectionProvider.getMySQLConnection());
        specializzazioneTable = new SpecializzazioneTable(connectionProvider.getMySQLConnection());
        competenzaTable = new CompetenzaTable(connectionProvider.getMySQLConnection());
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        cfCol.setCellValueFactory(new PropertyValueFactory<>("cf"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        officeCol.setCellValueFactory(new PropertyValueFactory<>("office"));
        telephoneCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        emailCol.setCellValueFactory(cellData -> {
            Veterinario veterinario = cellData.getValue();
            Optional<String> emailOptional = veterinario.getEmail();
            String email = emailOptional.orElse("");
            return new SimpleStringProperty(email);
        });
        birthDateCol.setCellValueFactory(cellData -> {
            Date birthDate = cellData.getValue().getBirthDate();
            return new SimpleObjectProperty<>(Utils.dateToSqlDate(birthDate));
        });
        vetList = FXCollections.observableArrayList(veterinarioTable.findAll());
        vetTable.getItems().setAll(vetList);
    }

    public void onInsertClick(ActionEvent actionEvent) {

        if (idVetField.getText().isEmpty() || nameField.getText().isEmpty() || lastNameField.getText().isEmpty() || officeField.getText().isEmpty()
            || cfField.getText().isEmpty() || birthDateField.getValue() == null || addressField.getText().isEmpty() || telephoneField.getText().isEmpty()) {
            showPopUp("Inserisci tutti i campi!", null, Alert.AlertType.WARNING);
        } else {
            int id = Integer.parseInt(idVetField.getText());
            String name = nameField.getText();
            String lastName = lastNameField.getText();
            String cf = cfField.getText();
            String office = officeField.getText();
            Date birthDate = Utils.buildDate(birthDateField.getValue().getDayOfMonth(), birthDateField.getValue().getMonthValue(), birthDateField.getValue().getYear()).get();
            String address = addressField.getText();
            String telephone = telephoneField.getText();
            Optional<String> email = Optional.ofNullable(emailField.getText());
            Optional<String> specialization = Optional.ofNullable(specializationInsertField.getText());
            Optional<String> specialization2 = Optional.ofNullable(specializationField2.getText());
            Optional<String> specialization3 = Optional.ofNullable(specializationField3.getText());

            if (veterinarioTable.findByPrimaryKey(id).isPresent()) {
                showPopUp("Veterinario già registrato!", null, Alert.AlertType.WARNING);
            } else {
                Veterinario veterinario = new Veterinario(id, office, cf, name, lastName, birthDate, address, telephone, email);
                veterinarioTable.save(veterinario);
                if (specialization.isPresent()) {
                    if (specializzazioneTable.findByPrimaryKey(specialization.get()).isEmpty()) {
                        Specializzazione specializzazione = new Specializzazione(specialization.get());
                        specializzazioneTable.save(specializzazione);
                    }
                    Competenza competenza = new Competenza(specialization.get(), id);
                    competenzaTable.save(competenza);
                }

                if (specialization2.isPresent()) {
                    if (specializzazioneTable.findByPrimaryKey(specialization2.get()).isEmpty()) {
                        Specializzazione specializzazione = new Specializzazione(specialization2.get());
                        specializzazioneTable.save(specializzazione);
                    }
                    Competenza competenza = new Competenza(specialization2.get(), id);
                    competenzaTable.save(competenza);
                }

                if (specialization3.isPresent()) {
                    if (specializzazioneTable.findByPrimaryKey(specialization3.get()).isEmpty()) {
                        Specializzazione specializzazione = new Specializzazione(specialization3.get());
                        specializzazioneTable.save(specializzazione);
                    }
                    Competenza competenza = new Competenza(specialization3.get(), id);
                    competenzaTable.save(competenza);
                }
                vetList = FXCollections.observableArrayList(veterinarioTable.findAll());
                vetTable.getItems().setAll(vetList);
            }
        }

    }

    public void onResearchClick(ActionEvent actionEvent) {

        if (specializationField.getText().isEmpty()) {
            showPopUp("Inserisci tutti i campi!", null, Alert.AlertType.WARNING);
        } else {
            if (specializzazioneTable.findByPrimaryKey(specializationField.getText()).isEmpty()) {
                showPopUp("Non esistono veterinari con questa specializzazione!", null, Alert.AlertType.INFORMATION);
            } else {
                vetList = FXCollections.observableArrayList(veterinarioTable.getVetBySpecialization(specializationField.getText()));
                vetTable.getItems().setAll(vetList);
            }
        }

    }

    public void onShowClick(ActionEvent actionEvent) {
        vetList = FXCollections.observableArrayList(veterinarioTable.findAll());
        vetTable.getItems().setAll(vetList);
    }
}
