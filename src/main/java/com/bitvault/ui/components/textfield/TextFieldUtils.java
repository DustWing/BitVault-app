package com.bitvault.ui.components.textfield;

import javafx.animation.PauseTransition;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class TextFieldUtils {


    /**
     * @param textField    node to add filter
     * @param filteredList list we are filtering - javafx
     * @param onFilter     the rule of filter e.g. when input text contains a value from filteredList
     */
    public static <E> void addFilter(final TextField textField, final FilteredList<E> filteredList, final BiFunction<E, String, Boolean> onFilter) {
        PauseTransition delay = new PauseTransition(Duration.millis(200));
        TextFieldFilterListener<E> listener = new TextFieldFilterListener<>(delay, filteredList, onFilter);
        textField.textProperty().addListener(listener);
    }


    /**
     * Add autocomplete to textField. This is for short lists only.
     *
     * @param textField to add autocomplete functionality
     * @param entries   list of available auto complete
     */
    public static void addAutoComplete(TextField textField, List<String> entries) {

        ContextMenu contextMenu = new ContextMenu();
        textField.setContextMenu(contextMenu);

        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> onTextChange(textField, entries, contextMenu, newValue, null)
        );

    }

    /**
     * Add autocomplete to textField. This is for short lists only.
     *
     * @param textField to add autocomplete functionality
     * @param entries   list of available auto complete
     * @param onSelect  return the selected string value
     */
    public static void addAutoComplete(TextField textField, List<String> entries, Consumer<String> onSelect) {

        ContextMenu contextMenu = new ContextMenu();
        textField.setContextMenu(contextMenu);


        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> onTextChange(textField, entries, contextMenu, newValue, onSelect)
        );

    }

    private static void onTextChange(TextField textField, List<String> entries, ContextMenu contextMenu, String newValue, Consumer<String> onSelect) {

        if (newValue == null) { //do nothing
            return;
        }

        final List<String> filtered = entries.stream()
                .filter(entry -> entry.toLowerCase().startsWith(newValue.toLowerCase()))
                .toList();

        final List<MenuItem> menuItemList = filtered.stream()
                .map(entry -> getMenuItem(contextMenu, textField, entry, onSelect))
                .toList();

        contextMenu.getItems().clear();

        if (!menuItemList.isEmpty()) {
            contextMenu.getItems().addAll(menuItemList);
            if (!contextMenu.isShowing()) {
                contextMenu.show(textField, Side.BOTTOM, 0, 0);
            }
        }
    }

    private static MenuItem getMenuItem(ContextMenu contextMenu, TextField textField, String entry, Consumer<String> onSelect) {
        MenuItem menuItem = new MenuItem(entry);
        menuItem.setOnAction(event -> {
            textField.setText(entry);
            textField.positionCaret(entry.length());
            contextMenu.hide();
            if (onSelect != null) onSelect.accept(entry);
        });
        return menuItem;
    }


}
