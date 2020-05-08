package seryp.model;

public class DetailKerusakan {
    private String noFaktur;
    private String idKerusakan;
    private String idBarang;
    private int unit;
    private int totalEstimasiMin;
    private int totalEstimasiMax;

    public String getNoFaktur() {
        return noFaktur;
    }

    public void setNoFaktur(String noFaktur) {
        this.noFaktur = noFaktur;
    }

    public String getIdKerusakan() {
        return idKerusakan;
    }

    public void setIdKerusakan(String idKerusakan) {
        this.idKerusakan = idKerusakan;
    }

    public String getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(String idBarang) {
        this.idBarang = idBarang;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getTotalEstimasiMin() {
        return totalEstimasiMin;
    }

    public void setTotalEstimasiMin(int totalEstimasiMin) {
        this.totalEstimasiMin = totalEstimasiMin;
    }

    public int getTotalEstimasiMax() {
        return totalEstimasiMax;
    }

    public void setTotalEstimasiMax(int totalEstimasiMax) {
        this.totalEstimasiMax = totalEstimasiMax;
    }
}
