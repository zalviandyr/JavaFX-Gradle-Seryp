package seryp.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import seryp.model.Kerusakan;
import seryp.model.User;
import seryp.model.dao.KerusakanDao;
import seryp.utils.*;
import seryp.utils.boxes.AlertBox;
import seryp.utils.boxes.ConfirmBox;

import java.net.URL;
import java.sql.SQLException;
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
    public ToggleButton toggleBtnCariKerusakan;
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
    public static User userLogin; // data yang dikirim dari AdminController

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // instance required object
        kerusakanDao = new KerusakanDao();

        // set topBar
        getWindowControl().setTopBar(topBar);

        // set sideBar
        getWindowControl().setSideBar(sideBar, KerusakanController.userLogin);

        // set paneSetting
        getWindowControl().setPaneSetting(toggleBtnSetting, paneSetting, settingBar);

        // Setting Text Field
        txtId.setEditable(false);

        // set button delete and update visible = false
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);

        // set pane cari kerusakan dan pane setting unexpanded dan animated
        paneCariKerusakan.setAnimated(true);
        paneCariKerusakan.setExpanded(false);
    }

    @FXML
    void btnCariAction() {
        ObservableList<Kerusakan> observableList;

        try {
            if (txtCariKerusakan.getText().equals("")) {
                observableList = kerusakanDao.searchAll();
            } else {
                String keyword = txtCariKerusakan.getText();
                observableList = kerusakanDao.search(keyword);
            }

            setComboBoxResult(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cboResultAction() {
        /**
         * Avoid error null pointer
         * Karena pada saat setelah melakukan pencarian lagi maka cboResult akan tereksekusi otomatis (masih belum tau kenapa),
         * yang mengakibatkan adanya nilai kembalian yang null
         */

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
//            e.printStackTrace();
        }
    }

    @FXML
    void toggleBtnCariKerusakanAction() {
        // cleaning
        txtCariKerusakan.setText("");
        txtId.setText("");
        getFieldControl().cleanComboBoxResult(cboResult);

        if (toggleBtnCariKerusakan.isSelected()) {
            // change style button
            toggleBtnCariKerusakan.getStyleClass().remove("seryp-btn-primary");
            toggleBtnCariKerusakan.getStyleClass().add("seryp-btn-secondary");
            // set visibility button update and delete
            btnUpdate.setVisible(true);
            btnAdd.setVisible(false);
            btnDelete.setVisible(true);
            paneCariKerusakan.setExpanded(true);
        } else {
            // change style button
            toggleBtnCariKerusakan.getStyleClass().remove("seryp-btn-secondary");
            toggleBtnCariKerusakan.getStyleClass().add("seryp-btn-primary");
            // set visibility button update and delete
            btnUpdate.setVisible(false);
            btnAdd.setVisible(true);
            btnDelete.setVisible(false);
            paneCariKerusakan.setExpanded(false);
        }
    }

    @FXML
    void btnUpdateAction() {
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
            getFieldControl().cleanComboBoxResult(cboResult);
            AlertBox.display("Berhasil Update", "Berhasil update data kerusakan");
        } catch (SQLException | NullPointerException e) {
            AlertBox.display("Gagal Update", "Gagal update data kerusakan");
//                    e.printStackTrace();
        }
    }

    @FXML
    void btnAddAction() {
        try {
            String id = getUtil().getCustomId(kerusakanDao.getAllId(), "K");
            String namaKerusakan = txtNamaKerusakan.getText();
            String deskripsi = txtAreaDeskripsi.getText();
            int estimasiMin = getValidation().validateInteger(txtEstimasiMin.getText());
            int estimasiMax = getValidation().validateInteger(txtEstimasiMax.getText());

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

    @FXML
    void btnDeleteAction() {
        try {
            if (!(txtId.getText().equals(""))) {
                boolean option = ConfirmBox.display("Konfirmasi", "Apakah anda yakin ingin menghapus ?\nData data yang berkaitan dengan data tersebut akan terhapus.");
                if (option) {
                    String id = txtId.getText();
                    kerusakanDao.delete(id);

                    cleanField();
                    getFieldControl().cleanComboBoxResult(cboResult);
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

    private void cleanField() {
        txtCariKerusakan.setText("");
        txtId.setText("");
        txtNamaKerusakan.setText("");
        txtAreaDeskripsi.setText("");
        txtEstimasiMin.setText("");
        txtEstimasiMax.setText("");
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
}
