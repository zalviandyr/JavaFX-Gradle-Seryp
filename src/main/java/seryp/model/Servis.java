package seryp.model;

import java.time.LocalDate;

public class Servis {
    private String noFaktur;
    private String username;
    private String idPelanggan;
    private String merkLabel;
    private LocalDate tanggalHariIni;
    private LocalDate batasHari;
    private boolean statusDP;
    private int uangDP;
    private boolean statusPembayaran;
    private int uangPembayaran;

    public String getNoFaktur() {
        return noFaktur;
    }

    public void setNoFaktur(String noFaktur) {
        this.noFaktur = noFaktur;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public String getMerkLabel() {
        return merkLabel;
    }

    public void setMerkLabel(String merkLabel) {
        this.merkLabel = merkLabel;
    }

    public LocalDate getTanggalHariIni() {
        return tanggalHariIni;
    }

    public void setTanggalHariIni(LocalDate tanggalHariIni) {
        this.tanggalHariIni = tanggalHariIni;
    }

    public LocalDate getBatasHari() {
        return batasHari;
    }

    public void setBatasHari(LocalDate batasHari) {
        this.batasHari = batasHari;
    }

    public boolean isStatusDP() {
        return statusDP;
    }

    public void setStatusDP(boolean statusDP) {
        this.statusDP = statusDP;
    }

    public int getUangDP() {
        return uangDP;
    }

    public void setUangDP(int uangDP) {
        this.uangDP = uangDP;
    }

    public boolean isStatusPembayaran() {
        return statusPembayaran;
    }

    public void setStatusPembayaran(boolean statusPembayaran) {
        this.statusPembayaran = statusPembayaran;
    }

    public int getUangPembayaran() {
        return uangPembayaran;
    }

    public void setUangPembayaran(int uangPembayaran) {
        this.uangPembayaran = uangPembayaran;
    }
}
