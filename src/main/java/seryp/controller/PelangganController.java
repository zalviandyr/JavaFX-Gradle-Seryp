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
import seryp.model.Pelanggan;
import seryp.model.User;
import seryp.model.dao.PelangganDao;
import seryp.utils.*;
import seryp.utils.boxes.AlertBox;
import seryp.utils.boxes.ConfirmBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PelangganController extends SerypUtil implements Initializable {
    public HBox topBar;
    public VBox sideBar;
    public Button btnCari;
    public Button btnUpdate;
    public Button btnAdd;
    public Button btnDelete;
    public Button btnBack;
    public Button btnNext;
    public ToggleButton btnCariPelanggan;
    public TitledPane paneCariPelanggan;
    public TextField txtCariPelanggan;
    public TextField txtId;
    public TextField txtNamaPelanggan;
    public TextField txtNoHandphone;
    public TextArea txtAreaAlamat;
    public RadioButton rbLaki;
    public RadioButton rbPerempuan;
    public ComboBox<Pelanggan> cboResult;
    private Pelanggan pelanggan;
    private PelangganDao pelangganDao;
    public static User userLogin; // data yang dikirim KaryawanController

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
        btnCariAction();
        btnCariPelangganAction();
        btnUpdateAction();
        btnAddAction();
        btnDeleteAction();
        btnBackAction();
        btnNextAction();
        cboResultAction();
    }

    private void init() {
        // Instance object required
        pelangganDao = new PelangganDao();

        // set topBar
        getWindowControl().setTopBar(topBar);

        // set sideBar
        getWindowControl().setSideBar(sideBar, PelangganController.userLogin);

        // Set radio button group
        ToggleGroup jekelGroup = new ToggleGroup();
        rbLaki.setToggleGroup(jekelGroup);
        rbPerempuan.setToggleGroup(jekelGroup);

        // Set text field id non editable
        txtId.setEditable(false);

        // Set default value radio button
        rbLaki.setSelected(true);

        // set invisible button update and delete
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);

        // set unexpanded pane cari pelanggan
        paneCariPelanggan.setAnimated(true);
        paneCariPelanggan.setExpanded(false);
    }

    private void btnCariAction() {
        btnCari.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ObservableList<Pelanggan> observableList = null;

                try {
                    if (txtCariPelanggan.getText().equals("")) {
                        observableList = convertToObservableList(pelangganDao.searchAll());
                    } else {
                        String keyword = txtCariPelanggan.getText();
                        observableList = convertToObservableList(pelangganDao.search(keyword));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                setComboBoxResult(observableList);
            }
        });
    }

    private void btnCariPelangganAction() {
        btnCariPelanggan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // cleaning
                txtCariPelanggan.setText("");
                txtId.setText("");
                cleanComboBoxResult();

                if (btnCariPelanggan.isSelected()) {
                    // change style button
                    btnCariPelanggan.getStyleClass().remove("seryp-btn-primary");
                    btnCariPelanggan.getStyleClass().add("seryp-btn-secondary");
                    // set visibility button update and delete
                    btnUpdate.setVisible(true);
                    btnAdd.setVisible(false);
                    btnDelete.setVisible(true);
                    paneCariPelanggan.setExpanded(true);
                } else {
                    // change style button
                    btnCariPelanggan.getStyleClass().remove("seryp-btn-secondary");
                    btnCariPelanggan.getStyleClass().add("seryp-btn-primary");
                    // set visibility button update and delete
                    btnUpdate.setVisible(false);
                    btnAdd.setVisible(true);
                    btnDelete.setVisible(false);
                    paneCariPelanggan.setExpanded(false);
                }
            }
        });
    }

    private void btnUpdateAction() {
        btnUpdate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String idPelanggan = txtId.getText();
                String namaPelanggan = txtNamaPelanggan.getText();
                String noHandphone = getValidation().validateNoHandphone(txtNoHandphone.getText());
                String alamat = txtAreaAlamat.getText();
                String jekel = getFieldControl().getTextSelectedRadios(rbLaki, rbPerempuan);

                try {
                    pelanggan = new Pelanggan();
                    pelanggan.setIdPelanggan(idPelanggan);
                    pelanggan.setNama(namaPelanggan);
                    pelanggan.setNoHp(noHandphone);
                    pelanggan.setAlamat(alamat);
                    pelanggan.setJekel(jekel);

                    pelangganDao.update(pelanggan);
                    cleanField();
                    cleanComboBoxResult();
                    AlertBox.display("Berhasil update", "Berhasil update pelanggan");
                } catch (SQLException | NullPointerException e) {
                    AlertBox.display("Gagal update", "Gagal update pelanggan");
//                    e.printStackTrace();
                }
            }
        });
    }

    private void btnAddAction() {
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String idPelanggan = getUtil().getCustomId(getIdPelanggan(), "P");
                String namaPelanggan = txtNamaPelanggan.getText();
                String noHandphone = getValidation().validateNoHandphone(txtNoHandphone.getText());
                String alamat = txtAreaAlamat.getText();
                String jekel = getFieldControl().getTextSelectedRadios(rbLaki, rbPerempuan);

                try {
                    pelanggan = new Pelanggan();
                    pelanggan.setIdPelanggan(idPelanggan);
                    pelanggan.setNama(namaPelanggan);
                    pelanggan.setNoHp(noHandphone);
                    pelanggan.setAlamat(alamat);
                    pelanggan.setJekel(jekel);

                    pelangganDao.add(pelanggan);
                    cleanField();
                    AlertBox.display("Berhasil tambah", "Berhasil tambah pelanggan baru");
                } catch (SQLException | NullPointerException e) {
                    AlertBox.display("Gagal tambah", "Gagal tambah pelanggan baru");
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
                    // cek jika id kosong, maka tidak ada penghapusan user
                    if (!(txtId.getText().equals(""))) {
                        boolean option = ConfirmBox.display("Apakah yakin menghapus pelanggan ?", "Jika pelanggan tersebut dihapus maka transaksi yang melalui pelanggan tersebut akan terhapus.");
                        String idPelanggan = txtId.getText();

                        // cek option
                        if (option) {
                            pelangganDao.delete(idPelanggan);

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
                getWindowControl().moveToScene(btnBack, "karyawan");
            }
        });
    }

    private void btnNextAction() {
        btnNext.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (txtId.getText().equals("")) {
                    AlertBox.display("Error", "ID Pelanggan masih kosong, pilih pelanggan yang sudah ada atau tambah baru.");
                } else {
                    ServiceController.namaPelanggan = txtNamaPelanggan.getText(); // Set nama pelanggan for ServiceController
                    ServiceController.idPelanggan = txtId.getText(); // Set Id pelanggan for ServiceController
                    getWindowControl().moveToScene(btnNext, "service");
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
                    Pelanggan pelanggan = cboResult.getValue(); // possible Null Pointer
                    pelanggan = pelangganDao.get(pelanggan.getIdPelanggan());

                    txtId.setText(pelanggan.getIdPelanggan());
                    txtNamaPelanggan.setText(pelanggan.getNama());
                    txtAreaAlamat.setText(pelanggan.getAlamat());
                    txtNoHandphone.setText(pelanggan.getNoHp());

                    if (pelanggan.getJekel().equals("Laki laki")) {
                        rbLaki.setSelected(true);
                    }
                    if (pelanggan.getJekel().equals("Perempuan")) {
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
        txtCariPelanggan.setText("");
        txtId.setText("");
        txtNamaPelanggan.setText("");
        txtNoHandphone.setText("");
        txtAreaAlamat.setText("");
        rbLaki.setSelected(true);
    }

    ObservableList<Pelanggan> convertToObservableList(ResultSet resultSet) {
        /**
         * Convert Result Set contains ID and Name to Observable List
         */
        ObservableList<Pelanggan> observableList = FXCollections.observableArrayList();

        try {
            while (resultSet.next()) {
                observableList.add(new Pelanggan(resultSet.getString(1), resultSet.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return observableList;
    }

    void setComboBoxResult(ObservableList<Pelanggan> observableList) {
        Callback<ListView<Pelanggan>, ListCell<Pelanggan>> cellCallback = new Callback<ListView<Pelanggan>, ListCell<Pelanggan>>() {
            @Override
            public ListCell<Pelanggan> call(ListView<Pelanggan> pelangganListView) {
                ListCell<Pelanggan> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Pelanggan pelanggan, boolean b) {
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

        cboResult.setPromptText("Search Result Here!");
        cboResult.setItems(observableList);
        cboResult.setCellFactory(cellCallback);
        cboResult.setButtonCell(cellCallback.call(null));
    }

    void cleanComboBoxResult() {
        cboResult.getItems().clear();
    }

    List<String> getIdPelanggan() {
        try {
            List<String> listId = new ArrayList<>();

            ResultSet resultSet = pelangganDao.getAllId();
            while (resultSet.next()) {
                listId.add(resultSet.getString(1));
            }

            return listId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
