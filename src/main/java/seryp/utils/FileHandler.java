package seryp.utils;

import seryp.model.IdentitasToko;
import seryp.model.dao.IdentitasTokoDao;

import java.io.*;
import java.sql.SQLException;

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
                destinasi = identitasToko.getFotoProfilPath() + File.separator + fotoUser;
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
}
