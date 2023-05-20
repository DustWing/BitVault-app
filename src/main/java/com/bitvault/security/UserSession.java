package com.bitvault.security;

import com.bitvault.services.factory.ServiceFactory;
import com.bitvault.services.factory.TestServiceFactory;
import com.bitvault.ui.model.Profile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Supplier;

public class UserSession {

    private final String username;
    private final EncryptionProvider encryptionProvider;
    private final ServiceFactory serviceFactory;
    private Profile profile;

    private final Duration coolDown = Duration.ofSeconds(10);
    private LocalDateTime previousAuthTime;

    public static UserSession createTest(){
        final String username = "Test";
        final String password = "Test";

        final EncryptionProvider encryptionProvider = new AesEncryptionProvider(password.toCharArray());
        final ServiceFactory serviceFactory = new TestServiceFactory(encryptionProvider);
        return new UserSession(username, encryptionProvider, serviceFactory);
    }

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

    public synchronized boolean authWithCoolDown(Supplier<Boolean> action) {
        final LocalDateTime now = LocalDateTime.now();

        if (previousAuthTime == null) {
            boolean actionSuccess = action.get();
            if(actionSuccess){
                previousAuthTime = now;
            }
            return actionSuccess;
        }

        boolean askForPassword = now.isAfter(previousAuthTime.plus(coolDown));

        if(!askForPassword){
            return true;
        }

        boolean actionSuccess = action.get();
        if(actionSuccess){
            previousAuthTime = now;
        }
        return actionSuccess;
    }

    public synchronized boolean isAuthOnCoolDown(){
        if (previousAuthTime == null) {
            return false;
        }
        final LocalDateTime now = LocalDateTime.now();
        return now.isBefore(previousAuthTime.plus(coolDown));
    }
}
