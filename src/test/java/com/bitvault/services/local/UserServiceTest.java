package com.bitvault.services.local;

import com.bitvault.algos.ArgonEncoder;
import com.bitvault.consts.Consts;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.database.provider.LocalDB;
import com.bitvault.services.interfaces.IUserService;
import com.bitvault.ui.model.User;
import com.bitvault.util.Result;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class UserServiceTest {

    private static IUserService userService;

    @BeforeAll
    static void init() {

        final String location = Consts.location;

        if (location.isBlank()) {
            throw new RuntimeException("Set up location for test file");
        }

        final ConnectionProvider connectionProvider = new LocalDB(location);
        final ArgonEncoder argonEncoder = new ArgonEncoder();
        userService = new UserService(connectionProvider, argonEncoder);

    }

    @Test
    void register() {

        final User user = new User("id1", "name1", "credentials1");
        Result<User> registerResult = userService.register(user);

        if (registerResult.isFail()) {
            fail(registerResult.getError());
        }

        Result<User> authenticateRes = userService.authenticate(user);

        if (authenticateRes.isFail()) {
            fail(authenticateRes.getError());
        }

        User user1 = authenticateRes.get();

        assertEquals(user.id(), user1.id());

    }
}