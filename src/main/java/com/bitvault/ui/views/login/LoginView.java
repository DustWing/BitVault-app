package com.bitvault.ui.views.login;

import com.bitvault.security.UserSession;
import com.bitvault.services.factory.IServiceFactory;
import com.bitvault.services.factory.LocalServiceFactory;
import com.bitvault.ui.components.BitVaultFlatButton;
import com.bitvault.ui.components.BitVaultVBox;
import com.bitvault.ui.components.textfield.BvPasswordField;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.model.User;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.ui.views.DashBoardView;
import com.bitvault.ui.views.factory.ViewFactory;
import com.bitvault.util.Labels;
import com.bitvault.util.Result;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignE.EYE;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignE.EYE_OFF;


public class LoginView extends BitVaultVBox {

    private final LoginVM loginVM;

    public LoginView(
            LoginVM loginVM,
            Runnable newAccBtn
    ) {

        super();
        this.loginVM = loginVM;


        BvTextField username = new BvTextField()
                .withBinding(loginVM.usernameProperty())
                .withPromptText(Labels.i18n("username"))
                .withDefaultSize()
                .withText("a")
                .isRequired(true);

        StackPane passwordSp = createPasswordField();

        BvTextField location = new BvTextField()
                .withBinding(loginVM.locationProperty())
                .withPromptText(Labels.i18n("file.name"))
                .isRequired(true)
                .withDefaultSize()
                .withText("F:/Documents/TestFiles/test2.vault");

//        loginVM.getValidatedForm().addAll(
//                username,
//                password,
//                location
//        );

        BitVaultFlatButton loginButton = new BitVaultFlatButton(Labels.i18n("login"));
        loginButton.setOnAction(event -> loginBtnAction());
        loginButton.setDefaultButton(true);

        BitVaultFlatButton newAccountBtn = new BitVaultFlatButton(Labels.i18n("new.account"));
        newAccountBtn.setOnAction(event -> newAccBtn.run());

        BitVaultFlatButton chooseFileBtn = new BitVaultFlatButton(Labels.i18n("choose.file"));
        chooseFileBtn.setOnAction(event -> chooseFileAction());


        this.getChildren().addAll(
                username,
                passwordSp,
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

    private StackPane createPasswordField(){

        FontIcon eyeOn = new FontIcon(EYE);
        Button eyeOnBtn = new Button("", eyeOn);
        eyeOnBtn.setFocusTraversable(false);

        FontIcon eyeOff = new FontIcon(EYE_OFF);
        Button eyeOffBtn = new Button("", eyeOff);
        eyeOffBtn.setFocusTraversable(false);


        final BvTextField passwordTf = new BvTextField()
                .withBinding(loginVM.passwordProperty())
                .withText("a")
                .withDefaultSize()
                .withPromptText(Labels.i18n("password"))
                .withRight(eyeOnBtn);
        passwordTf.setVisible(false);

        final BvPasswordField passwordPf = new BvPasswordField()
                .withBinding(loginVM.passwordProperty())
                .withText("a")
                .withDefaultSize()
                .withPromptText(Labels.i18n("password"))
                .withRight(eyeOffBtn);

        passwordPf.setVisible(true);

        eyeOnBtn.setOnAction(event -> {
            passwordTf.setVisible(false);
            passwordPf.setVisible(true);
            JavaFxUtil.focus(passwordPf);
        });

        eyeOffBtn.setOnAction(event -> {
            passwordTf.setVisible(true);
            passwordPf.setVisible(false);

            JavaFxUtil.focus(passwordTf);
        });

        StackPane passwordSp = new StackPane(passwordTf, passwordPf);

        return passwordSp;
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
