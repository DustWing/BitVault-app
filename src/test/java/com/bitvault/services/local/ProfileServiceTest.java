package com.bitvault.services.local;

import com.bitvault.consts.Consts;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.database.provider.LocalDB;
import com.bitvault.ui.model.Profile;
import com.bitvault.services.interfaces.IProfileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

        profileService.create(profile)
                .apply(
                        profile1 -> assertEquals(profile.name(), profile1.name()),
                        Assertions::fail

                );
    }

    @Test
    void getProfiles() {
        profileService.getProfiles()
                .apply(
                        System.out::println,
                        Assertions::fail
                );
    }

    @Test
    void update() {
        profileService.getProfiles()
                .apply(
                        profiles -> {
                            assertFalse(profiles.isEmpty());

                            final Profile profile1 = profiles.get(0);

                            profileService.update(
                                    new Profile(
                                            profile1.id(),
                                            "updatedName",
                                            profile1.createdOn(),
                                            LocalDateTime.now()
                                    )
                            ).apply(
                                    Assertions::assertTrue,
                                    Assertions::fail
                            );

                        },
                        Assertions::fail
                );
    }

    @Test
    void delete() {

        profileService.getProfiles()
                .apply(
                        profiles -> {
                            assertFalse(profiles.isEmpty());

                            final Profile profile1 = profiles.get(0);

                            profileService.delete(
                                    profile1
                            ).apply(
                                    Assertions::assertTrue,
                                    Assertions::fail
                            );

                        },
                        Assertions::fail
                );
    }
}