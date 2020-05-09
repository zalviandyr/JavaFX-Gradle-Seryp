package seryp.model.dao;

import seryp.model.Servis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServisDao {
    private final Connection CONN;

    public ServisDao() {
        Koneksi koneksi = new Koneksi();
        CONN = koneksi.getConnection();
    }

    public Servis get(String noFaktur) throws SQLException {
        Servis servis = null;
        String sql = "SELECT * FROM servis WHERE noFaktur = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, noFaktur);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            servis = new Servis();
            servis.setNoFaktur(resultSet.getString(1));
            servis.setUsername(resultSet.getString(2));
            servis.setIdPelanggan(resultSet.getString(3));
            servis.setMerkLabel(resultSet.getString(4));
            servis.setTanggalHariIni(resultSet.getDate(5).toLocalDate());
            servis.setBatasHari(resultSet.getDate(6).toLocalDate());
            servis.setStatusDP(resultSet.getBoolean(7));
            servis.setUangDP(resultSet.getInt(8));
            servis.setStatusPembayaran(resultSet.getBoolean(9));
            servis.setUangPembayaran(resultSet.getInt(10));
        }

        return servis;
    }

    public List<Servis> getAll() throws SQLException {
        List<Servis> servisList = new ArrayList<>();
        String sql = "SELECT * FROM servis";
        ResultSet resultSet = CONN.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            Servis servis = new Servis();
            servis.setNoFaktur(resultSet.getString(1));
            servis.setUsername(resultSet.getString(2));
            servis.setIdPelanggan(resultSet.getString(3));
            servis.setMerkLabel(resultSet.getString(4));
            servis.setTanggalHariIni(resultSet.getDate(5).toLocalDate());
            servis.setBatasHari(resultSet.getDate(6).toLocalDate());
            servis.setStatusDP(resultSet.getBoolean(7));
            servis.setUangDP(resultSet.getInt(8));
            servis.setStatusPembayaran(resultSet.getBoolean(9));
            servis.setUangPembayaran(resultSet.getInt(10));

            servisList.add(servis);
        }

        return servisList;
    }

    public ResultSet getAllId() throws SQLException {
        String sql = "SELECT RIGHT(noFaktur, 3) FROM servis ORDER BY noFaktur ASC";
        return CONN.createStatement().executeQuery(sql);
    }

    public int getCountServisPelanggan(String idPelanggan) throws SQLException {
        String sql = "SELECT COUNT(*) FROM servis WHERE idPelanggan = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, idPelanggan);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public String getNoFaktur(String merekLabel) throws SQLException {
        merekLabel = "%" + merekLabel + "%";
        String sql = "SELECT noFaktur FROM servis WHERE merkLabel LIKE ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, merekLabel);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    public void add(Servis servis) throws SQLException {
        String sql = "INSERT INTO servis VALUES (?, ?, ?, ?, ?, ? ,? ,?, ?, ?)";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, servis.getNoFaktur());
        preparedStatement.setString(2, servis.getUsername());
        preparedStatement.setString(3, servis.getIdPelanggan());
        preparedStatement.setString(4, servis.getMerkLabel());
        preparedStatement.setDate(5, Date.valueOf(servis.getTanggalHariIni()));
        preparedStatement.setDate(6, Date.valueOf(servis.getBatasHari()));
        preparedStatement.setBoolean(7, servis.isStatusDP());
        preparedStatement.setInt(8, servis.getUangDP());
        preparedStatement.setBoolean(9, servis.isStatusPembayaran());
        preparedStatement.setInt(10, servis.getUangPembayaran());

        preparedStatement.executeUpdate();
    }

    public void setPembayaranDP(String noFaktur, int jumlahUang) throws SQLException {
        String sql = "UPDATE servis SET statusDP = ?, uangDP = ? WHERE noFaktur = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setBoolean(1, true);
        preparedStatement.setInt(2, jumlahUang);
        preparedStatement.setString(3, noFaktur);

        preparedStatement.executeUpdate();
    }

    public void setPembayaranTunai(String noFaktur, int jumlahUang) throws SQLException {
        String sql = "UPDATE servis SET statusPembayaran = ?, uangPembayaran = ? WHERE noFaktur = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setBoolean(1, true);
        preparedStatement.setInt(2, jumlahUang);
        preparedStatement.setString(3, noFaktur);

        preparedStatement.executeUpdate();
    }
}
