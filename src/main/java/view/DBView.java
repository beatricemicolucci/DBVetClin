package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

import java.io.IOException;

public class DBView extends AbstractView{

    @FXML
    private Tab tabPadroni;

    @FXML
    private Tab tabAnimali;

    @FXML
    private Tab tabInterventi;

    @FXML
    private Tab tabVisite;

    @FXML
    private Tab tabTerapie;

    @FXML
    private Tab tabVeterinari;

    @FXML
    private Tab tabFatture;

    @FXML
    public void init() throws IOException {
        super.getStage().sizeToScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/Padroni.fxml"));
        tabPadroni.setContent(loader.load());
        TabPadroni padroniController = loader.getController();
        padroniController.init();

        loader = new FXMLLoader(getClass().getResource("/pages/Animali.fxml"));
        tabAnimali.setContent(loader.load());
        TabAnimali animaliController = loader.getController();
        animaliController.init();

        loader = new FXMLLoader(getClass().getResource("/pages/Interventi.fxml"));
        tabInterventi.setContent(loader.load());
        TabInterventi interventiController = loader.getController();
        interventiController.init();

        loader = new FXMLLoader(getClass().getResource("/pages/Visite.fxml"));
        tabVisite.setContent(loader.load());
        TabVisite visiteController = loader.getController();
        visiteController.init();

        loader = new FXMLLoader(getClass().getResource("/pages/Terapie.fxml"));
        tabTerapie.setContent(loader.load());
        TabTerapie terapieController = loader.getController();
        terapieController.init();

        loader = new FXMLLoader(getClass().getResource("/pages/Veterinari.fxml"));
        tabVeterinari.setContent(loader.load());
        TabVeterinari veterinariController = loader.getController();
        veterinariController.init();

        loader = new FXMLLoader(getClass().getResource("/pages/Fatture.fxml"));
        tabFatture.setContent(loader.load());
        TabFatture fattureController = loader.getController();
        fattureController.init();

    }

}
