package com.bitvault.ui.views.login;

import com.bitvault.security.AesEncryptionProvider;
import com.bitvault.security.EncryptionProvider;
import com.bitvault.security.UserSession;
import com.bitvault.services.factory.ServiceFactory;
import com.bitvault.services.factory.LocalServiceFactory;
import com.bitvault.ui.components.validation.ValidateForm;
import com.bitvault.ui.model.User;
import com.bitvault.util.Result;
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

    public Result<UserSession> login() {

        boolean formValidate = validateForm.validate();

        if (!formValidate) {
            return Result.error(new Exception(""));
        }

        final String location = this.location.get();
        final String username = this.username.get();
        final String password = this.password.get();

        final EncryptionProvider encryptionProvider = new AesEncryptionProvider(password.toCharArray());

        final ServiceFactory serviceFactory = new LocalServiceFactory(location, encryptionProvider);

        final Result<User> authResult = serviceFactory.getUserService()
                .authenticate(username, password);

        if (authResult.isFail()) {
            return Result.error(authResult.getError());
        }

        final UserSession userSession = new UserSession(username, encryptionProvider, serviceFactory);

        return Result.ok(userSession);
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
