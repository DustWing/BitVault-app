package com.bitvault.ui.model;

import java.util.List;

public record Settings(
        UserNameFile lastUserNameFile,
        List<UserNameFile> userNameFiles,
        Integer masterPasswordCoolDown,
        Integer passwordGenerateLength

) {

    public static int defaultMasterPasswordCoolDown = 5;
    public static int defaultPasswordGenerateLength = 16;


    public static Settings createOnLogin(UserNameFile lastUserNameFile) {
        return new Settings(
                lastUserNameFile,
                List.of(lastUserNameFile),
                defaultMasterPasswordCoolDown,
                defaultPasswordGenerateLength
        );
    }

    public Settings copyOnLogin(UserNameFile lastUserNameFile, List<UserNameFile> userNameFiles) {
        return new Settings(
                lastUserNameFile,
                userNameFiles,
                this.masterPasswordCoolDown == null ? defaultMasterPasswordCoolDown : masterPasswordCoolDown,
                this.passwordGenerateLength == null ? defaultPasswordGenerateLength : passwordGenerateLength
        );
    }
}
