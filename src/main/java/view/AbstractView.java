package view;

import javafx.stage.Stage;

public class AbstractView {

    private Stage stage;

    public final void setStage(final Stage stage) {
        this.stage = stage;
    }

    public final Stage getStage() {
        return this.stage;
    }

}
