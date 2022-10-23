package com.bitvault.services.local;

import com.bitvault.consts.Consts;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.database.provider.LocalDB;
import com.bitvault.enums.Action;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.Profile;
import com.bitvault.ui.model.SecureDetails;
import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.services.interfaces.IPasswordService;
import com.bitvault.services.interfaces.IProfileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PasswordServiceTest {

    static IPasswordService passwordService;
    static ICategoryService categoryService;
    static IProfileService profileService;


    @BeforeAll
    static void init() {
        final String location = Consts.location;

        if (location.isBlank()) {
            throw new RuntimeException("Set up location for test file");
        }

        final ConnectionProvider connectionProvider = new LocalDB(location);
        passwordService = new PasswordService(connectionProvider);
        categoryService = new CategoryService(connectionProvider);
        profileService = new ProfileService(connectionProvider);

    }

    @Test
    void create() {

        final List<Category> categoryList = new ArrayList<>();
        categoryService.getCategories().apply(
                categoryList::addAll,
                Assertions::fail
        );

        final List<Profile> profileList = new ArrayList<>();
        profileService.getProfiles()
                .apply(
                        profileList::addAll,
                        Assertions::fail

                );

        final SecureDetails secureDetails = new SecureDetails(
                null,
                categoryList.get(0),
                profileList.get(0),
                "Domain.com",
                "title",
                "description",
                false,
                LocalDateTime.now(),
                null,
                null,
                null,
                false,
                false
        );

        final Password password = new Password(
                null,
                "username",
                "password",
                secureDetails,
                Action.NEW
        );

        passwordService.create(
                password
        ).apply(
                password1 -> assertEquals(password1.password(), password.password()),
                Assertions::fail
        );

    }

    @Test
    void getPasswords() {

        passwordService.getPasswords()
                .apply(System.out::println, Assertions::fail);

    }

    @Test
    void update() {

        passwordService.getPasswords()
                .apply(
                        passwordList -> {
                            if (passwordList.isEmpty()) {
                                return;
                            }
                            Password password = passwordList.get(0);

                            final SecureDetails secureDetails = new SecureDetails(
                                    password.secureDetails().id(),
                                    password.secureDetails().category(),
                                    password.secureDetails().profile(),
                                    "DomainUpdated.com",
                                    "titleUpdated",
                                    "descriptionUpdated",
                                    true,
                                    password.secureDetails().createdOn(),
                                    LocalDateTime.now(),
                                    LocalDateTime.now().plusYears(10),
                                    null,
                                    false,
                                    false
                            );

                            final Password passwordUpdated = new Password(
                                    password.id(),
                                    password.username() + "updated",
                                    password.password() + "updated",
                                    secureDetails,
                                    password.action()
                            );

                            passwordService.update(passwordUpdated).
                                    apply(
                                            Assertions::assertTrue,
                                            Assertions::fail
                                    );
                        },
                        Assertions::fail
                );

    }

    @Test
    void delete() {

        passwordService.getPasswords()
                .apply(
                        passwordList -> {
                            if (!passwordList.isEmpty()) {
                                passwordService.delete(passwordList.get(0));
                            }
                        }, Assertions::fail
                );

    }
}