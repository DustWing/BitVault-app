package com.bitvault.services.factory;

import com.bitvault.services.interfaces.*;

public interface ServiceFactory {

    IPasswordService getPasswordService();

    ICategoryService getCategoryService();

    IUserService getUserService();

    IProfileService getProfileService();

    ISettingsService getSettingsService();

    ISyncService getSyncService();
}
