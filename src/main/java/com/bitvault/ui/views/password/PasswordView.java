package com.bitvault.ui.views.password;

import com.bitvault.ui.components.BitVaultHBox;
import com.bitvault.ui.components.BitVaultVBox;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.TimerBar;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.hyperlink.HyperLinkCell;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.ui.utils.KeyCombinationConst;
import com.bitvault.ui.views.categories.CategoryVM;
import com.bitvault.ui.views.categories.CategoryView;
import com.bitvault.util.Labels;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;


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
