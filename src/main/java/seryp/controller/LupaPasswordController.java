package seryp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import seryp.model.User;
import seryp.model.dao.UserDao;
import seryp.utils.SerypUtil;
import seryp.utils.boxes.AlertBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LupaPasswordController extends SerypUtil implements Initializable {
    public HBox topBar;
    public TextField txtUsername;
    public TextField txtNoHandphone;
    public PasswordField txtNewPassword;
    public PasswordField txtConfirmNewPassword;
    public Label lblStatus;
    public TitledPane paneLupaPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set topBar
        getWindowControl().setTopBar(topBar);

        // set lblStatus
        lblStatus.setText("");

        // set paneLupaPassword unexpanded dan make animated
        paneLupaPassword.setAnimated(true);
        paneLupaPassword.setExpanded(false);
    }

    @FXML
    void btnCariUserAction() {
        if (!(txtUsername.getText().equals("") && txtNoHandphone.getText().equals(""))) {
            try {
                String username = txtUsername.getText();
                String noHandphone = getValidation().validateNoHandphone(txtNoHandphone.getText());

                User user = new UserDao().get(username);

                if (user == null) {
                    lblStatus.setText("Username tidak ada!");
                } else {
                    if (user.getNoHp().equals(noHandphone)) {
                        txtUsername.setEditable(false);
                        txtNoHandphone.setEditable(false);
                        paneLupaPassword.setExpanded(true);
                    } else {
                        lblStatus.setText("No Handphone salah!");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnSimpanPasswordAction() {
        if (!(txtNewPassword.getText().equals("") && txtConfirmNewPassword.getText().equals(""))) {
            String username = txtUsername.getText();

            if (txtNewPassword.getText().equals(txtConfirmNewPassword.getText())) {
                String newPassword = getUtil().md5Hash(txtNewPassword.getText());

                try {
                    new UserDao().updatePassword(username, newPassword);
                    AlertBox.display("Berhasil Update Password", "Update password berhasil. \n Mohon untuk diingat password!");
                    getWindowControl().moveToScene(lblStatus, "login");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                lblStatus.setText("Password harus sama!");
            }

        }
    }
}
