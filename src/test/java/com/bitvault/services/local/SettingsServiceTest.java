package com.bitvault.services.local;

import com.bitvault.services.interfaces.ISettingsService;
import com.bitvault.ui.model.Settings;
import com.bitvault.ui.model.UserNameFile;
import com.bitvault.util.Result;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SettingsServiceTest {

    @Test
    void save() {
        ISettingsService settingsService = new SettingsService();


        UserNameFile lastUserNameFile = new UserNameFile(
                "username", "filepath"
        );

        UserNameFile userNameFile1 = new UserNameFile(
                "username1", "filepath1"
        );
        UserNameFile userNameFile2 = new UserNameFile(
                "username1", "filepath1"
        );

        Settings settings = new Settings(
                lastUserNameFile,
                List.of(userNameFile1, userNameFile2),
                5,
                6
        );

        Result<Boolean> saveResult = settingsService.save(settings);

        if (saveResult.hasError()) {
            fail(saveResult.getError());
        }

        Result<Settings> loadResult = settingsService.load();

        if(loadResult.hasError()){
            fail(loadResult.getError());
        }

        Settings loadedSettings = loadResult.get();

        assertEquals(settings, loadedSettings);

    }
}