package seryp.utils;

import seryp.utils.boxes.AlertBox;

public class Validation {
    public String validateNoHandphone(String str) {
        // jika no hp lebih dari 12 digit
        if (str.length() > 12) {
            AlertBox.display("Format Error", "No Handphone tidak boleh lebih 12 digit");
            return null;
        }

        // jika no hp didepannya tidak mengandung 08
        if (!str.startsWith("08")) {
            AlertBox.display("Format Error", "No Handphone harus menggunakan 08xxxx");
            return null;
        }

        // jika no hp tidak mengandung angka
        try {
            Long.parseLong(str);

            // kalau pas, akan mengembalikan nilai
            return str;
        } catch (NumberFormatException e) {
            AlertBox.display("Format Error", "No Handphone hanya boleh mengandung angka");
            return null;
        }
    }

    public Integer validateInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            AlertBox.display("Format Error", "Hanya menerima input angka");
        }
        return null;
    }
}
