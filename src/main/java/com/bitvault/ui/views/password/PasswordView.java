package com.bitvault.ui.views.password;

import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.views.categories.CategoryVM;
import com.bitvault.ui.views.categories.CategoryView;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;


public class PasswordView extends BorderPane {

    private final PasswordVM passwordVM;
    private final PasswordTableView passwordTableView;
    private final Button newCatBtn;
    private final Button showPassword;


    public PasswordView(PasswordVM passwordVM) {
        this.passwordVM = passwordVM;

        this.passwordTableView = new PasswordTableView(this.passwordVM);

        this.newCatBtn = new BvButton("New cat")
                .withDefaultSize()
                .action(event -> onNewCatAction());

        this.showPassword = new BvButton("Passwords")
                .withDefaultSize()
                .action(event -> onPasswordAction());

        this.setLeft(newCatBtn);
        this.setCenter(passwordTableView);
        BorderPane.setMargin(passwordTableView, BvInsets.all10);

    }

    public void onNewCatAction() {
        CategoryVM categoryVM = new CategoryVM(this.passwordVM.getCategoryService());
        CategoryView categoryView = new CategoryView(categoryVM);
        this.setLeft(showPassword);
        this.setCenter(categoryView);
        BorderPane.setMargin(categoryView, BvInsets.all10);
    }

    public void onPasswordAction() {
        passwordVM.init();// to reload passwords
        this.setLeft(newCatBtn);
        this.setCenter(passwordTableView);
        BorderPane.setMargin(passwordTableView, BvInsets.all10);
    }


}
