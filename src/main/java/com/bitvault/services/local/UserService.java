package com.bitvault.services.local;

import com.bitvault.database.creator.DbCreator;
import com.bitvault.database.creator.IDbCreator;
import com.bitvault.database.daos.*;
import com.bitvault.database.models.CategoryDM;
import com.bitvault.database.models.ProfileDM;
import com.bitvault.database.models.UserDM;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.security.Authenticator;
import com.bitvault.services.interfaces.IUserService;
import com.bitvault.ui.model.User;
import com.bitvault.ui.utils.BvColors;
import com.bitvault.util.DateTimeUtils;
import com.bitvault.util.Result;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserService implements IUserService {

    private final Authenticator authenticator;
    private final ConnectionProvider connectionProvider;

    public UserService(final ConnectionProvider connectionProvider, final Authenticator authenticator) {
        this.connectionProvider = connectionProvider;
        this.authenticator = authenticator;
    }

    @Override
    public Result<User> register(User user) {

        final String newCred = authenticator.hash((user.name() + user.credentials()).toCharArray());

        final IDbCreator dbCreator = new DbCreator(connectionProvider);
        dbCreator.create();

        try (Connection connection = connectionProvider.connect()) {
            final UserDM convert = UserDM.convert(user.withNewCredentials(newCred));

            final IUserDao userDao = new UserDao(connection);
            userDao.create(convert);

            //create default category - must always have one
            createCategory(connection);

            //create fault profile - must always have one
            createProfile(connection);

        } catch (SQLException e) {
            return Result.error(e);
        }

        return Result.ok(user);

    }

    private void createCategory(final Connection connection) {
        //create default category - must always have one
        final ICategoryDao categoryDao = new CategoryDao(connection);
        final CategoryDM defaultCat = new CategoryDM(
                UUID.randomUUID().toString(),
                "Default",
                BvColors.toHex(Color.AQUA),
                DateTimeUtils.formatToUtc(LocalDateTime.now()),
                null,
                "Password"
        );
        categoryDao.create(defaultCat);
    }

    private void createProfile(final Connection connection) {
        final IProfileDao profileDao = new ProfileDao(connection);
        final ProfileDM defaultProfile = new ProfileDM(
                UUID.randomUUID().toString(),
                "Default",
                DateTimeUtils.formatToUtc(LocalDateTime.now()),
                null
        );
        profileDao.create(defaultProfile);
    }

    @Override
    public Result<User> authenticate(String username, String password) {

        final UserDM dbUser;
        try (Connection connection = connectionProvider.connect()) {
            final IUserDao userDao = new UserDao(connection);
            dbUser = userDao.get();
        } catch (Exception e) {
            return Result.error(e);
        }

        boolean verify = authenticator.verify(dbUser.credentials(), (username + password).toCharArray());
        if (!verify) {
            return Result.error(new RuntimeException("Invalid user"));
        }
        return Result.ok(UserDM.convert(dbUser));
    }
}
