package com.bitvault.ui.views.password;

import com.bitvault.enums.Action;
import com.bitvault.ui.components.BitVaultFlatButton;
import com.bitvault.ui.components.BitVaultVBox;
import com.bitvault.ui.components.BvScaffold;
import com.bitvault.ui.components.category.BvCategoryDd;
import com.bitvault.ui.components.textfield.BvPasswordInput;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.components.validation.ValidateForm;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.Profile;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.Labels;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;

import java.util.List;
import java.util.function.Consumer;

public class PasswordDetailsView extends BitVaultVBox {

    private final PasswordDetailsVM passwordDetailsVM;


    public static PasswordDetailsView edit(Password password, List<Category> categories, Profile profile, Consumer<Password> onAction) {
        final PasswordDetailsVM vm = new PasswordDetailsVM(password, categories, profile, Action.EDIT, onAction, new ValidateForm());
        return new PasswordDetailsView(vm);
    }

    public static PasswordDetailsView create(List<Category> categories, Profile profile, Consumer<Password> onAction) {
        final PasswordDetailsVM vm = new PasswordDetailsVM(null, categories, profile, Action.NEW, onAction, new ValidateForm());
        return new PasswordDetailsView(vm);
    }

    public PasswordDetailsView(PasswordDetailsVM passwordDetailsVM) {
        this.passwordDetailsVM = passwordDetailsVM;

        final BvTextField usernameTf = new BvTextField()
                .withBinding(passwordDetailsVM.userNamePropertyProperty())
                .setRequired(true)
                .withDefaultSize()
                .withPromptText(Labels.i18n("username"));


        final BvPasswordInput bvPasswordInput = new BvPasswordInput(passwordDetailsVM.passwordPropertyProperty());


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


        final BvCategoryDd bvCategoryDd = new BvCategoryDd(
                passwordDetailsVM.getCategories(),
                passwordDetailsVM.newCategoryNameProperty(),
                passwordDetailsVM::setSelectedColor);


//        passwordDetailsVM.getValidatedForm().add(usernameTf);
//        passwordDetailsVM.getValidatedForm().add(passwordTf);

        List<Node> list = List.of(usernameTf,
                bvPasswordInput,
                domainTf,
                descriptionTf,
                expiresOn,
                bvCategoryDd
        );
        BvScaffold bvScaffold = new BvScaffold(list, okButton);

        this.getChildren().add(bvScaffold);

        this.setAlignment(Pos.TOP_CENTER);
        this.setFillWidth(true);
        this.setPadding(BvInsets.all10);
        JavaFxUtil.vGrowAlways(this);
    }


    public void saveBtnAction() {
        boolean valid = passwordDetailsVM.save();

        if (valid) {
            this.getScene().getWindow().hide();
        }
    }


}
