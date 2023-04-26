package com.bitvault.services.local;

import com.bitvault.consts.Consts;
import com.bitvault.security.AesEncryptionProvider;
import com.bitvault.security.EncryptionProvider;
import com.bitvault.security.UserSession;
import com.bitvault.services.factory.LocalServiceFactory;
import com.bitvault.services.factory.ServiceFactory;
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

    static UserSession userSession;

    @BeforeAll
    static void init() {
        final String location = Consts.location;

        if (location.isBlank()) {
            throw new RuntimeException("Set up location for test file");
        }

        EncryptionProvider encryptionProvider= new AesEncryptionProvider("password".toCharArray());

        final ServiceFactory serviceFactory = new LocalServiceFactory(location, encryptionProvider);

        userSession = new UserSession("username", encryptionProvider, serviceFactory);

    }

    @Test
    void create() {

        Result<List<Category>> resultCat = userSession.getServiceFactory().getCategoryService().getCategories();

        if (resultCat.isFail()) {
            fail(resultCat.getError());
            return;
        }

        final List<Category> categoryList = resultCat.get();


        Result<List<Profile>> profilesResult = userSession.getServiceFactory().getProfileService().getProfiles();

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
                secureDetails
        );

        Result<Password> passwordResult = userSession.getServiceFactory().getPasswordService().create(password);
        if (passwordResult.isFail()) {
            fail(resultCat.getError());
            return;
        }

        Password password1 = passwordResult.get();

        String decryptNewPass = userSession.getEncryptionProvider().decrypt(password1.getPassword());

        assertEquals(decryptNewPass, password.getPassword());

    }

    @Test
    void getPasswords() {

        Result<List<Password>> passwordsResult = userSession.getServiceFactory().getPasswordService().getPasswords();
        if (passwordsResult.isFail()) {
            fail(passwordsResult.getError());
            return;
        }

        System.out.println(passwordsResult.get());


    }

    @Test
    void update() {

        Result<List<Password>> passwordsResults = userSession.getServiceFactory().getPasswordService().getPasswords();

        List<Password> passwordList = passwordsResults.get();

        if (passwordList.isEmpty()) {
            System.out.println("No passwords found");
            return;
        }

        Password password = passwordList.get(0);

        final SecureDetails secureDetails = new SecureDetails(
                password.getSecureDetails().getId(),
                password.getSecureDetails().getCategory(),
                password.getSecureDetails().getProfile(),
                "DomainUpdated.com",
                "titleUpdated",
                "descriptionUpdated",
                true,
                password.getSecureDetails().getCreatedOn(),
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(10),
                null,
                false,
                false
        );

        final Password passwordUpdated = new Password(
                password.getId(),
                password.getUsername() + "updated",
                password.getPassword() + "updated",
                secureDetails
        );

        Result<Password> updateResult = userSession.getServiceFactory().getPasswordService().update(passwordUpdated);

        if (updateResult.isFail()) {
            fail(updateResult.getError());
        }


    }

    @Test
    void delete() {

        Result<List<Password>> passwordsResults = userSession.getServiceFactory().getPasswordService().getPasswords();

        List<Password> passwordList = passwordsResults.get();

        if (passwordList.isEmpty()) {
            System.out.println("No passwords found");
            return;
        }

        Result<Boolean> deleteResult = userSession.getServiceFactory().getPasswordService().delete(passwordList.get(0));

        if (deleteResult.isFail()) {
            fail(deleteResult.getError());
        }
    }
}