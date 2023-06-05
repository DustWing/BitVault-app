package com.bitvault.ui.model;

import com.bitvault.ui.hyperlink.IWebLocation;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;


public class SyncValue<T extends IWebLocation> implements IWebLocation {

    public enum ActionState {
        SAVE,
        DISCARD,
        REQUIRED,
        NON_REQUIRED

    }


    private final T oldValue;
    private final T newValue;


    private final SimpleObjectProperty<ActionState> actionState;
    private final SimpleStringProperty warningMsg;

    public static <T extends IWebLocation> SyncValue<T> createNew(T newValue) {
        return new SyncValue<>(null, newValue, ActionState.NON_REQUIRED, "");
    }

    public static <T extends IWebLocation> SyncValue<T> createConflict(T oldValue, T newValue) {
        return new SyncValue<>(oldValue, newValue, ActionState.REQUIRED, "conflict");
    }

    public static <T extends IWebLocation> SyncValue<T> createWarning(T newValue, String warning) {
        return new SyncValue<>(null, newValue, ActionState.REQUIRED, warning);
    }

    private SyncValue(T oldValue, T newValue, ActionState actionState, String warning) {
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.actionState = new SimpleObjectProperty<>(actionState);
        this.warningMsg = new SimpleStringProperty(warning);
    }

    public T getOldValue() {
        return oldValue;
    }

    public T getNewValue() {
        return newValue;
    }

    public boolean isNew() {
        return oldValue == null;
    }


    public ActionState getActionState() {
        return actionState.get();
    }

    public SimpleObjectProperty<ActionState> actionStateProperty() {
        return actionState;
    }


    public String getWarningMsg() {
        return warningMsg.get();
    }

    public SimpleStringProperty warningMsgProperty() {
        return warningMsg;
    }

    @Override
    public String getDomain() {
        return this.getNewValue().getDomain();
    }

    @Override
    public SimpleStringProperty domainProperty() {
        return this.getNewValue().domainProperty();
    }
}
