package com.bitvault.database.models;

import com.bitvault.model.Category;
import com.bitvault.model.Profile;
import com.bitvault.model.SecureDetails;
import com.bitvault.util.DateTimeUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public record SecureDetailsDM(
        String id,
        String categoryId,
        String profileId,
        String domain,
        String title,
        String description,
        boolean favourite,
        String createdOn,
        String modifiedOn,
        String expiresOn,
        String importedOn,
        boolean requiresMp,
        boolean shared
) {

    public static SecureDetailsDM create(ResultSet rs) throws SQLException {

        return new SecureDetailsDM(
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getString(6),
                rs.getBoolean(7),
                rs.getString(8),
                rs.getString(9),
                rs.getString(10),
                rs.getString(11),
                rs.getBoolean(12),
                rs.getBoolean(13)
        );
    }

    public static SecureDetailsDM createNew(final String id,final SecureDetails secureDetails) {
        return new SecureDetailsDM(
                id,
                secureDetails.category().id(),
                secureDetails.profile().id(),
                secureDetails.domain(),
                secureDetails.title(),
                secureDetails.description(),
                secureDetails.favourite(),
                DateTimeUtils.format(secureDetails.createdOn()),
                DateTimeUtils.format(secureDetails.modifiedOn()),
                DateTimeUtils.format(secureDetails.expiresOn()),
                DateTimeUtils.format(secureDetails.importedOn()),
                secureDetails.requiresMp(),
                secureDetails.shared()
        );
    }

    public static SecureDetailsDM convert(final SecureDetails secureDetails) {
        return new SecureDetailsDM(
                secureDetails.id(),
                secureDetails.category().id(),
                secureDetails.profile().id(),
                secureDetails.domain(),
                secureDetails.title(),
                secureDetails.description(),
                secureDetails.favourite(),
                DateTimeUtils.format(secureDetails.createdOn()),
                DateTimeUtils.format(secureDetails.modifiedOn()),
                DateTimeUtils.format(secureDetails.expiresOn()),
                DateTimeUtils.format(secureDetails.importedOn()),
                secureDetails.requiresMp(),
                secureDetails.shared()
        );
    }

    public static SecureDetails convert(final SecureDetailsDM secureDetails, Category category, Profile profile) {

        return new SecureDetails(
                secureDetails.id(),
                category,
                profile,
                secureDetails.domain(),
                secureDetails.title(),
                secureDetails.description(),
                secureDetails.favourite(),
                DateTimeUtils.parse(secureDetails.createdOn()),
                DateTimeUtils.parse(secureDetails.modifiedOn()),
                DateTimeUtils.parse(secureDetails.expiresOn()),
                DateTimeUtils.parse(secureDetails.importedOn()),
                secureDetails.requiresMp(),
                secureDetails.shared()
        );
    }
}
