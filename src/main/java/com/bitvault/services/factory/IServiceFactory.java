package com.bitvault.services.factory;

import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.services.interfaces.IPasswordService;
import com.bitvault.services.interfaces.IProfileService;
import com.bitvault.services.interfaces.IUserService;

public interface IServiceFactory {
    String getLocation();

    IPasswordService getPasswordService();

    ICategoryService getCategoryService();

    IUserService getUserService();

    IProfileService getProfileService();
}
