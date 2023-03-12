package com.bitvault.database.models;

import com.bitvault.ui.model.Password;

import java.sql.ResultSet;
import java.sql.SQLException;

public record PasswordDM(
        String id,
        String username,
        String password,
        String secureDetailsId
) {

    public static PasswordDM create(ResultSet rs) throws SQLException {

        return new PasswordDM(
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4)
        );
    }

}
