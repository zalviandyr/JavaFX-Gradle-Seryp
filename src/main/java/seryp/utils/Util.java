package seryp.utils;

import seryp.model.dao.InstalasiDao;

import java.sql.SQLException;
import java.util.List;

public class Util {
    public String getCustomId(List<String> listId, String customChar) {
        try {
            int id = 0;
            for (String str : listId) {
                id = Integer.parseInt(str);
            }

            id += 1;
            if (Integer.toString(id).length() == 1) {
                return customChar + "00" + id;
            } else if (Integer.toString(id).length() == 2) {
                return customChar + "0" + id;
            } else {
                return customChar + id;
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createDatabase() throws SQLException {
        InstalasiDao instalasiDao = new InstalasiDao();
        instalasiDao.createTableBarang();
        instalasiDao.createTableIdentitasToko();
        instalasiDao.createTableKerusakan();
        instalasiDao.createTablePelanggan();
        instalasiDao.createTableUser();
        instalasiDao.createTableServis();
        instalasiDao.createTableDetailKerusakan();
    }
}
