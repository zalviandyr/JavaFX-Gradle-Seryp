package seryp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seryp.model.*;
import seryp.model.dao.*;
import seryp.utils.SerypUtil;
import seryp.utils.boxes.AlertBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class PembayaranController extends SerypUtil implements Initializable {
    public HBox topBar;
    public VBox sideBar;
    public Button btnCari;
    public Button btnBack;
    public Button btnKonfirmasiPembayaran;
    public TextField txtCariNoFaktur;
    public TextField txtNamaPelanggan;
    public TextField txtMerekLabel;
    public TextField txtPembayaranDP;
    public TextField txtPembayaranTunai;
    public TableView<KerusakanDanBarang> tblKerusakanDanBarang;
    public Label lblEstimasi;
    public CheckBox cbPembayaranDP;
    public CheckBox cbPembayaranTunai;
    private KerusakanDao kerusakanDao;
    private BarangDao barangDao;
    private ServisDao servisDao;
    private PelangganDao pelangganDao;
    private DetailKerusakanDao detailKerusakanDao;
    public static User userLogin; // data from karyawan seryp.controller

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
        cbPembayaranDPAction();
        cbPembayaranTunaiAction();
        btnCariAction();
        btnBackAction();
        btnKonfirmasiPembayaranAction();
    }

    private void init() {
        // instance required object
        kerusakanDao = new KerusakanDao();
        barangDao = new BarangDao();
        servisDao = new ServisDao();
        pelangganDao = new PelangganDao();
        detailKerusakanDao = new DetailKerusakanDao();

        // set topBar
        getWindowControl().setTopBar(topBar);

        // set sideBar
        getWindowControl().setSideBar(sideBar, PembayaranController.userLogin);

        // set text field pembayaran non editable
        txtPembayaranDP.setEditable(false);
        txtPembayaranTunai.setEditable(false);
    }

    private void cbPembayaranDPAction() {
        cbPembayaranDP.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (cbPembayaranDP.isSelected()) {
                    txtPembayaranDP.setEditable(true);
                } else {
                    txtPembayaranDP.setEditable(false);
                }
            }
        });
    }

    private void cbPembayaranTunaiAction() {
        cbPembayaranTunai.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (cbPembayaranTunai.isSelected()) {
                    txtPembayaranTunai.setEditable(true);
                } else {
                    txtPembayaranTunai.setEditable(false);
                }
            }
        });
    }

    private void btnCariAction() {
        btnCari.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (!txtCariNoFaktur.getText().equals("")) {
                        ObservableList<KerusakanDanBarang> observableList = FXCollections.observableArrayList();
                        // untuk label bawah table
                        int totalEstimasiMin = 0;
                        int totalEstimasiMax = 0;
                        String noFaktur = txtCariNoFaktur.getText();
                        Servis servis = servisDao.get(noFaktur);

                        if (servis != null) {
                            List<DetailKerusakan> list = detailKerusakanDao.get(noFaktur);

                            // set data pelanggan
                            Pelanggan pelanggan = pelangganDao.get(servis.getIdPelanggan());
                            txtNamaPelanggan.setText(pelanggan.getNama());
                            txtMerekLabel.setText(servis.getMerkLabel());

                            // clean and reset component
                            cbPembayaranDP.setDisable(false);
                            cbPembayaranTunai.setDisable(false);
                            cbPembayaranDP.setSelected(false);
                            cbPembayaranTunai.setSelected(false);
                            txtPembayaranDP.setText("");
                            txtPembayaranTunai.setText("");

                            // set jika pelanggan sudah membayar dp atau tunai
                            if (servis.isStatusDP()) {
                                // disable
                                cbPembayaranDP.setDisable(true);
                                txtPembayaranDP.setEditable(false);

                                cbPembayaranDP.setSelected(true);
                                txtPembayaranDP.setText(String.valueOf(servis.getUangDP()));
                            }

                            if (servis.isStatusPembayaran()) {
                                //disable
                                cbPembayaranTunai.setDisable(true);
                                txtPembayaranTunai.setEditable(false);

                                cbPembayaranTunai.setSelected(true);
                                txtPembayaranTunai.setText(String.valueOf(servis.getUangPembayaran()));
                            }

                            for (DetailKerusakan detailKerusakan : list) {
                                String kerusakanDanBarang, estimasi;
                                String idKerusakan = detailKerusakan.getIdKerusakan();
                                String idBarang = detailKerusakan.getIdBarang();
                                int estimasiMin = detailKerusakan.getTotalEstimasiMin();
                                int estimasiMax = detailKerusakan.getTotalEstimasiMax();

                                Kerusakan kerusakan = kerusakanDao.get(idKerusakan);
                                // jika barang yang diganti tidak ada
                                if (idBarang == null){
                                    kerusakanDanBarang = kerusakan.getNama();
                                } else {
                                    Barang barang = barangDao.get(idBarang);
                                    kerusakanDanBarang = kerusakan.getNama() + " + " + barang.getNama() + " " + detailKerusakan.getUnit() + " unit";
                                }
                                estimasi = estimasiMin + " - " + estimasiMax;
                                observableList.add(new KerusakanDanBarang(kerusakanDanBarang, estimasi));

                                totalEstimasiMin += estimasiMin;
                                totalEstimasiMax += estimasiMax;
                            }

                            // Set label bawah table
                            lblEstimasi.setText("Rp. " + totalEstimasiMin + " - " + "Rp. " + totalEstimasiMax);

                            // untuk menset nilai setiap cell (row) per column maka harus memberi sebuah PropertyValueFactory,
                            // yang mana parameter pertama harus sama dengan variable pada kelas
                            TableColumn<KerusakanDanBarang, ?> colListKerusakanDanBarang = tblKerusakanDanBarang.getColumns().get(0);
                            colListKerusakanDanBarang.setCellValueFactory(new PropertyValueFactory<>("kerusakanDanBarang"));

                            TableColumn<KerusakanDanBarang, ?> colEstimasi = tblKerusakanDanBarang.getColumns().get(1);
                            colEstimasi.setCellValueFactory(new PropertyValueFactory<>("estimasi"));

                            tblKerusakanDanBarang.setItems(observableList);
                        } else {
                            AlertBox.display("Error", "No Faktur tidak ada, periksa kembali!");
                        }
                    } else {
                        AlertBox.display("Error", "No Faktur tidak boleh kosong");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
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

    private void btnKonfirmasiPembayaranAction() {
        btnKonfirmasiPembayaran.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (!txtCariNoFaktur.getText().equals("")) {
                        String noFaktur = txtCariNoFaktur.getText();
                        Servis servis = servisDao.get(noFaktur);

                        // cek jika pelanggan sudah membayar maka tombolok konfimasi pembayaran tidak melakukan apapun
                        if (servis.isStatusPembayaran()) {
                            cbPembayaranDP.setDisable(true);
                            cbPembayaranTunai.setDisable(true);
                            txtPembayaranDP.setEditable(false);
                            txtPembayaranTunai.setEditable(false);
                            AlertBox.display("Tidak ada aksi", "Pelanggan sudah melakukan pembayaran");
                        } else if (cbPembayaranDP.isSelected() || cbPembayaranTunai.isSelected()) {
                            if (cbPembayaranDP.isSelected()) {
                                int pembayaranDP = getValidation().validateInteger(txtPembayaranDP.getText());
                                servisDao.setPembayaranDP(noFaktur, pembayaranDP);
                            }

                            if (cbPembayaranTunai.isSelected()) {
                                int pembayaranTunai = getValidation().validateInteger(txtPembayaranTunai.getText());
                                servisDao.setPembayaranTunai(noFaktur, pembayaranTunai);
                            }
                            AlertBox.display("Berhasil", "Konfimasi Pembayaran pelanggan berhasil");
                            getWindowControl().moveToScene(btnKonfirmasiPembayaran, "karyawan");
                        } else {
                            AlertBox.display("Error", "Centang salah satu pembayaran!");
                        }
                    } else {
                        AlertBox.display("Error", "No Faktur tidak boleh kosong");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
