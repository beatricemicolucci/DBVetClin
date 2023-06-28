package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class WelcomePage extends AbstractView{

    private String url = "jdbc:mysql://localhost:3306/clinica_veterinaria";
    private String username = "root";
    private String password = "Beatrice02@";
    private Stage stage;
    @FXML
    private ImageView welcomeImage;

    public void init() {
        welcomeImage.setImage(new Image("/images/dogs.jpg"));

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            String scriptPath = "src/main/resources/scriptSQL/PROGETTO_CLINICA.ddl";
            StringBuilder script = new StringBuilder();
            String line;

            try (BufferedReader reader = new BufferedReader(new FileReader(scriptPath))) {
                while ((line = reader.readLine()) != null) {
                    script.append(line).append("\n");
                }
            }

            int rowsAffected = statement.executeUpdate(script.toString());

            System.out.println("Script executed successfully! Rows affected: " + rowsAffected);

        } catch (Exception e) {
            System.out.println("Error executing script: " + e.getMessage());
        }

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
