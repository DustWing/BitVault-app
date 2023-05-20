package com.bitvault.services.interfaces;

import com.bitvault.ui.model.Settings;
import com.bitvault.util.Result;

public interface ISettingsService {


    Result<Settings> load();

    Result<Boolean> save(Settings settings);


}
