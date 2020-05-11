package seryp.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import seryp.model.Barang;

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

    public void cleanComboBoxResult(ComboBox<?> comboBox) {
        comboBox.getItems().clear();
    }

    public void setComboBoxStatus (ComboBox<String> comboBoxStatus, String promptText, String ...items) {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(items);

        comboBoxStatus.setPromptText(promptText);
        comboBoxStatus.setItems(observableList);
    }

    public void cleanComboBoxStatus(ComboBox<String> comboBoxStatus) {
        // Fungsi dari ini adalah agar kalau pas selesai clear combo box prompt text ny tidak hilang
        ListCell<String> listCell = new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText(item);
                }
            }
        };

        comboBoxStatus.getItems().clear();
        comboBoxStatus.setButtonCell(listCell);
    }
}
