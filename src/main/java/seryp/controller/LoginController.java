package seryp.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import seryp.model.User;
import seryp.model.dao.UserDao;
import seryp.utils.SerypUtil;
import seryp.utils.boxes.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class LoginController extends SerypUtil implements Initializable {
    public HBox topBar;
    public Button btnLogin;
    public Button btnRemoveRedEye;
    public TextField inputUsername;
    public PasswordField inputPassword;
    public TextField inputPassword2;
    public Label lblAlert;
    private User user;
    private UserDao userDao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
        btnRemoveRedEyeAction();
        btnLoginAction();
        inputPasswordAction();
        inputActionError(inputUsername, inputPassword);
    }

    private void init() {
        // instance required object
        userDao = new UserDao();

        // set inputPassword2 hidden
        inputPassword2.setVisible(false);

        // init topbar
        getWindowControl().setTopBar(topBar);
    }

    private void btnRemoveRedEyeAction() {
        btnRemoveRedEye.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!inputPassword.getText().equals("")) {
                    String password = inputPassword.getText();
                    // hidden password field
                    inputPassword.setVisible(false);

                    // show text field password
                    inputPassword2.setText(password);
                    inputPassword2.setVisible(true);
                }
            }
        });

        btnRemoveRedEye.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String password = inputPassword2.getText();
                // hidden text field password
                inputPassword2.setVisible(false);

                // show password field
                inputPassword.setText(password);
                inputPassword.setVisible(true);
            }
        });
    }

    private void btnLoginAction() {
        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                loginAction();
            }
        });
    }

    // Set TextField shape to normal mode
    private void inputActionError(TextField... textFields) {
        for (TextField textField : textFields) {
            textField.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    textField.getStyleClass().remove("seryp-text-field-error");
                }
            });
        }
    }

    private void inputPasswordAction() {
        inputPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    loginAction();
                }
            }
        });
    }

    void setErrorField() {
        // Set TextField shape to error mode
        inputUsername.getStyleClass().add("seryp-text-field-error");
        inputPassword.getStyleClass().add("seryp-text-field-error");
        lblAlert.setText("Invalid Username and Password");
    }

    void loginAction() {

        String username = inputUsername.getText();
        String password = inputPassword.getText();

        // Check if TextField is not empty
        if (username.equals("") || password.equals("")) {
            setErrorField();
        } else {
            try {
                user = userDao.get(inputUsername.getText());

                if (user == null) {
                    setErrorField();
                } else {
                    // cek password
                    if (user.getPassword().equals(inputPassword.getText())) {
                        // update lastLogin
                        userDao.updateLastLogin(user.getUsername(), LocalDate.now());
                        // change scene
                        if (user.getStatusUser().equals("Admin")) {
                            AdminController.userLogin = user;
                            getWindowControl().moveToScene(btnLogin, "admin");
                        } else if (user.getStatusUser().equals("Karyawan Aktif")) {
                            KaryawanController.userLogin = user;
                            getWindowControl().moveToScene(btnLogin, "karyawan");
                        } else {
                            AlertBox.display("Login Gagal", "Maaf anda terdaftar sebagai \"Karyawan Tidak Aktif\".\n Mohon hubungi Admin untuk menyelesaikan masalah.");
                        }
                    } else {
                        setErrorField();
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
