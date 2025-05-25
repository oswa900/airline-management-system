module com.airlane.airlinemanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;
    requires itextpdf;
    requires java.desktop;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.bootstrapfx.core;

    opens com.airlane.airlinemanagementsystem to javafx.fxml;
    opens com.airlane.airlinemanagementsystem.controller to javafx.fxml;
    opens com.airlane.airlinemanagementsystem.model to javafx.base;
    opens com.airlane.airlinemanagementsystem.util to javafx.base;
    opens com.airlane.airlinemanagementsystem.controller.admin to javafx.fxml;


    exports com.airlane.airlinemanagementsystem;
}
