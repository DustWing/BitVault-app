package com.bitvault.ui.views;

import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.ui.views.login.LoginVM;
import com.bitvault.ui.views.login.LoginView;
import com.bitvault.ui.views.newaccount.NewAccountVM;
import com.bitvault.ui.views.newaccount.NewAccountView;
import com.bitvault.util.Labels;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.BorderPane;

public class WelcomeView extends BorderPane {

    private final LoginView loginView;
    private final NewAccountView newAccountView;
    private final Hyperlink backLink;
    private final Hyperlink newAccountLink;

    public WelcomeView() {
        this.loginView = new LoginView(new LoginVM());
        this.newAccountView = new NewAccountView(new NewAccountVM());

        this.backLink = new Hyperlink(Labels.i18n("login"));
        this.backLink.setOnAction(event -> showLoginView());
        this.backLink.setPadding(BvInsets.all10);
        BorderPane.setAlignment(this.backLink,Pos.CENTER_LEFT);

        this.newAccountLink = new Hyperlink(Labels.i18n("new.account"));
        this.newAccountLink.setOnAction(event -> newAccount());
        this.newAccountLink.setPadding(BvInsets.all10);
        BorderPane.setAlignment(this.newAccountLink,Pos.CENTER_RIGHT);

        this.showLoginView();
    }


    private void newAccount() {
        this.setCenter(this.newAccountView);
        this.setBottom(this.backLink);
        JavaFxUtil.fadeIn(this.newAccountView);
    }

    private void showLoginView() {
        this.setCenter(this.loginView);
        this.setBottom(this.newAccountLink);
        JavaFxUtil.fadeIn(this.loginView);
    }

}
