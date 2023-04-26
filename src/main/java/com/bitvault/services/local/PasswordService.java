package com.bitvault.services.local;

import com.bitvault.database.daos.*;
import com.bitvault.database.models.CategoryDM;
import com.bitvault.database.models.PasswordDM;
import com.bitvault.database.models.ProfileDM;
import com.bitvault.database.models.SecureDetailsDM;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.security.EncryptionProvider;
import com.bitvault.services.interfaces.IPasswordService;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.Profile;
import com.bitvault.ui.model.SecureDetails;
import com.bitvault.util.Result;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PasswordService implements IPasswordService {

    private final ConnectionProvider connectionProvider;

    private final EncryptionProvider encryptionProvider;

    public PasswordService(final ConnectionProvider connectionProvider, EncryptionProvider encryptionProvider) {
        this.connectionProvider = connectionProvider;
        this.encryptionProvider = encryptionProvider;
    }

    @Override
    public Result<List<Password>> getPasswords() {

        try (Connection connection = connectionProvider.connect()) {

            final IPasswordDao passwordDao = new PasswordDao(connection);
            final List<PasswordDM> passwords = passwordDao.get();

            final ICategoryDao categoryDao = new CategoryDao(connection);
            final List<CategoryDM> categories = categoryDao.get();

            final ISecureDetailsDao secureDetailsDao = new SecureDetailsDao(connection);
            final List<SecureDetailsDM> secureDetailsList = secureDetailsDao.get();

            final IProfileDao profileDao = new ProfileDao(connection);
            final List<ProfileDM> profileList = profileDao.get();

            final List<Password> results = convert(passwords, categories, secureDetailsList, profileList);

            return Result.ok(results);
        } catch (Exception e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<Password> create(Password password) {

        try (Connection connection = connectionProvider.connect()) {

            connection.setAutoCommit(false);

            checkProfile(connection, password.getSecureDetails().getProfile().id());

            checkCategory(connection, password.getSecureDetails().getCategory());

            try {

                final String id = UUID.randomUUID().toString();

                //save details
                final SecureDetailsDM secureDetailsDM = saveSecureDetails(connection, id, password.getSecureDetails());

                //save pass
                final PasswordDM passwordDM = savePasswordDM(connection, id, password);

                connection.commit();

                final SecureDetails secureDetails = SecureDetailsDM.convert(
                        secureDetailsDM,
                        password.getSecureDetails().getCategory(),
                        password.getSecureDetails().getProfile()
                );

                final Password passwordResult = new Password(
                        passwordDM.id(),
                        password.getUsername(),
                        passwordDM.password(),
                        secureDetails);

                return Result.ok(passwordResult);

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            return Result.error(e);
        }

    }

    private void checkProfile(Connection connection, String id) {

        final ProfileDM profileDM = new ProfileDao(connection)
                .get(id);

        if (profileDM == null) {
            //Applicable only if file was edited by hand or error in code
            throw new IllegalArgumentException("No Profile found");
        }
    }

    private void checkCategory(Connection connection, Category category) {
        final CategoryDao categoryDao = new CategoryDao(connection);

        final CategoryDM categoryDM = categoryDao.get(category.id());

        if (categoryDM == null) {
            //Applicable only if file was edited by hand or error in code
            throw new IllegalArgumentException("No category found");
        }

    }

    private SecureDetailsDM saveSecureDetails(Connection connection, String id, SecureDetails secureDetails) {
        final SecureDetailsDM secureDetailsDM = SecureDetailsDM.createNew(id, secureDetails);
        final ISecureDetailsDao secureDetailsDao = new SecureDetailsDao(connection);
        secureDetailsDao.create(secureDetailsDM);

        return secureDetailsDM;
    }

    private PasswordDM savePasswordDM(Connection connection, String id, Password password) {

        final String encryptUsername = encryptionProvider.encrypt(password.getUsername());
        final String encryptPassword = encryptionProvider.encrypt(password.getPassword());

        final PasswordDM passwordDM = new PasswordDM(id, encryptUsername, encryptPassword, id);
        final IPasswordDao passwordDao = new PasswordDao(connection);
        passwordDao.create(passwordDM);
        return passwordDM;
    }

    @Override
    public Result<Password> update(Password password) {

        try (Connection connection = connectionProvider.connect()) {
            connection.setAutoCommit(false);

            checkProfile(connection, password.getSecureDetails().getProfile().id());

            checkCategory(connection, password.getSecureDetails().getCategory());

            try {
                final SecureDetailsDM secureDetailsDM = SecureDetailsDM.convert(password.getSecureDetails());

                final ISecureDetailsDao secureDetailsDao = new SecureDetailsDao(connection);
                secureDetailsDao.update(secureDetailsDM);

                final String encryptUsername = encryptionProvider.encrypt(password.getUsername());
                final String encryptPassword = encryptionProvider.encrypt(password.getPassword());
                final PasswordDM passwordDM = new PasswordDM(password.getId(), encryptUsername, encryptPassword, password.getId());

                final IPasswordDao passwordDao = new PasswordDao(connection);
                passwordDao.update(passwordDM);

                connection.commit();

                return Result.ok(password);

            } catch (SQLException e) {
                connection.rollback();
                return Result.error(e);
            }

        } catch (SQLException e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<Boolean> delete(Password password) {

        try (Connection connection = connectionProvider.connect()) {


            final ISecureDetailsDao secureDetailsDao = new SecureDetailsDao(connection);
            secureDetailsDao.delete(password.getSecureDetails().getId());

            final IPasswordDao passwordDao = new PasswordDao(connection);
            passwordDao.delete(password.getId());

            return Result.Success;

        } catch (SQLException e) {
            return Result.error(e);
        }

    }

    private List<Password> convert(
            final List<PasswordDM> passwords,
            final List<CategoryDM> categories,
            final List<SecureDetailsDM> secureDetailsList,
            final List<ProfileDM> profileList
    ) {

        final Map<String, SecureDetailsDM> detailsMap = secureDetailsList
                .stream()
                .collect(Collectors.toMap(SecureDetailsDM::id, Function.identity()));

        final Map<String, CategoryDM> catMap = categories
                .stream()
                .collect(Collectors.toMap(CategoryDM::id, Function.identity()));

        final Map<String, ProfileDM> profileMap = profileList
                .stream()
                .collect(Collectors.toMap(ProfileDM::id, Function.identity()));


        return passwords.stream()
                .map(passwordDM -> getPassword(detailsMap, catMap, profileMap, passwordDM))
                .toList();
    }

    private Password getPassword(
            Map<String, SecureDetailsDM> detailsMap,
            Map<String, CategoryDM> catMap,
            Map<String, ProfileDM> profileMap,
            PasswordDM passwordDM
    ) {
        final SecureDetailsDM details = detailsMap.get(passwordDM.secureDetailsId());

        final Category category = CategoryDM.convert(catMap.get(details.categoryId()));

        final Profile profile = ProfileDM.convert(profileMap.get(details.profileId()));

        final SecureDetails secureDetails = SecureDetailsDM.convert(details, category, profile);

        final String decryptUserName = encryptionProvider.decrypt(passwordDM.username());

        return new Password(passwordDM.id(), decryptUserName, passwordDM.password(), secureDetails);
    }

}
