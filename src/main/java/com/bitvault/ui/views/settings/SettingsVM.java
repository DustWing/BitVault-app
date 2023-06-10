package com.bitvault.ui.views.settings;

import com.bitvault.security.UserSession;
import com.bitvault.ui.components.alert.ErrorAlert;
import com.bitvault.ui.model.Settings;
import com.bitvault.util.Result;
import javafx.beans.property.SimpleObjectProperty;

public class SettingsVM {


    private final SimpleObjectProperty<Integer> masterPasswordCoolDown = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Integer> passwordGenerationLength = new SimpleObjectProperty<>();


    private final UserSession userSession;

    public SettingsVM(UserSession userSession) {
        this.userSession = userSession;
        this.init();
    }

    private void init() {

        final Settings settings = this.userSession.getSettings();

        this.masterPasswordCoolDown.set(settings.masterPasswordCoolDown());
        this.passwordGenerationLength.set(settings.passwordGenerateLength());
    }

    public void save() {

        final Settings settingsOld = this.userSession.getSettings();

        final Integer masterPasswordCd = masterPasswordCoolDown.get();
        final Integer passLength = passwordGenerationLength.get();

        final Settings newSettings = new Settings(
                settingsOld.lastUserNameFile(),
                settingsOld.userNameFiles(),
                masterPasswordCd,
                passLength
        );

        Result<Boolean> save = this.userSession.getServiceFactory().getSettingsService().save(newSettings);
        if (save.hasError()) {
            ErrorAlert.show("Error saving setting", save.getError());
        }

        this.userSession.setSettings(newSettings);
    }

    public SimpleObjectProperty<Integer> masterPasswordCoolDownProperty() {
        return masterPasswordCoolDown;
    }


    public SimpleObjectProperty<Integer> passwordGenerationLengthProperty() {
        return passwordGenerationLength;
    }
}
