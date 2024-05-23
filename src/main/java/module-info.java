module com.helha.java.q2.cinephile {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    exports com.helha.java.q2.cinephile.Controllers;
    exports com.helha.java.q2.cinephile.Models;
    exports com.helha.java.q2.cinephile.Views;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    opens com.helha.java.q2.cinephile.Controllers to javafx.fxml;
    opens com.helha.java.q2.cinephile.Views to javafx.fxml;

}

