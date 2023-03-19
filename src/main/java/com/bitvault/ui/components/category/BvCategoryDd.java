package com.bitvault.ui.components.category;

import com.bitvault.ui.components.TextColorComboBox;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.utils.BvSpacing;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.Labels;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

public class BvCategoryDd extends VBox {

    private final ObservableList<Category> categories = FXCollections.observableArrayList();
    private final SimpleObjectProperty<Category> selectedCategory = new SimpleObjectProperty<>();
    private final SimpleStringProperty newCategoryName;
    private final Consumer<Color> onColorSelect;
    private final SimpleBooleanProperty showNewCat = new SimpleBooleanProperty();


    public BvCategoryDd(
            List<Category> categories,
            SimpleStringProperty newCategoryName,
            Consumer<Color> onColorSelect
    ) {

        this.newCategoryName = newCategoryName;
        this.onColorSelect = onColorSelect;


        this.categories.addAll(categories);
        final Category newCat = new Category("EMPTY", "New Category", "#FFFFFF", LocalDateTime.now(), LocalDateTime.now(), "Password");
        this.categories.add(newCat);


        this.selectedCategory.addListener((observable, oldValue, newValue) -> {
            showNewCat.set(newValue.equals(newCat));
        });

        final TextColorComboBox<Category> categoriesDd = TextColorComboBox.withCircle(this.categories);
        categoriesDd.valueProperty().bindBidirectional(selectedCategory);
        JavaFxUtil.defaultSize(categoriesDd);

        final BvTextField newCategory = new BvTextField()
                .withBinding(newCategoryName)
                .withDefaultSize()
                .withPromptText(Labels.i18n("New Category"));

        final ColorPicker colorPicker = new ColorPicker();
        colorPicker.setOnAction(event -> onColorSelect.accept(colorPicker.getValue()));
        JavaFxUtil.defaultSize(colorPicker);

        final HBox hBox = new HBox(newCategory, colorPicker);
        hBox.visibleProperty().bind(showNewCat);
        hBox.managedProperty().bind(showNewCat);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(BvSpacing.SMALL);
        JavaFxUtil.hGrowAlways(hBox);

        this.getChildren().addAll(
                categoriesDd,
                hBox
        );

        this.setAlignment(Pos.CENTER);
        this.setSpacing(BvSpacing.SMALL);
        JavaFxUtil.vGrowAlways(this);

    }
}
