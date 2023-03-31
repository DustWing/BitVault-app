package com.bitvault.ui.views.newaccount;

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
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class NewAccountView extends BitVaultVBox {

    private final NewAccountVM newAccountVM;

    public NewAccountView(NewAccountVM newAccountVM) {
        super();
        this.newAccountVM = newAccountVM;

        BvTextField username = new BvTextField()
                .withBinding(newAccountVM.usernameProperty())
                .withPromptText(Labels.i18n("username"))
                .withDefaultSize()
                .setRequired(true);

        BvPasswordInput password = new BvPasswordInput(newAccountVM.passwordProperty());

        BvTextField fileName = new BvTextField()
                .withBinding(newAccountVM.fileNameProperty())
                .withPromptText(Labels.i18n("file.name"))
                .withDefaultSize()
                .setRequired(true);

        BvButton chooseFileBtn = new BvButton(Labels.i18n("choose.file")).withDefaultSize();
        chooseFileBtn.setOnAction(event -> chooseFileAction());


        BvButton loginButton = new BvButton(Labels.i18n("create")).withDefaultSize();
        loginButton.setOnAction(event -> createBtnAction());
        loginButton.setDefaultButton(true);


//        newAccountVM.getValidatedForm().addAll(
//                username,
//                password,
//                fileName
//        );

        this.getChildren().addAll(
                username,
                password,
                fileName,
                chooseFileBtn,
                loginButton
        );

        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.setFillWidth(true);
        this.setPadding(BvInsets.all10);

    }

    private void chooseFileAction() {

        final File file = JavaFxUtil.chooseDir(this.getScene().getWindow(), Labels.i18n("choose.file"));
        try {
            if (file != null) {
                newAccountVM.locationProperty().set(file.getCanonicalPath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createBtnAction() {

        final Result<UserSession> userSessionResult = newAccountVM.create();

        if (userSessionResult.isFail()) {
            //TODO handle
            return;
        }

        final UserSession userSession = userSessionResult.get();

        final DashBoardView view = DashBoardView.create(userSession);
        final Scene scene = new Scene(view, 1080, 960);

        final Stage stage = (Stage) this.getScene().getWindow();
        stage.setWidth(1080);
        stage.setHeight(960);
        stage.centerOnScreen();

        //change scene
        stage.setScene(scene);


    }
}
