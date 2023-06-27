package view;

import db.ConnectionProvider;
import db.tables.AnimaleTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Animale;
import utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TabAnimali {

    private AnimaleTable animaleTable;
    private ConnectionProvider connectionProvider;
    private List<Animale> animalsList;

    @FXML
    private TextField microchipInsert;

    @FXML
    private TextField animalName;

    @FXML
    private TextField animalRace;

    @FXML
    private DatePicker animalBirthDate;

    @FXML
    private TextField cfInsert;

    @FXML
    private ChoiceBox animalGender;

    @FXML
    private TextField microchipView;

    @FXML
    private TableView animalTable;

    public void init() {
        connectionProvider = new ConnectionProvider();
        animaleTable = new AnimaleTable(connectionProvider.getMySQLConnection());
        animalsList = new ArrayList<>();
        ObservableList<String> choicesList = FXCollections.observableArrayList("F", "M");
        animalGender.setItems(choicesList);
        animalGender.setValue("F");
    }

    public void onAnimalInsertClick(final ActionEvent e) {
        int microchip = Integer.valueOf(microchipInsert.getText()).intValue();
        String name = animalName.getText();
        String race = animalRace.getText();
        Date birthDate = Utils.buildDate(animalBirthDate.getValue().getDayOfMonth(), animalBirthDate.getValue().getMonthValue(), animalBirthDate.getValue().getYear()).get();
        String gender = (String) animalGender.getValue();
        String cf = cfInsert.getText();

        if ( name.isEmpty() || microchipInsert.getText().isEmpty() || race.isEmpty() || birthDate == null || cf.isEmpty()) {
            return;
        }

        Animale animale = new Animale(microchip, name, race, birthDate, gender, cf);
        animaleTable.save(animale);
        animalsList = animaleTable.findAll();
        animalTable.getItems().setAll(animalsList);

    }

    public void onAnimalViewClick(final ActionEvent e) {
        animalsList = animaleTable.showTopTen();
        animalTable.getItems().setAll(animalsList);
    }

    public void onAnimalVisitDayViewClick(final ActionEvent e) {

        if (microchipView.getText().isEmpty()) {
            return;
        }

        int microchip = Integer.valueOf(microchipView.getText());
        Optional<Animale> animale = animaleTable.findByPrimaryKey(microchip);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Spesa Totale");
        alert.setHeaderText(null);

        if (animale.isPresent()) {
            Date visitDay = animaleTable.showNextVisit(microchip);
            alert.setContentText("Prossima visita: " + Utils.buildDate(visitDay.getDay(), visitDay.getMonth(), visitDay.getYear()));
        } else {
            alert.setContentText("Animale non trovato.");
        }

        alert.showAndWait();
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.close();

    }

}
