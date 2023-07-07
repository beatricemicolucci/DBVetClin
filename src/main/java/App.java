import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.WelcomePage;

import java.io.IOException;

public class App extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        System.out.println("ciao");
        //primaryStage.setResizable(true);
        primaryStage.setTitle("DBVetClin");

        final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("pages/WelcomePage.fxml"));
        Parent root = null;

        try {
            root = loader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        if (primaryStage.getScene() == null) {
            primaryStage.setScene(new Scene(root));
        } else {
            primaryStage.getScene().setRoot(root);
        }

        final WelcomePage view = loader.getController();
        view.setStage(primaryStage);
        view.init(primaryStage);
        primaryStage.show();
    }
}
