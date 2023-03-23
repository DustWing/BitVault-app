package com.bitvault.ui.views.categories;

import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.model.Identifiable;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.utils.BvColors;
import com.bitvault.ui.utils.BvSpacing;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.Result;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.UUID;
import java.util.function.Function;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignD.DELETE;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignP.PENCIL;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignC.CHECK;


public class CategoryRowView extends HBox implements Identifiable {
    private final CategoryRowVM categoryRowVM;
    private Function<CategoryRowView, Result<Boolean>> onDelete;


    public static CategoryRowView createFromCategory(final Category category, Function<CategoryRowView, Result<Boolean>> onDelete) {
        final Color color = BvColors.fromHex(category.color());
        final CategoryRowVM categoryRowVM = new CategoryRowVM(category.id(), category.name(), color);
        return new CategoryRowView(categoryRowVM, onDelete);
    }

    public static CategoryRowView createNew(Function<CategoryRowView, Result<Boolean>> onDelete) {
        final String id = UUID.randomUUID().toString();
        final CategoryRowVM categoryRowVM = new CategoryRowVM(id, "", BvColors.random());
        categoryRowVM.allowEditProperty().set(false);
        categoryRowVM.allowSaveProperty().set(true);
        return new CategoryRowView(categoryRowVM, onDelete);
    }


    public CategoryRowView(CategoryRowVM categoryRowVM, Function<CategoryRowView, Result<Boolean>> onDelete) {
        this.categoryRowVM = categoryRowVM;
        this.onDelete = onDelete;

        final BvTextField categoryName = new BvTextField()
                .withDefaultSize()
                .withBinding(this.categoryRowVM.categoryNameProperty())
                .withPromptText("Category")
                .setRequired(true);
        categoryName.disableProperty().bind(this.categoryRowVM.allowEditProperty());

        final ColorPicker colorPicker = new ColorPicker();
        colorPicker.disableProperty().bind(this.categoryRowVM.allowEditProperty());
        JavaFxUtil.defaultSize(colorPicker);
        if (this.categoryRowVM.getCategoryColor() != null) colorPicker.setValue(categoryRowVM.getCategoryColor());
        colorPicker.setOnAction(event -> this.categoryRowVM.categoryColorProperty().set(colorPicker.getValue()));


        final FontIcon deleteIcon = new FontIcon(DELETE);
        final BvButton deleteButton = new BvButton("", deleteIcon);
        deleteButton.setOnAction(actionEvent -> onDelete.apply(this));

        final FontIcon editIcon = new FontIcon(PENCIL);
        final BvButton edit = new BvButton("", editIcon);
        edit.setOnAction(actionEvent -> this.categoryRowVM.edit());
        edit.visibleProperty().bind(this.categoryRowVM.allowEditProperty());
        edit.managedProperty().bind(this.categoryRowVM.allowEditProperty());

        final FontIcon saveIcon = new FontIcon(CHECK);
        final BvButton saveBtn = new BvButton("", saveIcon);
        saveBtn.setOnAction(actionEvent -> this.categoryRowVM.edit());
        saveBtn.visibleProperty().bind(this.categoryRowVM.allowSaveProperty());
        saveBtn.managedProperty().bind(this.categoryRowVM.allowSaveProperty());

        this.getChildren().addAll(categoryName, colorPicker, deleteButton, edit, saveBtn);
        this.setFillHeight(true);
        this.setSpacing(BvSpacing.SMALL);
        this.setAlignment(Pos.CENTER);
        JavaFxUtil.hGrowAlways(this);

    }


    public CategoryRowView onDeleteAction(Function<CategoryRowView, Result<Boolean>> onDelete) {
        this.onDelete = onDelete;
        return this;
    }

    @Override
    public String getUniqueId() {
        return this.categoryRowVM.getId();
    }
}
