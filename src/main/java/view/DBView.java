package view;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

public class DBView extends AbstractView{

    @FXML
    private TabPane tabPane;

    private TabPadroni tabPadroniController;
    private TabAnimali tabAnimaliController;
    private TabInterventi tabInterventiController;
    private TabVisite tabVisiteController;
    private TabTerapie tabTerapieController;

    public void init(){
        super.getStage().sizeToScene();
        tabPadroniController = (TabPadroni) tabPane.getTabs().get(0).getUserData();
        tabAnimaliController = (TabAnimali) tabPane.getTabs().get(1).getUserData();
        tabInterventiController = (TabInterventi) tabPane.getTabs().get(2).getUserData();
        tabVisiteController = (TabVisite) tabPane.getTabs().get(3).getUserData();
        tabTerapieController = (TabTerapie) tabPane.getTabs().get(4).getUserData();
    }
}
