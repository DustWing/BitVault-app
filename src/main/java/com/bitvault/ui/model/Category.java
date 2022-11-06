package com.bitvault.ui.model;

import com.bitvault.ui.listcell.ITextColorCell;

import java.time.LocalDateTime;

public record Category(
        String id,
        String name,
        String color,
        LocalDateTime createdOn,
        LocalDateTime modifiedOn,
        String type

) implements ITextColorCell {


    @Override
    public String getColor() {
        return color;
    }

    @Override
    public String getText() {
        return name;
    }

    public static Category create(
            String name,
            String color,
            LocalDateTime createdOn,
            LocalDateTime modifiedOn,
            String type
    ) {
        return new Category(
                null,
                name,
                color,
                createdOn,
                modifiedOn,
                type
        );
    }

}
