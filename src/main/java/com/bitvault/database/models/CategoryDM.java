package com.bitvault.database.models;

import com.bitvault.ui.model.Category;
import com.bitvault.util.DateTimeUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryDM(
        String id,
        String name,
        String color,
        String createdOn,
        String modifiedOn,
        String type

) {

    /**
     * @param rs Result set from sqlite
     * @return Conversion(mapping) of result set from Sqlite to {@link CategoryDM}
     * @throws SQLException {@link ResultSet}.getString() can throw SQLException
     */
    public static CategoryDM create(final ResultSet rs) throws SQLException {
        return new CategoryDM(
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getString(6)
        );
    }

    /**
     * Converts {@link Category} to {@link CategoryDM}, to save a new entry to database. Use UUID for ID
     *
     * @param category The category we want to save
     * @return new instance of {@link CategoryDM}
     */
    public static CategoryDM createNew(final Category category) {
        return new CategoryDM(
                UUID.randomUUID().toString(),
                category.name(),
                category.color(),
                DateTimeUtils.formatToUtc(LocalDateTime.now()),
                null,
                category.type()
        );
    }

    public static CategoryDM convertUpdate(final Category category) {
        return new CategoryDM(
                category.id(),
                category.name(),
                category.color(),
                null,
                DateTimeUtils.formatToUtc(LocalDateTime.now()),
                null
        );
    }


    public static Category convert(final CategoryDM cat) {
        return new Category(
                cat.id(),
                cat.name(),
                cat.color(),
                DateTimeUtils.parseToLocal(cat.createdOn()),
                DateTimeUtils.parseToLocal(cat.modifiedOn()),
                cat.type()
        );
    }
}
