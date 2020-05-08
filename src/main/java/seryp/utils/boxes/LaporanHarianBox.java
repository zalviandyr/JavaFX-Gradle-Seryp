package seryp.utils.boxes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.time.LocalDate;

public class LaporanHarianBox extends SerypBox {
    private static String value;

    public static String display() {
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.getStyleClass().add("seryp-date-picker");

        Button btnOk = new Button("Ok");
        btnOk.setPrefWidth(100);
        btnOk.getStyleClass().add("seryp-btn-primary");
        btnOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage window = (Stage) btnOk.getScene().getWindow();
                value = datePicker.getValue().toString();
                window.close();
            }
        });

        setBox("Laporan Harian Box", "Masukkan tanggal seryp.laporan!", datePicker, btnOk);
        return value;
    }
}
