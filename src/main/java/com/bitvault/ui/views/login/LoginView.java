package com.bitvault.ui.views.login;

import com.bitvault.security.UserSession;
import com.bitvault.ui.async.AsyncTask;
import com.bitvault.ui.async.AsyncTaskException;
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
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignF.FOLDER;


public class LoginView extends VBox {

    private final LoginVM loginVM;

    public LoginView(LoginVM loginVM) {

        this.loginVM = loginVM;

        //TODO for testing - remove later
        loginVM.usernameProperty().set("a");
        loginVM.passwordProperty().set("a");
        loginVM.locationProperty().set("F:/Documents/TestFiles/test2.vault");

        final BvTextField username = getUserNameTf();

        final BvPasswordInput passwordSp = new BvPasswordInput()
                .withBinding(loginVM.passwordProperty())
                .required(true);


        final BvTextField location = getLocationTf();

        final Button loginButton = getLoginBtn();

        loginVM.getValidatedForm().addAll(
                username,
                passwordSp,
                location
        );

        this.getChildren().addAll(
                username,
                passwordSp,
                location,
                loginButton
        );

        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.setFillWidth(true);
        this.setPadding(BvInsets.all10);
        this.disableProperty().bind(loginVM.loadingProperty());
    }

    private BvTextField getUserNameTf() {

        return new BvTextField()
                .withBinding(loginVM.usernameProperty())
                .withPromptText(Labels.i18n("username"))
                .withDefaultSize()
                .required(true);
    }

    private BvTextField getLocationTf() {
        final FontIcon folderIcon = new FontIcon(FOLDER);
        final BvButton chooseFileBtn = new BvButton("", folderIcon)
                .action(event -> chooseFileAction());

        return new BvTextField()
                .withBinding(loginVM.locationProperty())
                .withPromptText(Labels.i18n("file.name"))
                .required(true)
                .withDefaultSize()
                .withRight(chooseFileBtn);
    }

    private void chooseFileAction() {

        final File file = JavaFxUtil.chooseFile(this.getScene().getWindow(), Labels.i18n("choose.file"));
        if (file != null) {
            loginVM.locationProperty().set(file.getAbsolutePath());
        }
    }

    private Button getLoginBtn() {
        return new BvButton(Labels.i18n("login"))
                .action(event -> loginBtnAction())
                .defaultButton(true)
                .withDefaultSize();
    }

    private void loginBtnAction() {

        AsyncTask.toRun(loginVM::login)
                .onSuccess(this::onLoginSuccess)
                .onFailure(this::onException)
                .start();

    }

    private void onLoginSuccess(Result<UserSession> loginResults) {

        if (loginResults.isFail()) {
            //TODO handle
            this.loginVM.loadingProperty().set(false);
            return;
        }

        final UserSession userSession = loginResults.get();

        final DashBoardView view = DashBoardView.create(userSession);
        final Scene scene = new Scene(view, 1080, 960);

        final Stage stage = (Stage) this.getScene().getWindow();
        stage.setWidth(1080);
        stage.setHeight(960);
        stage.centerOnScreen();

        //change scene
        stage.setScene(scene);
    }

    private void onException(AsyncTaskException asyncTaskException) {
        asyncTaskException.printStackTrace();
        this.loginVM.loadingProperty().set(false);
    }

}
