package view;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

public class DBView extends AbstractView{

    @FXML
    private TabPane tabPane;

    private TabPadroni tabPadroniController;
    private TabAnimali tabAnimaliController;

    public void init(){
        tabPadroniController = (TabPadroni) tabPane.getTabs().get(0).getUserData();
        tabAnimaliController = (TabAnimali) tabPane.getTabs().get(1).getUserData();
    }
}
