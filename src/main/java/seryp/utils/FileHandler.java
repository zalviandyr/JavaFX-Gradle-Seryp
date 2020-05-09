package seryp.utils;

import seryp.model.*;
import seryp.model.dao.IdentitasTokoDao;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

public class FileHandler {
    private File file;
    private String destinasi;
    private IdentitasToko identitasToko;

    public FileHandler() {
        try {
            identitasToko = new IdentitasTokoDao().get();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public FileHandler(File file) {
        try {
            identitasToko = new IdentitasTokoDao().get();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        // cek jika file null
        if (file == null) {
            return "File";
        } else {
            // cek jika file tidak null akan tetapi ada
            if (file.exists()) {
                return file.getName();
            } else {
                return "File";
            }
        }
    }

    public void setPathCopy(String username) {
        if (file == null) {
            destinasi = "";
        } else {
            if (file.exists()) {
                // cari extensi file
                String ext = "";

                int i = file.getName().lastIndexOf('.');
                if (i > 0) {
                    ext = file.getName().substring(i + 1);
                }
                String fotoUser = username + "." + ext;
                destinasi = identitasToko.getSerypBasePath() + File.separator + "Foto profil" + File.separator + fotoUser;
            } else {
                destinasi = "";
            }
        }
    }

    public String getPathCopy() {
        if (file == null) {
            return "";
        } else {
            if (file.exists()) {
                return destinasi;
            } else {
                return "";
            }
        }
    }

    public void copyFileToPath() {
        try {
            if (!(destinasi.equals(""))) {
                if (file != null && file.exists()) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    // tulis file ke destinasi

                    FileOutputStream fileOutputStream = new FileOutputStream(destinasi);


                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fileInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, length);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile() {
        if (file != null) {
            file.delete();
        }
    }

    public void backupIdentitasTokoCsv(IdentitasToko identitasToko, File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("nama").append(',');
            stringBuilder.append("alamat").append(',');
            stringBuilder.append("baseSerypPath").append('\n');

            stringBuilder.append(identitasToko.getNamaToko()).append(',');
            stringBuilder.append(identitasToko.getAlamat()).append(',');
            stringBuilder.append(identitasToko.getSerypBasePath()).append('\n');

            printWriter.print(stringBuilder.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backupUserCsv(List<User> userList, File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("username").append(',');
            stringBuilder.append("password").append(',');
            stringBuilder.append("noHp").append(',');
            stringBuilder.append("nama").append(',');
            stringBuilder.append("fotoProfil").append(',');
            stringBuilder.append("jekel").append(',');
            stringBuilder.append("tanggalLahir").append(',');
            stringBuilder.append("alamat").append(',');
            stringBuilder.append("statusUser").append(',');
            stringBuilder.append("lastLogin").append(',');
            stringBuilder.append("created").append('\n');

            for (User user : userList) {
                stringBuilder.append(user.getUsername()).append(',');
                stringBuilder.append(user.getPassword()).append(',');
                stringBuilder.append(user.getNoHp()).append(',');
                stringBuilder.append(user.getNama()).append(',');
                stringBuilder.append(user.getFotoProfil()).append(',');
                stringBuilder.append(user.getJekel()).append(',');
                stringBuilder.append(user.getTanggalLahir()).append(',');
                stringBuilder.append(user.getAlamat()).append(',');
                stringBuilder.append(user.getStatusUser()).append(',');
                stringBuilder.append(user.getLastLogin().toString()).append(',');
                stringBuilder.append(user.getCreated().toString()).append('\n');
            }

            printWriter.print(stringBuilder.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backupBarangCsv(List<Barang> barangList, File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("idBarang").append(',');
            stringBuilder.append("nama").append(',');
            stringBuilder.append("deskripsi").append(',');
            stringBuilder.append("stok").append(',');
            stringBuilder.append("harga").append(',');
            stringBuilder.append("status").append('\n');

            for (Barang barang : barangList) {
                stringBuilder.append(barang.getIdBarang()).append(',');
                stringBuilder.append(barang.getNama()).append(',');
                stringBuilder.append(barang.getDeskripsi()).append(',');
                stringBuilder.append(barang.getStok()).append(',');
                stringBuilder.append(barang.getHarga()).append(',');
                stringBuilder.append(barang.getStatus()).append('\n');
            }

            printWriter.print(stringBuilder.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backupKerusakanCsv(List<Kerusakan> kerusakanList, File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("idKerusakan").append(',');
            stringBuilder.append("nama").append(',');
            stringBuilder.append("deskripsi").append(',');
            stringBuilder.append("estimasiMin").append(',');
            stringBuilder.append("estimasiMax").append('\n');

            for (Kerusakan kerusakan : kerusakanList) {
                stringBuilder.append(kerusakan.getIdKerusakan()).append(',');
                stringBuilder.append(kerusakan.getNama()).append(',');
                stringBuilder.append(kerusakan.getDeskripsi()).append(',');
                stringBuilder.append(kerusakan.getEstimasiMin()).append(',');
                stringBuilder.append(kerusakan.getEstimasiMax()).append('\n');
            }

            printWriter.print(stringBuilder.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void backupPelangganCsv(List<Pelanggan> pelangganList, File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("idPelanggan").append(',');
            stringBuilder.append("nama").append(',');
            stringBuilder.append("alamat").append(',');
            stringBuilder.append("noHp").append(',');
            stringBuilder.append("jekel").append('\n');

            for (Pelanggan pelanggan : pelangganList) {
                stringBuilder.append(pelanggan.getIdPelanggan()).append(',');
                stringBuilder.append(pelanggan.getNama()).append(',');
                stringBuilder.append(pelanggan.getAlamat()).append(',');
                stringBuilder.append(pelanggan.getNoHp()).append(',');
                stringBuilder.append(pelanggan.getJekel()).append('\n');
            }

            printWriter.print(stringBuilder.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void backupServisCsv(List<Servis> servisList, File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("noFaktur").append(',');
            stringBuilder.append("username").append(',');
            stringBuilder.append("idPelanggan").append(',');
            stringBuilder.append("merkLabel").append(',');
            stringBuilder.append("tanggal").append(',');
            stringBuilder.append("batasHari").append(',');
            stringBuilder.append("statusDP").append(',');
            stringBuilder.append("uangDP").append(',');
            stringBuilder.append("statusPembayaran").append(',');
            stringBuilder.append("uangPembayaran").append('\n');

            for (Servis servis : servisList) {
                stringBuilder.append(servis.getNoFaktur()).append(',');
                stringBuilder.append(servis.getUsername()).append(',');
                stringBuilder.append(servis.getIdPelanggan()).append(',');
                stringBuilder.append(servis.getMerkLabel()).append(',');
                stringBuilder.append(servis.getTanggalHariIni().toString()).append(',');
                stringBuilder.append(servis.getBatasHari().toString()).append(',');
                stringBuilder.append(servis.isStatusDP()).append(',');
                stringBuilder.append(servis.getUangDP()).append(',');
                stringBuilder.append(servis.isStatusPembayaran()).append(',');
                stringBuilder.append(servis.getUangPembayaran()).append('\n');
            }

            printWriter.print(stringBuilder.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backupDetailKerusakanCsv(List<DetailKerusakan> detailKerusakanList, File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("noFaktur").append(',');
            stringBuilder.append("idKerusakan").append(',');
            stringBuilder.append("idBarang").append(',');
            stringBuilder.append("unit").append(',');
            stringBuilder.append("totalEstimasiMin").append(',');
            stringBuilder.append("totalEstimasiMax").append('\n');

            for (DetailKerusakan detailKerusakan : detailKerusakanList) {
                stringBuilder.append(detailKerusakan.getNoFaktur()).append(',');
                stringBuilder.append(detailKerusakan.getIdKerusakan()).append(',');
                stringBuilder.append(detailKerusakan.getIdBarang()).append(',');
                stringBuilder.append(detailKerusakan.getUnit()).append(',');
                stringBuilder.append(detailKerusakan.getTotalEstimasiMin()).append(',');
                stringBuilder.append(detailKerusakan.getTotalEstimasiMax()).append('\n');
            }

            printWriter.print(stringBuilder.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
