package com.bitvault.database.daos;

import com.bitvault.database.models.UserDM;
import com.bitvault.model.User;

public interface IUserDao {

    UserDM get();

    void create(UserDM user);


}
