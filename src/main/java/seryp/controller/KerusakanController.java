package seryp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import seryp.model.Kerusakan;
import seryp.model.User;
import seryp.model.dao.IdentitasTokoDao;
import seryp.model.dao.KerusakanDao;
import seryp.utils.*;
import seryp.utils.boxes.AlertBox;
import seryp.utils.boxes.ConfirmBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class KerusakanController extends SerypUtil implements Initializable {
    public HBox topBar;
    public VBox sideBar;
    public VBox settingBar;
    public Button btnBack;
    public Button btnCari;
    public Button btnUpdate;
    public Button btnAdd;
    public Button btnDelete;
    public ToggleButton btnCariKerusakan;
    public ToggleButton toggleBtnSetting;
    public TitledPane paneCariKerusakan;
    public TitledPane paneSetting;
    public TextField txtId;
    public TextField txtCariKerusakan;
    public TextField txtNamaKerusakan;
    public TextField txtEstimasiMin;
    public TextField txtEstimasiMax;
    public TextArea txtAreaDeskripsi;
    public ComboBox<Kerusakan> cboResult;
    private KerusakanDao kerusakanDao;
    private Kerusakan kerusakan;
    private IdentitasTokoDao identitasTokoDao;
    public static User userLogin; // data yang dikirim dari AdminController

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
        btnCariAction();
        btnCariKerusakanAction();
        btnUpdateAction();
        btnAddAction();
        btnDeleteAction();
        btnBackAction();
        toggleBtnSettingAction();
        cboResultAction();
    }

    private void init() {
        // instance required object
        kerusakanDao = new KerusakanDao();
        identitasTokoDao = new IdentitasTokoDao();

        // set topBar
        getWindowControl().setTopBar(topBar);

        // set sideBar
        getWindowControl().setSideBar(sideBar, KerusakanController.userLogin);

        // set settingBar
        try {
            getWindowControl().setSettingBar(settingBar, identitasTokoDao.get());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Setting Text Field
        txtId.setEditable(false);

        // set button delete and update visible = false
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);

        // set pane cari kerusakan dan pane setting unexpanded dan animated
        paneCariKerusakan.setAnimated(true);
        paneCariKerusakan.setExpanded(false);

        paneSetting.setAnimated(true);
        paneSetting.setExpanded(false);
    }

    private void btnCariAction() {
        btnCari.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ObservableList<Kerusakan> observableList;

                try {
                    if (txtCariKerusakan.getText().equals("")) {
                        observableList = convertToObservableList(kerusakanDao.searchAll());
                    } else {
                        String keyword = txtCariKerusakan.getText();
                        observableList = convertToObservableList(kerusakanDao.search(keyword));
                    }

                    setComboBoxResult(observableList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void btnCariKerusakanAction() {
        btnCariKerusakan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // cleaning
                txtCariKerusakan.setText("");
                txtId.setText("");
                cleanComboBoxResult();

                if (btnCariKerusakan.isSelected()) {
                    // change style button
                    btnCariKerusakan.getStyleClass().remove("seryp-btn-primary");
                    btnCariKerusakan.getStyleClass().add("seryp-btn-secondary");
                    // set visibility button update and delete
                    btnUpdate.setVisible(true);
                    btnAdd.setVisible(false);
                    btnDelete.setVisible(true);
                    paneCariKerusakan.setExpanded(true);
                } else {
                    // change style button
                    btnCariKerusakan.getStyleClass().remove("seryp-btn-secondary");
                    btnCariKerusakan.getStyleClass().add("seryp-btn-primary");
                    // set visibility button update and delete
                    btnUpdate.setVisible(false);
                    btnAdd.setVisible(true);
                    btnDelete.setVisible(false);
                    paneCariKerusakan.setExpanded(false);
                }
            }
        });
    }

    private void btnUpdateAction() {
        btnUpdate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String id = txtId.getText();
                String namaKerusakan = txtNamaKerusakan.getText();
                String deskripsi = txtAreaDeskripsi.getText();
                int estimasiMin = getValidation().validateInteger(txtEstimasiMin.getText());
                int estimasiMax = getValidation().validateInteger(txtEstimasiMax.getText());

                try {
                    kerusakan = new Kerusakan();

                    kerusakan.setIdKerusakan(id);
                    kerusakan.setNama(namaKerusakan);
                    kerusakan.setDeskripsi(deskripsi);
                    kerusakan.setEstimasiMin(estimasiMin);
                    kerusakan.setEstimasiMax(estimasiMax);

                    kerusakanDao.update(kerusakan);
                    cleanField();
                    cleanComboBoxResult();
                    AlertBox.display("Berhasil Update", "Berhasil update data kerusakan");
                } catch (SQLException | NullPointerException e) {
                    AlertBox.display("Gagal Update", "Gagal update data kerusakan");
//                    e.printStackTrace();
                }
            }
        });
    }

    private void btnAddAction() {
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String id = getUtil().getCustomId(getIdKerusakan(), "K");
                String namaKerusakan = txtNamaKerusakan.getText();
                String deskripsi = txtAreaDeskripsi.getText();
                int estimasiMin = getValidation().validateInteger(txtEstimasiMin.getText());
                int estimasiMax = getValidation().validateInteger(txtEstimasiMax.getText());

                try {
                    kerusakan = new Kerusakan();

                    kerusakan.setIdKerusakan(id);
                    kerusakan.setNama(namaKerusakan);
                    kerusakan.setDeskripsi(deskripsi);
                    kerusakan.setEstimasiMin(estimasiMin);
                    kerusakan.setEstimasiMax(estimasiMax);

                    kerusakanDao.add(kerusakan);
                    cleanField();
                    AlertBox.display("Berhasil Tambah", "Berhasil tambah data kerusakan");
                } catch (SQLException | NullPointerException e) {
                    AlertBox.display("Gagal Tambah", "Gagal tambah data kerusakan");
//                    e.printStackTrace();
                }
            }
        });
    }

    private void btnDeleteAction() {
        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (!(txtId.getText().equals(""))) {
                        boolean option = ConfirmBox.display("Konfirmasi", "Apakah anda yakin ingin menghapus ?\nData data yang berkaitan dengan data tersebut akan terhapus.");
                        if (option){
                            String id = txtId.getText();
                            kerusakanDao.delete(id);

                            cleanField();
                            cleanComboBoxResult();
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

    private void btnBackAction() {
        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getWindowControl().moveToScene(btnBack, "admin");
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
                    Kerusakan kerusakan = cboResult.getValue(); // possible NullPointer
                    kerusakan = kerusakanDao.get(kerusakan.getIdKerusakan());

                    txtId.setText(kerusakan.getIdKerusakan());
                    txtNamaKerusakan.setText(kerusakan.getNama());
                    txtAreaDeskripsi.setText(kerusakan.getDeskripsi());
                    txtEstimasiMin.setText(String.valueOf(kerusakan.getEstimasiMin()));
                    txtEstimasiMax.setText(String.valueOf(kerusakan.getEstimasiMax()));
                } catch (SQLException | NullPointerException e) {
                    System.out.println("Ada yang null nih!");
//                    e.printStackTrace();
                }
            }
        });
    }

    private void cleanField() {
        txtCariKerusakan.setText("");
        txtId.setText("");
        txtNamaKerusakan.setText("");
        txtAreaDeskripsi.setText("");
        txtEstimasiMin.setText("");
        txtEstimasiMax.setText("");
    }

    List<String> getIdKerusakan() {
        try {
            List<String> listId = new ArrayList<>();

            ResultSet resultSet = kerusakanDao.getAllId();
            while (resultSet.next()) {
                listId.add(resultSet.getString(1));
            }

            return listId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    ObservableList<Kerusakan> convertToObservableList(ResultSet resultSet) {
        /**
         * Convert Result Set contains ID and Name to Observable List
         */
        ObservableList<Kerusakan> observableList = FXCollections.observableArrayList();

        try {
            while (resultSet.next()) {
                observableList.add(new Kerusakan(resultSet.getString(1), resultSet.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return observableList;
    }

    void setComboBoxResult(ObservableList<Kerusakan> observableList) {
        Callback<ListView<Kerusakan>, ListCell<Kerusakan>> cellCallback = new Callback<ListView<Kerusakan>, ListCell<Kerusakan>>() {
            @Override
            public ListCell<Kerusakan> call(ListView<Kerusakan> pelangganListView) {
                ListCell<Kerusakan> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Kerusakan pelanggan, boolean b) {
                        super.updateItem(pelanggan, b);

                        if (pelanggan != null) {
                            setText(pelanggan.getNama());
                        } else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        cboResult.setPromptText("Search Result Here !");
        cboResult.setItems(observableList);
        cboResult.setCellFactory(cellCallback);
        cboResult.setButtonCell(cellCallback.call(null));
    }

    void cleanComboBoxResult() {
        cboResult.getItems().clear();
    }
}
