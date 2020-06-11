package seryp.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import seryp.model.Barang;
import seryp.model.User;
import seryp.model.dao.BarangDao;
import seryp.utils.*;
import seryp.utils.boxes.AlertBox;
import seryp.utils.boxes.ConfirmBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BarangController extends SerypUtil implements Initializable {
    public HBox topBar;
    public VBox sideBar;
    public VBox settingBar;
    public Button btnBack;
    public Button btnUpdate;
    public Button btnAdd;
    public Button btnDelete;
    public Button btnCari;
    public ToggleButton toggleBtnCariBarang;
    public ToggleButton toggleBtnSetting;
    public TitledPane paneCariBarang;
    public TitledPane paneSetting;
    public TextField txtCariBarang;
    public TextField txtId;
    public TextField txtNamaBarang;
    public TextField txtHarga;
    public TextField txtStock;
    public TextArea txtAreaDeskripsi;
    public ComboBox<Barang> cboResult;
    public ComboBox<String> cboStatusBarang;
    private BarangDao barangDao;
    public static User userLogin = SerypUtil.getUserSession(); // data session

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // instance object
        barangDao = new BarangDao();

        // Set topBar
        getWindowControl().setTopBar(topBar);

        // Set sideBar
        getWindowControl().setSideBar(sideBar, BarangController.userLogin);

        // set paneSetting
        getWindowControl().setPaneSetting(toggleBtnSetting, paneSetting, settingBar);

        // init combo box
        getFieldControl().setComboBoxStatus(cboStatusBarang, "Status", "Ready", "Non Ready");

        // Setting Text Field
        txtId.setEditable(false);

        // set unexpanded and animated title pane
        paneCariBarang.setAnimated(true);
        paneCariBarang.setExpanded(false);

        // set button update and delete invisible
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
    }

    @FXML
    void btnCariAction() {
        ObservableList<Barang> observableList;

        try {
            if (txtCariBarang.getText().equals("")) {
                observableList = barangDao.searchAll();
            } else {
                String keyword = txtCariBarang.getText();
                observableList = barangDao.search(keyword);
            }

            setComboBoxResult(observableList);
        } catch (SQLException | NullPointerException e) {
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
            Barang barang = cboResult.getValue(); // possible NullPointer
            barang = barangDao.get(barang.getIdBarang());

            txtId.setText(barang.getIdBarang());
            txtNamaBarang.setText(barang.getNama());
            txtAreaDeskripsi.setText(barang.getDeskripsi());
            txtHarga.setText(String.valueOf(barang.getHarga()));
            txtStock.setText(String.valueOf(barang.getStok()));

            cboStatusBarang.setValue(barang.getStatus());
        } catch (SQLException | NullPointerException e) {
            System.out.println("Ada yang null nih!");
//                    e.printStackTrace();
        }
    }

    @FXML
    void toggleBtnCariBarangAction() {
        // cleaning
        txtCariBarang.setText("");
        txtId.setText("");
        getFieldControl().cleanComboBoxResult(cboResult);

        if (toggleBtnCariBarang.isSelected()) {
            // change style button
            toggleBtnCariBarang.getStyleClass().remove("seryp-btn-primary");
            toggleBtnCariBarang.getStyleClass().add("seryp-btn-secondary");
            btnUpdate.setVisible(true);
            btnAdd.setVisible(false);
            btnDelete.setVisible(true);
            paneCariBarang.setExpanded(true);
        } else {
            // change style button
            toggleBtnCariBarang.getStyleClass().remove("seryp-btn-secondary");
            toggleBtnCariBarang.getStyleClass().add("seryp-btn-primary");
            btnUpdate.setVisible(false);
            btnAdd.setVisible(true);
            btnDelete.setVisible(false);
            paneCariBarang.setExpanded(false);
        }
    }

    @FXML
    void cboStatusBarangAction() {
        try {
            if (cboStatusBarang.getValue().equals("Ready")) {
                txtStock.setText("");
                txtStock.setDisable(false);
                txtStock.setEditable(true);
            } else if (cboStatusBarang.getValue().equals("Non Ready")) {
                txtStock.setText("0");
                txtStock.setDisable(true);
                txtStock.setEditable(false);
            }
        } catch (NullPointerException e) {
            System.out.println("Ada yang null nih! (2)");
        }
    }

    @FXML
    void btnUpdateAction() {
        try {
            Barang barang = updateAdd("Update");
            System.out.println(barang);
            barangDao.update(barang);

            cleanField();
            getFieldControl().cleanComboBoxResult(cboResult);
            getFieldControl().cleanComboBoxStatus(cboStatusBarang);

            // combo box harus di set ulang agar item-item selain yang di set tidak hilang
            getFieldControl().setComboBoxStatus(cboStatusBarang, "Status", "Ready", "Non Ready");

            AlertBox.display("Berhasil Update", "Berhasil update data barang");
        } catch (SQLException | NullPointerException e) {
            AlertBox.display("Gagal Update", "Gagal update data barang");
//                    e.printStackTrace();
        }
    }

    @FXML
    void btnAddAction() {
        try {
            Barang barang = updateAdd("Add");

            barangDao.add(barang);
            cleanField();
            getFieldControl().cleanComboBoxStatus(cboStatusBarang);

            // combo box harus di set ulang agar item-item selain yang di set tidak hilang
            getFieldControl().setComboBoxStatus(cboStatusBarang, "Status", "Ready", "Non Ready");

            AlertBox.display("Berhasil Tambah", "Berhasil tambah data barang");
        } catch (SQLException | NullPointerException e) {
            AlertBox.display("Gagal Tambah", "Gagal tambah data barang");
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
                    barangDao.delete(id);

                    cleanField();
                    getFieldControl().cleanComboBoxResult(cboResult);
                    getFieldControl().cleanComboBoxStatus(cboStatusBarang);

                    // combo box harus di set ulang agar item-item selain yang di set tidak hilang
                    getFieldControl().setComboBoxStatus(cboStatusBarang, "Status", "Ready", "Non Ready");

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
        txtCariBarang.setText("");
        txtId.setText("");
        txtNamaBarang.setText("");
        txtAreaDeskripsi.setText("");
        txtHarga.setText("");
        txtStock.setText("");
    }

    void setComboBoxResult(ObservableList<Barang> observableList) {
        Callback<ListView<Barang>, ListCell<Barang>> cellCallback = new Callback<ListView<Barang>, ListCell<Barang>>() {
            @Override
            public ListCell<Barang> call(ListView<Barang> pelangganListView) {
                ListCell<Barang> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Barang pelanggan, boolean b) {
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

    Barang updateAdd(String aksi) {
        Barang barang = null;
        try {
            String id = "";
            String namaBarang, deskripsi, status;
            int harga, stok;

            // aksi
            if (aksi.equals("Add")) {
                id = getUtil().getCustomId(barangDao.getAllId(), "B");
            }

            if (aksi.equals("Update")) {
                id = txtId.getText();
            }

            namaBarang = txtNamaBarang.getText();
            deskripsi = txtAreaDeskripsi.getText();
            harga = getValidation().validateInteger(txtHarga.getText());
            stok = getValidation().validateInteger(txtStock.getText());
            status = cboStatusBarang.getValue();

            barang = new Barang();
            barang.setIdBarang(id);
            barang.setNama(namaBarang);
            barang.setDeskripsi(deskripsi);
            barang.setHarga(harga);
            barang.setStok(stok);
            barang.setStatus(status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return barang;
    }
}
