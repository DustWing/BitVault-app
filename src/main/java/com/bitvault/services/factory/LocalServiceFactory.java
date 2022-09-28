package com.bitvault.services.factory;

import com.bitvault.algos.ArgonEncoder;
import com.bitvault.database.creator.DbCreator;
import com.bitvault.database.creator.IDbCreator;
import com.bitvault.database.daos.IUserDao;
import com.bitvault.database.daos.UserDao;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.database.provider.LocalDB;
import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.services.interfaces.IPasswordService;
import com.bitvault.services.interfaces.IProfileService;
import com.bitvault.services.interfaces.IUserService;
import com.bitvault.services.local.CategoryService;
import com.bitvault.services.local.PasswordService;
import com.bitvault.services.local.ProfileService;
import com.bitvault.services.local.UserService;

public class LocalServiceFactory implements IServiceFactory {

    private final String location;
    private final IPasswordService passwordService;
    private final ICategoryService categoryService;
    private final IUserService userService;
    private final IProfileService profileService;

    public LocalServiceFactory(String location) {
        this.location = location;
        final ConnectionProvider connectionProvider = new LocalDB(location);

        final ArgonEncoder argonEncoder = new ArgonEncoder();

        this.categoryService = new CategoryService(connectionProvider);
        this.passwordService = new PasswordService(connectionProvider);
        this.userService = new UserService(connectionProvider, argonEncoder);
        this.profileService = new ProfileService(connectionProvider);
    }

    @Override
    public String getLocation() {
        return location;
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
