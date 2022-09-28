package com.bitvault.database.daos;

import com.bitvault.database.models.ProfileDM;

import java.util.List;

public interface IProfileDao {

    List<ProfileDM> get();

    ProfileDM get(String id);

    void create(ProfileDM profile);

    void update(ProfileDM profile);

    void delete(String id);
}
