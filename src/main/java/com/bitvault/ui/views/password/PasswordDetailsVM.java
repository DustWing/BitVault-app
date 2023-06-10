package com.bitvault.ui.views.password;

import com.bitvault.enums.Action;
import com.bitvault.ui.components.alert.ErrorAlert;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.Profile;
import com.bitvault.ui.model.SecureDetails;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class PasswordDetailsVM {

    private final List<Category> categories;
    private final Consumer<Password> onAction;
    private final Password password;
    private final SimpleObjectProperty<LocalDate> expiresOn = new SimpleObjectProperty<>();


    public PasswordDetailsVM(
            final Password password,
            final List<Category> categories,
            final Profile profile,
            final Action action,
            final Consumer<Password> onAction
    ) {
        this.categories = categories;
        this.onAction = onAction;

        if (Action.EDIT.equals(action)) {
            this.password = password.deepCopy();
        } else {
            this.password = initNewPassword(categories.get(0), profile);
        }
    }

    private Password initNewPassword(Category category, Profile profile) {

        String id = UUID.randomUUID().toString();
        SecureDetails secureDetails = SecureDetails.prepareNew(id, category, profile);

        return new Password(id, "", "", secureDetails);
    }

    public void save() {

        LocalDateTime expiresOn = getExpiresOn() == null ? null : getExpiresOn().atStartOfDay();
        password.getSecureDetails().expiresOnProperty().set(expiresOn);

        onAction.accept(this.password);

    }


    public Password getPassword() {
        return password;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public LocalDate getExpiresOn() {
        return expiresOn.get();
    }

    public SimpleObjectProperty<LocalDate> expiresOnProperty() {
        return expiresOn;
    }
}
