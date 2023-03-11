package com.bitvault.ui.viewmodel;

import com.bitvault.services.factory.LocalServiceFactory;
import com.bitvault.security.UserSession;
import com.bitvault.ui.components.validation.ValidateForm;
import com.bitvault.ui.model.User;
import com.bitvault.ui.views.factory.ViewFactory;
import com.bitvault.util.Result;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;

import java.util.UUID;

public class NewAccountVM {

    private final SimpleBooleanProperty offline = new SimpleBooleanProperty(true);
    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();
    private final SimpleStringProperty fileName = new SimpleStringProperty();
    private final SimpleStringProperty location = new SimpleStringProperty();
    private final ValidateForm validatedForm = new ValidateForm();

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

        String location = getLocation() + "/" + getFileName() + ".vault";

        final UserSession userSession = UserSession.newSession(location, getUsername(), getPassword());


        final LocalServiceFactory localServiceFactory = new LocalServiceFactory(location, userSession);

        Result<User> userResult = localServiceFactory.getUserService()
                .register(user);

        if (userResult.isFail()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error?");
            alert.setContentText(userResult.getError().getMessage());
            alert.showAndWait();
            //
        }

        User registeredUser = userResult.get();


        return new ViewFactory(userSession, localServiceFactory);
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

    public ValidateForm getValidatedForm() {
        return validatedForm;
    }
}
