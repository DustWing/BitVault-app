package com.bitvault.ui.model;

import com.bitvault.ui.hyperlink.IWebLocation;

import java.util.Objects;

public final class Password implements IWebLocation {
    private final String id;
    private final String username;
    private final String password;
    private final SecureDetails secureDetails;

    public Password(
            String id,
            String username,
            String password,
            SecureDetails secureDetails
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.secureDetails = secureDetails;
    }

    public Password copyOf() {
        return new Password(
                this.id,
                this.username,
                this.password,
                this.secureDetails
        );
    }

    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUrl() {
        return this.secureDetails.getDomain();
    }

    public SecureDetails getSecureDetails() {
        return secureDetails;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Password) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.username, that.username) &&
                Objects.equals(this.password, that.password) &&
                Objects.equals(this.secureDetails, that.secureDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, secureDetails);
    }

    @Override
    public String toString() {
        return "Password[" +
                "id=" + id + ", " +
                "username=" + username + ", " +
                "password=" + password + ", " +
                "secureDetails=" + secureDetails + ']';
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

//        if (this.getSecureDetails().getCategory().getText().contains(toLower)) {
//            contains = true;
//        }
//
//        if (this.getSecureDetails().getDescription() != null && this.getSecureDetails().getDescription().contains(toLower)) {
//            contains = true;
//        }

        return contains;
    }

}
