package com.bitvault.ui.views.password;

import com.bitvault.enums.Action;
import com.bitvault.ui.components.validation.ValidateForm;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.Profile;
import com.bitvault.ui.model.SecureDetails;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class PasswordDetailsVM {

    private final Password password;
    private final Action action;
    private final List<Category> categories;
    private final Profile profile;
    private final Consumer<Password> onAction;

    private final SimpleStringProperty titleProperty = new SimpleStringProperty();
    private final SimpleStringProperty userNameProperty = new SimpleStringProperty();
    private final SimpleStringProperty passwordProperty = new SimpleStringProperty();
    private final SimpleStringProperty domainProperty = new SimpleStringProperty();
    private final SimpleStringProperty descriptionProperty = new SimpleStringProperty();
    private final SimpleObjectProperty<Category> selectedCat = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<LocalDate> expiresOn = new SimpleObjectProperty<>();

    public PasswordDetailsVM(
            Password password,
            List<Category> categories,
            Profile profile,
            Action action,
            Consumer<Password> onAction
    ) {
        this.password = password;
        this.categories = categories;
        this.profile = profile;
        this.action = action;
        this.onAction = onAction;

        this.selectedCatProperty().set(categories.get(0));

        if (Action.EDIT.equals(action)) {
            editSetUp();
        }
    }

    private void editSetUp() {
        this.titleProperty.set(password.getSecureDetails().getTitle());
        this.userNameProperty.set(password.getUsername());
        this.passwordProperty.set(password.getPassword());
        this.domainProperty.set(password.getSecureDetails().getDomain());
        this.descriptionProperty.set(password.getSecureDetails().getDescription());
        this.expiresOn.set(password.getSecureDetails().getExpiresOn() == null ? null : password.getSecureDetails().getExpiresOn().toLocalDate());
        this.selectedCat.set(password.getSecureDetails().getCategory());
    }


    public void save() {

        final LocalDateTime now = LocalDateTime.now();

        final String id = Action.EDIT.equals(action)
                ? password.getId() : UUID.randomUUID().toString();

        final LocalDateTime modifiedOn = Action.EDIT.equals(action) ? now : null;

        final SecureDetails secureDetails = new SecureDetails(
                id,
                getSelectedCat(),
                profile,
                getDomainProperty(),
                getTitleProperty(),
                getDescriptionProperty(),
                false,
                now,
                modifiedOn,
                getExpiresOn() == null ? null : getExpiresOn().atStartOfDay(),
                null,
                false,
                false
        );

        final Password passResult = new Password(
                id,
                getUserNameProperty(),
                getPasswordProperty(),
                secureDetails
        );

        onAction.accept(passResult);

    }


    public Password getPassword() {
        return password;
    }

    public List<Category> getCategories() {

        return categories;
    }


    public String getUserNameProperty() {
        return userNameProperty.get();
    }

    public SimpleStringProperty userNamePropertyProperty() {
        return userNameProperty;
    }

    public String getPasswordProperty() {
        return passwordProperty.get();
    }

    public SimpleStringProperty passwordPropertyProperty() {
        return passwordProperty;
    }

    public String getDomainProperty() {
        return domainProperty.get();
    }

    public SimpleStringProperty domainPropertyProperty() {
        return domainProperty;
    }

    public String getDescriptionProperty() {
        return descriptionProperty.get();
    }

    public SimpleStringProperty descriptionPropertyProperty() {
        return descriptionProperty;
    }

    public Category getSelectedCat() {
        return selectedCat.get();
    }

    public SimpleObjectProperty<Category> selectedCatProperty() {
        return selectedCat;
    }

    public LocalDate getExpiresOn() {
        return expiresOn.get();
    }

    public SimpleObjectProperty<LocalDate> expiresOnProperty() {
        return expiresOn;
    }


    public String getTitleProperty() {
        return titleProperty.get();
    }

    public SimpleStringProperty titlePropertyProperty() {
        return titleProperty;
    }
}
