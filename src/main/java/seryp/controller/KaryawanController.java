package seryp.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seryp.model.User;
import seryp.utils.SerypUtil;
import seryp.utils.boxes.ConfirmBox;

import java.net.URL;
import java.util.ResourceBundle;

public class KaryawanController extends SerypUtil implements Initializable {
    public HBox topBar;
    public VBox sideBar;
    public Button btnTransaksi;
    public Button btnPembayaran;
    public Button btnLogOut;
    public static User userLogin; // data yang dikirim LoginController

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
        btnTransaksiAction();
        btnPembayaranAction();
        btnLogOutAction();
    }

    private void init() {
        // set topBar
        getWindowControl().setTopBar(topBar);

        // set sideBar
        getWindowControl().setSideBar(sideBar, KaryawanController.userLogin);

        // init user and send to another scene
        PelangganController.userLogin = KaryawanController.userLogin;
        PembayaranController.userLogin = KaryawanController.userLogin;
        ServiceController.userLogin = KaryawanController.userLogin;
        ListController.userLogin = KaryawanController.userLogin;
    }

    private void btnTransaksiAction() {
        btnTransaksi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getWindowControl().moveToScene(btnTransaksi, "pelanggan");
            }
        });
    }

    private void btnPembayaranAction() {
        btnPembayaran.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getWindowControl().moveToScene(btnPembayaran, "pembayaran");
            }
        });
    }

    private void btnLogOutAction() {
        btnLogOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                boolean option = ConfirmBox.display("Yakin ?", "Apakah yakin mau keluar ?");

                if (option) {
                    getWindowControl().moveToScene(btnLogOut, "login");
                }
            }
        });
    }
}
