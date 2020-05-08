package seryp.model.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class InstalasiDao {
    private final Connection CONN;

    public InstalasiDao() {
        Koneksi koneksi = new Koneksi();
        koneksi.createDatabase();

        CONN = koneksi.getConnection();
    }

    public void createTableBarang() throws SQLException {
        String sql = "CREATE TABLE `barang` (" +
                "`idBarang` varchar(10) NOT NULL," +
                "`nama` varchar(50) NOT NULL," +
                "`deskripsi` text NOT NULL," +
                "`stok` int(11) NOT NULL," +
                "`harga` int(11) NOT NULL," +
                "`status` varchar(15) NOT NULL," +
                "PRIMARY KEY (`idBarang`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        CONN.createStatement().executeUpdate(sql);
    }

    public void createTableIdentitasToko() throws SQLException {
        String sql = "CREATE TABLE `identitas_toko` (" +
                "`nama` varchar(100) NOT NULL," +
                "`alamat` text NOT NULL," +
                "`fotoProfilPath` text NOT NULL" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        CONN.createStatement().executeUpdate(sql);
    }

    public void createTableKerusakan() throws SQLException {
        String sql = "CREATE TABLE `kerusakan` (" +
                "`idKerusakan` varchar(10) NOT NULL," +
                "`nama` varchar(50) NOT NULL," +
                "`deskripsi` text NOT NULL," +
                "`estimasiMin` int(11) NOT NULL," +
                "`estimasiMax` int(11) NOT NULL," +
                "PRIMARY KEY (`idKerusakan`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        CONN.createStatement().executeUpdate(sql);
    }

    public void createTablePelanggan() throws SQLException {
        String sql = "CREATE TABLE `pelanggan` (" +
                "`idPelanggan` varchar(10) NOT NULL," +
                "`nama` varchar(50) NOT NULL," +
                "`alamat` text NOT NULL," +
                "`noHp` varchar(12) NOT NULL," +
                "`jekel` enum('Laki laki','Perempuan') NOT NULL," +
                "PRIMARY KEY (`idPelanggan`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        CONN.createStatement().executeUpdate(sql);
    }

    public void createTableUser() throws SQLException {
        String sql = "CREATE TABLE `user` (" +
                "`username` varchar(25) NOT NULL," +
                "`password` varchar(20) NOT NULL," +
                "`noHp` varchar(12) NOT NULL," +
                "`nama` varchar(50) NOT NULL," +
                "`fotoProfil` varchar(100) DEFAULT NULL," +
                "`jekel` enum('Laki laki','Perempuan') NOT NULL," +
                "`tanggalLahir` date NOT NULL," +
                "`alamat` text NOT NULL," +
                "`statusUser` enum('Admin','Karyawan Aktif','Karyawan Tidak Aktif') NOT NULL," +
                "`lastLogin` date DEFAULT NULL," +
                "`created` date NOT NULL," +
                "PRIMARY KEY (`username`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        CONN.createStatement().executeUpdate(sql);
    }

    public void createTableServis() throws SQLException {
        String sql = "CREATE TABLE `servis` (" +
                "`noFaktur` varchar(10) NOT NULL," +
                "`username` varchar(25) NOT NULL," +
                "`idPelanggan` varchar(10) NOT NULL," +
                "`merkLabel` varchar(45) NOT NULL," +
                "`tanggal` date NOT NULL," +
                "`batasHari` date NOT NULL," +
                "`statusDP` tinyint(1) NOT NULL," +
                "`uangDP` int(11) NOT NULL," +
                "`statusPembayaran` tinyint(1) NOT NULL," +
                "`uangPembayaran` int(11) NOT NULL," +
                "PRIMARY KEY (`noFaktur`)," +
                "KEY `servis_ibfk_1` (`username`)," +
                "KEY `servis_ibfk_2` (`idPelanggan`)," +
                "CONSTRAINT `servis_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE," +
                "CONSTRAINT `servis_ibfk_2` FOREIGN KEY (`idPelanggan`) REFERENCES `pelanggan` (`idPelanggan`) ON DELETE CASCADE ON UPDATE CASCADE" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        CONN.createStatement().executeUpdate(sql);
    }

    public void createTableDetailKerusakan() throws SQLException {
        String sql = "CREATE TABLE `detail_kerusakan` (" +
                "`noFaktur` varchar(10) DEFAULT NULL," +
                "`idKerusakan` varchar(10) DEFAULT NULL," +
                "`idBarang` varchar(10) DEFAULT NULL," +
                "`unit` int(11) DEFAULT NULL," +
                "`totalEstimasiMin` int(11) DEFAULT NULL," +
                "`totalEstimasiMax` int(11) DEFAULT NULL," +
                "KEY `detail_kerusakan_ibfk_1` (`noFaktur`)," +
                "KEY `detail_kerusakan_ibfk_2` (`idKerusakan`)," +
                "KEY `detail_kerusakan_ibfk_3` (`idBarang`)," +
                "CONSTRAINT `detail_kerusakan_ibfk_1` FOREIGN KEY (`noFaktur`) REFERENCES `servis` (`noFaktur`) ON DELETE CASCADE ON UPDATE CASCADE," +
                "CONSTRAINT `detail_kerusakan_ibfk_2` FOREIGN KEY (`idKerusakan`) REFERENCES `kerusakan` (`idKerusakan`) ON DELETE CASCADE ON UPDATE CASCADE," +
                "CONSTRAINT `detail_kerusakan_ibfk_3` FOREIGN KEY (`idBarang`) REFERENCES `barang` (`idBarang`) ON DELETE CASCADE ON UPDATE CASCADE" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        CONN.createStatement().executeUpdate(sql);
    }
}
