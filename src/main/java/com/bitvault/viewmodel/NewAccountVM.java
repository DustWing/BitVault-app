package com.bitvault.viewmodel;

import com.bitvault.components.ValidatedForm;
import com.bitvault.model.User;
import com.bitvault.services.factory.LocalServiceFactory;
import com.bitvault.views.factory.ViewFactory;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.UUID;

public class NewAccountVM {

    private final SimpleBooleanProperty offline = new SimpleBooleanProperty(true);
    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();
    private final SimpleStringProperty fileName = new SimpleStringProperty();
    private final SimpleStringProperty location = new SimpleStringProperty();
    private final ValidatedForm validatedForm = new ValidatedForm();

    public NewAccountVM() {
    }

    public boolean validate() {

        return validatedForm.validate();
    }

    public ViewFactory create() {

        User user = new User(
                UUID.randomUUID().toString(),
                getUsername(),
                getPassword()
        );

        final LocalServiceFactory localServiceFactory = new LocalServiceFactory(getLocation() + "/" + getFileName() + ".vault");
        localServiceFactory.getUserService().register(user).apply(
                user1 -> {
                },
                exception -> {
                }

        );

        return new ViewFactory(localServiceFactory);
    }


    public String getFileName() {
        return fileName.get();
    }

    public SimpleStringProperty fileNameProperty() {
        return fileName;
    }

    public boolean isOffline() {
        return offline.get();
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

    public ValidatedForm getValidatedForm() {
        return validatedForm;
    }
}
