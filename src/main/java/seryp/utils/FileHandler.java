package seryp.utils;

import seryp.model.*;
import seryp.model.dao.IdentitasTokoDao;

import java.io.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private File file;
    private String destinasi;

    public FileHandler() {
    }

    public FileHandler(File file) {
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
        try {
            if (file == null) {
                destinasi = "";
            } else {
                if (file.exists()) {
                    IdentitasToko identitasToko = new IdentitasTokoDao().get();
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
        } catch (SQLException e) {
            e.printStackTrace();
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

    public void backupIdentitasTokoSerypBk(IdentitasToko identitasToko, File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(identitasToko.getNamaToko()).append('|');
            stringBuilder.append(identitasToko.getAlamat()).append('|');
            stringBuilder.append(identitasToko.getSerypBasePath()).append('\n');

            printWriter.print(stringBuilder.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public IdentitasToko readIdentitasTokoSerypBk(File file) {
        IdentitasToko identitasToko = null;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader csvReader = new BufferedReader(fileReader);

            String rowData;
            while ((rowData = csvReader.readLine()) != null) {
                identitasToko = new IdentitasToko();
                String[] data = rowData.split("\\|");
                identitasToko.setNamaToko(data[0]);
                identitasToko.setAlamat(data[1]);
                identitasToko.setSerypBasePath(data[2]);
            }

            fileReader.close();
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return identitasToko;
    }

    public void backupUserSerypBk(List<User> userList, File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            StringBuilder stringBuilder = new StringBuilder();

            for (User user : userList) {
                stringBuilder.append(user.getUsername()).append('|');
                stringBuilder.append(user.getPassword()).append('|');
                stringBuilder.append(user.getNoHp()).append('|');
                stringBuilder.append(user.getNama()).append('|');
                stringBuilder.append(user.getFotoProfil()).append('|');
                stringBuilder.append(user.getJekel()).append('|');
                stringBuilder.append(user.getTanggalLahir()).append('|');
                stringBuilder.append(user.getAlamat()).append('|');
                stringBuilder.append(user.getStatusUser()).append('|');
                stringBuilder.append(user.getLastLogin().toString()).append('|');
                stringBuilder.append(user.getCreated().toString()).append('\n');
            }

            printWriter.print(stringBuilder.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> readUserSerypBk(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader csvReader = new BufferedReader(fileReader);

            String rowData;
            List<User> userList = new ArrayList<>();
            while ((rowData = csvReader.readLine()) != null) {
                String[] data = rowData.split("\\|");
                User user = new User();
                user.setUsername(data[0]);
                user.setPassword(data[1]);
                user.setNoHp(data[2]);
                user.setNama(data[3]);
                user.setFotoProfil(data[4]);
                user.setJekel(data[5]);
                user.setTanggalLahir(Date.valueOf(data[6]).toLocalDate());
                user.setAlamat(data[7]);
                user.setStatusUser(data[8]);
                user.setLastLogin(Date.valueOf(data[9]).toLocalDate());
                user.setCreated(Date.valueOf(data[10]).toLocalDate());

                userList.add(user);
            }

            fileReader.close();
            csvReader.close();
            return userList;
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
//            e.printStackTrace();
            return null; // jika file kosong
        }
    }

    public void backupBarangSerypBk(List<Barang> barangList, File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            StringBuilder stringBuilder = new StringBuilder();

            for (Barang barang : barangList) {
                stringBuilder.append(barang.getIdBarang()).append('|');
                stringBuilder.append(barang.getNama()).append('|');
                stringBuilder.append(barang.getDeskripsi()).append('|');
                stringBuilder.append(barang.getStok()).append('|');
                stringBuilder.append(barang.getHarga()).append('|');
                stringBuilder.append(barang.getStatus()).append('\n');
            }

            printWriter.print(stringBuilder.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Barang> readBarangSerypBk(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader csvReader = new BufferedReader(fileReader);

            String rowData;
            List<Barang> barangList = new ArrayList<>();
            while ((rowData = csvReader.readLine()) != null) {
                String[] data = rowData.split("\\|");
                Barang barang = new Barang();
                barang.setIdBarang(data[0]);
                barang.setNama(data[1]);
                barang.setDeskripsi(data[2]);
                barang.setStok(Integer.parseInt(data[3]));
                barang.setHarga(Integer.parseInt(data[4]));
                barang.setStatus(data[5]);

                barangList.add(barang);
            }

            fileReader.close();
            csvReader.close();
            return barangList;
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
//            e.printStackTrace();
            return null; // jika file kosong
        }
    }

    public void backupKerusakanSerypBk(List<Kerusakan> kerusakanList, File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            StringBuilder stringBuilder = new StringBuilder();

            for (Kerusakan kerusakan : kerusakanList) {
                stringBuilder.append(kerusakan.getIdKerusakan()).append('|');
                stringBuilder.append(kerusakan.getNama()).append('|');
                stringBuilder.append(kerusakan.getDeskripsi()).append('|');
                stringBuilder.append(kerusakan.getEstimasiMin()).append('|');
                stringBuilder.append(kerusakan.getEstimasiMax()).append('\n');
            }

            printWriter.print(stringBuilder.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public List<Kerusakan> readKerusakanSerypBk(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader csvReader = new BufferedReader(fileReader);

            String rowData;
            List<Kerusakan> kerusakanList = new ArrayList<>();
            while ((rowData = csvReader.readLine()) != null) {
                String[] data = rowData.split("\\|");
                Kerusakan kerusakan = new Kerusakan();
                kerusakan.setIdKerusakan(data[0]);
                kerusakan.setNama(data[1]);
                kerusakan.setDeskripsi(data[2]);
                kerusakan.setEstimasiMin(Integer.parseInt(data[3]));
                kerusakan.setEstimasiMax(Integer.parseInt(data[4]));

                kerusakanList.add(kerusakan);
            }

            fileReader.close();
            csvReader.close();
            return kerusakanList;
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
//            e.printStackTrace();
            return null; // jika file kosong
        }
    }

    public void backupPelangganSerypBk(List<Pelanggan> pelangganList, File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            StringBuilder stringBuilder = new StringBuilder();

            for (Pelanggan pelanggan : pelangganList) {
                stringBuilder.append(pelanggan.getIdPelanggan()).append('|');
                stringBuilder.append(pelanggan.getNama()).append('|');
                stringBuilder.append(pelanggan.getAlamat()).append('|');
                stringBuilder.append(pelanggan.getNoHp()).append('|');
                stringBuilder.append(pelanggan.getJekel()).append('\n');
            }

            printWriter.print(stringBuilder.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public List<Pelanggan> readPelangganSerypBk(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader csvReader = new BufferedReader(fileReader);

            String rowData;
            List<Pelanggan> pelangganList = new ArrayList<>();
            while ((rowData = csvReader.readLine()) != null) {
                String[] data = rowData.split("\\|");
                Pelanggan pelanggan = new Pelanggan();
                pelanggan.setIdPelanggan(data[0]);
                pelanggan.setNama(data[1]);
                pelanggan.setAlamat(data[2]);
                pelanggan.setNoHp(data[3]);
                pelanggan.setJekel(data[4]);

                pelangganList.add(pelanggan);
            }

            fileReader.close();
            csvReader.close();
            return pelangganList;
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
//            e.printStackTrace();
            return null; // jika file kosong
        }
    }

    public void backupServisSerypBk(List<Servis> servisList, File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            StringBuilder stringBuilder = new StringBuilder();

            for (Servis servis : servisList) {
                stringBuilder.append(servis.getNoFaktur()).append('|');
                stringBuilder.append(servis.getUsername()).append('|');
                stringBuilder.append(servis.getIdPelanggan()).append('|');
                stringBuilder.append(servis.getMerkLabel()).append('|');
                stringBuilder.append(servis.getTanggalHariIni().toString()).append('|');
                stringBuilder.append(servis.getBatasHari().toString()).append('|');
                stringBuilder.append(servis.isStatusDP()).append('|');
                stringBuilder.append(servis.getUangDP()).append('|');
                stringBuilder.append(servis.isStatusPembayaran()).append('|');
                stringBuilder.append(servis.getUangPembayaran()).append('\n');
            }

            printWriter.print(stringBuilder.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Servis> readServisSerypBk(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader csvReader = new BufferedReader(fileReader);

            String rowData;
            List<Servis> servisList = new ArrayList<>();
            while ((rowData = csvReader.readLine()) != null) {
                String[] data = rowData.split("\\|");
                Servis servis = new Servis();
                servis.setNoFaktur(data[0]);
                servis.setUsername(data[1]);
                servis.setIdPelanggan(data[2]);
                servis.setMerkLabel(data[3]);
                servis.setTanggalHariIni(Date.valueOf(data[4]).toLocalDate());
                servis.setBatasHari(Date.valueOf(data[5]).toLocalDate());
                servis.setStatusDP(Boolean.parseBoolean(data[6]));
                servis.setUangDP(Integer.parseInt(data[7]));
                servis.setStatusPembayaran(Boolean.parseBoolean(data[8]));
                servis.setUangPembayaran(Integer.parseInt(data[9]));

                servisList.add(servis);
            }

            fileReader.close();
            csvReader.close();
            return servisList;
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
//            e.printStackTrace();
            return null; // jika file kosong
        }
    }

    public void backupDetailKerusakanSerypBk(List<DetailKerusakan> detailKerusakanList, File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            StringBuilder stringBuilder = new StringBuilder();

            for (DetailKerusakan detailKerusakan : detailKerusakanList) {
                stringBuilder.append(detailKerusakan.getNoFaktur()).append('|');
                stringBuilder.append(detailKerusakan.getIdKerusakan()).append('|');
                stringBuilder.append(detailKerusakan.getIdBarang()).append('|');
                stringBuilder.append(detailKerusakan.getUnit()).append('|');
                stringBuilder.append(detailKerusakan.getTotalEstimasiMin()).append('|');
                stringBuilder.append(detailKerusakan.getTotalEstimasiMax()).append('\n');
            }

            printWriter.print(stringBuilder.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public List<DetailKerusakan> readDetailKerusakanSerypBk(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader csvReader = new BufferedReader(fileReader);

            String rowData;
            List<DetailKerusakan> detailKerusakanList = new ArrayList<>();
            while ((rowData = csvReader.readLine()) != null) {
                String[] data = rowData.split("\\|");
                DetailKerusakan detailKerusakan = new DetailKerusakan();
                detailKerusakan.setNoFaktur(data[0]);
                detailKerusakan.setIdKerusakan(data[1]);
                detailKerusakan.setIdBarang(data[2]);
                detailKerusakan.setUnit(Integer.parseInt(data[3]));
                detailKerusakan.setTotalEstimasiMin(Integer.parseInt(data[4]));
                detailKerusakan.setTotalEstimasiMax(Integer.parseInt(data[5]));

                detailKerusakanList.add(detailKerusakan);
            }

            fileReader.close();
            csvReader.close();
            return detailKerusakanList;
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
//            e.printStackTrace();
            return null; // jika file kosong
        }
    }
}
