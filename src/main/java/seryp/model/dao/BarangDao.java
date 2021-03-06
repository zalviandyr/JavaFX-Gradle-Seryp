package seryp.model.dao;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seryp.model.Barang;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class BarangDao {
    private final Connection CONN;

    public BarangDao() {
        Koneksi koneksi = new Koneksi();
        CONN = koneksi.getConnection();
    }

    public Barang get(String idBarang) throws SQLException {
        Barang barang = null;
        String sql = "SELECT * FROM barang WHERE idBarang = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, idBarang);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            barang = new Barang();
            barang.setIdBarang(resultSet.getString(1));
            barang.setNama(resultSet.getString(2));
            barang.setDeskripsi(resultSet.getString(3));
            barang.setStok(resultSet.getInt(4));
            barang.setHarga(resultSet.getInt(5));
            barang.setStatus(resultSet.getString(6));
        }
        return barang;
    }

    public List<Barang> getAll() throws SQLException {
        List<Barang> barangList = new ArrayList<>();
        String sql = "SELECT * FROM barang";
        ResultSet resultSet = CONN.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            Barang barang = new Barang();
            barang.setIdBarang(resultSet.getString(1));
            barang.setNama(resultSet.getString(2));
            barang.setDeskripsi(resultSet.getString(3));
            barang.setStok(resultSet.getInt(4));
            barang.setHarga(resultSet.getInt(5));
            barang.setStatus(resultSet.getString(6));

            barangList.add(barang);
        }
        return barangList;
    }

    public List<String> getAllId() throws SQLException {
        String sql = "SELECT RIGHT(idBarang, 3) FROM barang";
        ResultSet resultSet = CONN.createStatement().executeQuery(sql);

        List<String> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(resultSet.getString(1));
        }
        return list;
    }

    public void add(Barang barang) throws SQLException {
        String sql = "INSERT INTO barang VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, barang.getIdBarang());
        preparedStatement.setString(2, barang.getNama());
        preparedStatement.setString(3, barang.getDeskripsi());
        preparedStatement.setInt(4, barang.getStok());
        preparedStatement.setInt(5, barang.getHarga());
        preparedStatement.setString(6, barang.getStatus());
        preparedStatement.executeUpdate();
    }

    public void addAll(List<Barang> barangList) throws SQLException {
        if (barangList != null) {
            String sql = "INSERT INTO barang VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = CONN.prepareStatement(sql);

            for (Barang barang : barangList) {
                preparedStatement.setString(1, barang.getIdBarang());
                preparedStatement.setString(2, barang.getNama());
                preparedStatement.setString(3, barang.getDeskripsi());
                preparedStatement.setInt(4, barang.getStok());
                preparedStatement.setInt(5, barang.getHarga());
                preparedStatement.setString(6, barang.getStatus());

                preparedStatement.executeUpdate();
            }
        }
    }

    public void update(Barang barang) throws SQLException {
        String sql = "UPDATE barang SET nama = ?, deskripsi = ?, stok = ?, harga = ?, status = ? WHERE idBarang = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, barang.getNama());
        preparedStatement.setString(2, barang.getDeskripsi());
        preparedStatement.setInt(3, barang.getStok());
        preparedStatement.setInt(4, barang.getHarga());
        preparedStatement.setString(5, barang.getStatus());
        preparedStatement.setString(6, barang.getIdBarang());
        preparedStatement.executeUpdate();
    }

    public void updateStok(String idBarang, int stok) throws SQLException {
        String sql = "UPDATE barang SET stok = ? WHERE idBarang = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setInt(1, stok);
        preparedStatement.setString(2, idBarang);

        preparedStatement.executeUpdate();
    }

    public void delete(String idBarang) throws SQLException {
        String sql = "DELETE FROM barang WHERE idBarang = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, idBarang);
        preparedStatement.executeUpdate();
    }

    public ObservableList<Barang> searchAll() throws SQLException {
        String sql = "SELECT idBarang, nama FROM barang";
        ResultSet resultSet = CONN.createStatement().executeQuery(sql);

        ObservableList<Barang> observableList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            observableList.add(new Barang(resultSet.getString(1), resultSet.getString(2)));
        }
        return observableList;
    }

    public ObservableList<Barang> search(String keyword) throws SQLException {
        keyword = "%" + keyword + "%";
        String sql = "SELECT idBarang, nama FROM barang WHERE nama LIKE ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, keyword);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<Barang> observableList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            observableList.add(new Barang(resultSet.getString(1), resultSet.getString(2)));
        }
        return observableList;
    }
}
