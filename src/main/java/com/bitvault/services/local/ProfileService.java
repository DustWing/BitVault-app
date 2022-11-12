package com.bitvault.services.local;

import com.bitvault.database.daos.IProfileDao;
import com.bitvault.database.daos.ProfileDao;
import com.bitvault.database.models.ProfileDM;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.services.interfaces.IProfileService;
import com.bitvault.ui.model.Profile;
import com.bitvault.util.Result;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ProfileService implements IProfileService {

    private final ConnectionProvider connectionProvider;

    public ProfileService(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }


    @Override
    public Result<List<Profile>> getProfiles() {
        try (Connection connection = connectionProvider.connect()) {

            final List<ProfileDM> profiles = new ProfileDao(connection).get();

            return Result.ok(
                    profiles.stream()
                            .map(
                                    ProfileDM::convert
                            )
                            .toList()
            );
        } catch (SQLException e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<Profile> create(Profile profile) {
        try (Connection connection = connectionProvider.connect()) {

            final IProfileDao profileDao = new ProfileDao(connection);

            final String id = UUID.randomUUID().toString();

            final ProfileDM profileDM = ProfileDM.createNew(id, profile);

            //save
            profileDao.create(profileDM);

            final Profile profileResult = ProfileDM.convert(profileDM);
            return Result.ok(profileResult);

        } catch (SQLException e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<Boolean> update(Profile profile) {
        try (Connection connection = connectionProvider.connect()) {

            final IProfileDao profileDao = new ProfileDao(connection);

            final ProfileDM profileDM = profileDao.get(profile.id());

            if (profileDM == null) {
                return Result.error(new Exception("No Profile found"));
            }

            profileDao.update(
                    ProfileDM.convert(profile)
            );

            return Result.Success;

        } catch (SQLException e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<Boolean> delete(Profile profile) {
        try (Connection connection = connectionProvider.connect()) {

            final IProfileDao profileDao = new ProfileDao(connection);

            final ProfileDM profileDM = profileDao.get(profile.id());

            if (profileDM == null) {
                return Result.error(new Exception("No Profile found"));
            }

            profileDao.delete(profile.id());

            return Result.Success;

        } catch (SQLException e) {
            return Result.error(e);
        }
    }
}
