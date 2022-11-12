package com.bitvault.services.local;

import com.bitvault.consts.Consts;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.database.provider.LocalDB;
import com.bitvault.enums.Action;
import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.services.interfaces.IPasswordService;
import com.bitvault.services.interfaces.IProfileService;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.Profile;
import com.bitvault.ui.model.SecureDetails;
import com.bitvault.util.Result;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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

        Result<List<Category>> resultCat = categoryService.getCategories();

        if (resultCat.isFail()) {
            fail(resultCat.getError());
            return;
        }

        final List<Category> categoryList = resultCat.get();


        Result<List<Profile>> profilesResult = profileService.getProfiles();

        if (profilesResult.isFail()) {
            fail(resultCat.getError());
            return;
        }

        final List<Profile> profileList = profilesResult.get();


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

        Result<Password> passwordResult = passwordService.create(password);
        if (passwordResult.isFail()) {
            fail(resultCat.getError());
            return;
        }

        Password password1 = passwordResult.get();

        assertEquals(password1.password(), password.password());

    }

    @Test
    void getPasswords() {

        Result<List<Password>> passwordsResult = passwordService.getPasswords();
        if (passwordsResult.isFail()) {
            fail(passwordsResult.getError());
            return;
        }

        System.out.println(passwordsResult.get());


    }

    @Test
    void update() {

        Result<List<Password>> passwordsResults = passwordService.getPasswords();

        List<Password> passwordList = passwordsResults.get();

        if (passwordList.isEmpty()) {
            System.out.println("No passwords found");
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

        Result<Boolean> updateResult = passwordService.update(passwordUpdated);

        if (updateResult.isFail()) {
            fail(updateResult.getError());
        }


    }

    @Test
    void delete() {

        Result<List<Password>> passwordsResults = passwordService.getPasswords();

        List<Password> passwordList = passwordsResults.get();

        if (passwordList.isEmpty()) {
            System.out.println("No passwords found");
            return;
        }

        Result<Boolean> deleteResult = passwordService.delete(passwordList.get(0));

        if (deleteResult.isFail()) {
            fail(deleteResult.getError());
        }
    }
}