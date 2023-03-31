package com.bitvault.security;

import com.bitvault.services.factory.ServiceFactory;

public class UserSession {

    private final String username;
    private final EncryptionProvider encryptionProvider;
    private final ServiceFactory serviceFactory;


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
}
