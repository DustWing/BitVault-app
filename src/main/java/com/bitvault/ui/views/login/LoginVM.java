package com.bitvault.ui.views.login;

import com.bitvault.ui.components.validation.ValidateForm;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class LoginVM {

    private final SimpleBooleanProperty offline = new SimpleBooleanProperty(true);
    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();
    private final SimpleStringProperty location = new SimpleStringProperty();
    private final ValidateForm validateForm = new ValidateForm();

    public LoginVM() {
    }

    public boolean validate() {
        return validateForm.validate();
    }

    public boolean isOffline() {
        return offline.get();
    }

    public ValidateForm getValidatedForm() {
        return validateForm;
    }

    public SimpleBooleanProperty offlineProperty() {
        return offline;
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public String getLocation() {
        return location.get();
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }
}
