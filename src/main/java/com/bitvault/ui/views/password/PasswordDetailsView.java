package com.bitvault.ui.views.password;

import com.bitvault.enums.Action;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.BvDoubleColumn;
import com.bitvault.ui.components.BvScaffold;
import com.bitvault.ui.components.TextColorComboBox;
import com.bitvault.ui.components.alert.ErrorAlert;
import com.bitvault.ui.components.textfield.BvPasswordInput;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.components.validation.ValidateForm;
import com.bitvault.ui.components.validation.ValidateResult;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.Profile;
import com.bitvault.ui.utils.BvHeights;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.Labels;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;

public class PasswordDetailsView extends VBox {

    private final PasswordDetailsVM passwordDetailsVM;

    private final ValidateForm validatedForm = new ValidateForm();


    public static PasswordDetailsView editPassword(Password password, List<Category> categories, Profile profile, Consumer<Password> onAction) {
        final PasswordDetailsVM vm = new PasswordDetailsVM(password, categories, profile, Action.EDIT, onAction, new ValidateForm());
        return new PasswordDetailsView(vm);
    }

    public static PasswordDetailsView newPassword(List<Category> categories, Profile profile, Consumer<Password> onAction) {
        final PasswordDetailsVM vm = new PasswordDetailsVM(null, categories, profile, Action.NEW, onAction, new ValidateForm());
        return new PasswordDetailsView(vm);
    }

    public PasswordDetailsView(PasswordDetailsVM passwordDetailsVM) {
        this.passwordDetailsVM = passwordDetailsVM;

        final BvTextField titleTf = getTitleTf();

        final GridPane usernamePassword = createUsernamePassword();

        final BvTextField domainTf = getDomainTf();

        final TextArea descriptionTf = createDescription();

        final DatePicker expiresOn = getExpiryDp();

        ObservableList<Category> categories = FXCollections.observableArrayList(passwordDetailsVM.getCategories());
        final TextColorComboBox<Category> categoriesDd = TextColorComboBox.withCircle(categories);
        categoriesDd.valueProperty().bindBidirectional(passwordDetailsVM.selectedCatProperty());
        JavaFxUtil.defaultSize(categoriesDd);

        BvDoubleColumn expiryCategory = new BvDoubleColumn(List.of(expiresOn), List.of(categoriesDd));

        final BvButton okButton = new BvButton(Labels.i18n("save"))
                .withDefaultSize()
                .action(event -> saveBtnAction())
                .defaultButton(true);
        BorderPane.setAlignment(okButton, Pos.CENTER);


        List<Node> list = List.of(
                titleTf,
                usernamePassword,
                domainTf,
                descriptionTf,
                expiryCategory
        );
        BvScaffold bvScaffold = new BvScaffold(list, okButton);

        this.getChildren().add(bvScaffold);

        this.setAlignment(Pos.TOP_CENTER);
        this.setFillWidth(true);
        this.setPrefSize(600, 500);
        JavaFxUtil.vGrowAlways(this);
    }

    private BvTextField getTitleTf() {

        final BvTextField titleTf = new BvTextField()
                .withBinding(passwordDetailsVM.titlePropertyProperty())
                .required(true)
                .withDefaultSize()
                .withPromptText(Labels.i18n("title"));
        return titleTf;
    }

    private GridPane createUsernamePassword() {

        final BvTextField usernameTf = new BvTextField()
                .withBinding(passwordDetailsVM.userNamePropertyProperty())
                .required(true)
                .withDefaultSize()
                .withPromptText(Labels.i18n("username"));
        validatedForm.add(usernameTf);


        final BvPasswordInput passwordInput = new BvPasswordInput()
                .withBinding(passwordDetailsVM.passwordPropertyProperty());
        validatedForm.add(passwordInput);

        return new BvDoubleColumn(List.of(usernameTf), List.of(passwordInput));
    }

    private BvTextField getDomainTf() {
        final BvTextField domainTf = new BvTextField()
                .withBinding(passwordDetailsVM.domainPropertyProperty())
                .withSize(410, BvHeights.MEDIUM)
                .withPromptText(Labels.i18n("domain"));
        return domainTf;
    }

    private TextArea createDescription() {
        TextArea descriptionTf = new TextArea();
        descriptionTf.textProperty().bindBidirectional(passwordDetailsVM.descriptionPropertyProperty());
        descriptionTf.setPromptText(Labels.i18n("description"));
        descriptionTf.setMinWidth(410);
        descriptionTf.setMaxWidth(410);
        descriptionTf.setMaxHeight(BvHeights.LARGE);
        descriptionTf.setMinHeight(BvHeights.LARGE);
        return descriptionTf;
    }

    private DatePicker getExpiryDp() {
        final DatePicker expiresOn = new DatePicker();
        expiresOn.setPromptText(Labels.i18n("expires.on"));
        JavaFxUtil.defaultSize(expiresOn);
        expiresOn.valueProperty().bindBidirectional(this.passwordDetailsVM.expiresOnProperty());
        return expiresOn;
    }


    public void saveBtnAction() {


        ValidateResult validateResult = validatedForm.validate();

        if (!validateResult.valid()) {
            ErrorAlert.show("New Password", validateResult.errorMessages().toString());
            return;
        }
        passwordDetailsVM.save();
        this.getScene().getWindow().hide();
    }
}
