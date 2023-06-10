package com.bitvault.services.interfaces;

import com.bitvault.ui.model.Password;
import com.bitvault.util.Result;

import java.util.List;

public interface ISyncService {

    List<Result<Password>> savePasswords(List<Password> passwords);

}
