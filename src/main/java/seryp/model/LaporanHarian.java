package seryp.model;

public class LaporanHarian {
    private String namaPelanggan;
    private String merekLabel;
    private String namaKaryawan;
    private String estimasi;

    public LaporanHarian(String namaPelanggan, String merekLabel, String namaKaryawan, String estimasi) {
        this.namaPelanggan = namaPelanggan;
        this.merekLabel = merekLabel;
        this.namaKaryawan = namaKaryawan;
        this.estimasi = estimasi;
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
}
