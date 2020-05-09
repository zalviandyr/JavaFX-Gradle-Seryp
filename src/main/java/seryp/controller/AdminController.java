package seryp.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.util.JRLoader;
import seryp.model.*;
import seryp.model.dao.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import seryp.utils.SerypUtil;
import seryp.utils.boxes.AlertBox;
import seryp.utils.boxes.ConfirmBox;
import seryp.utils.boxes.LaporanBulananBox;
import seryp.utils.boxes.LaporanHarianBox;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AdminController extends SerypUtil implements Initializable {
    public HBox topBar;
    public VBox sideBar;
    public VBox settingBar;
    public Button btnLogOut;
    public Button btnKerusakan;
    public Button btnBarang;
    public Button btnUser;
    public Button btnBackup;
    public ToggleButton toggleBtnSetting;
    public TitledPane paneSetting;
    private IdentitasTokoDao identitasTokoDao;
    private LaporanDao laporanDao;
    public static User userLogin; // data yang dikirim dari LoginController

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // instance required object
        identitasTokoDao = new IdentitasTokoDao();
        laporanDao = new LaporanDao();

        // set effect inner shadow btnUser, btnBarang, btnKerusakan;
        setEffectInnerShadow(btnUser, btnBarang, btnKerusakan);

        // set topBar
        getWindowControl().setTopBar(topBar);

        // set sideBar
        getWindowControl().setSideBar(sideBar, AdminController.userLogin);

        // set paneSetting
        getWindowControl().setPaneSetting(toggleBtnSetting, paneSetting, settingBar);

        // init user and send to another scene
        BarangController.userLogin = AdminController.userLogin;
        KerusakanController.userLogin = AdminController.userLogin;
        UserController.userLogin = AdminController.userLogin;
    }

    @FXML
    void btnBackupAction() {
        try {
            IdentitasToko identitasToko = new IdentitasTokoDao().get();
            String backupPath = identitasToko.getSerypBasePath() + File.separator + "Backup";

            List<User> userList = new UserDao().getAll();
            List<Barang> barangList = new BarangDao().getAll();
            List<Kerusakan> kerusakanList = new KerusakanDao().getAll();
            List<Pelanggan> pelangganList = new PelangganDao().getAll();
            List<Servis> servisList = new ServisDao().getAll();
            List<DetailKerusakan> detailKerusakanList = new DetailKerusakanDao().getAll();

            getFileHandler().backupIdentitasTokoSerypBk(identitasToko, backupPath);
            getFileHandler().backupUserSerypBk(userList, backupPath);
            getFileHandler().backupBarangSerypBk(barangList, backupPath);
            getFileHandler().backupKerusakanSerypBk(kerusakanList, backupPath);
            getFileHandler().backupPelangganSerypBk(pelangganList, backupPath);
            getFileHandler().backupServisSerypBk(servisList, backupPath);
            getFileHandler().backupDetailKerusakanSerypBk(detailKerusakanList, backupPath);

            AlertBox.display("Berhasil Backup", "Hasil backup tersimpan di \"" + backupPath + "\"");
        } catch (SQLException e) {
//                    e.printStackTrace();
            AlertBox.display("Gagal Backup", "Tidak ada yang dibackup!");
        }
    }

    @FXML
    void btnUserAction() {
        getWindowControl().moveToScene(btnUser, "user");
    }

    @FXML
    void btnBarangAction() {
        getWindowControl().moveToScene(btnBarang, "barang");
    }

    @FXML
    void btnKerusakanAction() {
        getWindowControl().moveToScene(btnKerusakan, "kerusakan");
    }

    @FXML
    void btnLaporanHarianAction() {
        try {
            // get tanggal via box
            String tanggal = LaporanHarianBox.display();
            List<LaporanHarian> laporanHarianList = laporanDao.getLaporanHarian(tanggal);

            if (laporanHarianList != null) {
                // ambil file report
                URL url = getClass().getResource("/seryp/laporan/harian/Laporan_Harian.jasper");
                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);

                IdentitasToko identitasToko = identitasTokoDao.get();
                Map<String, Object> map = new HashMap<>();
                map.put("hari", tanggal);
                map.put("namaToko", identitasToko.getNamaToko());
                map.put("alamat", identitasToko.getAlamat());
                map.put("banyakPelanggan", laporanDao.getPelangganHarian(tanggal));
                map.put("totalEstimasi", laporanDao.getTotalEstimasiHarian(tanggal));
                map.put("namaAdmin", AdminController.userLogin.getNama());

                JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(laporanHarianList);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, jrBeanCollectionDataSource);

                // file chooser to save struk
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
                fileChooser.getExtensionFilters().add(extensionFilter);
                fileChooser.setInitialFileName("Laporan Harian (" + tanggal + ")" + ".pdf");
                File filetoSave = fileChooser.showSaveDialog(new Stage());

                if (filetoSave != null)
                    JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(filetoSave));
            } else {
                AlertBox.display("Tidak ada", "Laporan pada tanggal tersebut tidak ada!");
            }
        } catch (JRException | FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnLaporanBulananAction() {
        try {
            // get bulan dan tahun from box
            String bulanTahun = LaporanBulananBox.display();
            String[] splitText = bulanTahun.split("\\|");
            String namaBulan = splitText[0];
            int bulan = Integer.parseInt(splitText[1]);
            int tahun = Integer.parseInt(splitText[2]);

            List<LaporanBulanan> laporanBulananList = laporanDao.getLaporanBulanan(bulan, tahun);

            if (laporanBulananList != null) {
                // ambil file report
                URL url = getClass().getResource("/seryp/laporan/bulanan/Laporan_Bulanan.jasper");
                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);

                IdentitasToko identitasToko = identitasTokoDao.get();
                Map<String, Object> map = new HashMap<>();
                map.put("bulan", namaBulan + " - " + tahun);
                map.put("namaToko", identitasToko.getNamaToko());
                map.put("alamat", identitasToko.getAlamat());
                map.put("banyakPelanggan", laporanDao.getPelangganBulanan(bulan));
                map.put("pemasukkan", laporanDao.getPemasukkanBulanan(bulan));
                map.put("namaAdmin", AdminController.userLogin.getNama());

                JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(laporanBulananList);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, jrBeanCollectionDataSource);

                // file chooser to save struk
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
                fileChooser.getExtensionFilters().add(extensionFilter);
                fileChooser.setInitialFileName("Laporan Bulanan (" + namaBulan + " - " + tahun + ")" + ".pdf");
                File filetoSave = fileChooser.showSaveDialog(new Stage());

                if (filetoSave != null)
                    JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(filetoSave));
            } else {
                AlertBox.display("Tidak ada", "Laporan pada bulan dan tahun tersebut tidak ada!");
            }
        } catch (FileNotFoundException | JRException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnLogOutAction() {
        boolean option = ConfirmBox.display("Yakin ?", "Apakah yakin mau keluar ?");

        if (option) {
            getWindowControl().moveToScene(btnLogOut, "login");
        }
    }

    private void setEffectInnerShadow(Button... buttons) {
        for (Button button : buttons) {
            button.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    button.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, Color.BLACK, 10, 0, 0, 0));
                }
            });

            button.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    button.setEffect(null);
                }
            });
        }
    }
}
