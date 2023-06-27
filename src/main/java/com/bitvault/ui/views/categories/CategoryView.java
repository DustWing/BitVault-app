package com.bitvault.ui.views.categories;

import atlantafx.base.theme.Tweaks;
import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.utils.BvColors;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.Labels;
import com.bitvault.util.Result;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignK.KEYBOARD_BACKSPACE;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignP.PLUS;

public class CategoryView extends BorderPane {

    private final CategoryVM categoryVM;

    private final ListView<CategoryRowView> listView;
    private final ProgressIndicator progressIndicator = new ProgressIndicator();
    private final Label noRecordLbl = new Label(Labels.i18n("no.records"));
    private final Runnable backAction;

    public static CategoryView createTest() {

        ICategoryService categoryService = () -> Result.ok(List.of(
                new Category(UUID.randomUUID().toString(), "Cat1", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password", false),
                new Category(UUID.randomUUID().toString(), "Cat2", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password", false),
                new Category(UUID.randomUUID().toString(), "Cat2", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password", false),
                new Category(UUID.randomUUID().toString(), "Cat2", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password", false)
        ));

        CategoryVM categoryVM = new CategoryVM(categoryService);

        return new CategoryView(categoryVM, () -> {
        });

    }


    public CategoryView(CategoryVM categoryVM, Runnable backAction) {
        this.categoryVM = categoryVM;
        this.backAction = backAction;
        this.listView = createListView();

        this.categoryVM.load();

        this.setTop(createToolBar());
        this.setCenter(listView);
    }

    private ListView<CategoryRowView> createListView() {
        ListView<CategoryRowView> listView = new ListView<>(this.categoryVM.getCategories());
        listView.setPlaceholder(new Label(Labels.i18n("no.records")));
        listView.setFixedCellSize(60);
        listView.getStyleClass().add(Tweaks.EDGE_TO_EDGE);

        this.categoryVM.loadingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) loading();
            else onLoaded();
        });

        BorderPane.setMargin(listView, BvInsets.all10);

        return listView;
    }

    private ToolBar createToolBar() {

        ToolBar toolBar = new ToolBar(
                backBtn(),
                new Separator(Orientation.VERTICAL),
                createAddNewBtn()
        );

        return toolBar;
    }

    private Button createAddNewBtn() {
        final FontIcon plusIcon = new FontIcon(PLUS);

        BvButton btn = new BvButton(Labels.i18n("add.new"), plusIcon)
                .withDefaultSize()
                .action(actionEvent -> addNew());
        btn.setTooltip(new Tooltip(Labels.i18n("add.new.category")));

        btn.disableProperty().bind(this.categoryVM.loadingProperty());
        return btn;
    }

    public Button backBtn() {
        final FontIcon backIcon = new FontIcon(KEYBOARD_BACKSPACE);


        BvButton btn = new BvButton(Labels.i18n("back"), backIcon)
                .withOnlyIconDefaultSize()
                .action(event -> backAction.run());
        btn.setTooltip(new Tooltip(Labels.i18n("back")));

        return btn;
    }


    private void loading() {
        this.listView.setPlaceholder(this.progressIndicator);
    }

    private void onLoaded() {
        this.listView.setPlaceholder(this.noRecordLbl);
    }


    private void addNew() {
        final CategoryRowView categoryRowView = this.categoryVM.addNewCategory();
        JavaFxUtil.scrollToLast(this.listView);
        categoryRowView.focus();
    }

}
