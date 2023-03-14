package com.bitvault.ui.views;

import com.bitvault.ui.components.BitVaultVBox;
import com.bitvault.ui.views.login.LoginVM;
import com.bitvault.ui.views.newaccount.NewAccountVM;
import com.bitvault.ui.views.login.LoginView;
import com.bitvault.ui.views.newaccount.NewAccountView;
import javafx.geometry.Pos;

import static com.bitvault.ui.utils.JavaFxUtil.changeScene;

public class WelcomeView extends BitVaultVBox {

    private final LoginView loginView;

    public WelcomeView() {
        this.loginView = new LoginView(new LoginVM(), this::newAccount);
        this.getChildren().add(loginView);
        this.setAlignment(Pos.CENTER);
    }


    private void newAccount() {

        NewAccountView view = new NewAccountView(
                new NewAccountVM(),
                (newAccountView) -> changeScene(this, newAccountView, loginView)
        );
        changeScene(this, loginView, view);
    }

}
