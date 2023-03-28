package com.bitvault.ui.listnode;

import com.bitvault.ui.utils.JavaFxUtil;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ListView;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * This class is use to convert a list of one type to another for a ListView.
 * <p>
 * Example:
 * <blockquote><pre>
 *      ObservableList&lt;Item&gt; theList = ...;
 *
 *      theList.addListener(
 *          new ToNodeConverter<>(listView, data, item -> {
 *                     final VBox vBox = new VBox(
 *                             new Label(item.getId())
 *                     );
 *                     return vBox;
 *                 })
 *      );
 * </pre></blockquote>
 *
 * @param <E> the type of the list we are converting to nodes
 * @param <R> the type of the conversion
 */
public class ToNodeConverted<E extends Identifiable, R extends IdentifiableNode> implements ListChangeListener<E> {

    private final ListView<R> listView;
    private final Function<E, R> converter;

    /**
     * @param listView  the list view we are updating
     * @param converter a function to specify how to convert type {@link E} to type {@link R}
     */
    public ToNodeConverted(ListView<R> listView, Function<E, R> converter) {
        this.listView = listView;
        this.converter = converter;
    }

    @Override
    public void onChanged(Change<? extends E> c) {
        if (!c.next()) {
            return;
        }

        if (c.wasAdded()) {
            c.getAddedSubList().forEach(
                    e -> {
                        R to = converter.apply(e);
                        listView.getItems().add(to);
                    }
            );
            //scroll to last added element
            JavaFxUtil.scrollToLast(listView);
        } else if (c.wasRemoved()) {
            var ids = c.getRemoved().stream()
                    .map(Identifiable::getUniqueId)
                    .collect(Collectors.toSet());
            listView.getItems().removeIf(r -> ids.contains(r.getUniqueId()));
        }
    }
}
