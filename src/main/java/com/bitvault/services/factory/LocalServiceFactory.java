package com.bitvault.services.factory;

import com.bitvault.algos.ArgonEncoder;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.database.provider.LocalDB;
import com.bitvault.security.EncryptionProvider;
import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.services.interfaces.IPasswordService;
import com.bitvault.services.interfaces.IProfileService;
import com.bitvault.services.interfaces.IUserService;
import com.bitvault.services.local.CategoryService;
import com.bitvault.services.local.PasswordService;
import com.bitvault.services.local.ProfileService;
import com.bitvault.services.local.UserService;

public class LocalServiceFactory implements IServiceFactory {

    private final EncryptionProvider encryptionProvider;
    private final IPasswordService passwordService;
    private final ICategoryService categoryService;
    private final IUserService userService;
    private final IProfileService profileService;

    public LocalServiceFactory(String location, EncryptionProvider encryptionProvider) {
        this.encryptionProvider = encryptionProvider;
        final ConnectionProvider connectionProvider = new LocalDB(location);

        final ArgonEncoder argonEncoder = new ArgonEncoder();

        this.categoryService = new CategoryService(connectionProvider);
        this.passwordService = new PasswordService(connectionProvider, encryptionProvider);
        this.userService = new UserService(connectionProvider, argonEncoder);
        this.profileService = new ProfileService(connectionProvider);
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


}
