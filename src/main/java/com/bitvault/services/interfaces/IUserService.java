package com.bitvault.services.interfaces;

import com.bitvault.ui.model.User;
import com.bitvault.util.Result;

public interface IUserService {

    Result<User> register(User user);

    Result<User> authenticate(User user);
}
