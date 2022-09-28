package com.bitvault.database.models;

import com.bitvault.model.Profile;
import com.bitvault.util.DateTimeUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public record ProfileDM(
        String id,
        String name,
        String createdOn,
        String modifiedOn
) {

    public static ProfileDM create(ResultSet rs) throws SQLException {

        return new ProfileDM(
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4)
        );
    }

    public static ProfileDM createNew(final String id, Profile profile) {

        return new ProfileDM(
                id,
                profile.name(),
                DateTimeUtils.format(profile.createdOn()),
                DateTimeUtils.format(profile.modifiedOn())
        );
    }

    public static ProfileDM convert(Profile profile) {

        return new ProfileDM(
                profile.id(),
                profile.name(),
                DateTimeUtils.format(profile.createdOn()),
                DateTimeUtils.format(profile.modifiedOn())
        );
    }

    public static Profile convert(ProfileDM profile) {

        return new Profile(
                profile.id(),
                profile.name(),
                DateTimeUtils.parse(profile.createdOn()),
                DateTimeUtils.parse(profile.modifiedOn())
        );
    }
}
