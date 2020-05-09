package seryp.model.dao;

import seryp.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final Connection CONN;

    public UserDao() {
        Koneksi koneksi = new Koneksi();
        CONN = koneksi.getConnection();
    }

    public User get(String username) throws SQLException {
        User user = null;

        String sql = "SELECT * FROM user WHERE username = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, username);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            user = new User();
            user.setUsername(resultSet.getString(1));
            user.setPassword(resultSet.getString(2));
            user.setNoHp(resultSet.getString(3));
            user.setNama(resultSet.getString(4));
            user.setFotoProfil(resultSet.getString(5));
            user.setJekel(resultSet.getString(6));
            user.setTanggalLahir(resultSet.getDate(7).toLocalDate());
            user.setAlamat(resultSet.getString(8));
            user.setStatusUser(resultSet.getString(9));
        }

        return user;
    }

    public List<User> getAll() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user";
        ResultSet resultSet = CONN.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            User user = new User();
            user.setUsername(resultSet.getString(1));
            user.setPassword(resultSet.getString(2));
            user.setNoHp(resultSet.getString(3));
            user.setNama(resultSet.getString(4));
            user.setFotoProfil(resultSet.getString(5));
            user.setJekel(resultSet.getString(6));
            user.setTanggalLahir(resultSet.getDate(7).toLocalDate());
            user.setAlamat(resultSet.getString(8));
            user.setStatusUser(resultSet.getString(9));
            try {
                user.setLastLogin(resultSet.getDate(10).toLocalDate());
            } catch (NullPointerException e) {
                user.setLastLogin(null);
            }
            user.setCreated(resultSet.getDate(11).toLocalDate());

            list.add(user);
        }

        return list;
    }

    public void add(User user) throws SQLException {
        String sql = "INSERT INTO user(username, password, noHp, nama, fotoProfil, jekel, tanggalLahir, alamat, statusUser, lastLogin, created)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getNoHp());
        preparedStatement.setString(4, user.getNama());
        preparedStatement.setString(5, user.getFotoProfil());
        preparedStatement.setString(6, user.getJekel());
        preparedStatement.setDate(7, Date.valueOf(user.getTanggalLahir()));
        preparedStatement.setString(8, user.getAlamat());
        preparedStatement.setString(9, user.getStatusUser());
        preparedStatement.setDate(10, Date.valueOf(user.getLastLogin()));
        preparedStatement.setDate(11, Date.valueOf(user.getCreated()));

        preparedStatement.executeUpdate();
    }

    public void addAll(List<User> userList) throws SQLException {
        if (userList != null) {
            String sql = "INSERT INTO user(username, password, noHp, nama, fotoProfil, jekel, tanggalLahir, alamat, statusUser, lastLogin, created)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = CONN.prepareStatement(sql);

            for (User user : userList) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getNoHp());
                preparedStatement.setString(4, user.getNama());
                preparedStatement.setString(5, user.getFotoProfil());
                preparedStatement.setString(6, user.getJekel());
                preparedStatement.setDate(7, Date.valueOf(user.getTanggalLahir()));
                preparedStatement.setString(8, user.getAlamat());
                preparedStatement.setString(9, user.getStatusUser());
                preparedStatement.setDate(10, Date.valueOf(user.getLastLogin()));
                preparedStatement.setDate(11, Date.valueOf(user.getCreated()));

                preparedStatement.executeUpdate();
            }
        }
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE user SET password = ?, noHp = ?, nama = ?, fotoProfil = ?, jekel = ?, tanggalLahir = ?, alamat = ?, statusUser = ? WHERE username = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, user.getPassword());
        preparedStatement.setString(2, user.getNoHp());
        preparedStatement.setString(3, user.getNama());
        preparedStatement.setString(4, user.getFotoProfil());
        preparedStatement.setString(5, user.getJekel());
        preparedStatement.setDate(6, Date.valueOf(user.getTanggalLahir()));
        preparedStatement.setString(7, user.getAlamat());
        preparedStatement.setString(8, user.getStatusUser());

        preparedStatement.setString(9, user.getUsername());
        preparedStatement.executeUpdate();
    }

    public void delete(String username) throws SQLException {
        String sql = "DELETE FROM user WHERE username = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, username);

        preparedStatement.executeUpdate();
    }

    public ResultSet search(String keyword) throws SQLException {
        keyword = '%' + keyword + '%';
        String sql = "SELECT username FROM user WHERE username LIKE ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setString(1, keyword);

        return preparedStatement.executeQuery();
    }

    public ResultSet searchAll() throws SQLException {
        String sql = "SELECT username FROM user";
        return CONN.createStatement().executeQuery(sql);
    }

    public void updateLastLogin(String username, LocalDate lastLogin) throws SQLException {
        String sql = "UPDATE user SET lastLogin = ? WHERE username = ?";
        PreparedStatement preparedStatement = CONN.prepareStatement(sql);
        preparedStatement.setDate(1, Date.valueOf(lastLogin));
        preparedStatement.setString(2, username);

        preparedStatement.executeUpdate();
    }
}
