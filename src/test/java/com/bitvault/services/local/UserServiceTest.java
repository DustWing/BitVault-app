package com.bitvault.services.local;

import com.bitvault.algos.ArgonEncoder;
import com.bitvault.consts.Consts;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.database.provider.LocalDB;
import com.bitvault.model.User;
import com.bitvault.services.interfaces.IUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        userService.register(user);
        userService.authenticate(user)
                .apply(
                        user1 -> assertEquals(user.id(), user1.id()),
                        Assertions::fail
                );
    }
}