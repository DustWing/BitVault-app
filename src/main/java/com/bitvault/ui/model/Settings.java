package com.bitvault.ui.model;

import java.util.List;

public record Settings(
        UserNameFile lastUserNameFile,
        List<UserNameFile> userNameFiles,
        Integer masterPasswordCoolDown,
        Integer passwordGenerateLength

) {

    public static Settings createOnLogin(UserNameFile lastUserNameFile) {
        return new Settings(lastUserNameFile, List.of(lastUserNameFile), 1, 16);
    }

    public Settings copyOnLogin(UserNameFile lastUserNameFile, List<UserNameFile> userNameFiles) {
        return new Settings(
                lastUserNameFile,
                userNameFiles,
                this.masterPasswordCoolDown,
                this.passwordGenerateLength
        );
    }
}
