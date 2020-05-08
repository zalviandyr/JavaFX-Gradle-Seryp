package seryp.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import seryp.model.IdentitasToko;
import seryp.model.User;
import seryp.model.dao.IdentitasTokoDao;
import seryp.model.dao.Koneksi;
import seryp.model.dao.UserDao;
import seryp.utils.SerypUtil;
import seryp.utils.boxes.AlertBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SplashController extends SerypUtil implements Initializable {
    public ProgressBar progressBar;
    public Label lblStatus;
    private UserDao userDao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
        threadAction();
    }

    private void init() {
        // set label
        lblStatus.setText("");

        // set progress bar
        progressBar.setProgress(0);
    }

    private void threadAction() {
        // animation agar progress bar smooth
        Animation animation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(progressBar.progressProperty(), 1))
        );

        // task untuk mengatur aliran eksekusi pada method
        Task<Void> loadingWorker = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // kondisi yang mana akan pindah ke scene login jika terpenuhi
                boolean kondisi = true;

                for (int i = 0; i < 10; i++) {
                    Thread.sleep(500);
                    if (i < 3) {
                        updateMessage("Checking database connection");
                        if (!checkConnectionDB()) {
                            kondisi = false;
                            animation.pause();
                            break;
                        }
                    } else if (i < 6) {
                        updateMessage("Checking database Seryp exist");
                        if (!isDBExist()) {
                            kondisi = false;
                            animation.pause();
                            break;
                        } else {
                            userDao = new UserDao();
                        }
                    } else {
                        updateMessage("Deleting cache foto profil");
                        deleteCacheFotoProfil();
                    }
                }

                if (kondisi) {
                    // buat stage baru ketika loading selesai
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                FXMLLoader loader = getWindowControl().getFXMLLoader("login");
                                Parent parent = loader.load();
                                Scene scene = new Scene(parent);
                                Stage stage = new Stage();

                                // set borderless
                                scene.setFill(Color.TRANSPARENT);
                                stage.initStyle(StageStyle.TRANSPARENT);
                                stage.setScene(scene);
                                stage.show();

                                lblStatus.getScene().getWindow().hide();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                return null;
            }
        };

//        progressBar.progressProperty().bind(loadingWorker.progressProperty());
        loadingWorker.messageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                lblStatus.setText(newValue);
            }
        });
        animation.setCycleCount(1);
        animation.play();
        new Thread(loadingWorker).start();
    }

    boolean checkConnectionDB() {
        boolean result = new Koneksi().isConnection();
        if (!result) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    AlertBox.display("Failed to connect database", "Hidupkan database anda!!");

                    // tutup window loading jika error
                    Stage stage = (Stage) lblStatus.getScene().getWindow();
                    stage.close();
                }
            });
        }
        return result;
    }

    boolean isDBExist() {
        boolean result = new Koneksi().isDBSerypExist();
        if (!result) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    AlertBox.display("Failed to find Database", "Proses instalasi akan dilakukan!");

                    getWindowControl().moveToScene(lblStatus, "instalasi");
                }
            });
        }
        return result;
    }

    void deleteCacheFotoProfil() {
        try {
            List<User> list = userDao.getAll();
            IdentitasToko identitasToko = new IdentitasTokoDao().get();

            List<File> fileListFotoUser = new ArrayList<>();
            for (User user : list) {
                File file = new File(user.getFotoProfil());
                if (file.exists()) {
                    fileListFotoUser.add(file);
                }
            }

            // get all file from path
            File file1 = new File(identitasToko.getFotoProfilPath());
            File[] listFiles = file1.listFiles();
            List<File> fileListFoto = null;
            if (listFiles != null) {
                // list all files from path
                fileListFoto = new ArrayList<>(Arrays.asList(listFiles));
            }

            // remove file
            List<File> listFileRemove = null;
            if (fileListFoto != null) {
                listFileRemove = new ArrayList<>();
                for (File file : fileListFoto) {
//                    System.out.println("file + " + file);
                    // list files from db
                    // jika list files from db exits
                    if (fileListFotoUser.isEmpty()) {
                        listFileRemove.add(file);
                    } else {
                        for (File fileUser : fileListFotoUser) {
//                        System.out.println("file2 + " + fileUser);
                            // cek jika ada file yang sama
                            if (!file.getName().equals(fileUser.getName())) {
                                // simpan file yang tidak ada user pakai ke list
                                listFileRemove.add(file);
                            }
                        }
                    }
                }
            }

            if (listFileRemove != null) {
                for (File file : listFileRemove) {
                    // delete file unused
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
