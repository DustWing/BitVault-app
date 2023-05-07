package com.bitvault.ui.views.dashboard;

import com.bitvault.BitVault;
import com.bitvault.security.UserSession;
import com.bitvault.services.factory.ServiceFactory;
import com.bitvault.ui.components.alert.ErrorAlert;
import com.bitvault.ui.model.Profile;
import com.bitvault.util.Messages;
import com.bitvault.util.Result;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;

public final class DashBoardVM {


    private final UserSession userSession;
    private final ServiceFactory serviceFactory;
    private List<Profile> profiles;
    private final SimpleObjectProperty<Profile> selectedProfile;


    public DashBoardVM(UserSession userSession) {
        this.userSession = userSession;
        this.serviceFactory = userSession.getServiceFactory();
        this.selectedProfile = new SimpleObjectProperty<>();
        this.profiles = new ArrayList<>();
        init();
    }

    private void init() {

        Result<List<Profile>> profilesResult = serviceFactory.getProfileService().getProfiles();

        if (profilesResult.isFail()) {
            return;
        }

        profiles = profilesResult.get();

        if (profiles.isEmpty()) {
            ErrorAlert.show("Dashboard", Messages.i18n("no.profiles.found"));
            throw new RuntimeException("Dashboard no profile, do not recover");
        }

        Profile profile = profiles.get(0);
        selectedProfileProperty().set(profile);
        this.userSession.setProfile(profile);

    }

    public void logout() {
        BitVault.runOnCloseActions();
        this.userSession.discard();
    }

    public Profile getSelectedProfile() {
        return selectedProfile.get();
    }

    public SimpleObjectProperty<Profile> selectedProfileProperty() {
        return selectedProfile;
    }

    public UserSession getUserSession() {
        return userSession;
    }
}
