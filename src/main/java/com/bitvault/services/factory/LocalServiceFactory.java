package com.bitvault.services.factory;

import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.database.provider.LocalDB;
import com.bitvault.security.ArgonAuthenticator;
import com.bitvault.security.Authenticator;
import com.bitvault.security.EncryptionProvider;
import com.bitvault.services.interfaces.*;
import com.bitvault.services.local.*;

public class LocalServiceFactory implements ServiceFactory {

    private final IPasswordService passwordService;
    private final ICategoryService categoryService;
    private final IUserService userService;
    private final IProfileService profileService;
    private final ISettingsService settingsService;
    private final ISyncService syncService;

    public LocalServiceFactory(String location, EncryptionProvider encryptionProvider) {
        final ConnectionProvider connectionProvider = new LocalDB(location);
        final Authenticator argonAuthenticator = new ArgonAuthenticator();

        this.categoryService = new CategoryService(connectionProvider);
        this.passwordService = new PasswordService(connectionProvider, encryptionProvider);
        this.userService = new UserService(connectionProvider, argonAuthenticator);
        this.profileService = new ProfileService(connectionProvider);
        this.settingsService = new SettingsService();
        this.syncService = new SyncService(passwordService);
    }


    @Override
    public IPasswordService getPasswordService() {
        return passwordService;
    }

    @Override
    public ICategoryService getCategoryService() {
        return categoryService;
    }

    @Override
    public IUserService getUserService() {
        return userService;
    }

    @Override
    public IProfileService getProfileService() {
        return profileService;
    }

    @Override
    public ISettingsService getSettingsService() {
        return this.settingsService;
    }

    @Override
    public ISyncService getSyncService() {
        return syncService;
    }
}
