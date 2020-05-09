package seryp.model.dao;

import seryp.model.Pelanggan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PelangganDao {
    private final Connection CONN;

    public PelangganDao() {
        Koneksi koneksi = new Koneksi();
        CONN = koneksi.getConnection();
    }

    public Pelanggan get(String idPelanggan) throws SQLException {
        Pelanggan pelanggan = null;

        String sql = "SELECT * FROM pelanggan WHERE idPelanggan = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, idPelanggan);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            pelanggan = new Pelanggan();

            pelanggan.setIdPelanggan(resultSet.getString(1));
            pelanggan.setNama(resultSet.getString(2));
            pelanggan.setAlamat(resultSet.getString(3));
            pelanggan.setNoHp(resultSet.getString(4));
            pelanggan.setJekel(resultSet.getString(5));
        }

        return pelanggan;
    }

    public List<Pelanggan> getAll() throws SQLException {
        List<Pelanggan> pelangganList = new ArrayList<>();
        String sql = "SELECT * FROM pelanggan";
        ResultSet resultSet = CONN.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            Pelanggan pelanggan = new Pelanggan();
            pelanggan.setIdPelanggan(resultSet.getString(1));
            pelanggan.setNama(resultSet.getString(2));
            pelanggan.setAlamat(resultSet.getString(3));
            pelanggan.setNoHp(resultSet.getString(4));
            pelanggan.setJekel(resultSet.getString(5));

            pelangganList.add(pelanggan);
        }

        return pelangganList;
    }

    public ResultSet getAllId() throws SQLException {
        String sql = "SELECT RIGHT(idPelanggan, 3) FROM pelanggan";
        return CONN.createStatement().executeQuery(sql);
    }

    public void add(Pelanggan pelanggan) throws SQLException {
        String sql = "INSERT INTO pelanggan VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, pelanggan.getIdPelanggan());
        preparedStatement.setString(2, pelanggan.getNama());
        preparedStatement.setString(3, pelanggan.getAlamat());
        preparedStatement.setString(4, pelanggan.getNoHp());
        preparedStatement.setString(5, pelanggan.getJekel());

        preparedStatement.executeUpdate();
    }

    public void update(Pelanggan pelanggan) throws SQLException {
        String sql = "UPDATE pelanggan SET nama = ?, alamat = ?, noHp = ?, jekel = ? WHERE idPelanggan = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, pelanggan.getNama());
        preparedStatement.setString(2, pelanggan.getAlamat());
        preparedStatement.setString(3, pelanggan.getNoHp());
        preparedStatement.setString(4, pelanggan.getJekel());

        preparedStatement.setString(5, pelanggan.getIdPelanggan());
        preparedStatement.executeUpdate();
    }

    public void delete(String idPelanggan) throws SQLException {
        String sql = "DELETE FROM pelanggan WHERE idPelanggan = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, idPelanggan);

        preparedStatement.executeUpdate();
    }

    public ResultSet searchAll() throws SQLException {
        String sql = "SELECT idPelanggan, nama FROM pelanggan";
        return CONN.createStatement().executeQuery(sql);
    }

    public ResultSet search(String keyword) throws SQLException {
        keyword = "%" + keyword + "%";
        String sql = "SELECT idPelanggan, nama FROM pelanggan WHERE nama LIKE ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, keyword);

        return preparedStatement.executeQuery();
    }
}
