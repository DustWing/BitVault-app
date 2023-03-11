package com.bitvault.ui.viewmodel;

import com.bitvault.security.UserSession;
import com.bitvault.services.factory.IServiceFactory;
import com.bitvault.ui.model.Profile;
import com.bitvault.ui.views.password.PasswordView;
import com.bitvault.ui.views.password.PasswordVM;
import com.bitvault.util.Result;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;

public final class DashBoardVM {


    private final UserSession userSession;
    private final IServiceFactory serviceFactory;
    private List<Profile> profiles;
    private final SimpleObjectProperty<Profile> selectedProfile;


    public DashBoardVM(UserSession userSession, final IServiceFactory serviceFactory) {
        this.userSession = userSession;
        this.serviceFactory = serviceFactory;
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

    public PasswordView getPasswordView() {

        final PasswordVM passwordVM = new PasswordVM(
                userSession,
                serviceFactory.getPasswordService(),
                serviceFactory.getCategoryService(),
                getSelectedProfile()
        );

        return new PasswordView(passwordVM);
    }
}
