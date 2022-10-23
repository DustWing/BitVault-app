package com.bitvault.services.local;

import com.bitvault.algos.ArgonEncoder;
import com.bitvault.database.creator.DbCreator;
import com.bitvault.database.creator.IDbCreator;
import com.bitvault.database.daos.*;
import com.bitvault.database.models.CategoryDM;
import com.bitvault.database.models.ProfileDM;
import com.bitvault.database.models.UserDM;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.ui.model.User;
import com.bitvault.services.interfaces.IUserService;
import com.bitvault.ui.utils.BvColors;
import com.bitvault.util.DateTimeUtils;
import com.bitvault.util.Result;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserService implements IUserService {

    private final ArgonEncoder argonEncoder;
    private final ConnectionProvider connectionProvider;

    public UserService(final ConnectionProvider connectionProvider, final ArgonEncoder argonEncoder) {
        this.connectionProvider = connectionProvider;
        this.argonEncoder = argonEncoder;
    }

    @Override
    public Result<User> register(User user) {

        final String newCred = argonEncoder.hash((user.name() + user.credentials()).toCharArray());

        final IDbCreator dbCreator = new DbCreator(connectionProvider);
        dbCreator.create();

        try (Connection connection = connectionProvider.connect()) {
            final IUserDao userDao = new UserDao(connection);
            userDao.create(
                    UserDM.convert(
                            user.copy(newCred)
                    )
            );

            //create default category - must always have one
            createCategory(connection);

            //create fault profile - must always have one
            createProfile(connection);

        } catch (SQLException e) {
            return Result.exception(e);
        }

        return Result.value(user);

    }

    private void createCategory(final Connection connection) {
        //create default category - must always have one
        final ICategoryDao categoryDao = new CategoryDao(connection);
        final CategoryDM defaultCat = new CategoryDM(
                UUID.randomUUID().toString(),
                "Default",
                BvColors.toHex(Color.AQUA),
                DateTimeUtils.format(LocalDateTime.now()),
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
                DateTimeUtils.format(LocalDateTime.now()),
                null
        );
        profileDao.create(defaultProfile);
    }

    @Override
    public Result<User> authenticate(User user) {

        final UserDM dbUser;
        try (Connection connection = connectionProvider.connect()) {
            final IUserDao userDao = new UserDao(connection);
            dbUser = userDao.get();
        } catch (SQLException e) {
            return Result.exception(e);
        }

        boolean verify = argonEncoder.verify(dbUser.credentials(), (user.name() + user.credentials()).toCharArray());
        if (!verify) {
            return Result.exception(
                    new RuntimeException("Invalid user")
            );
        }
        return Result.value(
                UserDM.convert(dbUser)
        );
    }
}
