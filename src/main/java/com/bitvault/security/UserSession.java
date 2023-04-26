package com.bitvault.security;

import com.bitvault.services.factory.ServiceFactory;
import com.bitvault.ui.model.Profile;

public class UserSession {

    private final String username;
    private final EncryptionProvider encryptionProvider;
    private final ServiceFactory serviceFactory;

    private Profile profile;

    public UserSession(String username, EncryptionProvider encryptionProvider, ServiceFactory serviceFactory) {
        this.username = username;
        this.encryptionProvider = encryptionProvider;
        this.serviceFactory = serviceFactory;
    }

    public void discard() {
        this.encryptionProvider.destroy();
    }


    public String getUsername() {
        return username;
    }


    public EncryptionProvider getEncryptionProvider() {
        return encryptionProvider;
    }

    public ServiceFactory getServiceFactory() {
        return serviceFactory;
    }


    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
