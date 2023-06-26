package view;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DBView extends AbstractView{

    @FXML
    private TabPane tabPane;

    private TabPadroni tabPadroniController;

    public void init(){
        tabPadroniController = (TabPadroni) tabPane.getTabs().get(0).getUserData();
    }
}
