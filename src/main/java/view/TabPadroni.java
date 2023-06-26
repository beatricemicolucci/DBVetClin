package view;

import db.ConnectionProvider;
import db.tables.PadroneTable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Padrone;
import utils.Utils;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TabPadroni {

    private PadroneTable padroneTable;
    private ConnectionProvider connectionProvider;
    private List<Padrone> ownersList;

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
    private TableView ownersTable;

    public void init() {
        connectionProvider = new ConnectionProvider();
        padroneTable = new PadroneTable(connectionProvider.getMySQLConnection());
        ownersList = new ArrayList<>();
    }

    @FXML
    public void onInsertOwnerClick(final ActionEvent e) {
        String name = ownerName.getText();
        String lastName = ownerLastName.getText();
        String cf = ownerCFInsert.getText();
        Date birthDate = Utils.buildDate(ownerBirthDate.getValue().getDayOfMonth(), ownerBirthDate.getValue().getMonthValue(), ownerBirthDate.getValue().getYear()).get();
        String address = ownerAddress.getText();
        String telephone = ownerTelephone.getText();
        Optional<String> email = Optional.ofNullable(ownerEmail.getText());

        if ( name.isEmpty() || lastName.isEmpty() || cf.isEmpty() || birthDate == null || address.isEmpty() || telephone.isEmpty()) {
            return;
        }

        Padrone padrone = new Padrone(cf, name, lastName, birthDate, address, telephone, email);
        padroneTable.save(padrone);
        ownersList = padroneTable.findAll();
        ownersTable.getItems().setAll(ownersList);
    }

    @FXML
    public void onViewOwnerClick(final ActionEvent e) {

        if (ownerCFExpenditure.getText().isEmpty()) {
            return;
        }
        Optional<Padrone> padrone = padroneTable.findByPrimaryKey(ownerCFExpenditure.getText());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Spesa Totale");
        alert.setHeaderText(null);

        if (padrone.isPresent()) {
            float expense = padroneTable.showTotalExpenseOfOwner(ownerCFExpenditure.getText());
            alert.setContentText("Spesa Totale: " + expense);
        } else {
            alert.setContentText("Padrone non trovato");
        }

        alert.showAndWait();
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.close();

    }

    @FXML
    public void onUpdateOwnerClick(final ActionEvent e) {
        String cf = ownerCFUpdate.getText();
        String newOwnerAddress = newAddress.getText();
        if (!(cf.isEmpty() || newOwnerAddress.isEmpty())) {
            Optional<Padrone> padrone = padroneTable.findByPrimaryKey(cf);
            if (padrone.isPresent()) {
                if (padroneTable.update(padrone.get())) {
                    ownersList = padroneTable.findAll();
                    ownersTable.getItems().setAll(ownersList);
                }
            }
        }
    }

}
