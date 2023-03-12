package com.bitvault.ui.utils;

import com.bitvault.ui.model.Identifiable;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ListView;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
 *                     vBox.setId(item.getId());
 *                     return vBox;
 *                 })
 *      );
 * </pre></blockquote>
 *
 * @param <E> the type of the list we are converting to nodes
 * @param <R> the type of the conversion
 */
public class ListNodeConverter<E extends Identifiable, R extends Node> implements ListChangeListener<E> {

    private final ListView<R> listView;
    private final Function<E, R> converter;
    private final List<R> nodes;

    /**
     * @param listView  the list view we are updating
     * @param nodes     the current list of notes - Not retrieved from {@link ListView}
     *                  to avoid {@link javafx.collections.transformation.FilteredList} warnings with generics
     * @param converter a function to specify how to convert type {@link E} to type {@link R}
     */
    public ListNodeConverter(ListView<R> listView, final List<R> nodes, Function<E, R> converter) {
        this.listView = listView;
        this.nodes = nodes;
        this.converter = converter;
    }

    @Override
    public void onChanged(Change<? extends E> c) {
        if (!c.next()) {
            return;
        }

        if (c.wasReplaced()) {
            c.getAddedSubList().forEach(
                    e -> {
                        OptionalInt indexOpt = IntStream.range(0, nodes.size())
                                .filter(i -> e.getId().equals(nodes.get(i).getId()))
                                .findFirst();

                        R to = converter.apply(e);
                        if (indexOpt.isPresent()) {
                            nodes.set(indexOpt.getAsInt(), to);
                        }
                    }
            );
        } else if (c.wasAdded()) {
            c.getAddedSubList().forEach(
                    e -> {
                        R to = converter.apply(e);
                        nodes.add(to);
                    }
            );
            //scroll to last added element
            JavaFxUtil.scrollToLast(listView);
        } else if (c.wasRemoved()) {
            var ids = c.getRemoved().stream().map(
                    Identifiable::getId
            ).collect(Collectors.toSet());
            nodes.removeIf(r -> ids.contains(r.getId()));
        }
    }
}
