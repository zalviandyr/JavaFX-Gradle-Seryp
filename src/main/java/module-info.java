module Gradle_Seryp{
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.java;
    requires java.sql;
    requires jasperreports;

//    requires jdk.jlink;
    requires java.desktop;
//    requires jdk.incubator.jpackage; // untuk jpackage

    opens seryp.controller to javafx.fxml;
    exports seryp;
    exports seryp.model; // untuk jasper report bisa baca
}