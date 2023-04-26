package com.bitvault.ui.model;

import com.bitvault.ui.hyperlink.IWebLocation;



public class SyncValue<T extends IWebLocation> implements IWebLocation {

    public enum Action {
        SAVE,
        DISCARD,
        NONE

    }


    private Action action = Action.NONE;
    private final T oldValue;
    private final T newValue;

    public static <T extends IWebLocation> SyncValue<T> createNew(T newValue) {
        return new SyncValue<>(null, newValue);
    }

    public static <T extends IWebLocation> SyncValue<T> createConflict(T oldValue, T newValue) {
        return new SyncValue<>(oldValue, newValue);
    }

    private SyncValue(T oldValue, T newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
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

    public void setAction(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public boolean warning() {
        return !isNew() && action.equals(Action.NONE);
    }

    @Override
    public String getUrl() {
        return this.getNewValue().getUrl();
    }


}
