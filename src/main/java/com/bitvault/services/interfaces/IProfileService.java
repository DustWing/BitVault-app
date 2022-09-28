package com.bitvault.services.interfaces;

import com.bitvault.model.Profile;
import com.bitvault.util.Result;

import java.util.List;

public interface IProfileService {
    Result<List<Profile>> getProfiles();

    Result<Profile> create(final Profile profile);

    Result<Boolean> update(final Profile profile);

    Result<Boolean> delete(final Profile profile);
}
