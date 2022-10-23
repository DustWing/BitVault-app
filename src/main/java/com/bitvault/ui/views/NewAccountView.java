package com.bitvault.ui.views;

import com.bitvault.ui.components.BitVaultFlatButton;
import com.bitvault.ui.components.BitVaultVBox;
import com.bitvault.ui.components.WrappedTextField;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.views.factory.ViewFactory;
import com.bitvault.util.JavaFxUtil;
import com.bitvault.util.Labels;
import com.bitvault.ui.viewmodel.NewAccountVM;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;


public class NewAccountView extends BitVaultVBox {

    private final NewAccountVM newAccountVM;

    public NewAccountView(
            NewAccountVM newAccountVM,
            Consumer<NewAccountView> onBack
    ) {
        super();
        this.newAccountVM = newAccountVM;

        WrappedTextField username = new WrappedTextField()
                .withBinding(newAccountVM.usernameProperty())
                .withPlaceholder(Labels.i18n("username"))
                .required(true);

        WrappedTextField password = new WrappedTextField()
                .withBinding(newAccountVM.passwordProperty())
                .withPlaceholder(Labels.i18n("password"))
                .required(true);

        WrappedTextField fileName = new WrappedTextField()
                .withBinding(newAccountVM.fileNameProperty())
                .withPlaceholder(Labels.i18n("file.name"))
                .required(true);

        BitVaultFlatButton chooseFileBtn = new BitVaultFlatButton(Labels.i18n("choose.file"));
        chooseFileBtn.setOnAction(event -> chooseFileAction());
        chooseFileBtn.setDefaultButton(true);

        BitVaultFlatButton loginButton = new BitVaultFlatButton(Labels.i18n("create"));
        loginButton.setOnAction(event -> createBtnAction());
        loginButton.setDefaultButton(true);

        BitVaultFlatButton backButton = new BitVaultFlatButton(Labels.i18n("back"));
        backButton.setOnAction(event -> onBack.accept(this));
        backButton.setDefaultButton(true);

        newAccountVM.getValidatedForm().addAll(
                username,
                password,
                fileName
        );

        this.getChildren().addAll(
                username,
                password,
                fileName,
                chooseFileBtn,
                loginButton,
                backButton
        );

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

        boolean valid = newAccountVM.validate();

        if (!valid) {
            return;
        }

        final ViewFactory viewFactory = newAccountVM.create();

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
