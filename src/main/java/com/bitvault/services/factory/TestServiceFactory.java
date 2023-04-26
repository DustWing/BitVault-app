package com.bitvault.services.factory;

import com.bitvault.security.EncryptionProvider;
import com.bitvault.services.cached.PasswordServiceCached;
import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.services.interfaces.IPasswordService;
import com.bitvault.services.interfaces.IProfileService;
import com.bitvault.services.interfaces.IUserService;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.SecureDetails;
import com.bitvault.util.Result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TestServiceFactory implements ServiceFactory {

    private final IPasswordService passwordService;


    public TestServiceFactory(EncryptionProvider encryptionProvider) {

        IPasswordService passwordService = () -> Result.ok(List.of(createPassword(encryptionProvider)));

        IPasswordService passwordServiceCached = new PasswordServiceCached(passwordService);

        this.passwordService = passwordServiceCached;

    }

    private Password createPassword(EncryptionProvider encryptionProvider){
        String id = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        Category category = new Category(
                id,
                "Default",
                "#FFFFFF",
                LocalDateTime.now(),
                null,
                "PASSWORD"
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


}
