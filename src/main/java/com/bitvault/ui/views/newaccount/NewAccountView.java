package com.bitvault.ui.views.newaccount;

import com.bitvault.security.UserSession;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.alert.ErrorAlert;
import com.bitvault.ui.components.textfield.BvPasswordInput;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.utils.*;
import com.bitvault.ui.views.dashboard.DashBoardView;
import com.bitvault.util.Labels;
import com.bitvault.util.Messages;
import com.bitvault.util.Result;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.IOException;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignF.FOLDER;


public class NewAccountView extends VBox {

    private final NewAccountVM newAccountVM;

    public NewAccountView(NewAccountVM newAccountVM) {
        super();
        this.newAccountVM = newAccountVM;

        BvTextField username = new BvTextField()
                .withBinding(newAccountVM.usernameProperty())
                .withPromptText(Labels.i18n("username"))
                .withDefaultSize()
                .required(true);

        BvPasswordInput password = new BvPasswordInput()
                .withBinding(newAccountVM.passwordProperty());


        final Label fileFolderLbl = new Label();
        newAccountVM.locationProperty().addListener((observable, oldValue, newValue) ->
                {
                    if (newValue != null) {
                        fileFolderLbl.setText(newValue);
                    }
                }
        );

        final FontIcon folderIcon = new FontIcon(FOLDER);
        final BvButton chooseFileBtn = new BvButton("", folderIcon);
        chooseFileBtn.setOnAction(event -> chooseFileAction());

        final BvTextField filePath = new BvTextField()
                .withBinding(newAccountVM.locationProperty())
                .withPromptText(Labels.i18n("file.path"))
                .withDefaultSize()
                .withRight(chooseFileBtn)
                .required(true)
                .toolTip(Messages.i18n("choose.database.file.location"));

        final BvTextField fileName = new BvTextField()
                .withBinding(newAccountVM.fileNameProperty())
                .withPromptText(Labels.i18n("file.name"))
                .withDefaultSize()
                .required(true)
                .toolTip(Messages.i18n("choose.database.name"));

        BvButton loginButton = new BvButton(Labels.i18n("create")).withDefaultSize();
        loginButton.setOnAction(event -> createBtnAction());
        loginButton.setDefaultButton(true);


        newAccountVM.getValidatedForm().addAll(
                username,
                password,
                filePath,
                fileName
        );

        this.getChildren().addAll(
                username,
                password,
                filePath,
                fileName,
                loginButton
        );

        this.setSpacing(BvSpacing.SMALL);
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

        if (userSessionResult.hasError()) {
            ErrorAlert.show("New account error", userSessionResult.getError());
            return;
        }

        final UserSession userSession = userSessionResult.get();

        final DashBoardView view = DashBoardView.create(userSession);
        final BvSceneSize aDefault = BvSceneSize.Default;
        final Stage stage = (Stage) this.getScene().getWindow();

        ViewLoader.load(stage, aDefault.width(), aDefault.height(), () -> view);

    }
}
