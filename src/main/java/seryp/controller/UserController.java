package seryp.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import seryp.model.User;
import seryp.model.dao.UserDao;
import seryp.utils.SerypUtil;
import seryp.utils.boxes.*;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UserController extends SerypUtil implements Initializable {
    public HBox topBar;
    public VBox sideBar;
    public VBox settingBar;
    public Label lblFile;
    public Button btnBack;
    public Button btnUpdate;
    public Button btnAdd;
    public Button btnDelete;
    public Button btnCari;
    public Button btnFileChoose;
    public Button btnRemoveRedEye;
    public ToggleButton toggleBtnCariUser;
    public ToggleButton toggleBtnSetting;
    public TitledPane paneCariUser;
    public TitledPane paneSetting;
    public TextField txtUsername;
    public TextField txtPassword2;
    public PasswordField txtPassword;
    public TextField txtNama;
    public TextField txtNoHandphone;
    public TextField txtCariUser;
    public TextArea txtAreaAlamat;
    public DatePicker datePickerTanggalLahir;
    public RadioButton rbLaki;
    public RadioButton rbPerempuan;
    public ComboBox<String> cboStatusUser;
    public ComboBox<User> cboResult;
    private UserDao userDao;
    public static User userLogin; // data yang dikirim dari AdminController

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // instance object object yang diperlukan
        userDao = new UserDao();

        // set topBar
        getWindowControl().setTopBar(topBar);

        // set sideBar
        getWindowControl().setSideBar(sideBar, UserController.userLogin);

        // set paneSetting
        getWindowControl().setPaneSetting(toggleBtnSetting, paneSetting, settingBar);

        // Mengelompokkan radio button
        ToggleGroup jekelGroup = new ToggleGroup();
        rbLaki.setToggleGroup(jekelGroup);
        rbPerempuan.setToggleGroup(jekelGroup);

        // Set default Radio Button
        rbLaki.setSelected(true);

        // Set tanggal default hari ini
        datePickerTanggalLahir.setValue(LocalDate.now());
        datePickerTanggalLahir.setEditable(false);

        // Set combo box statusKaryawan
        getFieldControl().setComboBoxStatus(cboStatusUser, "Status User", "Admin", "Karyawan Aktif", "Karyawan Tidak Aktif");

        // set button update and delete invisible
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);

        // set text field password invisible
        txtPassword2.setVisible(false);

        // Set pane cari user and pane setting unexpanded and animated
        paneCariUser.setAnimated(true);
        paneCariUser.setExpanded(false);
    }

    @FXML
    void btnCariAction() {
        ObservableList<User> observableList;

        try {
            if (txtCariUser.getText().equals("")) {
                observableList = userDao.searchAll();
            } else {
                String keyword = txtCariUser.getText();
                observableList = userDao.search(keyword);
            }

            setComboBoxResult(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cboResultAction() {
        /*
         * Avoid error null pointer
         * Karena pada saat setelah melakukan pencarian lagi maka cboResult akan tereksekusi otomatis (masih belum tau kenapa),
         * yang mengakibatkan adanya nilai kembalian yang null
         */

        try {
            cleanField();
            // cboResult bisa saja mengembalikan nilai null, maka dari itu memakai exception
            User user = cboResult.getValue(); // possible NullPointer
            String username = user.getUsername();

            user = userDao.get(username);
            txtUsername.setText(user.getUsername());
            txtPassword.setText(user.getPassword());
            txtNama.setText(user.getNama());
            txtNoHandphone.setText(String.valueOf(user.getNoHp()));
            txtAreaAlamat.setText(user.getAlamat());
            datePickerTanggalLahir.setValue(user.getTanggalLahir());
            cboStatusUser.setValue(user.getStatusUser());

            // set file handler
            setFileHandler(new File(user.getFotoProfil()));
            getFileHandler().setPathCopy(username);
            lblFile.setText(getFileHandler().getFileName());

            if (user.getJekel().equals("Laki laki")) {
                rbLaki.setSelected(true);
            }
            if (user.getJekel().equals("Perempuan")) {
                rbPerempuan.setSelected(true);
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("Ada yang null nih!");
//                    e.printStackTrace();
        }
    }

    @FXML
    void toggleBtnCariUserAction() {
        // cleaning
        txtCariUser.setText("");
        getFieldControl().cleanComboBoxResult(cboResult);

        if (toggleBtnCariUser.isSelected()) {
            // change style button
            toggleBtnCariUser.getStyleClass().remove("seryp-btn-primary");
            toggleBtnCariUser.getStyleClass().add("seryp-btn-secondary");

            btnUpdate.setVisible(true);
            btnAdd.setVisible(false);
            btnDelete.setVisible(true);
            txtPassword.setEditable(false); // set password field cannot edit
            txtPassword2.setEditable(false); // set text field cannot edit
            txtUsername.setEditable(false); // set username cannot edit
            paneCariUser.setExpanded(true);
        } else {
            // change style button
            toggleBtnCariUser.getStyleClass().remove("seryp-btn-secondary");
            toggleBtnCariUser.getStyleClass().add("seryp-btn-primary");

            btnUpdate.setVisible(false);
            btnAdd.setVisible(true);
            btnDelete.setVisible(false);
            txtPassword.setEditable(true); // set password field cannot edit
            txtPassword2.setEditable(true); // set text field cannot edit
            txtUsername.setEditable(true); // set username editable
            paneCariUser.setExpanded(false);
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
        if (!txtPassword.getText().equals("")) {
            String password = txtPassword2.getText();
            // hidden text field password
            txtPassword2.setVisible(false);

            // show password field
            txtPassword.setText(password);
            txtPassword.setVisible(true);
        }
    }

    @FXML
    void btnFileChooseAction() {
        FileChooser fileChooser = new FileChooser();
        // set filter extension
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File fileFoto = fileChooser.showOpenDialog(new Stage());

        if (fileFoto != null) {
            // set file handler require
            setFileHandler(fileFoto);
            getFileHandler().setPathCopy(txtUsername.getText());
            lblFile.setText(getFileHandler().getFileName());
        }
    }

    @FXML
    void btnUpdateAction() {
        try {
            User user = updateAdd("Update");

            userDao.update(user);
            if (getFileHandler().getFile() != null) // jika file null tidak akan mengkopi file
                if (!(user.getFotoProfil().equals(getFileHandler().getFile().getAbsolutePath())))
                    // jika path file sama dengan yang didatabase tidak akan dikopi, dikarenakan jika update maka dia akan mengkopi diri ny sendiri dan menimpa nya
                    getFileHandler().copyFileToPath(); // copy file
            cleanField();
            getFieldControl().cleanComboBoxResult(cboResult);
            getFieldControl().cleanComboBoxStatus(cboStatusUser);

            // combo box harus di set ulang agar item-item selain yang di set tidak hilang
            getFieldControl().setComboBoxStatus(cboStatusUser, "Status User", "Admin", "Karyawan Aktif", "Karyawan Tidak Aktif");

            AlertBox.display("Berhasil Update", "Berhasil update data");
        } catch (SQLException | NullPointerException e) {
//                    e.printStackTrace();
            AlertBox.display("Gagal Update", "Gagal untuk mengupdate data");
        }
    }

    @FXML
    void btnAddAction() {
        try {
            User user = updateAdd("Add");

            userDao.add(user);
            if (getFileHandler().getFile() != null) // jika file null tidak akan mengkopi file
                getFileHandler().copyFileToPath(); // copy file
            cleanField();
            getFieldControl().cleanComboBoxStatus(cboStatusUser);

            // combo box harus di set ulang agar item-item selain yang di set tidak hilang
            getFieldControl().setComboBoxStatus(cboStatusUser, "Status User", "Admin", "Karyawan Aktif", "Karyawan Tidak Aktif");

            AlertBox.display("Berhasil", "Penambahan user baru berhasil");
        } catch (SQLException | NullPointerException e) {
//                    e.printStackTrace();
            AlertBox.display("Gagal", "Penambahan user baru Gagal. \nUsername harus unik");
        }
    }

    @FXML
    void btnDeleteAction() {
        try {
            // cek jika username kosong, maka tidak ada penghapusan user
            if (!(txtUsername.getText().equals(""))) {
                boolean option = ConfirmBox.display("Apakah yakin menghapus user ?", "Jika user tersebut dihapus maka transaksi yang melalui karyawan/user tersebut akan terhapus. \n\n Lebih baik menonaktifkan karyawan tersebut dengan cara merubah statusnya 'Karyawan Tidak Akfif'");
                String username = txtUsername.getText();

                // cek option
                if (option) {
                    userDao.delete(username);

                    cleanField();
                    getFieldControl().cleanComboBoxResult(cboResult);
                    getFieldControl().cleanComboBoxStatus(cboStatusUser);
                    AlertBox.display("Berhasil Delete", "Berhasil menghapus data");
                }
            }
        } catch (SQLException e) {
            AlertBox.display("Gagal Delete", "Gagal menghapus data");
//                    e.printStackTrace();
        }
    }

    @FXML
    void btnBackAction() {
        getWindowControl().moveToScene(btnBack, "admin");
    }

    void cleanField() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtNoHandphone.setText("");
        txtNama.setText("");
        lblFile.setText("File");

        if (rbPerempuan.isSelected()) {
            rbLaki.setSelected(true);
        }

        datePickerTanggalLahir.setValue(LocalDate.now());
        txtAreaAlamat.setText("");
    }

    User updateAdd(String aksi) {
        User user = null;
        if (!(txtUsername.getText().equals("") && (txtPassword.getText().equals("") || txtPassword.getText().equals("")))) {
            // password dibagi menjadi dua karena, password user tidak bisa diganti admin
            String password = "";
            if (aksi.equals("Add")) {
                password = getUtil().md5Hash(txtPassword.getText());
            }
            if (aksi.equals("Update")) {
                password = txtPassword.getText();
            }
            String username = txtUsername.getText();
            String noHandphone = getValidation().validateNoHandphone(txtNoHandphone.getText());
            String nama = txtNama.getText();
            String fotoProfil = getFileHandler().getPathCopy();
            String jekel = getFieldControl().getTextSelectedRadios(rbLaki, rbPerempuan);
            LocalDate tanggalLahir = datePickerTanggalLahir.getValue();
            String alamat = txtAreaAlamat.getText();
            String statusKaryawan = cboStatusUser.getValue();

            user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setNoHp(noHandphone);
            user.setNama(nama);
            user.setFotoProfil(fotoProfil);
            user.setJekel(jekel);
            user.setTanggalLahir(tanggalLahir);
            user.setAlamat(alamat);
            user.setStatusUser(statusKaryawan);
            user.setLastLogin(LocalDate.now());
            user.setCreated(LocalDate.now());
        }

        return user;
    }

    void setComboBoxResult(ObservableList<User> observableList) {
        Callback<ListView<User>, ListCell<User>> callback = new Callback<ListView<User>, ListCell<User>>() {
            @Override
            public ListCell<User> call(ListView<User> param) {
                ListCell<User> cell = new ListCell<>(){
                    @Override
                    protected void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!empty) {
                            setText(item.getNama());
                        } else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        cboResult.setPromptText("Search Result Here!");
        cboResult.setItems(observableList);
        cboResult.setCellFactory(callback);
        cboResult.setButtonCell(callback.call(null));
    }
}
