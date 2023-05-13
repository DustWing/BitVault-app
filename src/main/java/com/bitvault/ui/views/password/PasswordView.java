package com.bitvault.ui.views.password;

import com.bitvault.security.UserSession;
import com.bitvault.services.interfaces.IUserService;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.PasswordInputDialog;
import com.bitvault.ui.model.User;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.views.categories.CategoryVM;
import com.bitvault.ui.views.categories.CategoryView;
import com.bitvault.util.Labels;
import com.bitvault.util.Result;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.util.Optional;


public class PasswordView extends BorderPane {

    private final PasswordVM passwordVM;
    private final PasswordTableView passwordTableView;

    private final Button categoriesBtn;
    private final Button backBtn;

    public PasswordView(PasswordVM passwordVM) {
        this.passwordVM = passwordVM;
        this.passwordTableView = new PasswordTableView(this.passwordVM);
        this.categoriesBtn = categoriesBtn();
        this.backBtn = backBtn();

        this.setTop(categoriesBtn);
        this.setCenter(passwordTableView);
        BorderPane.setMargin(passwordTableView, BvInsets.all10);
        BorderPane.setMargin(categoriesBtn, BvInsets.all10);
        BorderPane.setMargin(this.backBtn, BvInsets.all10);


    }

    public Button categoriesBtn() {

        return new BvButton(Labels.i18n("categories"))
                .withDefaultSize()
                .action(event -> onNewCatAction());
    }

    private void onNewCatAction() {
        CategoryVM categoryVM = new CategoryVM(this.passwordVM.getCategoryService());
        CategoryView categoryView = new CategoryView(categoryVM);
        this.setTop(this.backBtn);
        this.setCenter(categoryView);
        BorderPane.setMargin(categoryView, BvInsets.all10);

    }

    public Button backBtn() {

        return new BvButton(Labels.i18n("passwords"))
                .withDefaultSize()
                .action(event -> onPasswordAction());
    }

    private void onPasswordAction() {
        passwordVM.init();// to reload passwords
        this.passwordTableView.filterByCategory("ALL");
        this.setTop(this.categoriesBtn);
        this.setCenter(passwordTableView);
    }

}
