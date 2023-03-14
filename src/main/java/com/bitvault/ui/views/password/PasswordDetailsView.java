package com.bitvault.ui.views.password;

import com.bitvault.enums.Action;
import com.bitvault.ui.components.*;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.components.validation.ValidateForm;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.Profile;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.util.Labels;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;

import java.util.List;
import java.util.function.Consumer;

public class PasswordDetailsView extends BitVaultVBox {

    private final PasswordDetailsVM passwordDetailsVM;


    public static PasswordDetailsView edit(Password password, List<Category> categories, Profile profile, Consumer<Password> consumer) {
        final PasswordDetailsVM vm = new PasswordDetailsVM(password, categories, profile, Action.EDIT, consumer, new ValidateForm());
        return new PasswordDetailsView(vm);
    }

    public static PasswordDetailsView create(List<Category> categories, Profile profile, Consumer<Password> consumer) {
        final PasswordDetailsVM vm = new PasswordDetailsVM(null, categories, profile, Action.NEW, consumer, new ValidateForm());
        return new PasswordDetailsView(vm);
    }

    public PasswordDetailsView(PasswordDetailsVM passwordDetailsVM) {
        this.passwordDetailsVM = passwordDetailsVM;

        final BvTextField usernameTf = new BvTextField()
                .withBinding(passwordDetailsVM.userNamePropertyProperty())
                .isRequired(true)
                .withDefaultSize()
                .withPromptText(Labels.i18n("username"));

        final BvTextField passwordTf = new BvTextField()
                .withBinding(passwordDetailsVM.passwordPropertyProperty())
                .isRequired(true)
                .withDefaultSize()
                .withPromptText(Labels.i18n("password"));

        final BvTextField domainTf = new BvTextField()
                .withBinding(passwordDetailsVM.domainPropertyProperty())
                .withDefaultSize()
                .withPromptText(Labels.i18n("domain"));

        final BvTextField descriptionTf = new BvTextField()
                .withBinding(passwordDetailsVM.descriptionPropertyProperty())
                .withDefaultSize()
                .withPromptText(Labels.i18n("description"));

        final BitVaultFlatButton okButton = new BitVaultFlatButton(Labels.i18n("ok"));
        okButton.setOnAction(event -> saveBtnAction());
        okButton.setDefaultButton(true);

        final DatePicker expiresOn = new DatePicker();
        expiresOn.valueProperty().bindBidirectional(passwordDetailsVM.expiresOnProperty());

        ObservableList<Category> categories = FXCollections.observableArrayList(passwordDetailsVM.getCategories());
        final TextColorComboBox<Category> categoriesDd = TextColorComboBox.withRectangle(categories);
        categoriesDd.valueProperty().bindBidirectional(passwordDetailsVM.selectedCatProperty());


//        passwordDetailsVM.getValidatedForm().add(usernameTf);
//        passwordDetailsVM.getValidatedForm().add(passwordTf);

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
