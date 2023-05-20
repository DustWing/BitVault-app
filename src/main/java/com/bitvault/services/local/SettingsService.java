package com.bitvault.services.local;

import com.bitvault.services.interfaces.ISettingsService;
import com.bitvault.ui.model.Settings;
import com.bitvault.util.FileUtils;
import com.bitvault.util.Json;
import com.bitvault.util.Result;

public class SettingsService implements ISettingsService {

    private static final String uri = "settings/bitvault.json";

    @Override
    public Result<Settings> load() {

        final Result<String> stringResult = FileUtils.readFileToString(uri);

        if (stringResult.hasError()) {
            return Result.error(stringResult.getError());
        }

        final String content = stringResult.get();

        final Result<Settings> deserializeResult = Json.deserialize(content, Settings.class);

        if (deserializeResult.hasError()) {
            return Result.error(deserializeResult.getError());
        }

        final Settings settings = deserializeResult.get();

        return Result.ok(settings);
    }

    @Override
    public Result<Boolean> save(Settings settings) {

        final Result<String> serializedResult = Json.serialize(settings);

        if (serializedResult.hasError()) {
            return Result.error(serializedResult.getError());
        }

        final String content = serializedResult.get();

        final Result<Boolean> fileResult = FileUtils.createAndWriteToFile(uri, content);

        return fileResult;
    }
}
