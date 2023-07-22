package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class WelcomePage extends AbstractView{

    private String url = "jdbc:mysql://localhost:3306/clinica_veterinaria";
    private String username = "root";
    private String password = "Beatrice02@";
    private Stage stage;
    private String fileSeparator = System.getProperty("file.separator");
    @FXML
    private ImageView welcomeImage;

    public void init(Stage stage) {
        welcomeImage.setImage(new Image((getClass().getResource("/images/dogs.jpg")).toExternalForm()));

        //welcomeImage.setImage(new Image(fileSeparator + "images" + fileSeparator + "dogs.jpg"));
        this.stage = stage;
    }

    @FXML
    public void onEnterClick(final ActionEvent e) throws IOException {
        final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("pages/dbPage.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        if (this.getStage().getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        final DBView view = loader.getController();
        view.setStage(stage);
        view.init();

        stage.show();
    }

}
