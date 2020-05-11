package seryp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.util.JRLoader;
import seryp.model.*;
import seryp.model.dao.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import seryp.utils.SerypUtil;
import seryp.utils.boxes.ConfirmBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ListController extends SerypUtil implements Initializable {
    public HBox topBar;
    public VBox sideBar;
    public Button btnBack;
    public Button btnCetakStruk;
    public Label lblTotalEstimasi;
    public TableView<KerusakanDanBarang> tblListKerusakan;
    public DatePicker datePickerTanggalHariIni;
    public DatePicker datePickerTanggalBatasHari;
    private BarangDao barangDao;
    private KerusakanDao kerusakanDao;
    private ServisDao servisDao;
    private PelangganDao pelangganDao;
    private UserDao userDao;
    private IdentitasTokoDao identitasTokoDao;
    private DetailKerusakanDao detailKerusakanDao;
    private LaporanDao laporanDao;
    public static User userLogin; // data yang dikirim KaryawanController
    public static List<DetailKerusakan> listDetailKerusakan; // data yang dikirim dari service seryp.controller
    public static Servis servis; // data yang dikirim dari service seryp.controller

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // instance required object
        barangDao = new BarangDao();
        kerusakanDao = new KerusakanDao();
        servisDao = new ServisDao();
        pelangganDao = new PelangganDao();
        userDao = new UserDao();
        detailKerusakanDao = new DetailKerusakanDao();
        identitasTokoDao = new IdentitasTokoDao();
        laporanDao = new LaporanDao();

        // set topBar
        getWindowControl().setTopBar(topBar);

        // set sideBar
        getWindowControl().setSideBar(sideBar, ListController.userLogin);

        // set Table List Kerusakan
        setTblListKerusakan();

        // Set DatePicker
        datePickerTanggalHariIni.setValue(LocalDate.now());
        datePickerTanggalHariIni.setEditable(false);
        datePickerTanggalBatasHari.setValue(LocalDate.now().plusDays(20));
        datePickerTanggalBatasHari.setEditable(false);
    }

    @FXML
    void btnBackAction() {
        boolean option = ConfirmBox.display("Yakin ?", "Semua kerusakan yang diinputkan akan hilang. \n Apakah yakin untuk kembali ?");
        if (option)
            getWindowControl().moveToScene(btnBack, "pelanggan");
    }

    @FXML
    void btnCetakStrukAction() {
        try {
            // Add servis ketika sudah pas untuk mencetak struk
            if (ListController.servis != null) {
                if (servisDao.get(ListController.listDetailKerusakan.get(0).getNoFaktur()) == null) {
                    // Add dilakukan jika no faktur belum ada, jika ada maka hanya mencetak struk tanpa menginsert data ke database
                    boolean option = ConfirmBox.display("Konfirmasi input data", "Setelah sudah mencetak struk data-data kerusakan tidak bisa diubah.\nApakah yakin cetak struk ?");
                    if (option) {
                        servisDao.add(ListController.servis);

                        for (DetailKerusakan detailKerusakan : ListController.listDetailKerusakan) {
                            // update stok barang
                            String idBarang = detailKerusakan.getIdBarang();
                            if (!(idBarang.equals("null"))) {
                                Barang barang = barangDao.get(idBarang);
                                int stokSisa = barang.getStok() - detailKerusakan.getUnit();
                                barangDao.updateStok(barang.getIdBarang(), stokSisa);
                            }
                            detailKerusakanDao.add(detailKerusakan);
                        }
                    }
                }

                // generate struk
                URL url = getClass().getResource("/seryp/laporan/struk/Struk.jasper");
                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);

                // get data
                Servis servis = servisDao.get(ListController.listDetailKerusakan.get(0).getNoFaktur());
                Pelanggan pelanggan = pelangganDao.get(servis.getIdPelanggan());
                User user = userDao.get(servis.getUsername());
                IdentitasToko identitasToko = identitasTokoDao.get();
                String totalEstimasi = laporanDao.getTotalEstimasiStruk(servis.getNoFaktur());

                List<KerusakanDanBarang> list = new ArrayList<>();
                for (DetailKerusakan detailKerusakan : ListController.listDetailKerusakan) {
                    String kerusakanDanBarang, estimasi;
                    String idKerusakan = detailKerusakan.getIdKerusakan();
                    String idBarang = detailKerusakan.getIdBarang();
                    int unit = detailKerusakan.getUnit();
                    int estimasiMin = detailKerusakan.getTotalEstimasiMin();
                    int estimasiMax = detailKerusakan.getTotalEstimasiMax();

                    Kerusakan kerusakan = kerusakanDao.get(idKerusakan);
                    // jika barang yang diganti ada
                    if (idBarang.equals("null")) {
                        kerusakanDanBarang = kerusakan.getNama();
                    } else {
                        Barang barang = barangDao.get(idBarang);
                        kerusakanDanBarang = kerusakan.getNama() + " + " + barang.getNama() + " " + unit + " Unit";
                    }
                    estimasi = "Rp. " + estimasiMin + " - " + "Rp. " + estimasiMax;
                    list.add(new KerusakanDanBarang(kerusakanDanBarang, estimasi));
                }

                Map<String, Object> map = new HashMap<>();
                map.put("namaToko", identitasToko.getNamaToko());
                map.put("alamatToko", identitasToko.getAlamat());
                map.put("idFaktur", servis.getNoFaktur());
                map.put("namaPelanggan", pelanggan.getNama());
                map.put("merekLabel", servis.getMerkLabel());
                map.put("noHandphonePelanggan", pelanggan.getNoHp());
                map.put("tanggalBatasHari", servis.getBatasHari().toString());
                map.put("alamatPelanggan", pelanggan.getAlamat());
                map.put("totalEstimasi", totalEstimasi);
                map.put("namaKaryawan", user.getNama());
                map.put("noHandphoneKaryawan", user.getNoHp());
                map.put("tanggalHariIni", servis.getTanggalHariIni().toString());

                JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, jrBeanCollectionDataSource);

                // file chooser to save struk
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
                fileChooser.getExtensionFilters().add(extensionFilter);
                fileChooser.setInitialFileName("Struk Pelanggan - " + pelanggan.getNama() + "_" + servis.getNoFaktur() + ".pdf");
                File filetoSave = fileChooser.showSaveDialog(new Stage());

                if (filetoSave != null)
                    JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(filetoSave));
            }
        } catch (SQLException | FileNotFoundException | JRException e) {
            e.printStackTrace();
        }
    }

    void setTblListKerusakan() {
        try {
            ObservableList<KerusakanDanBarang> observableList = FXCollections.observableArrayList();
            // untuk label bawah table
            int totalEstimasiMin = 0;
            int totalEstimasiMax = 0;

            for (DetailKerusakan detailKerusakan : ListController.listDetailKerusakan) {
                String kerusakanDanBarang, estimasi;
                String idKerusakan = detailKerusakan.getIdKerusakan();
                String idBarang = detailKerusakan.getIdBarang();
                int unit = detailKerusakan.getUnit();
                int estimasiMin = detailKerusakan.getTotalEstimasiMin();
                int estimasiMax = detailKerusakan.getTotalEstimasiMax();

                Kerusakan kerusakan = kerusakanDao.get(idKerusakan);
                // jika barang yang diganti ada
                if (idBarang.equals("null")) {
                    kerusakanDanBarang = kerusakan.getNama();
                } else {
                    Barang barang = barangDao.get(idBarang);
                    kerusakanDanBarang = kerusakan.getNama() + " + " + barang.getNama() + " " + unit + " Unit";
                }
                estimasi = "Rp. " + estimasiMin + " - " + "Rp. " + estimasiMax;
                observableList.add(new KerusakanDanBarang(kerusakanDanBarang, estimasi));

                totalEstimasiMin += estimasiMin;
                totalEstimasiMax += estimasiMax;
            }

            // Set label bawah table
            lblTotalEstimasi.setText("Rp. " + totalEstimasiMin + " - " + "Rp. " + totalEstimasiMax);

            // untuk menset nilai setiap cell (row) per column maka harus memberi sebuah PropertyValueFactory,
            // yang mana parameter pertama harus sama dengan variable pada kelas
            TableColumn<KerusakanDanBarang, ?> colListKerusakanDanBarang = tblListKerusakan.getColumns().get(0);
            colListKerusakanDanBarang.setCellValueFactory(new PropertyValueFactory<>("kerusakanDanBarang"));

            TableColumn<KerusakanDanBarang, ?> colEstimasi = tblListKerusakan.getColumns().get(1);
            colEstimasi.setCellValueFactory(new PropertyValueFactory<>("estimasi"));

            tblListKerusakan.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
