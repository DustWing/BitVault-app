package com.bitvault.ui.views.newaccount;

import com.bitvault.security.AesEncryptionProvider;
import com.bitvault.security.EncryptionProvider;
import com.bitvault.security.UserSession;
import com.bitvault.services.factory.LocalServiceFactory;
import com.bitvault.services.factory.ServiceFactory;
import com.bitvault.ui.components.validation.ValidateForm;
import com.bitvault.ui.components.validation.ValidateResult;
import com.bitvault.ui.model.Settings;
import com.bitvault.ui.model.User;
import com.bitvault.ui.model.UserNameFile;
import com.bitvault.util.Result;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NewAccountVM {


    private static final Logger logger = LoggerFactory.getLogger(NewAccountVM.class);

    private final SimpleBooleanProperty offline = new SimpleBooleanProperty(true);
    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();
    private final SimpleStringProperty fileName = new SimpleStringProperty();
    private final SimpleStringProperty location = new SimpleStringProperty();
    private final ValidateForm validatedForm = new ValidateForm();

    public NewAccountVM() {
    }

    public Result<UserSession> create() {

        ValidateResult validateResult = validatedForm.validate();

        if (!validateResult.valid()) {
            return Result.error(new Exception(validateResult.errorMessages().toString()));
        }

        final String location = getFullFilePath();
        final String username = this.username.get();
        final String password = this.password.get();

        final EncryptionProvider encryptionProvider = new AesEncryptionProvider(password.toCharArray());

        final ServiceFactory serviceFactory = new LocalServiceFactory(location, encryptionProvider);

        final User user = new User(
                UUID.randomUUID().toString(),
                getUsername(),
                getPassword()
        );

        final Result<User> userResult = serviceFactory.getUserService()
                .register(user);

        if (userResult.hasError()) {
            return Result.error(userResult.getError());
        }

        final UserSession userSession = new UserSession(username, encryptionProvider, serviceFactory);
        final Settings settings = createSettings(userSession);
        userSession.setSettings(settings);

        return Result.ok(userSession);
    }

    private String getFullFilePath() {
        String separator = FileSystems.getDefault().getSeparator();
        return this.location.get() + separator + getFileName() + ".vault";
    }

    private Settings createSettings(UserSession userSession) {
        final String location = getFullFilePath();
        final Settings settings;
        final UserNameFile userNameFile = new UserNameFile(this.username.get(), location);

        Result<Settings> load = userSession.getServiceFactory().getSettingsService().load();
        if (load.hasError()) {
            logger.error("[createSettings]", load.getError());
            settings = Settings.createOnLogin(userNameFile);
        } else {
            settings = load.get();
        }

        final List<UserNameFile> userNameFiles = new ArrayList<>(settings.userNameFiles());
        userNameFiles.add(userNameFile);

        final List<UserNameFile> list = userNameFiles.stream().distinct().toList();
        final Settings settingsToSave = settings.copyOnLogin(userNameFile, list);

        final Result<Boolean> save = userSession.getServiceFactory().getSettingsService().save(settingsToSave);

        if (save.hasError()) {
            logger.error("", save.getError());
        }

        return settingsToSave;
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
