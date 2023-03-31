package com.bitvault.ui.views.login;

import com.bitvault.security.UserSession;
import com.bitvault.ui.components.BitVaultVBox;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.textfield.BvPasswordInput;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.ui.views.dashboard.DashBoardView;
import com.bitvault.util.Labels;
import com.bitvault.util.Result;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;


public class LoginView extends BitVaultVBox {

    private final LoginVM loginVM;

    public LoginView(LoginVM loginVM) {

        super();
        this.loginVM = loginVM;


        final BvTextField username = new BvTextField()
                .withBinding(loginVM.usernameProperty())
                .withPromptText(Labels.i18n("username"))
                .withDefaultSize()
                .withText("a")
                .setRequired(true);

        loginVM.passwordProperty().set("a");//TODO for testing
        final StackPane passwordSp = new BvPasswordInput(loginVM.passwordProperty());

        BvTextField location = new BvTextField()
                .withBinding(loginVM.locationProperty())
                .withPromptText(Labels.i18n("file.name"))
                .setRequired(true)
                .withDefaultSize()
                .withText("F:/Documents/TestFiles/test2.vault");

//        loginVM.getValidatedForm().addAll(
//                username,
//                password,
//                location
//        );

        BvButton loginButton = new BvButton(Labels.i18n("login"))
                .action(event -> loginBtnAction())
                .defaultButton(true)
                .withDefaultSize();


        BvButton chooseFileBtn = new BvButton(Labels.i18n("choose.file"))
                .action(event -> chooseFileAction())
                .withDefaultSize();


        this.getChildren().addAll(
                username,
                passwordSp,
                location,
                chooseFileBtn,
                loginButton
        );

        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.setFillWidth(true);
        this.setPadding(BvInsets.all10);
    }


    private void chooseFileAction() {

        final File file = JavaFxUtil.chooseFile(this.getScene().getWindow(), Labels.i18n("choose.file"));
        if (file != null) {
            loginVM.locationProperty().set(file.getAbsolutePath());
        }
    }

    private void loginBtnAction() {

        final Result<UserSession> loginResults = loginVM.login();

        if (loginResults.isFail()) {
            //TODO handle
            return;
        }

        final UserSession userSession = loginResults.get();

        final DashBoardView view =  DashBoardView.create(userSession);
        final Scene scene = new Scene(view, 1080, 960);

        final Stage stage = (Stage) this.getScene().getWindow();
        stage.setWidth(1080);
        stage.setHeight(960);
        stage.centerOnScreen();

        //change scene
        stage.setScene(scene);

    }

}
