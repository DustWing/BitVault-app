package com.bitvault.database.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public record SettingsDM(
        String name,
        String value,
        String createdOn,
        String modifiedOn
) {

    public static SettingsDM create(ResultSet resultSet) throws SQLException {

        return new SettingsDM(
                resultSet.getString(1),
                resultSet.getString(2),
                null,
                null
        );
    }
}
