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
import seryp.model.Barang;
import seryp.model.User;
import seryp.model.dao.BarangDao;
import seryp.model.dao.IdentitasTokoDao;
import seryp.utils.*;
import seryp.utils.boxes.AlertBox;
import seryp.utils.boxes.ConfirmBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    public ToggleButton btnCariBarang;
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
    public ComboBox<String> cboStatus;
    private BarangDao barangDao;
    private IdentitasTokoDao identitasTokoDao;
    private Barang barang;
    public static User userLogin; // data yang dikirim dari AdminController

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
        btnBackAction();
        btnCariAction();
        btnCariBarangAction();
        btnUpdateAction();
        btnAddAction();
        btnDeleteAction();
        toggleBtnSettingAction();
        cboResultAction();
    }

    private void init() {
        // instance object
        barangDao = new BarangDao();
        identitasTokoDao = new IdentitasTokoDao();

        // Set topBar
        getWindowControl().setTopBar(topBar);

        // Set sideBar
        getWindowControl().setSideBar(sideBar, BarangController.userLogin);

        // Set settingBar
        try {
            getWindowControl().setSettingBar(settingBar, identitasTokoDao.get());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // init combo box
        setComboBoxStatus();

        // Setting Text Field
        txtId.setEditable(false);

        // set unexpanded and animated title pane
        paneCariBarang.setAnimated(true);
        paneCariBarang.setExpanded(false);

        paneSetting.setAnimated(true);
        paneSetting.setExpanded(false);

        // set button update and delete invisible
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
    }

    private void btnBackAction() {
        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getWindowControl().moveToScene(btnBack, "admin");
            }
        });
    }

    private void btnCariAction() {
        btnCari.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ObservableList<Barang> observableList = null;

                try {
                    if (txtCariBarang.getText().equals("")) {
                        observableList = convertToObservableList(barangDao.searchAll());
                    } else {
                        String keyword = txtCariBarang.getText();
                        observableList = convertToObservableList(barangDao.search(keyword));
                    }

                    setComboBoxResult(observableList);
                } catch (SQLException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void btnCariBarangAction() {
        btnCariBarang.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // cleaning
                txtCariBarang.setText("");
                txtId.setText("");
                cleanComboBoxResult();

                if (btnCariBarang.isSelected()) {
                    // change style button
                    btnCariBarang.getStyleClass().remove("seryp-btn-primary");
                    btnCariBarang.getStyleClass().add("seryp-btn-secondary");
                    btnUpdate.setVisible(true);
                    btnAdd.setVisible(false);
                    btnDelete.setVisible(true);
                    paneCariBarang.setExpanded(true);
                } else {
                    // change style button
                    btnCariBarang.getStyleClass().remove("seryp-btn-secondary");
                    btnCariBarang.getStyleClass().add("seryp-btn-primary");
                    btnUpdate.setVisible(false);
                    btnAdd.setVisible(true);
                    btnDelete.setVisible(false);
                    paneCariBarang.setExpanded(false);
                }
            }
        });
    }

    private void btnUpdateAction() {
        btnUpdate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String id = txtId.getText();
                String namaBarang = txtNamaBarang.getText();
                String deskripsi = txtAreaDeskripsi.getText();
                int harga = getValidation().validateInteger(txtHarga.getText());
                int stok = getValidation().validateInteger(txtStock.getText());
                String status = cboStatus.getValue();

                try {
                    barang = new Barang();

                    barang.setIdBarang(id);
                    barang.setNama(namaBarang);
                    barang.setDeskripsi(deskripsi);
                    barang.setHarga(harga);
                    barang.setStok(stok);
                    barang.setStatus(status);

                    barangDao.update(barang);
                    cleanField();
                    cleanComboBoxResult();
                    cleanComboBoxStatus();
                    AlertBox.display("Berhasil Update", "Berhasil update data barang");
                } catch (SQLException | NullPointerException e) {
                    AlertBox.display("Gagal Update", "Gagal update data barang");
//                    e.printStackTrace();
                }
            }
        });
    }

    private void btnAddAction() {
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String id = getUtil().getCustomId(getIdBarang(), "B");
                String namaBarang = txtNamaBarang.getText();
                String deskripsi = txtAreaDeskripsi.getText();
                int harga = getValidation().validateInteger(txtHarga.getText());
                int stok = getValidation().validateInteger(txtStock.getText());
                String status = cboStatus.getValue();

                try {
                    barang = new Barang();

                    barang.setIdBarang(id);
                    barang.setNama(namaBarang);
                    barang.setDeskripsi(deskripsi);
                    barang.setHarga(harga);
                    barang.setStok(stok);
                    barang.setStatus(status);

                    barangDao.add(barang);
                    cleanField();
                    AlertBox.display("Berhasil Tambah", "Berhasil tambah data barang");
                } catch (SQLException | NullPointerException e) {
                    AlertBox.display("Gagal Tambah", "Gagal tambah data barang");
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

                        if (option) {
                            String id = txtId.getText();
                            barangDao.delete(id);

                            cleanField();
                            cleanComboBoxResult();
                            cleanComboBoxStatus();
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
                    Barang barang = cboResult.getValue(); // possible NullPointer
                    barang = barangDao.get(barang.getIdBarang());

                    txtId.setText(barang.getIdBarang());
                    txtNamaBarang.setText(barang.getNama());
                    txtAreaDeskripsi.setText(barang.getDeskripsi());
                    txtHarga.setText(String.valueOf(barang.getHarga()));
                    txtStock.setText(String.valueOf(barang.getStok()));

                    // combo box harus di set ulang agar item-item selain yang di set tidak hilang
                    setComboBoxStatus();
                    cboStatus.setValue(barang.getStatus());
                } catch (SQLException | NullPointerException e) {
                    System.out.println("Ada yang null nih!");
//                    e.printStackTrace();
                }
            }
        });
    }

    void cleanField() {
        txtCariBarang.setText("");
        txtId.setText("");
        txtNamaBarang.setText("");
        txtAreaDeskripsi.setText("");
        txtHarga.setText("");
        txtStock.setText("");
    }

    List<String> getIdBarang() {
        try {
            List<String> listId = new ArrayList<>();

            ResultSet resultSet = barangDao.getAllId();
            while (resultSet.next()) {
                listId.add(resultSet.getString(1));
            }

            return listId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    ObservableList<Barang> convertToObservableList(ResultSet resultSet) {
        /**
         * Convert Result Set contains ID and Name to Observable List
         */
        ObservableList<Barang> observableList = FXCollections.observableArrayList();

        try {
            while (resultSet.next()) {
                observableList.add(new Barang(resultSet.getString(1), resultSet.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return observableList;
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

    void setComboBoxStatus() {
        cboStatus.setPromptText("Status");
        cboStatus.getItems().addAll("Ready", "Non Ready");
    }

    void cleanComboBoxResult() {
        cboResult.getItems().clear();
    }

    void cleanComboBoxStatus() {
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

        cboStatus.getItems().clear();
        cboStatus.setButtonCell(listCell);
    }
}
