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

//    requires atlantafx.base;

    opens com.bitvault to javafx.fxml;
    opens com.bitvault.ui.views to javafx.fxml;
    opens com.bitvault.ui.components to javafx.fxml;
    opens com.bitvault.ui.controller to javafx.fxml;

    opens com.bitvault.ui.model to com.fasterxml.jackson.databind;
    opens com.bitvault.server.dto to com.fasterxml.jackson.databind;

    exports com.bitvault;

    exports com.bitvault.util;
    exports com.bitvault.ui.views;
    exports com.bitvault.enums;
    exports com.bitvault.algos;
    exports com.bitvault.ui.controller;


    exports com.bitvault.database.daos;
    exports com.bitvault.database.utils;
    exports com.bitvault.database.provider;
    exports com.bitvault.database.creator;
    exports com.bitvault.database.models;

    exports com.bitvault.services.factory;
    exports com.bitvault.services.interfaces;
    exports com.bitvault.services.local;

    exports com.bitvault.security;

    exports com.bitvault.ui.utils;
    exports com.bitvault.ui.components;
    exports com.bitvault.ui.listcell;
    exports com.bitvault.ui.model;
    exports com.bitvault.ui.toggle;

    exports com.bitvault.server.dto;
    exports com.bitvault.ui.views.login;
    opens com.bitvault.ui.views.login to javafx.fxml;
    exports com.bitvault.ui.components.textfield;
    opens com.bitvault.ui.components.textfield to javafx.fxml;
    exports com.bitvault.ui.components.validation;
    opens com.bitvault.ui.components.validation to javafx.fxml;
    exports com.bitvault.ui.views.password;
    opens com.bitvault.ui.views.password to javafx.fxml;
    exports com.bitvault.ui.hyperlink;
    exports com.bitvault.ui.views.dashboard;
    opens com.bitvault.ui.views.dashboard to javafx.fxml;
    exports com.bitvault.ui.views.newaccount;
    opens com.bitvault.ui.views.newaccount to javafx.fxml;
    exports com.bitvault.ui.listnode;
    opens com.bitvault.ui.listnode to com.fasterxml.jackson.databind;
    exports com.bitvault.ui.async;
    opens com.bitvault.ui.async to javafx.fxml;
    exports com.bitvault.ui.views.sync;
    opens com.bitvault.ui.views.sync to javafx.fxml;
    opens com.bitvault.util to javafx.fxml;

}