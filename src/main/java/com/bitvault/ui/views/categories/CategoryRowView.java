package com.bitvault.ui.views.categories;

import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.listnode.IdentifiableNode;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.utils.BvColors;
import com.bitvault.ui.utils.BvSpacing;
import com.bitvault.ui.utils.BvWidths;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.Result;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.UUID;
import java.util.function.Function;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignC.CHECK_BOLD;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignD.DELETE;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignP.PENCIL;


public class CategoryRowView extends HBox implements IdentifiableNode {
    private final CategoryRowVM categoryRowVM;
    private Function<CategoryRowView, Result<Boolean>> onDelete;

    final BvTextField categoryName;

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

        this.categoryName = new BvTextField()
                .withDefaultSize()
                .withBinding(this.categoryRowVM.categoryNameProperty())
                .withPromptText("Category")
                .setRequired(true);
        categoryName.disableProperty().bind(this.categoryRowVM.allowEditProperty());

        //color picker
        final ColorPicker colorPicker = new ColorPicker();
        colorPicker.disableProperty().bind(this.categoryRowVM.allowEditProperty());
        JavaFxUtil.defaultSize(colorPicker);
        if (this.categoryRowVM.getCategoryColor() != null) colorPicker.setValue(categoryRowVM.getCategoryColor());
        colorPicker.setOnAction(event -> this.categoryRowVM.categoryColorProperty().set(colorPicker.getValue()));

        //buttons
        final StackPane actionBtnPane = actionBtn();

        //adding to child
        this.getChildren().addAll(categoryName, colorPicker, actionBtnPane);
        this.setFillHeight(true);
        this.setSpacing(BvSpacing.SMALL);
        this.setAlignment(Pos.CENTER);
        JavaFxUtil.hGrowAlways(this);

    }

    private StackPane actionBtn() {

        //delete btn
        final FontIcon deleteIcon = new FontIcon(DELETE);
        final BvButton deleteButton = new BvButton("", deleteIcon);
        deleteButton.setOnAction(actionEvent -> onDelete.apply(this));
        deleteButton.disableProperty().bind(this.categoryRowVM.loadingProperty());

        //edit btn
        final FontIcon editIcon = new FontIcon(PENCIL);
        final BvButton edit = new BvButton("", editIcon);
        edit.setOnAction(actionEvent -> this.categoryRowVM.edit());
        edit.visibleProperty().bind(this.categoryRowVM.allowEditProperty());
        edit.managedProperty().bind(this.categoryRowVM.allowEditProperty());
        edit.disableProperty().bind(this.categoryRowVM.loadingProperty());


        //save btn
        final FontIcon saveIcon = new FontIcon(CHECK_BOLD);
        final BvButton saveBtn = new BvButton("", saveIcon);
        saveBtn.setOnAction(actionEvent -> save());
        saveBtn.visibleProperty().bind(this.categoryRowVM.allowSaveProperty());
        saveBtn.managedProperty().bind(this.categoryRowVM.allowSaveProperty());
        saveBtn.disableProperty().bind(this.categoryRowVM.loadingProperty());

        HBox hBox = new HBox();
        hBox.getChildren().addAll(deleteButton, edit, saveBtn);
        hBox.setFillHeight(true);
        hBox.setSpacing(BvSpacing.SMALL);
        hBox.setAlignment(Pos.CENTER);
        JavaFxUtil.hGrowAlways(this);

        final ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setBackground(Background.fill(BvColors.LOADING_BG));
        progressIndicator.setMaxWidth(BvWidths.SMALL);
        progressIndicator.setMaxHeight(40);
        progressIndicator.visibleProperty().bind(this.categoryRowVM.loadingProperty());

        final StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(hBox, progressIndicator);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setMaxWidth(BvWidths.SMALL);
        return stackPane;
    }

    private void save() {
        this.categoryRowVM.save();
        this.requestFocus();
    }

    public CategoryRowView onDeleteAction(Function<CategoryRowView, Result<Boolean>> onDelete) {
        this.onDelete = onDelete;
        return this;
    }

    @Override
    public void focus() {
        this.categoryName.requestFocus();
    }

    @Override
    public String getUniqueId() {
        return this.categoryRowVM.getId();
    }
}
