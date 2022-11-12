package com.bitvault.services.local;

import com.bitvault.database.daos.*;
import com.bitvault.database.models.CategoryDM;
import com.bitvault.database.models.PasswordDM;
import com.bitvault.database.models.ProfileDM;
import com.bitvault.database.models.SecureDetailsDM;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.enums.Action;
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

    public PasswordService(final ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
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

            return Result.ok(
                    convert(passwords, categories, secureDetailsList, profileList)
            );
        } catch (SQLException e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<Password> create(Password password) {

        try (Connection connection = connectionProvider.connect()) {

            connection.setAutoCommit(false);

            try {

                final ProfileDM profileDM = new ProfileDao(connection)
                        .get(
                                password.secureDetails().profile().id()
                        );
                if (profileDM == null) {
                    //Applicable only if file was edited by hand or error in code
                    throw new RuntimeException("No Profile found");
                }

                final CategoryDM categoryDM = new CategoryDao(connection)
                        .get(
                                password.secureDetails().category().id()
                        );
                if (categoryDM == null) {
                    //Applicable only if file was edited by hand or error in code
                    throw new RuntimeException("No category found");
                }

                final String id = UUID.randomUUID().toString();

                final SecureDetailsDM secureDetailsDM = SecureDetailsDM.createNew(id, password.secureDetails());
                final ISecureDetailsDao secureDetailsDao = new SecureDetailsDao(connection);
                secureDetailsDao.create(secureDetailsDM);

                final PasswordDM passwordDM = PasswordDM.createNew(id, password);
                final IPasswordDao passwordDao = new PasswordDao(connection);
                passwordDao.create(passwordDM);

                connection.commit();

                final SecureDetails secureDetails = SecureDetailsDM.convert(
                        secureDetailsDM,
                        password.secureDetails().category(),
                        password.secureDetails().profile()
                );

                return Result.ok(
                        PasswordDM.convert(
                                passwordDM,
                                secureDetails,
                                Action.DEFAULT
                        )
                );

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            return Result.error(e);
        }

    }

    @Override
    public Result<Boolean> update(Password password) {

        try (Connection connection = connectionProvider.connect()) {
            connection.setAutoCommit(false);

            try {

                final SecureDetailsDM secureDetailsDM = SecureDetailsDM.convert(password.secureDetails());

                final ISecureDetailsDao secureDetailsDao = new SecureDetailsDao(connection);
                secureDetailsDao.update(secureDetailsDM);

                final PasswordDM passwordDM = PasswordDM.convert(password);

                final IPasswordDao passwordDao = new PasswordDao(connection);
                passwordDao.update(passwordDM);

                connection.commit();

                return Result.Success;

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
            secureDetailsDao.delete(password.secureDetails().id());

            final IPasswordDao passwordDao = new PasswordDao(connection);
            passwordDao.delete(password.id());

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
                .collect(
                        Collectors.toMap(
                                SecureDetailsDM::id, Function.identity()
                        )
                );

        final Map<String, CategoryDM> catMap = categories
                .stream()
                .collect(
                        Collectors.toMap(
                                CategoryDM::id, Function.identity()
                        )
                );

        final Map<String, ProfileDM> profileMap = profileList
                .stream()
                .collect(
                        Collectors.toMap(
                                ProfileDM::id, Function.identity()
                        )
                );


        return passwords.stream().map(
                passwordDM -> {

                    final SecureDetailsDM details = detailsMap.get(passwordDM.secureDetailsId());

                    final Category category = CategoryDM.convert(catMap.get(details.categoryId()));

                    final Profile profile = ProfileDM.convert(profileMap.get(details.profileId()));

                    final SecureDetails secureDetails = SecureDetailsDM.convert(details, category, profile);

                    return PasswordDM.convert(passwordDM, secureDetails, Action.DEFAULT);
                }
        ).toList();

    }
}
