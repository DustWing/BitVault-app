package com.bitvault.server.dto;

public record SecureItemRqDto(
        String secureItemTypeId,
        String encryptedPayload
) {


    public enum SecureItemSharedType {
        PRIVATE,
        SHARED,
        SHARED_DISMISSED
    }

    public record DomainDetailsDto(
            String id,
            String name,
            String domain,
            String icon
    ) {
    }

    public record LocalPasswordDto(
            String id,
            String username,
            String password,
            DomainDetailsDto domainDetails,
            String details,
            String description,
            Boolean isFavourite,
            Boolean requiresMasterPassword,
            String category,
            String createdOn,
            String modifiedOn,
            String expiresOn,
            String importedOn,
            String profileId,
            SecureItemSharedType shared
    ) {
    }

    public record LocalProductKeyDto(
            String id,
            String key,
            DomainDetailsDto domainDetails,
            String details,
            String description,
            Boolean isFavourite,
            Boolean requiresMasterPassword,
            String category,
            String createdOn,
            String modifiedOn,
            String expiresOn,
            String importedOn,
            String profileId,
            SecureItemSharedType shared
    ) {
    }

    public record LocalCardDto(
            String id,
            String cardNumber,
            String cardCcv,
            String cardPin,
            String cardExpiryDate,
            String cardType,
            DomainDetailsDto domainDetails,
            String details,
            String description,
            Boolean isFavourite,
            Boolean requiresMasterPassword,
            String category,
            String createdOn,
            String modifiedOn,
            String expiresOn,
            String importedOn,
            String profileId,
            SecureItemSharedType shared
    ) {
    }

    public record LocalNoteDto(
            String id,
            String noteContent,
            DomainDetailsDto domainDetails,
            String details,
            String description,
            Boolean isFavourite,
            Boolean requiresMasterPassword,
            String category,
            String createdOn,
            String modifiedOn,
            String expiresOn,
            String importedOn,
            String profileId,
            SecureItemSharedType shared
    ) {
    }

}
