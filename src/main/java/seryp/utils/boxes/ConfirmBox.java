package seryp.utils.boxes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ConfirmBox extends SerypBox {
    static boolean answer;

    public static boolean display(String title, String message) {
        HBox btnContainer = new HBox();
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.setSpacing(20);

        Button btnYes = new Button("Yes");
        btnYes.setPrefWidth(80);
        btnYes.getStyleClass().addAll("seryp-round", "seryp-btn-primary");
        btnYes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage window = (Stage) btnYes.getScene().getWindow();
                answer = true;
                window.close();
            }
        });

        Button btnNo = new Button("Tidak");
        btnNo.setPrefWidth(80);
        btnNo.getStyleClass().addAll("seryp-round", "seryp-btn-primary");
        btnNo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage window = (Stage) btnNo.getScene().getWindow();
                answer = false;
                window.close();
            }
        });

        btnContainer.getChildren().addAll(btnYes, btnNo);
        setBox(title, message, btnContainer);

        return answer;
    }
}
