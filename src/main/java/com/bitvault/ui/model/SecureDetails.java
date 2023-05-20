package com.bitvault.ui.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public final class SecureDetails {
    private final String id;
    private final Category category;
    private final Profile profile;
    private final String domain;
    private final String title;
    private final String description;
    private final boolean favourite;
    private final LocalDateTime createdOn;
    private final LocalDateTime modifiedOn;
    private final LocalDateTime expiresOn;
    private final LocalDateTime importedOn;
    private final boolean requiresMp;
    private final boolean shared;

    public SecureDetails(
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
        this.id = id;
        this.category = category;
        this.profile = profile;
        this.domain = domain;
        this.title = title;
        this.description = description;
        this.favourite = favourite;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.expiresOn = expiresOn;
        this.importedOn = importedOn;
        this.requiresMp = requiresMp;
        this.shared = shared;
    }

    public String getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getDomain() {
        return domain;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    public LocalDateTime getExpiresOn() {
        return expiresOn;
    }

    public Optional<LocalDateTime> getExpiresOnOpt() {
        return Optional.ofNullable(expiresOn);
    }

    public LocalDateTime getImportedOn() {
        return importedOn;
    }

    public boolean isRequiresMp() {
        return requiresMp;
    }

    public boolean isShared() {
        return shared;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SecureDetails) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.category, that.category) &&
                Objects.equals(this.profile, that.profile) &&
                Objects.equals(this.domain, that.domain) &&
                Objects.equals(this.title, that.title) &&
                Objects.equals(this.description, that.description) &&
                this.favourite == that.favourite &&
                Objects.equals(this.createdOn, that.createdOn) &&
                Objects.equals(this.modifiedOn, that.modifiedOn) &&
                Objects.equals(this.expiresOn, that.expiresOn) &&
                Objects.equals(this.importedOn, that.importedOn) &&
                this.requiresMp == that.requiresMp &&
                this.shared == that.shared;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, profile, domain, title, description, favourite, createdOn, modifiedOn, expiresOn, importedOn, requiresMp, shared);
    }

    @Override
    public String toString() {
        return "SecureDetails[" +
                "id=" + id + ", " +
                "category=" + category + ", " +
                "profile=" + profile + ", " +
                "domain=" + domain + ", " +
                "title=" + title + ", " +
                "description=" + description + ", " +
                "favourite=" + favourite + ", " +
                "createdOn=" + createdOn + ", " +
                "modifiedOn=" + modifiedOn + ", " +
                "expiresOn=" + expiresOn + ", " +
                "importedOn=" + importedOn + ", " +
                "requiresMp=" + requiresMp + ", " +
                "shared=" + shared + ']';
    }


}
