package seryp;

import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.application.Application;
import seryp.utils.SerypUtil;

public class SerypFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        try {
            // Load Custom Font
            Font.loadFont(getClass().getResourceAsStream("assets/font/Ubuntu-B.ttf"), 14);
            /* Load Custom Font and getName();
            Font font = Font.loadFont(getClass().getResourceAsStream("seryp.view/assets/font/Ubuntu-B.ttf"), 14);
            System.out.println(font.getName());
             */
            Parent root = FXMLLoader.load(getClass().getResource("/seryp/layout/Splash.fxml"));

            Scene scene = new Scene(root);
            stage.setTitle("Seryp");
            stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/seryp/assets/icon/computer-logo-png-8.ico"))));
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
