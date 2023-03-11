package com.bitvault.ui.components.textfield;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;

import java.util.function.BiFunction;

public class TextFieldFilterListener<E> implements ChangeListener<String> {

    private final PauseTransition delay;
    private final FilteredList<E> filteredList;
    private final BiFunction<E, String, Boolean> onFilter;

    public TextFieldFilterListener(PauseTransition delay, FilteredList<E> filteredList, BiFunction<E, String, Boolean> onFilter) {
        this.delay = delay;
        this.filteredList = filteredList;
        this.onFilter = onFilter;
    }

    @Override
    public void changed(ObservableValue observable, String oldValue, String newValue) {


        delay.setOnFinished(
                event -> {
                    if (newValue == null || newValue.isBlank()) {
                        this.filteredList.setPredicate(s -> true);
                    } else {
                        this.filteredList.setPredicate(e -> onFilter.apply(e, newValue));
                    }
                }
        );

        delay.playFromStart();

    }
}
