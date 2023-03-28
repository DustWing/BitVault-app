package com.bitvault.ui.views.categories;

import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.utils.BvColors;
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
        this.listView = new ListView<>(categoryVM.getCategories());
        this.listView.setPlaceholder(new Label(Labels.i18n("no.records")));
        this.listView.setFixedCellSize(60);

        final FontIcon plusIcon = new FontIcon(PLUS);
        this.addNewBtn = new BvButton(Labels.i18n("add.new"), plusIcon)
                .withDefaultSize()
                .action(actionEvent -> addNew());

        this.categoryVM.loadingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) loading();
            else onLoaded();
        });

        this.setFillWidth(true);
        this.setAlignment(Pos.CENTER);

        this.categoryVM.load();
    }

    private void loading() {
        this.getChildren().clear();
        this.getChildren().add(this.progressIndicator);
        JavaFxUtil.vGrowAlways(this);

    }

    private void onLoaded() {
        this.getChildren().clear();
        this.getChildren().addAll(listView, addNewBtn);
        JavaFxUtil.vGrowAlways(this);
    }


    private void addNew() {
        CategoryRowView categoryRowView = this.categoryVM.addNewCategory();
        JavaFxUtil.scrollToLast(this.listView);
        categoryRowView.focus();
    }

}
