package seryp.model;

public class Kerusakan {
    private String idKerusakan;
    private String nama;
    private String deskripsi;
    private int estimasiMin;
    private int estimasiMax;

    public Kerusakan(){

    }

    public Kerusakan(String idKerusakan, String nama) {
        this.idKerusakan = idKerusakan;
        this.nama = nama;
    }

    public String getIdKerusakan() {
        return idKerusakan;
    }

    public void setIdKerusakan(String idKerusakan) {
        this.idKerusakan = idKerusakan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getEstimasiMin() {
        return estimasiMin;
    }

    public void setEstimasiMin(int estimasiMin) {
        this.estimasiMin = estimasiMin;
    }

    public int getEstimasiMax() {
        return estimasiMax;
    }

    public void setEstimasiMax(int estimasiMax) {
        this.estimasiMax = estimasiMax;
    }
}
