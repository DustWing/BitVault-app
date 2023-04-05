package com.bitvault.database.daos;

import com.bitvault.database.models.SecureDetailsDM;

import java.util.List;

public interface ISecureDetailsDao {
    List<SecureDetailsDM> get();

    void create(SecureDetailsDM secureDetails);

    int update(SecureDetailsDM secureDetails);

    int delete(String id);

    int countByCategoryId(String id);
}

