package com.bitvault.model;

import java.time.LocalDateTime;

public record Profile(
        String id,
        String name,
        LocalDateTime createdOn,
        LocalDateTime modifiedOn
) {

    public static Profile create(
            String name,
            LocalDateTime createdOn,
            LocalDateTime modifiedOn
    ) {
        return new Profile(
                null,
                name,
                createdOn,
                modifiedOn
        );
    }

}
