package seryp.controller;

import javafx.fxml.FXML;
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
        // set topBar
        getWindowControl().setTopBar(topBar);

        // set sideBar
        getWindowControl().setSideBar(sideBar, KaryawanController.userLogin);

        // set effect inner shadow button
        getUtil().setEffectInnerShadow(btnTransaksi, btnPembayaran);

        // init user and send to another scene
        PelangganController.userLogin = KaryawanController.userLogin;
        PembayaranController.userLogin = KaryawanController.userLogin;
        ServiceController.userLogin = KaryawanController.userLogin;
        ListController.userLogin = KaryawanController.userLogin;
    }

    @FXML
    void btnPembayaranAction() {
        getWindowControl().moveToScene(btnPembayaran, "pembayaran");
    }

    @FXML
    void btnTransaksiAction() {
        getWindowControl().moveToScene(btnTransaksi, "pelanggan");
    }

    @FXML
    void btnLogOutAction() {
        boolean option = ConfirmBox.display("Yakin ?", "Apakah yakin mau keluar ?");

        if (option) {
            getWindowControl().moveToScene(btnLogOut, "login");
        }
    }
}
