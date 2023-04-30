package com.bitvault.ui.views.login;

import com.bitvault.security.UserSession;
import com.bitvault.ui.async.AsyncTask;
import com.bitvault.ui.async.AsyncTaskException;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.alert.ErrorAlert;
import com.bitvault.ui.components.textfield.BvPasswordInput;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.components.validation.ValidateForm;
import com.bitvault.ui.components.validation.ValidateResult;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.utils.BvSceneSize;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.ui.utils.ViewLoader;
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
    private final ValidateForm validateForm;


    public LoginView(LoginVM loginVM) {
        this.loginVM = loginVM;

        final BvTextField username = getUserNameTf();

        final BvPasswordInput passwordSp = new BvPasswordInput()
                .withBinding(loginVM.passwordProperty())
                .required(true);

        final BvTextField location = getLocationTf();

        final Button loginButton = getLoginBtn();

        this.validateForm = new ValidateForm();
        this.validateForm.addAll(username, passwordSp, location);

        this.getChildren().addAll(username, passwordSp, location, loginButton);
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

        ValidateResult validate = validateForm.validate();
        if(!validate.valid()){
            return;
        }

        AsyncTask.toRun(loginVM::login)
                .onSuccess(this::onLoginSuccess)
                .onFailure(this::onException)
                .start();

    }

    private void onLoginSuccess(Result<UserSession> loginResults) {

        if (loginResults.isFail()) {
            this.loginVM.loadingProperty().set(false);
            ErrorAlert.show("woops",loginResults.getError());
            return;
        }

        final UserSession userSession = loginResults.get();

        final DashBoardView view = DashBoardView.create(userSession);
        final BvSceneSize aDefault = BvSceneSize.Default;
        final Stage stage = (Stage) this.getScene().getWindow();

        ViewLoader.load(stage, aDefault.width(), aDefault.height(), () -> view);

    }

    private void onException(AsyncTaskException asyncTaskException) {
        asyncTaskException.printStackTrace();
        this.loginVM.loadingProperty().set(false);
    }

}
