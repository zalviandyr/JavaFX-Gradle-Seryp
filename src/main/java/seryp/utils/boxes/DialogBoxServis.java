package seryp.utils.boxes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class DialogBoxServis extends SerypBox {
    private static String merekLabel = "";

    public static String display(String idPelanggan, int servisKe) {
        // GridPane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.getStyleClass().addAll("seryp-bg-blue", "seryp-round");

        // Set how many row
        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.SOMETIMES);
        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.SOMETIMES);
        RowConstraints row3 = new RowConstraints();
        row3.setVgrow(Priority.SOMETIMES);
        RowConstraints row4 = new RowConstraints();
        row4.setVgrow(Priority.SOMETIMES);

        // Set how many col
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100.0 / 3);
        col1.setHalignment(HPos.CENTER);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(100.0 / 3);
        col2.setHalignment(HPos.CENTER);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(100.0 / 3);
        col3.setHalignment(HPos.CENTER);

        gridPane.getColumnConstraints().addAll(col1, col2, col3);
        gridPane.getRowConstraints().addAll(row1, row2);

        // Label
        Label lblMsg = new Label("Masukkan merek dan label dari laptop pelanggan, \n sistem akan mengecek otomatis sudah berapa kali pelanggan tersebut menservis.");
        lblMsg.setTextAlignment(TextAlignment.CENTER);
        GridPane.setRowIndex(lblMsg, 0);
        GridPane.setColumnSpan(lblMsg, 3);

        Label lblMerek = new Label("Merek Laptop");
        GridPane.setRowIndex(lblMerek, 1);
        GridPane.setColumnIndex(lblMerek, 0);

        Label lblIdPelanggan = new Label("ID Pelanggan");
        GridPane.setRowIndex(lblIdPelanggan, 1);
        GridPane.setColumnIndex(lblIdPelanggan, 1);

        Label lblServisKe = new Label("Servis Ke");
        GridPane.setRowIndex(lblServisKe, 1);
        GridPane.setColumnIndex(lblServisKe, 2);

        // Text Field
        TextField txtMerek = new TextField();
        txtMerek.getStyleClass().add("seryp-round");
        GridPane.setRowIndex(txtMerek, 2);
        GridPane.setColumnIndex(txtMerek, 0);

        TextField txtIdPelanggan = new TextField();
        txtIdPelanggan.getStyleClass().add("seryp-round");
        txtIdPelanggan.setEditable(false);
        txtIdPelanggan.setText(idPelanggan);
        GridPane.setRowIndex(txtIdPelanggan, 2);
        GridPane.setColumnIndex(txtIdPelanggan, 1);

        TextField txtServisKe = new TextField();
        txtServisKe.getStyleClass().add("seryp-round");
        txtServisKe.setEditable(false);
        txtServisKe.setText(String.valueOf(servisKe));
        GridPane.setRowIndex(txtServisKe, 2);
        GridPane.setColumnIndex(txtServisKe, 2);

        // Button
        Button btnOk = new Button("Ok");
        btnOk.setPrefWidth(100);
        btnOk.getStyleClass().addAll("seryp-round", "seryp-btn-primary");
        GridPane.setRowIndex(btnOk, 3);
        GridPane.setColumnIndex(btnOk, 1);

        btnOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!(txtMerek.getText().equals(""))) {
                    Stage window = (Stage) btnOk.getScene().getWindow();
                    merekLabel = txtMerek.getText() + "-" + txtIdPelanggan.getText() + "-" + txtServisKe.getText();
                    window.close();
                }
            }
        });

        gridPane.getChildren().addAll(lblMsg, lblMerek, lblIdPelanggan, lblServisKe);
        gridPane.getChildren().addAll(txtMerek, txtIdPelanggan, txtServisKe);
        gridPane.getChildren().add(btnOk);
        setBox("Dialog Box", "Masukkan Merek Laptop", gridPane);

        return merekLabel;
    }
}
