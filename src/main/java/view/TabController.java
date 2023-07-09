package view;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public abstract class  TabController {
    abstract void init();

    public void showPopUp(final String message, final String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        if (alert.isShowing()) {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.close();
        }
    }
}
