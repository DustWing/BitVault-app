package com.bitvault.ui.views.login;

import com.bitvault.security.UserSession;
import com.bitvault.services.factory.IServiceFactory;
import com.bitvault.services.factory.LocalServiceFactory;
import com.bitvault.ui.components.BitVaultVBox;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.textfield.BvPasswordInput;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.model.User;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.ui.views.dashboard.DashBoardView;
import com.bitvault.ui.views.factory.ViewFactory;
import com.bitvault.util.Labels;
import com.bitvault.util.Result;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
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


        BvButton newAccountBtn = new BvButton(Labels.i18n("new.account"))
                .action(event -> newAccBtn.run())
                .withDefaultSize();

        BvButton chooseFileBtn = new BvButton(Labels.i18n("choose.file"))
                .action(event -> chooseFileAction())
                .withDefaultSize();;


        this.getChildren().addAll(
                username,
                passwordSp,
                location,
                chooseFileBtn,
                loginButton,
                newAccountBtn
        );

        this.setSpacing(10);
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

        if (!loginVM.validate()) {
            return;
        }

        final UserSession userSession = UserSession.newSession(loginVM.getLocation(), loginVM.getUsername(), loginVM.getPassword());

        final IServiceFactory serviceFactory = new LocalServiceFactory(loginVM.getLocation(), userSession);

        final User user = new User(
                null,
                loginVM.getUsername(),
                loginVM.getPassword()
        );


        Result<User> authResult = serviceFactory.getUserService()
                .authenticate(user);

        if (authResult.isFail()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error?");
            alert.setContentText(authResult.getError().getMessage());
            alert.showAndWait();
            return;
        }


        final ViewFactory viewFactory = new ViewFactory(userSession, serviceFactory);
        final DashBoardView view = viewFactory.getDashboardView();
        final Scene scene = new Scene(view, 1080, 960);

        final Stage stage = (Stage) this.getScene().getWindow();
        stage.setWidth(1080);
        stage.setHeight(960);
        stage.centerOnScreen();

        //change scene
        stage.setScene(scene);

    }

}
