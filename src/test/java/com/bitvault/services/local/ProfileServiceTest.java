package com.bitvault.services.local;

import com.bitvault.consts.Consts;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.database.provider.LocalDB;
import com.bitvault.services.interfaces.IProfileService;
import com.bitvault.ui.model.Profile;
import com.bitvault.util.Result;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Order(2)
class ProfileServiceTest {

    static IProfileService profileService;


    @BeforeAll
    static void init() {
        final String location = Consts.location;

        if (location.isBlank()) {
            throw new RuntimeException("Set up location for test file");
        }

        final ConnectionProvider connectionProvider = new LocalDB(location);

        profileService = new ProfileService(connectionProvider);
    }

    @Test
    void create() {

        Profile profile = Profile.create(
                "Profile",
                LocalDateTime.now(),
                null
        );

        Result<Profile> profileResult = profileService.create(profile);

        if (profileResult.hasError()) {
            fail(profileResult.getError());
        }

        Profile profile1 = profileResult.get();

        assertEquals(profile.name(), profile1.name());

    }

    @Test
    void getProfiles() {

        Result<List<Profile>> profilesResult = profileService.getProfiles();

        if (profilesResult.hasError()) {
            fail(profilesResult.getError());
        }

    }

    @Test
    void update() {

        Result<List<Profile>> profilesResult = profileService.getProfiles();
        if (profilesResult.hasError()) {
            fail(profilesResult.getError());
        }

        List<Profile> profiles = profilesResult.get();
        assertFalse(profiles.isEmpty());

        final Profile profile1 = profiles.get(0);

        Result<Boolean> updateResult = profileService.update(
                new Profile(
                        profile1.id(),
                        "updatedName",
                        profile1.createdOn(),
                        LocalDateTime.now()
                )
        );

        if (updateResult.hasError()) {
            fail(updateResult.getError());
        }
    }

    @Test
    void delete() {

        Result<List<Profile>> profilesResult = profileService.getProfiles();

        if (profilesResult.hasError()) {
            fail(profilesResult.getError());
        }
        List<Profile> profiles = profilesResult.get();

        assertFalse(profiles.isEmpty());

        final Profile profile1 = profiles.get(0);

        Result<Boolean> deleteRes = profileService.delete(profile1);
        if (deleteRes.hasError()) {
            fail(deleteRes.getError());
        }
    }
}