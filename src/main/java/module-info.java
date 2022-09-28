module com.bitvault {
    requires com.sun.jna;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires de.mkammerer.argon2.nolibs;
    requires javafx.controls;
//    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;

//    opens com.bitvault to javafx.fxml;
//    opens com.bitvault.views to javafx.fxml;
//    opens com.bitvault.components to javafx.fxml;
    opens com.bitvault.model to com.fasterxml.jackson.databind;

    exports com.bitvault;
    exports com.bitvault.components;
    exports com.bitvault.model;

    exports com.bitvault.toggle;
    exports com.bitvault.util;
    exports com.bitvault.viewmodel;
    exports com.bitvault.views;
    exports com.bitvault.views.factory;
    exports com.bitvault.enums;
    exports com.bitvault.algos;

    exports com.bitvault.database.daos;
    exports com.bitvault.database.utils;
    exports com.bitvault.database.provider;
    exports com.bitvault.database.creator;
    exports com.bitvault.database.models;

    exports com.bitvault.services.factory;
    exports com.bitvault.services.interfaces;
    exports com.bitvault.services.local;


}