package seryp.utils;

import javafx.scene.control.RadioButton;

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
}
