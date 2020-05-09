package seryp.model.dao;

import seryp.model.IdentitasToko;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IdentitasTokoDao {
    private final Connection CONN;

    public IdentitasTokoDao() {
        CONN = new Koneksi().getConnection();
    }

    public void add(IdentitasToko identitasToko) throws SQLException {
        String sql = "INSERT INTO identitas_toko VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, identitasToko.getNamaToko());
        preparedStatement.setString(2, identitasToko.getAlamat());
        preparedStatement.setString(3, identitasToko.getSerypBasePath());

        preparedStatement.executeUpdate();
    }

    public void update(IdentitasToko identitasToko) throws SQLException {
        String sql = "UPDATE identitas_toko SET nama = ?, alamat = ?, serypBasePath = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, identitasToko.getNamaToko());
        preparedStatement.setString(2, identitasToko.getAlamat());
        preparedStatement.setString(3, identitasToko.getSerypBasePath());

        preparedStatement.executeUpdate();
    }

    public IdentitasToko get() throws SQLException {
        IdentitasToko identitasToko = null;
        String sql = "SELECT * FROM identitas_toko";
        ResultSet resultSet = CONN.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            identitasToko = new IdentitasToko();
            identitasToko.setNamaToko(resultSet.getString(1));
            identitasToko.setAlamat(resultSet.getString(2));
            identitasToko.setSerypBasePath(resultSet.getString(3));
        }

        return identitasToko;
    }
}
