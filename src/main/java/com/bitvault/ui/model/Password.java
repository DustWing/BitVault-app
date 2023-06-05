package com.bitvault.ui.model;

import com.bitvault.ui.hyperlink.IWebLocation;
import javafx.beans.property.SimpleStringProperty;

public final class Password implements IWebLocation {
    private final String id;
    private final SimpleStringProperty username;
    private final SimpleStringProperty password;
    private final SecureDetails secureDetails;

    public Password(
            String id,
            String username,
            String password,
            SecureDetails secureDetails
    ) {
        this.id = id;
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.secureDetails = secureDetails;
    }

    public Password deepCopy() {
        return new Password(
                this.getId(),
                this.getUsername(),
                this.getPassword(),
                this.getSecureDetails().deepCopy()
        );
    }

    public void update(Password password) {
        this.usernameProperty().set(password.getUsername());
        this.passwordProperty().set(password.getPassword());
        this.getSecureDetails().update(password.getSecureDetails());
    }


    public String getId() {
        return id;
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public SecureDetails getSecureDetails() {
        return secureDetails;
    }


    @Override
    public String getDomain() {
        return this.secureDetails.getDomain();
    }

    @Override
    public SimpleStringProperty domainProperty() {
        return this.secureDetails.domainProperty();
    }


    public boolean contains(String value) {
        var toLower = value.toLowerCase();

        boolean contains = false;
        if (this.getUsername().toLowerCase().contains(toLower)) {
            contains = true;
        }
        if (this.getSecureDetails().getDomain() != null && this.getSecureDetails().getDomain().contains(toLower)) {
            contains = true;
        }

        if (this.getSecureDetails().getTitle() != null && this.getSecureDetails().getTitle().contains(toLower)) {
            contains = true;
        }

        return contains;
    }

}
