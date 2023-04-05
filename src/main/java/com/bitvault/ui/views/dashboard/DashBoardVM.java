package com.bitvault.ui.views.dashboard;

import com.bitvault.security.UserSession;
import com.bitvault.services.factory.ServiceFactory;
import com.bitvault.ui.model.Profile;
import com.bitvault.ui.views.password.PasswordVM;
import com.bitvault.ui.views.password.PasswordView;
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

        if (!profiles.isEmpty())
            selectedProfileProperty().set(profiles.get(0));

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
