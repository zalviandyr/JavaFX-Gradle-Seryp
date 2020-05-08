package seryp.model.dao;

import seryp.model.Kerusakan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KerusakanDao {
    private final Connection CONN;

    public KerusakanDao() {
        Koneksi koneksi = new Koneksi();
        CONN = koneksi.getConnection();
    }

    public Kerusakan get(String idKerusakan) throws SQLException {
        Kerusakan kerusakan = null;

        String sql = "SELECT * FROM kerusakan WHERE idKerusakan = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, idKerusakan);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            kerusakan = new Kerusakan();
            kerusakan.setIdKerusakan(resultSet.getString(1));
            kerusakan.setNama(resultSet.getString(2));
            kerusakan.setDeskripsi(resultSet.getString(3));
            kerusakan.setEstimasiMin(resultSet.getInt(4));
            kerusakan.setEstimasiMax(resultSet.getInt(5));
        }

        return kerusakan;
    }

    public ResultSet getAllId() throws SQLException {
        String sql = "SELECT RIGHT(idKerusakan, 3) FROM kerusakan";
        return CONN.createStatement().executeQuery(sql);
    }

    public void add(Kerusakan kerusakan) throws SQLException {
        String sql = "INSERT INTO kerusakan VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);

        preparedStatement.setString(1, kerusakan.getIdKerusakan());
        preparedStatement.setString(2, kerusakan.getNama());
        preparedStatement.setString(3, kerusakan.getDeskripsi());
        preparedStatement.setInt(4, kerusakan.getEstimasiMin());
        preparedStatement.setInt(5, kerusakan.getEstimasiMax());

        preparedStatement.executeUpdate();
    }

    public void update(Kerusakan kerusakan) throws SQLException {
        String sql = "UPDATE kerusakan SET nama = ?, deskripsi = ?, estimasiMin = ?, estimasiMax = ? WHERE idKerusakan = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);

        preparedStatement.setString(1, kerusakan.getNama());
        preparedStatement.setString(2, kerusakan.getDeskripsi());
        preparedStatement.setInt(3, kerusakan.getEstimasiMin());
        preparedStatement.setInt(4, kerusakan.getEstimasiMax());

        preparedStatement.setString(5, kerusakan.getIdKerusakan());
        preparedStatement.executeUpdate();
    }

    public void delete(String idKerusakan) throws SQLException {
        String sql = "DELETE FROM kerusakan WHERE idKerusakan = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, idKerusakan);

        preparedStatement.executeUpdate();
    }

    public ResultSet searchAll() throws SQLException {
        String sql = "SELECT idKerusakan, nama FROM kerusakan";
        return CONN.createStatement().executeQuery(sql);
    }

    public ResultSet search(String keyword) throws SQLException {
        keyword = "%" + keyword + "%";
        String sql = "SELECT idKerusakan, nama FROM kerusakan WHERE nama LIKE ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, keyword);

        return preparedStatement.executeQuery();
    }
}
