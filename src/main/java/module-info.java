module com.example.socialnetworkgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.example.socialnetworkgui2 to javafx.fxml;
    exports com.example.socialnetworkgui2;
    exports com.example.socialnetworkgui2.controller;
    opens com.example.socialnetworkgui2.controller to javafx.fxml;
}