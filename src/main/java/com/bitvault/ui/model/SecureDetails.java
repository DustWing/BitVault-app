package com.bitvault.ui.model;

import java.time.LocalDateTime;

public record SecureDetails(
        String id,
        Category category,
        Profile profile,
        String domain,
        String title,
        String description,
        boolean favourite,
        LocalDateTime createdOn,
        LocalDateTime modifiedOn,
        LocalDateTime expiresOn,
        LocalDateTime importedOn,
        boolean requiresMp,
        boolean shared
) {


}
