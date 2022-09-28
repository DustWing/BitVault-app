package com.bitvault.database.daos;

import com.bitvault.database.models.PasswordDM;

import java.util.List;

public interface IPasswordDao {


    List<PasswordDM> get();

    PasswordDM get(String id);

    void create(PasswordDM password);

    int update(PasswordDM password);

    int delete(String id);


}
