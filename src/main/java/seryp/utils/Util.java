package seryp.utils;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import seryp.model.dao.InstalasiDao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
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

    public String md5Hash(String str) {
        String result = "";
        try {
            // SOURCE https://www.geeksforgeeks.org/md5-hash-in-java/

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(str.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setEffectInnerShadow(Button... buttons) {
        for (Button button : buttons) {
            button.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    button.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, Color.BLACK, 10, 0, 0, 0));
                }
            });

            button.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    button.setEffect(null);
                }
            });
        }
    }
}
