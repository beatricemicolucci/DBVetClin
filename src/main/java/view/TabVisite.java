package view;

import db.ConnectionProvider;
import db.tables.VeterinarioTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Visita;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class TabVisite extends TabController {

    private ConnectionProvider connectionProvider;
    private VeterinarioTable veterinarioTable;
    private List<Visita> visitsList;

    @FXML
    private TextField idVetVisits;

    @FXML
    private DatePicker dateVisits;

    @FXML
    private TableView visitsTable;

    public void init() {
        connectionProvider = new ConnectionProvider();
        veterinarioTable = new VeterinarioTable(connectionProvider.getMySQLConnection());
        visitsList = new ArrayList<>();
    }

    public void onShowVetVisitsClick(final ActionEvent actionEvent) {
        final int idVet = Integer.valueOf(idVetVisits.getText());
        final Date date = Utils.buildDate(dateVisits.getValue().getDayOfMonth(), dateVisits.getValue().getMonthValue(), dateVisits.getValue().getYear()).get();

        visitsList = veterinarioTable.showVetVisits(idVet, date);
        visitsTable.getItems().setAll(visitsList);
    }
}
