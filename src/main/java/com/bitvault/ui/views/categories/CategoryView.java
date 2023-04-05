package com.bitvault.ui.views.categories;

import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.utils.BvColors;
import com.bitvault.ui.utils.BvStyles;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.Labels;
import com.bitvault.util.Result;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignP.PLUS;

public class CategoryView extends VBox {

    private final CategoryVM categoryVM;

    private final ListView<CategoryRowView> listView;
    private final ProgressIndicator progressIndicator = new ProgressIndicator();
    private final Label noRecordLbl = new Label(Labels.i18n("no.records"));
    private final BvButton addNewBtn;

    public static CategoryView createTest() {

        ICategoryService categoryService = () -> Result.ok(List.of(
                new Category(UUID.randomUUID().toString(), "Cat1", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password"),
                new Category(UUID.randomUUID().toString(), "Cat2", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password"),
                new Category(UUID.randomUUID().toString(), "Cat2", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password"),
                new Category(UUID.randomUUID().toString(), "Cat2", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password")
        ));

        CategoryVM categoryVM = new CategoryVM(categoryService);

        return new CategoryView(categoryVM);

    }


    public CategoryView(CategoryVM categoryVM) {
        this.categoryVM = categoryVM;
        this.listView = new ListView<>(this.categoryVM.getCategories());
        this.listView.setPlaceholder(new Label(Labels.i18n("no.records")));
        this.listView.setFixedCellSize(60);
        this.listView.getStyleClass().add(BvStyles.EDGE_TO_EDGE);

        final FontIcon plusIcon = new FontIcon(PLUS);
        this.addNewBtn = new BvButton(Labels.i18n("add.new"), plusIcon)
                .withDefaultSize()
                .action(actionEvent -> addNew());


        this.categoryVM.loadingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) loading();
            else onLoaded();
        });

        this.categoryVM.load();

        this.getChildren().addAll(this.listView, this.addNewBtn);
        this.setFillWidth(true);
        this.setAlignment(Pos.CENTER);
        JavaFxUtil.vGrowAlways(this);
    }

    private void loading() {
        this.addNewBtn.setDisable(true);
        this.listView.setPlaceholder(this.progressIndicator);
    }

    private void onLoaded() {
        this.addNewBtn.setDisable(false);
        this.listView.setPlaceholder(this.noRecordLbl);
    }


    private void addNew() {
        final CategoryRowView categoryRowView = this.categoryVM.addNewCategory();
        JavaFxUtil.scrollToLast(this.listView);
        categoryRowView.focus();
    }

}
