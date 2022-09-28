package com.bitvault.views;

import com.bitvault.components.*;
import com.bitvault.enums.Action;
import com.bitvault.model.Category;
import com.bitvault.model.Password;
import com.bitvault.model.Profile;
import com.bitvault.util.BvInsets;
import com.bitvault.util.Labels;
import com.bitvault.viewmodel.PasswordDetailsVM;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;

import java.util.List;
import java.util.function.Consumer;

public class PasswordDetailsView extends BitVaultVBox {

    private final PasswordDetailsVM passwordDetailsVM;


    public static PasswordDetailsView edit(Password password, List<Category> categories, Profile profile, Consumer<Password> consumer) {
        final PasswordDetailsVM vm = new PasswordDetailsVM(password, categories, profile, Action.EDIT, consumer, new ValidatedForm());
        return new PasswordDetailsView(vm);
    }

    public static PasswordDetailsView create(List<Category> categories, Profile profile, Consumer<Password> consumer) {
        final PasswordDetailsVM vm = new PasswordDetailsVM(null, categories, profile, Action.NEW, consumer, new ValidatedForm());
        return new PasswordDetailsView(vm);
    }

    public PasswordDetailsView(PasswordDetailsVM passwordDetailsVM) {
        this.passwordDetailsVM = passwordDetailsVM;

        final WrappedTextField usernameTf = new WrappedTextField()
                .withBinding(passwordDetailsVM.userNamePropertyProperty())
                .required(true)
                .withPlaceholder(Labels.i18n("username"));

        final WrappedTextField passwordTf = new WrappedTextField()
                .withBinding(passwordDetailsVM.passwordPropertyProperty())
                .required(true)
                .withPlaceholder(Labels.i18n("password"));

        final WrappedTextField domainTf = new WrappedTextField()
                .withBinding(passwordDetailsVM.domainPropertyProperty())
                .withPlaceholder(Labels.i18n("domain"));

        final WrappedTextField descriptionTf = new WrappedTextField()
                .withBinding(passwordDetailsVM.descriptionPropertyProperty())
                .withPlaceholder(Labels.i18n("description"));

        final BitVaultFlatButton okButton = new BitVaultFlatButton(Labels.i18n("ok"));
        okButton.setOnAction(event -> saveBtnAction());
        okButton.setDefaultButton(true);

        final DatePicker expiresOn = new DatePicker();
        expiresOn.valueProperty().bindBidirectional(passwordDetailsVM.expiresOnProperty());

        final TextColorComboBox<Category> categoriesDd = new TextColorComboBox<>(
                FXCollections.observableArrayList(
                        passwordDetailsVM.getCategories()
                )
        );
        categoriesDd.valueProperty().bindBidirectional(passwordDetailsVM.selectedCatProperty());


        passwordDetailsVM.getValidatedForm().add(usernameTf);
        passwordDetailsVM.getValidatedForm().add(passwordTf);

        this.getChildren().addAll(
                usernameTf,
                passwordTf,
                domainTf,
                descriptionTf,
                expiresOn,
                categoriesDd,
                okButton
        );

        this.setAlignment(Pos.TOP_CENTER);
        this.setFillWidth(true);
        this.setPadding(BvInsets.all10);
//        super.vGrowAlways();
    }


    public void saveBtnAction() {
        boolean valid = passwordDetailsVM.save();

        if (valid) {
            this.getScene().getWindow().hide();
        }
    }


}
