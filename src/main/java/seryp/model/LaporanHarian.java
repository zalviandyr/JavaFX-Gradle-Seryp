package seryp.model;

public class LaporanHarian {
    private String namaPelanggan;
    private String merekLabel;
    private String namaKaryawan;
    private String estimasi;
    private String statusDp;
    private String uangDp;
    private String statusTunai;
    private String uangTunai;

    public LaporanHarian(String namaPelanggan, String merekLabel, String namaKaryawan, String estimasi, String statusDp, String uangDp, String statusTunai, String uangTunai) {
        this.namaPelanggan = namaPelanggan;
        this.merekLabel = merekLabel;
        this.namaKaryawan = namaKaryawan;
        this.estimasi = estimasi;
        this.statusDp = statusDp;
        this.uangDp = uangDp;
        this.statusTunai = statusTunai;
        this.uangTunai = uangTunai;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public String getMerekLabel() {
        return merekLabel;
    }

    public String getNamaKaryawan() {
        return namaKaryawan;
    }

    public String getEstimasi() {
        return estimasi;
    }

    public String getStatusDp() {
        return statusDp;
    }

    public String getUangDp() {
        return uangDp;
    }

    public String getStatusTunai() {
        return statusTunai;
    }

    public String getUangTunai() {
        return uangTunai;
    }
}
