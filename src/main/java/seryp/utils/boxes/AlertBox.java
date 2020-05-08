package seryp.utils.boxes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AlertBox extends SerypBox{
    public static void display(String title, String message) {
        Button btnOk = new Button("Ok");
        btnOk.setPrefWidth(100);
        btnOk.getStyleClass().add("seryp-btn-primary");
        btnOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage)btnOk.getScene().getWindow();
                stage.close();
            }
        });

        setBox(title, message, btnOk);
    }
}
