package com.bitvault.database.provider;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalDB implements ConnectionProvider {

    private final String location;


    public LocalDB(String location) {
        this.location = location;
    }


    @Override
    public String getLocation() {
        return location;
    }

    public Connection connect() throws SQLException {
        boolean exists = new File(location).exists();
        if (!exists) {
            throw new IllegalArgumentException("no.local.db.found");
        }

        String dbPrefix = "jdbc:sqlite:";
        Connection connection;
        try {
            connection = DriverManager.getConnection(dbPrefix + location);
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE,
                    LocalDateTime.now() + ": Could not connect to SQLite DB at " +
                            location);
            throw exception;
        }
        return connection;
    }


    @Override
    public void connect(ConnectionConsumer<Connection> connectionConsumer) {

        boolean exists = new File(location).exists();
        if (!exists) {
            throw new IllegalArgumentException("no.local.db.found");
        }

        try (Connection connection = connect()) {
            connection.setAutoCommit(false);
            try {
                connectionConsumer.accept(
                        connection
                );
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to DB", e);
        }

    }

    public static boolean checkDrivers() {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new org.sqlite.JDBC());
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not start SQLite Drivers");
            return false;
        }
    }


}
