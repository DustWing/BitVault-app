package com.bitvault.ui.views.categories;

import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.BvScaffold;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.utils.BvColors;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.Labels;
import com.bitvault.util.Result;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignP.PLUS;

public class CategoryView extends VBox {

    private final CategoryVM categoryVM;

    private final BvScaffold bvScaffold;

    public static CategoryView createTest() {

        ICategoryService categoryService = () -> Result.ok(List.of(
                new Category(UUID.randomUUID().toString(), "Cat1", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password"),
                new Category(UUID.randomUUID().toString(), "Cat2", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password"),
                new Category(UUID.randomUUID().toString(), "Cat2", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password"),
                new Category(UUID.randomUUID().toString(), "Cat2", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password"),
                new Category(UUID.randomUUID().toString(), "Cat2", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password"),
                new Category(UUID.randomUUID().toString(), "Cat2", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password"),
                new Category(UUID.randomUUID().toString(), "Cat2", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password"),
                new Category(UUID.randomUUID().toString(), "Cat2", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password"),
                new Category(UUID.randomUUID().toString(), "Cat2", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password"),
                new Category(UUID.randomUUID().toString(), "Cat2", BvColors.randomHex(), LocalDateTime.now(), LocalDateTime.now(), "Password")

        ));

        CategoryVM categoryVM = new CategoryVM(categoryService);

        return new CategoryView(categoryVM);

    }


    public CategoryView(CategoryVM categoryVM) {
        this.categoryVM = categoryVM;


        final List<CategoryRowView> categoryRowViews = this.categoryVM.getCategories()
                .stream()
                .map(category -> CategoryRowView.createFromCategory(category, this::onDelete))
                .toList();
        final List<Node> nodes = new ArrayList<>(categoryRowViews);

        final FontIcon plusIcon = new FontIcon(PLUS);
        final BvButton addNewBtn = new BvButton(Labels.i18n("add.new"), plusIcon)
                .withDefaultSize()
                .action(actionEvent -> addNew());

        this.bvScaffold = BvScaffold.createDefault()
                .withChildren(nodes)
                .withFooter(addNewBtn)
                .enableScrollToLast();

        this.getChildren().add(bvScaffold);

        this.setFillWidth(true);
        this.setAlignment(Pos.CENTER);
        JavaFxUtil.vGrowAlways(this);
    }

    private Result<Boolean> onDelete(CategoryRowView categoryRowView) {
        this.bvScaffold.removeChild(categoryRowView);
        return Result.Success;
    }

    private void addNew() {
        CategoryRowView aNew = CategoryRowView.createNew(this::onDelete);
        this.bvScaffold.addChild(aNew);
        aNew.focus();
    }

}
