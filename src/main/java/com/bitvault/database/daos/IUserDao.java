package com.bitvault.database.daos;

import com.bitvault.database.models.UserDM;

public interface IUserDao {

    UserDM get();

    void create(UserDM user);


}
