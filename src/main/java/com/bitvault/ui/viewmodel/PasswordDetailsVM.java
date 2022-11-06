package com.bitvault.ui.viewmodel;

import com.bitvault.enums.Action;
import com.bitvault.ui.components.ValidatedForm;
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
    private final ValidatedForm validatedForm;
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
            Consumer<Password> onAction,
            ValidatedForm validatedForm
    ) {
        this.password = password;
        this.categories = categories;
        this.profile = profile;
        this.action = action;
        this.onAction = onAction;
        this.validatedForm = validatedForm;

        if (Action.EDIT.equals(action)) {
            editSetUp();
        }
    }

    private void editSetUp() {
        userNameProperty.set(password.username());
        passwordProperty.set(password.password());
        domainProperty.set(password.secureDetails().domain());
        descriptionProperty.set(password.secureDetails().description());
        expiresOn.set(password.secureDetails().expiresOn().toLocalDate());
        selectedCat.set(password.secureDetails().category());
    }


    public boolean save() {
        validatedForm.clear();
        if (!validatedForm.validate()) {
            return false;
        }

        final LocalDateTime now = LocalDateTime.now();

        final String id = Action.EDIT.equals(action)
                ? password.id() : UUID.randomUUID().toString();

        final LocalDateTime modifiedOn = Action.EDIT.equals(action) ? now : null;

        final SecureDetails secureDetails = new SecureDetails(
                id,
                getSelectedCat(),
                profile,
                getDomainProperty(),
                null,
                getDescriptionProperty(),
                false,
                now,
                modifiedOn,
                getExpiresOn().atStartOfDay(),
                null,
                false,
                false
        );

        final Password passResult = new Password(
                id,
                getUserNameProperty(),
                getPasswordProperty(),
                secureDetails,
                action
        );

        onAction.accept(passResult);
        return true;
    }


    public Password getPassword() {
        return password;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public ValidatedForm getValidatedForm() {
        return validatedForm;
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
}
