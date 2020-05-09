package seryp.model.dao;

import seryp.model.DetailKerusakan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetailKerusakanDao {
    private final Connection CON;

    public DetailKerusakanDao() {
        Koneksi koneksi = new Koneksi();
        CON = koneksi.getConnection();
    }

    public void add(DetailKerusakan detailKerusakan) throws SQLException {
        String sql = "INSERT INTO detail_kerusakan VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = CON.prepareStatement(sql);
        preparedStatement.setString(1, detailKerusakan.getNoFaktur());
        preparedStatement.setString(2, detailKerusakan.getIdKerusakan());
        // jika barang tidak ada
        if (detailKerusakan.getIdBarang().equals("null")) {
            preparedStatement.setNull(3, Types.VARCHAR);
        } else {
            preparedStatement.setString(3, detailKerusakan.getIdBarang());
        }

        preparedStatement.setInt(4, detailKerusakan.getUnit());
        preparedStatement.setInt(5, detailKerusakan.getTotalEstimasiMin());
        preparedStatement.setInt(6, detailKerusakan.getTotalEstimasiMax());

        preparedStatement.executeUpdate();
    }

    public List<DetailKerusakan> get(String noFaktur) throws SQLException {
        List<DetailKerusakan> list = null;
        String sql = "SELECT * FROM detail_kerusakan WHERE noFaktur = ?";
        PreparedStatement preparedStatement = CON.prepareStatement(sql);
        preparedStatement.setString(1, noFaktur);
        ResultSet resultSet = preparedStatement.executeQuery();

        // cek jika no faktur ada
        if (resultSet.isBeforeFirst()) {
            list = new ArrayList<>();
            DetailKerusakan detailKerusakan;
            while (resultSet.next()) {
                detailKerusakan = new DetailKerusakan();
                detailKerusakan.setNoFaktur(resultSet.getString(1));
                detailKerusakan.setIdKerusakan(resultSet.getString(2));
                detailKerusakan.setIdBarang(resultSet.getString(3));
                detailKerusakan.setUnit(resultSet.getInt(4));
                detailKerusakan.setTotalEstimasiMin(resultSet.getInt(5));
                detailKerusakan.setTotalEstimasiMax(resultSet.getInt(6));

                list.add(detailKerusakan);
            }
        }
        return list;
    }

    public List<DetailKerusakan> getAll() throws SQLException {
        List<DetailKerusakan> detailKerusakanList = new ArrayList<>();
        String sql = "SELECT * FROM detail_kerusakan";
        ResultSet resultSet = CON.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            DetailKerusakan detailKerusakan = new DetailKerusakan();
            detailKerusakan.setNoFaktur(resultSet.getString(1));
            detailKerusakan.setIdKerusakan(resultSet.getString(2));
            detailKerusakan.setIdBarang(resultSet.getString(3));
            detailKerusakan.setUnit(resultSet.getInt(4));
            detailKerusakan.setTotalEstimasiMin(resultSet.getInt(5));
            detailKerusakan.setTotalEstimasiMax(resultSet.getInt(6));

            detailKerusakanList.add(detailKerusakan);
        }

        return detailKerusakanList;
    }
}
