package seryp.model;

public class LaporanBulanan {
    private String namaBarang;
    private int stok;
    private int stokTerjual;

    public LaporanBulanan(String namaBarang, int stok, int stokTerjual) {
        this.namaBarang = namaBarang;
        this.stok = stok;
        this.stokTerjual = stokTerjual;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public int getStok() {
        return stok;
    }

    public int getStokTerjual() {
        return stokTerjual;
    }
}
