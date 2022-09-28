package com.bitvault.database.models;

import com.bitvault.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public record UserDM(
        String id,
        String name,
        String credentials
) {

    public static UserDM create(ResultSet rs) throws SQLException {
        return new UserDM(
                rs.getString(1),
                rs.getString(2),
                rs.getString(3)

        );
    }

    public static UserDM convert(User user) {
        return new UserDM(
                user.id(),
                user.name(),
                user.credentials()
        );
    }

    public static User convert(UserDM user) {

        return new User(
                user.id(),
                user.name(),
                user.credentials()
        );
    }
}
