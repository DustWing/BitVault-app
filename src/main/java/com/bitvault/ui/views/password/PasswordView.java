package com.bitvault.ui.views.password;

import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.TextColorComboBox;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.components.textfield.TextFieldUtils;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.ui.utils.ViewLoader;
import com.bitvault.ui.views.categories.CategoryVM;
import com.bitvault.ui.views.categories.CategoryView;
import com.bitvault.util.Labels;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignP.PLAYLIST_PLUS;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignP.PLUS;


public class PasswordView extends BorderPane {

    private final PasswordVM passwordVM;
    private final ToolBar toolBar;


    public PasswordView(PasswordVM passwordVM) {
        this.passwordVM = passwordVM;

        this.toolBar = createToolBar();

        this.setTop(toolBar);
        PasswordTableView passwordTableView = new PasswordTableView(this.passwordVM);
        this.setCenter(passwordTableView);
        BorderPane.setMargin(passwordTableView, BvInsets.all10);
    }

    private void onNewCatAction() {
        CategoryVM categoryVM = new CategoryVM(this.passwordVM.getCategoryService());
        CategoryView categoryView = new CategoryView(categoryVM, this::onBackAction);
        this.setTop(null);//remove top bar on categories
        this.setCenter(categoryView);

    }

    private void onBackAction() {
        passwordVM.init();// to reload passwords
        this.setTop(toolBar);
        PasswordTableView passwordTableView = new PasswordTableView(this.passwordVM);
        this.setCenter(passwordTableView);
        BorderPane.setMargin(passwordTableView, BvInsets.all10);

    }


    private ToolBar createToolBar() {

        final Button addNewBtn = getAddNewBtn();
        final Button categoriesBtn = categoriesBtn();
        final TextColorComboBox<Category> categoriesDd = categoryDropdown();
        final TextField searchTf = createSearchTf(this.passwordVM.getFilteredList());

        ToolBar toolBar = new ToolBar(
                addNewBtn,
                new Separator(),
                categoriesBtn,
                new Separator(),
                categoriesDd,
                searchTf
        );

        return toolBar;
    }

    public Button getAddNewBtn() {
        final FontIcon plusIcon = new FontIcon(PLUS);
        final BvButton addNewBtn = new BvButton("", plusIcon)
                .action(event -> showNewPassPopUp())
                .withOnlyIconDefaultSize();
        addNewBtn.setTooltip(new Tooltip(Labels.i18n("add.new.password")));

        return addNewBtn;
    }

    public Button categoriesBtn() {

        final FontIcon catIcon = new FontIcon(PLAYLIST_PLUS);
        BvButton btn = new BvButton("", catIcon)
                .withOnlyIconDefaultSize()
                .action(event -> onNewCatAction());

        btn.setTooltip(new Tooltip(Labels.i18n("to.categories")));

        return btn;
    }

    private TextColorComboBox<Category> categoryDropdown() {
        final TextColorComboBox<Category> categoriesDd = TextColorComboBox.withCircle(this.passwordVM.getCategories());
        categoriesDd.valueProperty().bindBidirectional(this.passwordVM.selectedCategoryProperty());

        categoriesDd.setOnAction(event -> {
                    Category selectedItem = categoriesDd.getValue();
                    if (selectedItem != null) {
                        this.filterByCategory(selectedItem.id(), this.passwordVM.getFilteredList());
                    }
                }
        );

        categoriesDd.setTooltip(new Tooltip(Labels.i18n("select.category")));
        JavaFxUtil.mediumSize(categoriesDd);
        return categoriesDd;
    }

    private BvTextField createSearchTf(FilteredList<Password> filteredList) {


        BvTextField bvTextField = new BvTextField()
                .withPromptText(Labels.i18n("search"))
                .withMediumSize();

        bvTextField.setTooltip(new Tooltip(Labels.i18n("filter.table")));

        TextFieldUtils.addFilter(bvTextField, filteredList, Password::contains);

        return bvTextField;
    }

    private void showNewPassPopUp() {

        final PasswordDetailsView view = PasswordDetailsView.newPassword(
                new ArrayList<>(passwordVM.getCategoriesList()),
                this.passwordVM.getProfile(),
                this.passwordVM.getPassLength(),
                this.passwordVM::create
        );

        ViewLoader.popUp(this.getScene().getWindow(), view, Labels.i18n("add.new.password")).show();
    }

    public void filterByCategory(String id, FilteredList<Password> list) {
        if ("ALL".equals(id)) {
            list.setPredicate(password -> true);
            return;
        }
        list.setPredicate(password -> password.getSecureDetails().getCategory().id().equals(id));
    }

}
