package seryp.model.dao;

import seryp.model.LaporanBulanan;
import seryp.model.LaporanHarian;
import seryp.utils.SerypUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LaporanDao extends SerypUtil {
    private final Connection CONN;

    public LaporanDao() {
        CONN = new Koneksi().getConnection();
    }

    public List<LaporanBulanan> getLaporanBulanan(int bulan, int tahun) throws SQLException {
        List<LaporanBulanan> laporanBulananList = null;
        String sql = "SELECT nama, stok, IF(SUM(unit) IS NULL, 0, SUM(unit))" +
                "FROM barang LEFT JOIN detail_kerusakan USING(idBarang) JOIN servis USING (noFaktur)" +
                "WHERE MONTH(tanggal) = ? AND YEAR(tanggal) = ? GROUP BY (nama)";

        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setInt(1, bulan);
        preparedStatement.setInt(2, tahun);
        ResultSet resultSet = preparedStatement.executeQuery();

        // jika seryp.laporan ada
        if(resultSet.isBeforeFirst()) {
            laporanBulananList = new ArrayList<>();
            while (resultSet.next()) {
                laporanBulananList.add(new LaporanBulanan(resultSet.getString(1), resultSet.getInt(2), resultSet.getInt(3)));
            }
        }
        return laporanBulananList;
    }

    public List<LaporanHarian> getLaporanHarian(String tanggal) throws SQLException {
        List<LaporanHarian> laporanHarianList = null;
        String sql = "SELECT pelanggan.nama,  merkLabel, user.nama, SUM(totalEstimasiMin), SUM(totalEstimasiMax), IF(statusDP = 1, 'Ya', 'Tidak'), uangDp, IF(statusPembayaran = 1, 'Ya', 'Tidak'), uangPembayaran " +
                "FROM servis JOIN pelanggan USING(idPelanggan) " +
                "JOIN user USING(username) " +
                "JOIN detail_kerusakan USING(noFaktur) " +
                "WHERE tanggal = ? GROUP BY noFaktur";

        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, tanggal);
        ResultSet resultSet = preparedStatement.executeQuery();

        // jika seryp.laporan ada
        if (resultSet.isBeforeFirst()) {
            laporanHarianList = new ArrayList<>();
            while (resultSet.next()) {
                String estimasi = getUtil().toIdr(resultSet.getInt(4)) + " - " + getUtil().toIdr(resultSet.getInt(5));
                String uangDp = getUtil().toIdr(resultSet.getInt(7)).toString();
                String uangTunai = getUtil().toIdr(resultSet.getInt(9)).toString();

                laporanHarianList.add(new LaporanHarian(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        estimasi,
                        resultSet.getString(6),
                        uangDp,
                        resultSet.getString(8),
                        uangTunai)
                );
            }
        }
        return laporanHarianList;
    }

    public String getPemasukkanBulanan(int bulan) throws SQLException {
        String sql = "SELECT SUM(uangPembayaran) FROM servis WHERE MONTH(tanggal) = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setInt(1, bulan);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int pemasukkan = resultSet.getInt(1);
            return getUtil().toIdr(pemasukkan).toString();
        }
        return "Rp. 0";
    }

    public int getPelangganBulanan(int bulan) throws SQLException {
        String sql = "SELECT COUNT(*) FROM servis WHERE MONTH(tanggal) = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setInt(1, bulan);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public int getPelangganHarian(String tanggal) throws SQLException {
        String sql = "SELECT COUNT(*) FROM servis WHERE tanggal = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, tanggal);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public String getTotalEstimasiHarian(String tanggal) throws SQLException {
        String sql = "SELECT SUM(totalEstimasiMin), SUM(totalEstimasiMax)" +
                "FROM detail_kerusakan JOIN servis USING(noFaktur)" +
                "WHERE tanggal = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, tanggal);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int totalEstimasiMin = resultSet.getInt(1);
            int totalEstimasiMax = resultSet.getInt(2);

            return getUtil().toIdr(totalEstimasiMin) + " - " + getUtil().toIdr(totalEstimasiMax);
        }
        return "Rp. 0";
    }

    public String getTotalDpHarian(String tanggal) throws SQLException {
        String sql = "SELECT SUM(uangDp) FROM servis WHERE tanggal = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, tanggal);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return getUtil().toIdr(resultSet.getInt(1)).toString();
        }
        return "Rp. 0";
    }

    public String getTotalTunaiHarian(String tanggal) throws SQLException {
        String sql = "SELECT SUM(uangPembayaran) FROM servis WHERE tanggal = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, tanggal);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return getUtil().toIdr(resultSet.getInt(1)).toString();
        }
        return "Rp. 0";
    }

    public String getTotalEstimasiStruk(String noFaktur) throws SQLException {
        String sql = "SELECT SUM(totalEstimasiMin), SUM(totalEstimasiMax) FROM detail_kerusakan WHERE noFaktur = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, noFaktur);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int totalEstimasiMin = resultSet.getInt(1);
            int totalEstimasiMax = resultSet.getInt(2);

            return getUtil().toIdr(totalEstimasiMin) + " - " + getUtil().toIdr(totalEstimasiMax);
        }

        return "Rp .0";
    }
}
