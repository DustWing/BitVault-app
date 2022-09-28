package com.bitvault.database.provider;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {


    interface ConnectionConsumer<T> {
        void accept(T t) throws SQLException;
    }

    String getLocation();

    Connection connect() throws SQLException;

    void connect(ConnectionConsumer<Connection> connectionConsumer);

}
