package com.bitvault.ui.views;

import com.bitvault.ui.components.BitVaultFlatButton;
import com.bitvault.ui.components.BitVaultVBox;
import com.bitvault.ui.components.WrappedTextField;
import com.bitvault.ui.model.User;
import com.bitvault.services.factory.IServiceFactory;
import com.bitvault.services.factory.LocalServiceFactory;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.views.factory.ViewFactory;
import com.bitvault.util.JavaFxUtil;
import com.bitvault.util.Labels;
import com.bitvault.ui.viewmodel.LoginVM;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class LoginView extends BitVaultVBox {

    private final LoginVM loginVM;

    public LoginView(
            LoginVM loginVM,
            Runnable newAccBtn
    ) {

        super();
        this.loginVM = loginVM;

        WrappedTextField username = new WrappedTextField()
                .withBinding(loginVM.usernameProperty())
                .withPlaceholder(Labels.i18n("username"))
                .required(true);

        WrappedTextField password = new WrappedTextField()
                .withBinding(loginVM.passwordProperty())
                .withPlaceholder(Labels.i18n("password"))
                .required(true);


        WrappedTextField location = new WrappedTextField()
                .withBinding(loginVM.locationProperty())
                .withPlaceholder(Labels.i18n("file.name"))
                .required(true);

        loginVM.getValidatedForm().addAll(
                username,
                password,
                location
        );

        BitVaultFlatButton loginButton = new BitVaultFlatButton(Labels.i18n("login"));
        loginButton.setOnAction(event -> loginBtnAction());
        loginButton.setDefaultButton(true);

        BitVaultFlatButton newAccountBtn = new BitVaultFlatButton(Labels.i18n("new.account"));
        newAccountBtn.setOnAction(event -> newAccBtn.run());

        BitVaultFlatButton chooseFileBtn = new BitVaultFlatButton(Labels.i18n("choose.file"));
        chooseFileBtn.setOnAction(event -> chooseFileAction());
        chooseFileBtn.setDefaultButton(true);


        loginVM.getValidatedForm().addAll(
                username,
                password
        );

        this.getChildren().addAll(
                username,
                password,
                location,
                chooseFileBtn,
                loginButton,
                newAccountBtn
        );

        this.setAlignment(Pos.CENTER);
        this.setFillWidth(true);
        this.setPadding(BvInsets.all10);
//        super.vGrowAlways();
    }

    private void chooseFileAction() {

        final File file = JavaFxUtil.chooseFile(this.getScene().getWindow(), Labels.i18n("choose.file"));
        if (file != null) {
            loginVM.locationProperty().set(file.getAbsolutePath());
        }
    }

    private void loginBtnAction() {


        var login = loginVM.login();
        if (login) {


            final IServiceFactory serviceFactory = new LocalServiceFactory(loginVM.getLocation());

            final User user = new User(
                    null,
                    loginVM.getUsername(),
                    loginVM.getPassword()
            );

            serviceFactory.getUserService().authenticate(user)
                    .apply(
                            user1 -> {
                            },
                            exception -> {
                            }
                    );


            ViewFactory viewFactory = new ViewFactory(serviceFactory);
            final DashBoardView view = viewFactory.getDashboardView();
            final Scene scene = new Scene(view, 1080, 960);
            //copy css
            scene.getStylesheets().addAll(this.getScene().getStylesheets());

            final Stage stage = (Stage) this.getScene().getWindow();
            stage.setWidth(1080);
            stage.setHeight(960);
            stage.centerOnScreen();

            //change scene
            stage.setScene(scene);

        }
    }

}
