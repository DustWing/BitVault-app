package com.bitvault.components;

import com.bitvault.util.ResourceLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;

public class BitVaultListView<T> extends ListView<T> {

    private final static String STYLE_CLASS = "bit-vault-list-view";

    private final String STYLE_SHEET = ResourceLoader.load("/com.bitvault/css/bitVaultListView.css");


    public BitVaultListView() {
        super();
        initialize();
    }

    public BitVaultListView(ObservableList<T> items) {
        super(items);
        initialize();
    }

    private void initialize() {
//        this.setSelectionModel(new DisabledSelectionModel<>());
        getStyleClass().add(STYLE_CLASS);

    }

    @Override
    public String getUserAgentStylesheet() {
        return STYLE_SHEET;
    }


    private static class DisabledSelectionModel<T> extends MultipleSelectionModel<T> {
        DisabledSelectionModel() {
            super.setSelectedIndex(-1);
            super.setSelectedItem(null);
        }

        @Override
        public ObservableList<Integer> getSelectedIndices() {
            return FXCollections.<Integer>emptyObservableList();
        }

        @Override
        public ObservableList<T> getSelectedItems() {
            return FXCollections.<T>emptyObservableList();
        }

        @Override
        public void selectAll() {
        }

        @Override
        public void selectFirst() {
        }

        @Override
        public void selectIndices(int index, int... indices) {
        }

        @Override
        public void selectLast() {
        }

        @Override
        public void clearAndSelect(int index) {
        }

        @Override
        public void clearSelection() {
        }

        @Override
        public void clearSelection(int index) {
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean isSelected(int index) {
            return false;
        }

        @Override
        public void select(int index) {
        }

        @Override
        public void select(T item) {
        }

        @Override
        public void selectNext() {
        }

        @Override
        public void selectPrevious() {
        }

    }
}
