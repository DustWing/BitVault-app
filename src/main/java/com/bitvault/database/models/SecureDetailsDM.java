package com.bitvault.database.models;

import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Profile;
import com.bitvault.ui.model.SecureDetails;
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
                secureDetails.getCategory().id(),
                secureDetails.getProfile().id(),
                secureDetails.getDomain(),
                secureDetails.getTitle(),
                secureDetails.getDescription(),
                secureDetails.isFavourite(),
                DateTimeUtils.format(secureDetails.getCreatedOn()),
                DateTimeUtils.format(secureDetails.getModifiedOn()),
                DateTimeUtils.format(secureDetails.getExpiresOn()),
                DateTimeUtils.format(secureDetails.getImportedOn()),
                secureDetails.isRequiresMp(),
                secureDetails.isShared()
        );
    }

    public static SecureDetailsDM convert(final SecureDetails secureDetails) {
        return new SecureDetailsDM(
                secureDetails.getId(),
                secureDetails.getCategory().id(),
                secureDetails.getProfile().id(),
                secureDetails.getDomain(),
                secureDetails.getTitle(),
                secureDetails.getDescription(),
                secureDetails.isFavourite(),
                DateTimeUtils.format(secureDetails.getCreatedOn()),
                DateTimeUtils.format(secureDetails.getModifiedOn()),
                DateTimeUtils.format(secureDetails.getExpiresOn()),
                DateTimeUtils.format(secureDetails.getImportedOn()),
                secureDetails.isRequiresMp(),
                secureDetails.isShared()
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
