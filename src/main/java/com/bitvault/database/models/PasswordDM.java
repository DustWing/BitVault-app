package com.bitvault.database.models;

import com.bitvault.enums.Action;
import com.bitvault.model.Password;
import com.bitvault.model.SecureDetails;

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

    public static PasswordDM createNew(final String id, final Password password) {

        return new PasswordDM(
                id,
                password.username(),
                password.password(),
                id
        );
    }

    public static PasswordDM convert(final Password password) {

        return new PasswordDM(
                password.id(),
                password.username(),
                password.password(),
                password.secureDetails().id()
        );
    }

    public static Password convert(final PasswordDM passwordDM, final SecureDetails secureDetails, final Action action) {
        return new Password(
                passwordDM.id(),
                passwordDM.username(),
                passwordDM.password(),
                secureDetails,
                action
        );
    }
}
