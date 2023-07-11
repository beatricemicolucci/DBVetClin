package view;

import db.ConnectionProvider;
import db.tables.AnimaleTable;
import db.tables.CartellaClinicaTable;
import db.tables.PadroneTable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Animale;
import utils.Utils;

import java.util.Date;
import java.util.Optional;

public class TabAnimali extends TabController {

    private AnimaleTable animaleTable;
    private PadroneTable padroneTable;
    private CartellaClinicaTable cartellaClinicaTable;
    private ObservableList<Animale> animalsList;

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
    private ChoiceBox<String> animalGender;

    @FXML
    private TextField microchipView;

    @FXML
    private TableView<Animale> animalTable;

    @FXML
    private TableColumn<Animale, Integer> microchipAnimal;

    @FXML
    private TableColumn<Animale, String> nameAnimal;

    @FXML
    private TableColumn<Animale, String> raceAnimal;

    @FXML
    private TableColumn<Animale, java.sql.Date> birthDateAnimal;

    @FXML
    private TableColumn<Animale, String> genderAnimal;

    @FXML
    private TableColumn<Animale, String> cfOwner;

    public void init() {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        animaleTable = new AnimaleTable(connectionProvider.getMySQLConnection());
        padroneTable = new PadroneTable(connectionProvider.getMySQLConnection());
        cartellaClinicaTable = new CartellaClinicaTable(connectionProvider.getMySQLConnection());
        animalsList = FXCollections.observableArrayList();
        ObservableList<String> choicesList = FXCollections.observableArrayList("F", "M");
        animalGender.setItems(choicesList);
        animalGender.setValue("F");
        microchipAnimal.setCellValueFactory(new PropertyValueFactory<>("microchip"));
        nameAnimal.setCellValueFactory(new PropertyValueFactory<>("name"));
        raceAnimal.setCellValueFactory(new PropertyValueFactory<>("race"));
        birthDateAnimal.setCellValueFactory(cellData -> {
            Animale animale = cellData.getValue();
            Date birthDate = animale.getBirthDate();
            return new SimpleObjectProperty<>(Utils.dateToSqlDate(birthDate));
        });
        genderAnimal.setCellValueFactory(new PropertyValueFactory<>("gender"));
        cfOwner.setCellValueFactory(new PropertyValueFactory<>("cfOwner"));
    }

    public void onAnimalInsertClick(final ActionEvent e) {
        int microchip = Integer.parseInt(microchipInsert.getText());
        String name = animalName.getText();
        String race = animalRace.getText();
        String gender = animalGender.getValue();
        String cf = cfInsert.getText();

        if ( name.isEmpty() || microchipInsert.getText().isEmpty() || race.isEmpty() || animalBirthDate.getValue() == null || cf.isEmpty()) {
            showPopUp("Inserisci tutti i campi!", null, Alert.AlertType.WARNING);
        } else {
            if (padroneTable.findByPrimaryKey(cf).isEmpty()) {
                showPopUp("Padrone non esistente, perfavore registralo nella sezione Padroni!", null, Alert.AlertType.WARNING);
            } else {
                if (animaleTable.findByPrimaryKey(microchip).isPresent()) {
                    showPopUp("Animale già registrato!", null, Alert.AlertType.WARNING);
                } else {
                    Date birthDate = Utils.buildDate(animalBirthDate.getValue().getDayOfMonth(), animalBirthDate.getValue().getMonthValue(), animalBirthDate.getValue().getYear()).get();
                    Animale animale = new Animale(microchip, name, race, birthDate, gender, cf);
                    animaleTable.save(animale);
                    animalsList = FXCollections.observableArrayList(animaleTable.findAll());
                    animalTable.getItems().setAll(animalsList);
                }
            }
        }

    }

    public void onAnimalViewClick(final ActionEvent e) {
        animalsList = FXCollections.observableArrayList(animaleTable.showTopTen());
        animalTable.getItems().setAll(animalsList);
    }

    public void onAnimalVisitDayViewClick(final ActionEvent e) {

        if (microchipView.getText().isEmpty()) {
            showPopUp("Inserisci tutti i campi!", null, Alert.AlertType.WARNING);
        }

        int microchip = Integer.valueOf(microchipView.getText());
        Optional<Animale> animale = animaleTable.findByPrimaryKey(microchip);

        if (animale.isPresent()) {
            Date visitDay = cartellaClinicaTable.showNextVisit(microchip);
            if (visitDay == null) {
                showPopUp("Non ci sono visite registrate relative a questo animale!", null, Alert.AlertType.INFORMATION);
            } else {
                showPopUp("Prossima visita: " + Utils.dateToSqlDate(visitDay), null, Alert.AlertType.INFORMATION);
            }

        } else {
            showPopUp("Animale non trovato!", null, Alert.AlertType.WARNING);
        }


    }

}
