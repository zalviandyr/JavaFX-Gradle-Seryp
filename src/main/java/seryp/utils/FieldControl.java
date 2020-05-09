package seryp.utils;

import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class FieldControl {
    public String getTextSelectedRadios(RadioButton... radioButtons) {
        String result = null;

        try {
            for (RadioButton radioButton : radioButtons) {
                if (radioButton.isSelected()) {
                    result = radioButton.getText();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void setTextFieldError(TextField ...textFields) {
        for (TextField textField : textFields) {
            textField.getStyleClass().add("seryp-text-field-error");
        }
    }

    public void setTextFieldNormalAction(TextField ...textFields) {
        for (TextField textField : textFields) {
            textField.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    textField.getStyleClass().remove("seryp-text-field-error");
                }
            });
        }
    }
}
