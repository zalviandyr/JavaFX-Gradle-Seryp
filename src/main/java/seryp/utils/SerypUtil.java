package seryp.utils;

import java.io.File;

public class SerypUtil {
    private FileHandler fileHandler;

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
}
