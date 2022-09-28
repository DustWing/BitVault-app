package com.bitvault.viewmodel;

import com.bitvault.components.ValidatedForm;
import com.bitvault.model.User;
import com.bitvault.services.factory.IServiceFactory;
import com.bitvault.services.factory.LocalServiceFactory;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class LoginVM {

    private final SimpleBooleanProperty offline = new SimpleBooleanProperty(true);
    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();
    private final SimpleStringProperty location = new SimpleStringProperty();
    private final ValidatedForm validatedForm = new ValidatedForm();

    public LoginVM() {
    }

    public boolean login() {


        return validatedForm.validate();
    }

    public boolean isOffline() {
        return offline.get();
    }

    public ValidatedForm getValidatedForm() {
        return validatedForm;
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
