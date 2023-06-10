package com.bitvault.services.factory;

import com.bitvault.security.EncryptionProvider;
import com.bitvault.services.cached.PasswordServiceCached;
import com.bitvault.services.interfaces.*;
import com.bitvault.services.local.SettingsService;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.SecureDetails;
import com.bitvault.util.Result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TestServiceFactory implements ServiceFactory {

    private final IPasswordService passwordService;

    private final ISettingsService settingsService;


    public TestServiceFactory(EncryptionProvider encryptionProvider) {

        IPasswordService passwordService = () -> Result.ok(List.of(createPassword(encryptionProvider)));

        this.passwordService = new PasswordServiceCached(passwordService);
        this.settingsService = new SettingsService();

    }

    private Password createPassword(EncryptionProvider encryptionProvider) {
        String id = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        Category category = new Category(
                id,
                "Default",
                "#FFFFFF",
                LocalDateTime.now(),
                null,
                "Password",
                false
        );

        SecureDetails secureDetails = new SecureDetails(
                id,
                category,
                null,
                "www.domain.com",
                "title",
                "description",
                false,
                now,
                null,
                null,
                null,
                false,
                false
        );

        String myPassword1 = encryptionProvider.encrypt("MyPassword1");

        Password password = new Password(
                id,
                "MyUserName",
                myPassword1,
                secureDetails
        );

        return password;
    }


    @Override
    public IPasswordService getPasswordService() {
        return passwordService;
    }

    @Override
    public ICategoryService getCategoryService() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public IUserService getUserService() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public IProfileService getProfileService() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public ISettingsService getSettingsService() {
        return this.settingsService;
    }

    @Override
    public ISyncService getSyncService() {
        throw new IllegalStateException("Not implemented");
    }

}
