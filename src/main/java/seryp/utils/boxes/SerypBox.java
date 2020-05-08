package seryp.utils.boxes;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SerypBox {
    private static double xOffset = 0;
    private static double yOffset = 0;

    protected static void setBox(String title, String message, Node...komponen) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);

        Label lblTitle = new Label(title);
        Label lblMessage = new Label(message);
        lblMessage.setTextAlignment(TextAlignment.CENTER);
        lblMessage.setWrapText(true);

        HBox topBar = new HBox();
        topBar.setPrefHeight(30);
        topBar.setPadding(new Insets(0, 10, 0, 10));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getStyleClass().addAll("seryp-bg-blue", "seryp-round", "seryp-shadow");
        topBar.getChildren().add(lblTitle);

        topBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                xOffset = window.getX() - mouseEvent.getScreenX();
                yOffset = window.getY() - mouseEvent.getScreenY();
            }
        });

        topBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                window.setX(mouseEvent.getScreenX() + xOffset);
                window.setY(mouseEvent.getScreenY() + yOffset);
            }
        });

        VBox body = new VBox();
        body.setPadding(new Insets(10, 10, 10, 10));
        body.setAlignment(Pos.CENTER);
        body.setSpacing(20);
        body.getStyleClass().addAll("seryp-bg-blue", "seryp-round", "seryp-shadow");
        body.getChildren().add(lblMessage);
        body.getChildren().addAll(komponen);

        VBox mainContainer = new VBox();
        mainContainer.getStylesheets().add(String.valueOf(SerypBox.class.getResource("/seryp/assets/style.css")));
        mainContainer.getStyleClass().add("seryp-bg-transparent");
        mainContainer.getChildren().addAll(topBar, body);
        VBox.setMargin(topBar, new Insets(10, 10, 5, 10));
        VBox.setMargin(body, new Insets(5, 10, 10, 10));

        Scene scene = new Scene(mainContainer);

        scene.setFill(Color.TRANSPARENT);
        window.initStyle(StageStyle.TRANSPARENT);
        window.setScene(scene);
        window.showAndWait();
    }
}
