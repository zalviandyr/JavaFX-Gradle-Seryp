package seryp.utils.boxes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LaporanBulananBox extends SerypBox {
    private static String value;

    public static String display(){
        ComboBox<String> cboBulan = new ComboBox<>();
        ObservableList<String> listBulan = FXCollections.observableArrayList();
        listBulan.addAll("Januari", "Feburari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "November", "Oktober", "Desember");
        cboBulan.getStyleClass().add("seryp-round");
        cboBulan.setPromptText("Bulan");
        cboBulan.setItems(listBulan);

        ComboBox<Integer> cboTahun = new ComboBox<>();
        ObservableList<Integer> listTahun = FXCollections.observableArrayList();
        listTahun.addAll(2020, 2021, 2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030);
        cboTahun.getStyleClass().add("seryp-round");
        cboTahun.setPromptText("Tahun");
        cboTahun.setItems(listTahun);

        Button btnOk = new Button("Ok");
        btnOk.setPrefWidth(100);
        btnOk.getStyleClass().add("seryp-btn-primary");
        btnOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (cboBulan.getValue() != null && cboTahun.getValue() != null) {
                    Stage window = (Stage) btnOk.getScene().getWindow();
                    value = cboBulan.getValue() + "|" + (cboBulan.getSelectionModel().getSelectedIndex() + 1) + "|" + cboTahun.getValue();
                    window.close();
                }
            }
        });

        HBox cboContainer = new HBox();
        cboContainer.setSpacing(10);
        cboContainer.setAlignment(Pos.CENTER);
        cboContainer.getChildren().addAll(cboBulan, cboTahun);

        setBox("Laporan Bulanan Box", "Masukkan bulan dan tahun seryp.laporan!", cboContainer, btnOk);
        return value;
    }
}
