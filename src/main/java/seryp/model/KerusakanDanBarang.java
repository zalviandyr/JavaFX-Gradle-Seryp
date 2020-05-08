package seryp.model;

public class KerusakanDanBarang {
    private String kerusakanDanBarang;
    private String estimasi;

    public KerusakanDanBarang(String kerusakanDanBarang, String estimasi) {
        this.kerusakanDanBarang = kerusakanDanBarang;
        this.estimasi = estimasi;
    }

    public String getKerusakanDanBarang() {
        return kerusakanDanBarang;
    }

    public void setKerusakanDanBaran(String kerusakanDanBarang) {
        this.kerusakanDanBarang = kerusakanDanBarang;
    }

    public String getEstimasi() {
        return estimasi;
    }

    public void setEstimasi(String estimasi) {
        this.estimasi = estimasi;
    }
}
