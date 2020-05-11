package seryp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    public Button btnLupaPassword;
    public TextField txtUsername;
    public PasswordField txtPassword;
    public TextField txtPassword2;
    public Label lblAlert;
    private UserDao userDao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // instance required object
        userDao = new UserDao();

        // set inputPassword2 hidden
        txtPassword2.setVisible(false);

        // set text field normal action
        getFieldControl().setTextFieldNormalAction(txtUsername, txtPassword);

        // init topbar
        getWindowControl().setTopBar(topBar);
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
    void btnLoginAction() {
        loginAction();
    }

    @FXML
    void btnLupaPasswordAction() {
        getWindowControl().moveToScene(btnLupaPassword, "lupa password");
    }

    // Set TextField shape to normal mode
    @FXML
    void txtPasswordKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            loginAction();
        }
    }

    void loginAction() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        // Check if TextField is not empty
        if (username.equals("") || password.equals("")) {
            getFieldControl().setTextFieldError(txtUsername, txtPassword);
            lblAlert.setText("Invalid Username and Password");
        } else {
            try {
                User user = userDao.get(txtUsername.getText());

                if (user == null) {
                    getFieldControl().setTextFieldError(txtUsername, txtPassword);
                    lblAlert.setText("Invalid Username and Password");
                } else {
                    // cek password
                    password = getUtil().md5Hash(txtPassword.getText());
                    if (user.getPassword().equals(password)) {
                        // update lastLogin
                        userDao.updateLastLogin(user.getUsername(), LocalDate.now());
                        // change scene
                        if (user.getStatusUser().equals("Admin")) {
                            // set session
                            SerypUtil.setUserSession(user);
                            getWindowControl().moveToScene(btnLogin, "admin");
                        } else if (user.getStatusUser().equals("Karyawan Aktif")) {
                            // set session
                            SerypUtil.setUserSession(user);
                            getWindowControl().moveToScene(btnLogin, "karyawan");
                        } else {
                            AlertBox.display("Login Gagal", "Maaf anda terdaftar sebagai \"Karyawan Tidak Aktif\".\n Mohon hubungi Admin untuk menyelesaikan masalah.");
                        }
                    } else {
                        getFieldControl().setTextFieldError(txtUsername, txtPassword);
                        lblAlert.setText("Invalid Username and Password");
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
