package com.bitvault.ui.viewmodel;

import com.bitvault.ui.model.Profile;
import com.bitvault.services.factory.IServiceFactory;
import com.bitvault.ui.views.PasswordView;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;

public final class DashBoardVM {

    private final IServiceFactory serviceFactory;
    private List<Profile> profiles;
    private SimpleObjectProperty<Profile> selectedProfile;


    public DashBoardVM(final IServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
        this.selectedProfile = new SimpleObjectProperty<>();
        this.profiles = new ArrayList<>();
        init();
    }

    private void init() {

        serviceFactory.getProfileService().getProfiles()
                .apply(
                        profiles1 -> {
                            profiles.addAll(profiles1);
                            selectedProfileProperty().set(profiles1.get(0));
                        },
                        exception -> {

                        }
                );
    }

    public Profile getSelectedProfile() {
        return selectedProfile.get();
    }

    public SimpleObjectProperty<Profile> selectedProfileProperty() {
        return selectedProfile;
    }

    public PasswordView getPasswordView() {

        final PasswordVM passwordVM = new PasswordVM(
                serviceFactory.getPasswordService(),
                serviceFactory.getCategoryService(),
                getSelectedProfile()
        );

        return new PasswordView(passwordVM);
    }
}
