package seryp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import seryp.model.User;
import seryp.model.dao.IdentitasTokoDao;
import seryp.model.dao.UserDao;
import seryp.utils.SerypUtil;
import seryp.utils.boxes.*;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
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
    public ToggleButton btnCariUser;
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
    public ComboBox<String> cboResult;
    private UserDao userDao;
    private IdentitasTokoDao identitasTokoDao;
    private User user;
    public static User userLogin; // data yang dikirim dari AdminController

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
        btnCariAction();
        btnCariUserAction();
        btnRemoveRedEyeAction();
        btnUpdateAction();
        btnAddAction();
        btnDeleteAction();
        btnBackAction();
        btnFileChooseAction();
        toggleBtnSettingAction();
        cboResultAction();
    }

    private void init() {
        // instance object object yang diperlukan
        identitasTokoDao = new IdentitasTokoDao();
        userDao = new UserDao();

        // set topBar
        getWindowControl().setTopBar(topBar);

        // set sideBar
        getWindowControl().setSideBar(sideBar, UserController.userLogin);

        // set settingBar
        try {
            getWindowControl().setSettingBar(settingBar, identitasTokoDao.get());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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
        setComboBoxStatusUser();

        // set button update and delete invisible
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);

        // set text field password invisible
        txtPassword2.setVisible(false);

        // Set pane cari user and pane setting unexpanded and animated
        paneCariUser.setAnimated(true);
        paneCariUser.setExpanded(false);

        paneSetting.setAnimated(true);
        paneSetting.setExpanded(false);
    }

    private void btnCariAction() {
        btnCari.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ObservableList<String> observableList;

                try {
                    if (txtCariUser.getText().equals("")) {
                        observableList = convertToObservableList(userDao.searchAll());
                    } else {
                        String keyword = txtCariUser.getText();
                        observableList = convertToObservableList(userDao.search(keyword));
                    }

                    setComboBoxResult(observableList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void btnCariUserAction() {
        btnCariUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // cleaning
                txtCariUser.setText("");
                cleanComboBoxResult();

                if (btnCariUser.isSelected()) {
                    // change style button
                    btnCariUser.getStyleClass().remove("seryp-btn-primary");
                    btnCariUser.getStyleClass().add("seryp-btn-secondary");
                    btnUpdate.setVisible(true);
                    btnAdd.setVisible(false);
                    btnDelete.setVisible(true);
                    txtUsername.setEditable(false); // set username cannot edit
                    paneCariUser.setExpanded(true);
                } else {
                    // change style button
                    btnCariUser.getStyleClass().remove("seryp-btn-secondary");
                    btnCariUser.getStyleClass().add("seryp-btn-primary");
                    btnUpdate.setVisible(false);
                    btnAdd.setVisible(true);
                    btnDelete.setVisible(false);
                    txtUsername.setEditable(true); // set username editable
                    paneCariUser.setExpanded(false);
                }
            }
        });
    }

    private void btnRemoveRedEyeAction() {
        btnRemoveRedEye.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!txtPassword.getText().equals("")) {
                    String password = txtPassword.getText();
                    // hidden password field
                    txtPassword.setVisible(false);

                    // show text field password
                    txtPassword2.setText(password);
                    txtPassword2.setVisible(true);
                }
            }
        });

        btnRemoveRedEye.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String password = txtPassword2.getText();
                // hidden text field password
                txtPassword2.setVisible(false);

                // show password field
                txtPassword.setText(password);
                txtPassword.setVisible(true);
            }
        });
    }

    private void btnUpdateAction() {
        btnUpdate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                user = new User();

                String username = txtUsername.getText();
                String password = txtPassword.getText();
                String noHandphone = getValidation().validateNoHandphone(txtNoHandphone.getText());
                String nama = txtNama.getText();
                String fotoProfil = getFileHandler().getPathCopy();
                String jekel = getFieldControl().getTextSelectedRadios(rbLaki, rbPerempuan);
                LocalDate tanggalLahir = datePickerTanggalLahir.getValue();
                String alamat = txtAreaAlamat.getText();
                String statusKaryawan = cboStatusUser.getValue();

                try {
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setNoHp(noHandphone);
                    user.setNama(nama);
                    user.setFotoProfil(fotoProfil);
                    user.setJekel(jekel);
                    user.setTanggalLahir(tanggalLahir);
                    user.setAlamat(alamat);
                    user.setStatusUser(statusKaryawan);

                    userDao.update(user);
                    if (getFileHandler().getFile() != null) // jika file null tidak akan mengkopi file
                        getFileHandler().copyFileToPath(); // copy file
                    cleanField();
                    cleanComboBoxResult();
                    cleanComboBoxStatusUser();
                    AlertBox.display("Berhasil Update", "Berhasil update data");
                } catch (SQLException | NullPointerException e) {
                    e.printStackTrace();
                    AlertBox.display("Gagal Update", "Gagal untuk mengupdate data");
                }
            }
        });
    }

    private void btnAddAction() {
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String username = txtUsername.getText();
                String password = txtPassword.getText();
                String noHandphone = getValidation().validateNoHandphone(txtNoHandphone.getText());
                String nama = txtNama.getText();
                String fotoProfil = getFileHandler().getPathCopy();
                String jekel = getFieldControl().getTextSelectedRadios(rbLaki, rbPerempuan);
                LocalDate tanggalLahir = datePickerTanggalLahir.getValue();
                String alamat = txtAreaAlamat.getText();
                String statusKaryawan = cboStatusUser.getValue();

                try {
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
                    user.setCreated(LocalDate.now());

                    userDao.add(user);
                    if (getFileHandler().getFile() != null) // jika file null tidak akan mengkopi file
                        getFileHandler().copyFileToPath(); // copy file
                    cleanField();
                    cleanComboBoxStatusUser();
                    AlertBox.display("Berhasil", "Penambahan user baru berhasil");
                } catch (SQLException | NullPointerException e) {
//                    e.printStackTrace();
                    AlertBox.display("Gagal", "Penambahan user baru Gagal. \nUsername harus unik");
                }
            }
        });
    }

    private void btnDeleteAction() {
        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // cek jika username kosong, maka tidak ada penghapusan user
                    if (!(txtUsername.getText().equals(""))) {
                        boolean option = ConfirmBox.display("Apakah yakin menghapus user ?", "Jika user tersebut dihapus maka transaksi yang melalui karyawan/user tersebut akan terhapus. \n\n Lebih baik menonaktifkan karyawan tersebut dengan cara merubah statusnya 'Karyawan Tidak Akfif'");
                        String username = txtUsername.getText();

                        // cek option
                        if (option) {
                            userDao.delete(username);

                            cleanField();
                            cleanComboBoxResult();
                            cleanComboBoxStatusUser();
                            deleteFotoProfil();
                            AlertBox.display("Berhasil Delete", "Berhasil menghapus data");
                        }
                    }
                } catch (SQLException e) {
                    AlertBox.display("Gagal Delete", "Gagal menghapus data");
//                    e.printStackTrace();
                }
            }
        });
    }

    void deleteFotoProfil() {
        getFileHandler().deleteFile();
    }

    private void btnBackAction() {
        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getWindowControl().moveToScene(btnBack, "admin");
            }
        });
    }

    private void btnFileChooseAction() {
        btnFileChoose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
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
        });
    }

    private void toggleBtnSettingAction() {
        toggleBtnSetting.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (toggleBtnSetting.isSelected()) {
                    // change style button
                    toggleBtnSetting.getStyleClass().remove("seryp-btn-primary");
                    toggleBtnSetting.getStyleClass().add("seryp-btn-secondary");
                    paneSetting.setExpanded(true);
                } else {
                    // change style button
                    toggleBtnSetting.getStyleClass().remove("seryp-btn-secondary");
                    toggleBtnSetting.getStyleClass().add("seryp-btn-primary");
                    paneSetting.setExpanded(false);
                }
            }
        });
    }

    private void cboResultAction() {
        /**
         * Avoid error null pointer
         * Karena pada saat setelah melakukan pencarian lagi maka cboResult akan tereksekusi otomatis (masih belum tau kenapa),
         * yang mengakibatkan adanya nilai kembalian yang null
         */

        cboResult.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    cleanField();
                    // cboResult bisa saja mengembalikan nilai null, maka dari itu memakai exception
                    String username = cboResult.getValue(); // possible NullPointer

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
        });
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

    ObservableList<String> convertToObservableList(ResultSet resultSet) {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        try {
            while (resultSet.next()) {
                observableList.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return observableList;
    }

    void setComboBoxResult(ObservableList<String> observableList) {
        Callback<ListView<String>, ListCell<String>> callback = new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> stringListView) {
                ListCell<String> cell = new ListCell<>(){
                    @Override
                    protected void updateItem(String s, boolean empty) {
                        super.updateItem(s, empty);

                        if (!empty) {
                            setText(s);
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

    void setComboBoxStatusUser() {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll("Admin", "Karyawan Aktif", "Karyawan Tidak Aktif");

        cboStatusUser.setPromptText("Status User");
        cboStatusUser.setItems(observableList);
    }

    void cleanComboBoxResult() {
        cboResult.getItems().clear();
    }

    void cleanComboBoxStatusUser() {
        // Fungsi dari ini adalah agar kalau pas selesai clear combo box prompt text ny tidak hilang
        ListCell<String> listCell = new ListCell<>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText(item);
                }
            }
        };

        cboStatusUser.getItems().clear();
        cboStatusUser.setButtonCell(listCell);
    }
}
