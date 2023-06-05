package com.bitvault.ui.model;

import com.bitvault.ui.listcell.ITextColorCell;
import com.bitvault.ui.listnode.Identifiable;

import java.time.LocalDateTime;

public record Category(
        String id,
        String name,
        String color,
        LocalDateTime createdOn,
        LocalDateTime modifiedOn,
        String type,
        boolean deleted

) implements ITextColorCell, Identifiable {

    public static Category createFake(
            String id,
            String name,
            String color) {
        return new Category(
                id,
                name,
                color,
                null,
                null,
                null,
                false
        );
    }

    public static Category createNew(
            String name,
            String color,
            String type
    ) {
        return new Category(
                null,
                name,
                color,
                null,
                null,
                type,
                false
        );
    }

    public static Category createUpdate(
            String id,
            String name,
            String color,
            String type
    ) {
        return new Category(
                id,
                name,
                color,
                null,
                null,
                type,
                false
        );
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public String getText() {
        return name;
    }

    @Override
    public String getUniqueId() {
        return id;
    }

}
