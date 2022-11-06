module com.bitvault {
    requires java.desktop;

    requires javafx.controls;
    requires javafx.fxml;

    requires com.sun.jna;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires de.mkammerer.argon2.nolibs;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;

    requires io.netty.transport;
    requires io.netty.handler;
    requires io.netty.codec.http;
    requires io.netty.buffer;
    requires java.net.http;
    requires io.netty.codec;
    requires io.netty.common;
//
//    requires com.google.zxing;
//    requires com.google.zxing.javase;

//    opens com.bitvault to javafx.fxml;
//    opens com.bitvault.views to javafx.fxml;
//    opens com.bitvault.components to javafx.fxml;
    opens com.bitvault.ui.model to com.fasterxml.jackson.databind;

    exports com.bitvault;

    exports com.bitvault.util;
    exports com.bitvault.ui.viewmodel;
    exports com.bitvault.ui.views;
    exports com.bitvault.ui.views.factory;
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
    exports com.bitvault.ui.utils;
    exports com.bitvault.ui.components;
    exports com.bitvault.ui.model;
    exports com.bitvault.ui.toggle;

}