package seryp.utils;

import seryp.model.User;

import java.io.File;

public class SerypUtil {
    private FileHandler fileHandler;
    private static User userSession;

    public WindowControl getWindowControl() {
        return new WindowControl();
    }

    public FieldControl getFieldControl() {
        return new FieldControl();
    }

    public Validation getValidation() {
        return new Validation();
    }

    public Util getUtil() {
        return new Util();
    }

    public void setFileHandler(File file) {
        this.fileHandler = new FileHandler(file);
    }

    public FileHandler getFileHandler() {
        if (fileHandler == null)
            return new FileHandler();
        else
            return this.fileHandler;
    }

    public static void setUserSession(User userSession) {
        SerypUtil.userSession = userSession;
    }

    public static User getUserSession() {
        return SerypUtil.userSession;
    }
}
