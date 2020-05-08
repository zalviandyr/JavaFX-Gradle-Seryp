package seryp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import seryp.model.IdentitasToko;
import seryp.model.User;
import seryp.model.dao.IdentitasTokoDao;
import seryp.model.dao.InstalasiDao;
import seryp.model.dao.UserDao;
import seryp.utils.SerypUtil;
import seryp.utils.boxes.AlertBox;
import seryp.utils.boxes.ConfirmBox;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class InstalasiController extends SerypUtil implements Initializable {
    public HBox topBar;
    public Button btnKonfirmasi;
    public Button btnClose;
    public TextField txtNamaToko;
    public TextArea txtAreaAlamatToko;
    public TextArea txtAreaFotoProfilPath;
    public TextField txtUsername;
    public TextField txtPassword2;
    public TextField txtNama;
    public TextField txtNoHandphone;
    public PasswordField txtPassword;
    public TextArea txtAreaAlamatUser;
    public RadioButton rbLaki;
    public RadioButton rbPerempuan;
    public DatePicker datePickerTanggalLahir;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    private void init() {
        // init radio button
        ToggleGroup toggleGroup = new ToggleGroup();
        rbLaki.setToggleGroup(toggleGroup);
        rbPerempuan.setToggleGroup(toggleGroup);
        rbLaki.setSelected(true);

        // set topBar
        getWindowControl().setTopBar(topBar);

        // init date picker
        datePickerTanggalLahir.setValue(LocalDate.now());

        // set txtAreaFotoProfilPath non editable
        txtAreaFotoProfilPath.setEditable(false);

        // set invisible txt password2
        txtPassword2.setVisible(false);
    }

    void createDatabaseSeryp() {
        InstalasiDao instalasiDao = new InstalasiDao();
        try {
            instalasiDao.createTableBarang();
            instalasiDao.createTableIdentitasToko();
            instalasiDao.createTableKerusakan();
            instalasiDao.createTablePelanggan();
            instalasiDao.createTableUser();
            instalasiDao.createTableServis();
            instalasiDao.createTableDetailKerusakan();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void fotoProfilPathAction() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(new Stage());

        // path img yang akan dibuat dan disimpan ke database
        if (file != null) {
            String pathImg = file.getAbsolutePath() + File.separator + "Seryp IMG";
            txtAreaFotoProfilPath.setText(pathImg);
        }
    }

    @FXML
    void btnRemoveRedEyePressed() {
        if (!txtPassword.getText().equals("")) {
            String password = txtPassword.getText();
            // hidden password field
            txtPassword.setVisible(false);

            // show text field password
            txtPassword2.setText(password);
            txtPassword2.setVisible(true);
        }
    }

    @FXML
    void btnRemoveRedEyeReleased() {
        String password = txtPassword2.getText();
        // hidden text field password
        txtPassword2.setVisible(false);

        // show password field
        txtPassword.setText(password);
        txtPassword.setVisible(true);
    }

    @FXML
    void btnKonfirmasiAction() {
        String namaToko = txtNamaToko.getText();
        String alamatToko = txtAreaAlamatToko.getText();
        String fotoProfilPath = txtAreaFotoProfilPath.getText();

        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String nama = txtNama.getText();
        String noHandphone = getValidation().validateNoHandphone(txtNoHandphone.getText());
        String jekel = getFieldControl().getTextSelectedRadios(rbLaki, rbPerempuan);
        LocalDate tanggalLahir = datePickerTanggalLahir.getValue();
        String alamat = txtAreaAlamatUser.getText();
        String fotoProfil = ""; // set foto profil user kosong saat instalasi

        try {
            IdentitasToko identitasToko = new IdentitasToko();
            identitasToko.setNamaToko(namaToko);
            identitasToko.setAlamat(alamatToko);
            identitasToko.setFotoProfilPath(fotoProfilPath);

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setNoHp(noHandphone);
            user.setNama(nama);
            user.setFotoProfil(fotoProfil);
            user.setJekel(jekel);
            user.setTanggalLahir(tanggalLahir);
            user.setAlamat(alamat);
            user.setStatusUser("Admin");
            user.setCreated(LocalDate.now());

            boolean konfirmasi = ConfirmBox.display("Konfirmasi Data", "Anda yakin ?\nPeriksa kembali data yang telah diinputkan!");
            if (konfirmasi) {
                // create database seryp
                createDatabaseSeryp();

                // create directory foto profil
                File file = new File(fotoProfilPath);
                file.mkdir();

                IdentitasTokoDao identitasTokoDao = new IdentitasTokoDao();
                UserDao userDao = new UserDao();
                identitasTokoDao.add(identitasToko);
                userDao.add(user);

                AdminController.userLogin = user;
                getWindowControl().moveToScene(btnKonfirmasi, "admin");
            }
        } catch (SQLException | NullPointerException e) {
//            e.printStackTrace();
            AlertBox.display("Instalasi Gagal", "Periksa kembali data-data apakah sudah benar!");
        }
    }
}