package view;

import db.ConnectionProvider;
import db.tables.AnimaleTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Animale;
import utils.Utils;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public void onAnimalInsert(ActionEvent e) {
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

    public void onAnimalView(final ActionEvent e) {

    }

    public void onAnimalVisitDayView(final ActionEvent e) {

    }

}
