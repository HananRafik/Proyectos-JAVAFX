module com.example.hanan_rafik_gestor {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    //Modulos agregados
    requires java.sql;
    requires static lombok;
    requires com.google.gson;


    opens com.example.hanan_rafik_gestor to javafx.fxml;
    exports com.example.hanan_rafik_gestor;
    exports com.example.hanan_rafik_gestor.model;
    opens com.example.hanan_rafik_gestor.model to javafx.fxml;
    exports com.example.hanan_rafik_gestor.database;
    opens com.example.hanan_rafik_gestor.database to javafx.fxml;
    exports com.example.hanan_rafik_gestor.utils;
    opens com.example.hanan_rafik_gestor.utils to javafx.fxml;
    exports com.example.hanan_rafik_gestor.dao;
    opens com.example.hanan_rafik_gestor.dao to javafx.fxml;


}