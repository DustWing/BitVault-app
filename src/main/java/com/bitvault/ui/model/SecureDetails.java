package com.bitvault.ui.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;

public final class SecureDetails {
    private final String id;
    private final SimpleObjectProperty<Category> category;
    private final SimpleObjectProperty<Profile> profile;
    private final SimpleStringProperty domain;
    private final SimpleStringProperty title;
    private final SimpleStringProperty description;
    private final SimpleBooleanProperty favourite;
    private final SimpleObjectProperty<LocalDateTime> createdOn;
    private final SimpleObjectProperty<LocalDateTime> modifiedOn;
    private final SimpleObjectProperty<LocalDateTime> expiresOn;
    private final SimpleObjectProperty<LocalDateTime> importedOn;
    private final SimpleBooleanProperty requiresMp;
    private final SimpleBooleanProperty shared;


    public static SecureDetails prepareNew(String id, Category category, Profile profile) {
        return new SecureDetails(
                id,
                category,
                profile,
                "",
                "",
                "",
                false,
                null,
                null,
                null,
                null,
                false,
                false
        );
    }

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
        this.category = new SimpleObjectProperty<>(category);
        this.profile = new SimpleObjectProperty<>(profile);
        this.domain = new SimpleStringProperty(domain);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.favourite = new SimpleBooleanProperty(favourite);
        this.createdOn = new SimpleObjectProperty<>(createdOn);
        this.modifiedOn = new SimpleObjectProperty<>(modifiedOn);
        this.expiresOn = new SimpleObjectProperty<>(expiresOn);
        this.importedOn = new SimpleObjectProperty<>(importedOn);
        this.requiresMp = new SimpleBooleanProperty(requiresMp);
        this.shared = new SimpleBooleanProperty(shared);
    }

    public SecureDetails deepCopy() {
        return new SecureDetails(
                this.getId(),
                this.getCategory(), // not deep copy
                this.getProfile(),// not deep copy
                this.getDomain(),
                this.getTitle(),
                this.getDescription(),
                this.isFavourite(),
                this.getCreatedOn(),
                this.getModifiedOn(),
                this.getExpiresOn(),
                this.getImportedOn(),
                this.isRequiresMp(),
                this.isShared()
        );
    }

    public void update(SecureDetails secureDetails) {
        this.categoryProperty().set(secureDetails.getCategory());
        this.profileProperty().set(secureDetails.getProfile());
        this.domainProperty().set(secureDetails.getDomain());
        this.titleProperty().set(secureDetails.getTitle());
        this.descriptionProperty().set(secureDetails.getDescription());
        this.favouriteProperty().set(secureDetails.isFavourite());
        this.createdOnProperty().set(secureDetails.getCreatedOn());
        this.modifiedOnProperty().set(secureDetails.getModifiedOn());
        this.expiresOnProperty().set(secureDetails.getExpiresOn());
        this.importedOnProperty().set(secureDetails.getImportedOn());
        this.requiresMpProperty().set(secureDetails.isRequiresMp());
        this.sharedProperty().set(secureDetails.isShared());
    }

    public String getId() {
        return id;
    }

    public Category getCategory() {
        return category.get();
    }

    public SimpleObjectProperty<Category> categoryProperty() {
        return category;
    }

    public Profile getProfile() {
        return profile.get();
    }

    public SimpleObjectProperty<Profile> profileProperty() {
        return profile;
    }

    public String getDomain() {
        return domain.get();
    }

    public SimpleStringProperty domainProperty() {
        return domain;
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public boolean isFavourite() {
        return favourite.get();
    }

    public SimpleBooleanProperty favouriteProperty() {
        return favourite;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn.get();
    }

    public SimpleObjectProperty<LocalDateTime> createdOnProperty() {
        return createdOn;
    }

    public LocalDateTime getModifiedOn() {
        return modifiedOn.get();
    }

    public SimpleObjectProperty<LocalDateTime> modifiedOnProperty() {
        return modifiedOn;
    }

    public LocalDateTime getExpiresOn() {
        return expiresOn.get();
    }

    public SimpleObjectProperty<LocalDateTime> expiresOnProperty() {
        return expiresOn;
    }

    public LocalDateTime getImportedOn() {
        return importedOn.get();
    }

    public SimpleObjectProperty<LocalDateTime> importedOnProperty() {
        return importedOn;
    }

    public boolean isRequiresMp() {
        return requiresMp.get();
    }

    public SimpleBooleanProperty requiresMpProperty() {
        return requiresMp;
    }

    public boolean isShared() {
        return shared.get();
    }

    public SimpleBooleanProperty sharedProperty() {
        return shared;
    }

}
