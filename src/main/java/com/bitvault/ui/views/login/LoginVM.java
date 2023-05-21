package com.bitvault.ui.views.login;

import com.bitvault.security.AesEncryptionProvider;
import com.bitvault.security.EncryptionProvider;
import com.bitvault.security.UserSession;
import com.bitvault.services.factory.LocalServiceFactory;
import com.bitvault.services.factory.ServiceFactory;
import com.bitvault.services.interfaces.ISettingsService;
import com.bitvault.services.local.SettingsService;
import com.bitvault.ui.model.Settings;
import com.bitvault.ui.model.User;
import com.bitvault.ui.model.UserNameFile;
import com.bitvault.util.Result;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoginVM {

    private static final Logger logger = LoggerFactory.getLogger(LoginVM.class);

    private final SimpleBooleanProperty offline = new SimpleBooleanProperty(true);
    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();
    private final SimpleStringProperty location = new SimpleStringProperty();
    private final SimpleBooleanProperty loading = new SimpleBooleanProperty();
    private Settings settings;

    public LoginVM() {

        ISettingsService settingsService = new SettingsService();

        Result<Settings> settingsResult = settingsService.load();

        if (!settingsResult.hasError()) {
            this.settings = settingsResult.get();
            username.set(settings.lastUserNameFile().username());
            location.set(settings.lastUserNameFile().filePath());
        }
    }

    public Result<UserSession> login() {

        loading.set(true);

        final String location = this.location.get();
        final String username = this.username.get();
        final String password = this.password.get();

        final EncryptionProvider encryptionProvider = new AesEncryptionProvider(password.toCharArray());

        final ServiceFactory serviceFactory = new LocalServiceFactory(location, encryptionProvider);

        final Result<User> authResult = serviceFactory.getUserService()
                .authenticate(username, password);

        if (authResult.hasError()) {
            return Result.error(authResult.getError());
        }

        final UserSession userSession = new UserSession(username, encryptionProvider, serviceFactory);

        Settings settings = createSettings();

        Result<Boolean> saveSettings = userSession.getServiceFactory().getSettingsService().save(settings);

        if (saveSettings.hasError()) {
            logger.error("", saveSettings.getError());
        }

        return Result.ok(userSession);
    }

    private Settings createSettings() {

        UserNameFile userNameFile = new UserNameFile(username.get(), location.get());

        if (this.settings == null) {
            return Settings.createOnLogin(userNameFile);
        }

        List<UserNameFile> userNameFiles = new ArrayList<>(this.settings.userNameFiles());
        userNameFiles.add(userNameFile);
        List<UserNameFile> list = userNameFiles.stream().distinct().toList();
        return this.settings.copyOnLogin(userNameFile, list);
    }

    public void setFilePathFromUsername(String username) {
        if (settings == null) {//do nothing
            return;
        }

        Optional<UserNameFile> any = settings.userNameFiles()
                .stream()
                .filter(userNameFile -> userNameFile.username().equals(username))
                .findAny();

        any.ifPresent(userNameFile -> this.location.setValue(userNameFile.filePath()));
    }

    public List<String> getSettingsUsernames() {
        if (settings == null) {
            return List.of();
        }
        return settings.userNameFiles().stream().map(UserNameFile::username).toList();
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

    public boolean isLoading() {
        return loading.get();
    }

    public SimpleBooleanProperty loadingProperty() {
        return loading;
    }

}
