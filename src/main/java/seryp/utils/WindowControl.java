package seryp.utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Ellipse;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import seryp.model.IdentitasToko;
import seryp.model.User;
import seryp.model.dao.IdentitasTokoDao;
import seryp.utils.boxes.AlertBox;
import seryp.utils.boxes.ConfirmBox;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;

public class WindowControl {
    double xOffset = 0;
    double yOffset = 0;

    public FXMLLoader getFXMLLoader(String destination) {
        String path = "";

        if (destination.equalsIgnoreCase("splash")) {
            path = "/seryp/layout/Splash.fxml";
        }
        if (destination.equalsIgnoreCase("instalasi")) {
            path = "/seryp/layout/Instalasi.fxml";
        }
        if (destination.equalsIgnoreCase("login")) {
            path = "/seryp/layout/Login.fxml";
        }
        if (destination.equalsIgnoreCase("service")) {
            path = "/seryp/layout/Service.fxml";
        }
        if (destination.equalsIgnoreCase("pelanggan")) {
            path = "/seryp/layout/Pelanggan.fxml";
        }
        if (destination.equalsIgnoreCase("admin")) {
            path = "/seryp/layout/Admin.fxml";
        }
        if (destination.equalsIgnoreCase("karyawan")) {
            path = "/seryp/layout/Karyawan.fxml";
        }
        if (destination.equalsIgnoreCase("pembayaran")) {
            path = "/seryp/layout/Pembayaran.fxml";
        }
        if (destination.equalsIgnoreCase("list")) {
            path = "/seryp/layout/List.fxml";
        }
        if (destination.equalsIgnoreCase("kerusakan")) {
            path = "/seryp/layout/Kerusakan.fxml";
        }
        if (destination.equalsIgnoreCase("barang")) {
            path = "/seryp/layout/Barang.fxml";
        }
        if (destination.equalsIgnoreCase("user")) {
            path = "/seryp/layout/User.fxml";
        }

        return new FXMLLoader(getClass().getResource(path));
    }

    public void moveToScene(Node node, String destination) {
        /*
         Fungsi untuk pindah ke scene berbeda dengan cara:
         Setiap komponen pada javaFX control memiliki super kelas yaitu kelas node,
         kelas control mempunyai fungsi .getScene().getWindow() untuk mendapatkan stage yang lengket pada komponen tersebut
         */
        try {
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = getFXMLLoader(destination).load();
            Scene scene = new Scene(root);

            // set borderless
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDragWindow(Node node, Pane pane) {
        /**
         * Memakai node, karena node adalah super class dari semua component control di javafx
         * Pane merupakan super class dari berbagai container dari JavaFX
         * */

        // set scene drag
        pane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Stage stage = (Stage) node.getScene().getWindow();
                xOffset = stage.getX() - mouseEvent.getScreenX();
                yOffset = stage.getY() - mouseEvent.getScreenY();
            }
        });

        pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Stage stage = (Stage) node.getScene().getWindow();
                stage.setX(mouseEvent.getScreenX() + xOffset);
                stage.setY(mouseEvent.getScreenY() + yOffset);
            }
        });
    }

    private void closeWindow(Node node) {
        boolean option = ConfirmBox.display("Yakin ?", "Apakah yakin mau keluar ?");

        if (option) {
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        }
    }

    private void minimizeWindow(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setIconified(true);
    }

    public void setTopBar(HBox topBar) {
        /**
         * Untuk menset topBar Draggable dan memberi aksi aksi pada tombol pada topbar
         */

        // set top bar draggable
        setDragWindow(topBar.getChildren().get(0), topBar);

        for (Node node : topBar.getChildren()) {
            // mencari button, mencari node yang child class nya Button
            if (node instanceof Button) {
                if (node.getId().equals("btnMinimize")) {
                    Button btnMinimize = (Button) node;
                    setInnerShadowButton(btnMinimize);

                    btnMinimize.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            minimizeWindow(node);
                        }
                    });
                }
                if (node.getId().equals("btnClose")) {
                    Button btnClose = (Button) node;
                    setInnerShadowButton(btnClose);

                    btnClose.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            closeWindow(node);
                        }
                    });
                }
            }
        }
    }

    private void setInnerShadowButton(Button button) {
        button.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, Color.rgb(185, 169, 169), 10, 0, 0, 0));

        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setEffect(null);
            }
        });

        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, Color.rgb(185, 169, 169), 10, 0, 0, 0));
            }
        });
    }

    public void setSideBar(VBox sideBar, User user) {
        for (Node node : sideBar.getChildren()) {
            if (node instanceof Label) {
                if (node.getId().equals("lblName")) {
                    Label lblName = (Label) node;
                    lblName.setText(user.getNama());
                }
                if (node.getId().equals("lblContactPerson")) {
                    Label lblContactPerson = (Label) node;
                    lblContactPerson.setText(user.getNoHp());
                }
            }

            if (node instanceof Ellipse) {
                if (node.getId().equals("imgFotoProfil")) {
                    try {
                        Ellipse imgFotoProfil = (Ellipse) node;
                        Image image;
                        String defaultPath = String.valueOf(getClass().getResource("/seryp/assets/img/avatar-png-1.png"));
                        if (user.getFotoProfil() == null || user.getFotoProfil().equals("")) {
                            image = new Image(defaultPath);
                        } else {
                            File file = new File(user.getFotoProfil());
                            // jika path foto profil ada di database dan file ada di directory local
                            if (file.exists()) {
                                InputStream inputStream = new FileInputStream(file);
                                image = new Image(inputStream);
                            } else {
                                // jika tidak
                                image = new Image(defaultPath);
                            }
                        }
                        ImagePattern imagePattern = new ImagePattern(image);
                        imgFotoProfil.setFill(imagePattern);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (node instanceof Button) {
                if (node.getId().equals("btnBackup")) {
                    Button btnBackup = (Button) node;
                    btnBackup.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            try {
                                FileChooser fileChooser = new FileChooser();
                                // set filter extension
                                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("SQL Files", "*.sql");
                                fileChooser.getExtensionFilters().add(extensionFilter);
                                fileChooser.setInitialFileName("Seryp backup - " + LocalDate.now() + ".sql");
                                File file = fileChooser.showSaveDialog(new Stage());

                                ProcessBuilder builder = new ProcessBuilder(
                                        "cmd.exe", "/c", "cd /d C:\\xampp\\mysql\\bin && mysqldump -u root seryp > ", file.getAbsolutePath());
                                builder.redirectErrorStream(true);
                                Process p = builder.start();
                                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                                String line;
                                while (true) {
                                    line = r.readLine();
                                    if (line == null) {
                                        break;
                                    }
                                    System.out.println(line);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }

    public void setSettingBar(VBox settingBar, IdentitasToko identitasToko) {
        TextField txtNamaTokoNode = null;
        TextArea txtFotoProfilPathNode = null;
        TextArea txtAlamatTokoNode = null;
        Button btnOkSetting = null;

        for (Node node : settingBar.getChildren()) {
            if (node instanceof TextField) {
                if (node.getId().equals("txtNamaToko"))
                    txtNamaTokoNode = (TextField) node;
            }

            if (node instanceof TextArea) {
                if (node.getId().equals("txtAlamatToko")) {
                    txtAlamatTokoNode = (TextArea) node;
                }
                if (node.getId().equals("txtFotoProfilPath"))
                    txtFotoProfilPathNode = (TextArea) node;
            }

            if (node instanceof Button)
                if (node.getId().equals("btnOkSetting"))
                    btnOkSetting = (Button) node;
        }

        TextField txtNamaToko = txtNamaTokoNode;
        TextArea txtFotoProfilPath = txtFotoProfilPathNode;
        TextArea txtAlamatToko = txtAlamatTokoNode;

        txtNamaToko.setText(identitasToko.getNamaToko());
        txtFotoProfilPath.setText(identitasToko.getFotoProfilPath());
        txtFotoProfilPath.setEditable(false);
        txtAlamatToko.setText(identitasToko.getAlamat());

        btnOkSetting.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!txtNamaToko.getText().equals("") && !txtAlamatToko.getText().equals("") && !txtFotoProfilPath.getText().equals("")) {
                    IdentitasTokoDao identitasTokoDao = new IdentitasTokoDao();
                    IdentitasToko identitasToko = new IdentitasToko();

                    identitasToko.setNamaToko(txtNamaToko.getText());
                    identitasToko.setAlamat(txtAlamatToko.getText());
                    identitasToko.setFotoProfilPath(txtFotoProfilPath.getText());

                    try {
                        identitasTokoDao.update(identitasToko);
                        AlertBox.display("Setting Berhasil", "Semua setting tersimpan ke database!");
                    } catch (SQLException e) {
//                        e.printStackTrace();
                        AlertBox.display("Setting Gagal", "Periksa kembali isi form!");
                    }
                } else
                    AlertBox.display("Setting Gagal", "Isi semua form yang ada!");
            }
        });
    }
}
