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
import seryp.model.*;
import seryp.model.dao.BarangDao;
import seryp.model.dao.KerusakanDao;
import seryp.model.dao.ServisDao;
import seryp.utils.*;
import seryp.utils.boxes.AlertBox;
import seryp.utils.boxes.ConfirmBox;
import seryp.utils.boxes.DialogBoxServis;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ServiceController extends SerypUtil implements Initializable {
    public HBox topBar;
    public VBox sideBar;
    public Button btnBack;
    public Button btnTambahkanKerusakan;
    public Button btnDone;
    public TextField txtNamaPelanggan;
    public TextField txtIdPelanggan;
    public TextField txtMerekLabel;
    public TextField txtUnit;
    public TextField txtUnitAda;
    public TextArea txtAreaDetailKerusakan;
    public TextArea txtAreaDetailKomponen;
    public CheckBox cbKomponenDiganti;
    public ComboBox<Kerusakan> cboKerusakan;
    public ComboBox<Barang> cboKomponenDiganti;
    private KerusakanDao kerusakanDao;
    private BarangDao barangDao;
    private ServisDao servisDao;
    private Servis servis;
    private DetailKerusakan detailKerusakan;
    private List<DetailKerusakan> listDetailKerusakan;
    public static User userLogin; // data yang dikirim KaryawanController
    public static String namaPelanggan;
    public static String idPelanggan;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
        getMerekLabel();
        btnBackAction();
        btnTambahkanKerusakanAction();
        btnDoneAction();
        cboKerusakanAction();
        cboKomponenDigantiAction();
        cbKomponenDigantiAction();
    }

    private void init() {
        // Instance object required
        kerusakanDao = new KerusakanDao();
        barangDao = new BarangDao();
        servisDao = new ServisDao();
        listDetailKerusakan = new ArrayList<>();

        // set topBar
        getWindowControl().setTopBar(topBar);

        // set sideBar
        getWindowControl().setSideBar(sideBar, ServiceController.userLogin);

        // set editable text field and text area
        txtNamaPelanggan.setEditable(false);
        txtIdPelanggan.setEditable(false);
        txtMerekLabel.setEditable(false);
        txtAreaDetailKerusakan.setEditable(false);
        txtUnit.setEditable(false);
        txtUnitAda.setEditable(false);
        txtAreaDetailKomponen.setEditable(false);

        // Set TextField nama pelanggan dan id pelanggan
        txtNamaPelanggan.setText(namaPelanggan);
        txtIdPelanggan.setText(idPelanggan);

        // Set combo box
        setComboBoxKerusakan();
        setComboBoxKomponenDiganti();
        cboKomponenDiganti.setDisable(true);
    }

    private void getMerekLabel() {
        try {
            // jiak merek label masih kosong
            if (txtMerekLabel.getText().equals("")) {
                // mengambil sudah berapa kali pelanggan tersebut menservis
                int servisKe = servisDao.getCountServisPelanggan(idPelanggan) + 1;
                txtMerekLabel.setText(DialogBoxServis.display(idPelanggan, servisKe));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void btnBackAction() {
        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                boolean option = ConfirmBox.display("Yakin ?", "Semua kerusakan yang diinputkan akan hilang. \n Apakah yakin untuk kembali ?");
                if (option)
                    getWindowControl().moveToScene(btnBack, "pelanggan");
            }
        });
    }

    private void btnTambahkanKerusakanAction() {
        btnTambahkanKerusakan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    String noFaktur = servisDao.getNoFaktur(txtMerekLabel.getText());

                    if (noFaktur == null) {
                        // Add no faktur baru jika merek label tidak ada
                        noFaktur = getUtil().getCustomId(getNoFaktur(), "F");
                        String username = ServiceController.userLogin.getUsername();
                        String idPelanggan = ServiceController.idPelanggan;
                        String merekLabel = txtMerekLabel.getText();
                        LocalDate tanggalHariIni = LocalDate.now();
                        LocalDate batasHari = LocalDate.now().plusDays(20);
                        boolean statusDp = false;
                        int uangDp = 0;
                        boolean statusPembayaran = false;
                        int uangPembayaran = 0;

                        servis = new Servis();
                        servis.setNoFaktur(noFaktur);
                        servis.setUsername(username);
                        servis.setIdPelanggan(idPelanggan);
                        servis.setMerkLabel(merekLabel);
                        servis.setTanggalHariIni(tanggalHariIni);
                        servis.setBatasHari(batasHari);
                        servis.setStatusDP(statusDp);
                        servis.setUangDP(uangDp);
                        servis.setStatusPembayaran(statusPembayaran);
                        servis.setUangPembayaran(uangPembayaran);
                    }
                    // setelah no faktur baru ditambah, maka akan menambah data ke detail kerusakan
                    addListDetailKerusakan(noFaktur);

                } catch (SQLException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void btnDoneAction() {
        btnDone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (listDetailKerusakan.isEmpty()) {
                    AlertBox.display("Gagal", "Gagal pindah karena kerusakan belum diinputkan");
                } else {
                    ListController.servis = servis;
                    ListController.listDetailKerusakan = listDetailKerusakan;
                    getWindowControl().moveToScene(btnDone, "list");
                }
            }
        });
    }

    private void cboKerusakanAction() {
        cboKerusakan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Kerusakan kerusakan = cboKerusakan.getValue();
                    kerusakan = kerusakanDao.get(kerusakan.getIdKerusakan());

                    txtAreaDetailKerusakan.setText(kerusakan.getDeskripsi());
                } catch (SQLException | NullPointerException e) {
                    System.out.println("Ada yang null nih!");
//                    e.printStackTrace();
                }
            }
        });
    }

    private void cboKomponenDigantiAction() {
        cboKomponenDiganti.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Barang barang = cboKomponenDiganti.getValue();
                    barang = barangDao.get(barang.getIdBarang());

                    txtUnitAda.setText(String.valueOf(barang.getStok()));
                    txtAreaDetailKomponen.setText(barang.getDeskripsi());
                } catch (SQLException | NullPointerException e) {
                    System.out.println("Ada yang null nih!");
//                    e.printStackTrace();
                }
            }
        });
    }

    private void cbKomponenDigantiAction() {
        cbKomponenDiganti.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (cbKomponenDiganti.isSelected()) {
                    cboKomponenDiganti.setDisable(false);
                    txtUnit.setEditable(true);
                } else {
                    cboKomponenDiganti.setDisable(true);
                    txtUnit.setEditable(false);
                }
                txtAreaDetailKomponen.setText("");
            }
        });
    }

    void cleanField() {
        txtAreaDetailKerusakan.setText("");
        txtUnit.setText("1");
        txtAreaDetailKomponen.setText("");
        txtUnitAda.setText("");
    }

    void addListDetailKerusakan(String noFaktur) {
        try {
            detailKerusakan = new DetailKerusakan();
            Kerusakan kerusakan = cboKerusakan.getValue();
            kerusakan = kerusakanDao.get(kerusakan.getIdKerusakan());

            // init barang
            String idBarang = "null";
            int hargaBarang = 0;
            int unit = 0;

            String idKerusakan = kerusakan.getIdKerusakan();
            int estimasiMinKerusakan = kerusakan.getEstimasiMin();
            int estimasiMaxKerusakan = kerusakan.getEstimasiMax();

            if (cbKomponenDiganti.isSelected()) {
                Barang barang = cboKomponenDiganti.getValue();
                barang = barangDao.get(barang.getIdBarang());
                idBarang = barang.getIdBarang();
                unit = Integer.parseInt(txtUnit.getText());
                hargaBarang = barang.getHarga() * unit;
            }

            int totalEstimasiMin = estimasiMinKerusakan + hargaBarang;
            int totalEstimasiMax = estimasiMaxKerusakan + hargaBarang;

            detailKerusakan.setNoFaktur(noFaktur);
            detailKerusakan.setIdKerusakan(idKerusakan);
            detailKerusakan.setIdBarang(idBarang);
            detailKerusakan.setUnit(unit);
            detailKerusakan.setTotalEstimasiMin(totalEstimasiMin);
            detailKerusakan.setTotalEstimasiMax(totalEstimasiMax);

            // jika txtUnitAda berisi text
            if (!txtUnitAda.getText().equals("")) {
                if (Integer.parseInt(txtUnit.getText()) > Integer.parseInt(txtUnitAda.getText())) {
                    AlertBox.display("Error", "Jumlah stok tidak memadai");
                } else {
                    listDetailKerusakan.add(detailKerusakan);
                    cleanField();
                }
            } else {
                listDetailKerusakan.add(detailKerusakan);
                cleanField();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void setComboBoxKerusakan() {
        try {
            ResultSet resultSet = kerusakanDao.searchAll();

            ObservableList<Kerusakan> observableList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                observableList.add(new Kerusakan(resultSet.getString(1), resultSet.getString(2)));
            }

            Callback<ListView<Kerusakan>, ListCell<Kerusakan>> callback = new Callback<ListView<Kerusakan>, ListCell<Kerusakan>>() {
                @Override
                public ListCell<Kerusakan> call(ListView<Kerusakan> kerusakanListView) {
                    ListCell<Kerusakan> listCell = new ListCell<>() {
                        @Override
                        protected void updateItem(Kerusakan kerusakan, boolean empty) {
                            super.updateItem(kerusakan, empty);

                            if (!empty) {
                                setText(kerusakan.getNama());
                            }
                        }
                    };
                    return listCell;
                }
            };

            cboKerusakan.setPromptText("Pilih Kerusakan");
            cboKerusakan.setItems(observableList);
            cboKerusakan.setCellFactory(callback);
            cboKerusakan.setButtonCell(callback.call(null));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void setComboBoxKomponenDiganti() {
        try {
            ResultSet resultSet = barangDao.searchAll();

            ObservableList<Barang> observableList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                observableList.add(new Barang(resultSet.getString(1), resultSet.getString(2)));
            }

            Callback<ListView<Barang>, ListCell<Barang>> callback = new Callback<ListView<Barang>, ListCell<Barang>>() {
                @Override
                public ListCell<Barang> call(ListView<Barang> barangListView) {
                    ListCell<Barang> listCell = new ListCell<>() {
                        @Override
                        protected void updateItem(Barang barang, boolean empty) {
                            super.updateItem(barang, empty);

                            if (!empty) {
                                setText(barang.getNama());
                            }
                        }
                    };
                    return listCell;
                }
            };

            cboKomponenDiganti.setPromptText("Pilih Komponen");
            cboKomponenDiganti.setItems(observableList);
            cboKomponenDiganti.setCellFactory(callback);
            cboKomponenDiganti.setButtonCell(callback.call(null));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    List<String> getNoFaktur() {
        try {
            List<String> listId = new ArrayList<>();

            ResultSet resultSet = servisDao.getAllId();
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
