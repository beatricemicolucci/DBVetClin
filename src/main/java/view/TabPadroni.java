package view;

import db.ConnectionProvider;
import db.tables.PadroneTable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Padrone;
import utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public class TabPadroni extends TabController {

    private PadroneTable padroneTable;
    private ObservableList<Padrone> ownersList;

    @FXML
    private TextField ownerName;

    @FXML
    private TextField ownerLastName;

    @FXML
    private TextField ownerCFInsert;

    @FXML
    private TextField ownerAddress;

    @FXML
    private TextField ownerTelephone;

    @FXML
    private TextField ownerEmail;

    @FXML
    private DatePicker ownerBirthDate;

    @FXML
    private TextField ownerCFExpenditure;

    @FXML
    private TextField ownerCFUpdate;

    @FXML
    private TextField newAddress;

    @FXML
    private TableView<Padrone> ownersTable;

    @FXML
    private TableColumn<Padrone, String> cfOwner;

    @FXML
    private TableColumn<Padrone, String> nameOwner;

    @FXML
    private TableColumn<Padrone, String> lastNameOwner;

    @FXML
    private TableColumn<Padrone, java.sql.Date> birthDateOwner;

    @FXML
    private TableColumn<Padrone, String> addressOwner;

    @FXML
    private TableColumn<Padrone, String> telephoneOwner;

    @FXML
    private TableColumn<Padrone, String> emailOwner;




    public void init() {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        padroneTable = new PadroneTable(connectionProvider.getMySQLConnection());
        ownersList = FXCollections.observableArrayList();
        cfOwner.setCellValueFactory(new PropertyValueFactory<>("cf"));
        nameOwner.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNameOwner.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        birthDateOwner.setCellValueFactory(cellData -> {
            Padrone padrone = cellData.getValue();
            Date birthDate = padrone.getBirthDate();
            return new SimpleObjectProperty<>(Utils.dateToSqlDate(birthDate));
        });
        addressOwner.setCellValueFactory(new PropertyValueFactory<>("address"));
        telephoneOwner.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        emailOwner.setCellValueFactory(cellData -> {
            Padrone padrone = cellData.getValue();
            Optional<String> emailOptional = padrone.getEmail();
            String email = emailOptional.orElse("");
            return new SimpleStringProperty(email);
        });
        ownersList = FXCollections.observableArrayList(padroneTable.findAll());
        ownersTable.getItems().setAll(ownersList);
    }

    @FXML
    public void onInsertOwnerClick(final ActionEvent e) {
        String name = ownerName.getText();
        String lastName = ownerLastName.getText();
        String cf = ownerCFInsert.getText();
        String address = ownerAddress.getText();
        String telephone = ownerTelephone.getText();
        Optional<String> email = Optional.ofNullable(ownerEmail.getText());

        if ( name.isEmpty() || lastName.isEmpty() || cf.isEmpty() || ownerBirthDate.getValue() == null || address.isEmpty() || telephone.isEmpty()) {
            showPopUp("Inserisci tutti i campi!", null, Alert.AlertType.WARNING);
        } else {
            Date birthDate = Utils.buildDate(ownerBirthDate.getValue().getDayOfMonth(), ownerBirthDate.getValue().getMonthValue(), ownerBirthDate.getValue().getYear()).get();
            Padrone padrone = new Padrone(cf, name, lastName, birthDate, address, telephone, email);
            if (padroneTable.findByPrimaryKey(cf).isPresent()) {
                showPopUp("Padrone esistente!", null, Alert.AlertType.WARNING);
            } else {
                padroneTable.save(padrone);
                ownersList = FXCollections.observableArrayList(padroneTable.findAll());
                ownersTable.getItems().setAll(ownersList);
            }
        }
    }

    @FXML
    public void onViewOwnerClick(final ActionEvent e) {

        if (ownerCFExpenditure.getText().isEmpty()) {
            showPopUp("Campo vuoto!", "Spesa Totale", Alert.AlertType.WARNING);
        } else {
            Optional<Padrone> padrone = padroneTable.findByPrimaryKey(ownerCFExpenditure.getText());

            if (padrone.isPresent()) {
                float expense = padroneTable.showTotalExpenseOfOwner(ownerCFExpenditure.getText());
                showPopUp("Spesa Totale: " + expense, "Spesa Totale", Alert.AlertType.INFORMATION);
            } else {
                showPopUp("Padrone non trovato", "Spesa Totale", Alert.AlertType.ERROR);
            }
        }


    }

    @FXML
    public void onUpdateOwnerClick(final ActionEvent e) {
        String cf = ownerCFUpdate.getText();
        String newOwnerAddress = newAddress.getText();
        if (cf.isEmpty() || newOwnerAddress.isEmpty()) {
            showPopUp("Inserisci tutti i campi!", null, Alert.AlertType.WARNING);
        } else {
            Optional<Padrone> padrone = padroneTable.findByPrimaryKey(cf);
            if (padrone.isPresent()) {
                padrone.get().setAddress(newOwnerAddress);
                if (padroneTable.update(padrone.get())) {
                    ownersList = FXCollections.observableArrayList(padroneTable.findAll());
                    ownersTable.getItems().setAll(ownersList);
                }
            } else {
                showPopUp("Padrone non trovato", null, Alert.AlertType.ERROR);
            }
        }
    }
}
