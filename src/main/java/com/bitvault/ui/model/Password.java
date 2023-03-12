package com.bitvault.ui.model;

import com.bitvault.enums.Action;
import com.bitvault.ui.hyperlink.IWebLocation;

import java.util.Objects;

public final class Password implements IWebLocation {
    private final String id;
    private final String username;
    private final String password;
    private final SecureDetails secureDetails;
    private final Action action;

    public Password(
            String id,
            String username,
            String password,
            SecureDetails secureDetails,
            Action action

    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.secureDetails = secureDetails;
        this.action = action;
    }

    public Password copyOf() {
        return new Password(
                this.id,
                this.username,
                this.password,
                this.secureDetails,
                this.action
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

    public Action getAction() {
        return action;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Password) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.username, that.username) &&
                Objects.equals(this.password, that.password) &&
                Objects.equals(this.secureDetails, that.secureDetails) &&
                Objects.equals(this.action, that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, secureDetails, action);
    }

    @Override
    public String toString() {
        return "Password[" +
                "id=" + id + ", " +
                "username=" + username + ", " +
                "password=" + password + ", " +
                "secureDetails=" + secureDetails + ", " +
                "action=" + action + ']';
    }


}
