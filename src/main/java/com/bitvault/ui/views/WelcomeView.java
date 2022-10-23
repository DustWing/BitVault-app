package com.bitvault.ui.views;

import com.bitvault.ui.components.BitVaultVBox;
import com.bitvault.ui.viewmodel.LoginVM;
import com.bitvault.ui.viewmodel.NewAccountVM;

import static com.bitvault.util.JavaFxUtil.changeScene;

public class WelcomeView extends BitVaultVBox {

    private final LoginView loginView;

    public WelcomeView() {
        this.loginView = new LoginView(new LoginVM(), this::newAccount);
        this.getChildren().add(loginView);
    }


    private void newAccount() {

        NewAccountView view = new NewAccountView(
                new NewAccountVM(),
                (newAccountView) -> changeScene(this, newAccountView, loginView));
        changeScene(this, loginView, view);
    }

}
