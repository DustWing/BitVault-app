package com.bitvault.ui.views.password;

import com.bitvault.enums.Action;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.BvDoubleColumn;
import com.bitvault.ui.components.TextColorComboBox;
import com.bitvault.ui.components.alert.ErrorAlert;
import com.bitvault.ui.components.textfield.BvPasswordInput;
import com.bitvault.ui.components.textfield.BvTextField;
import com.bitvault.ui.components.validation.ValidateForm;
import com.bitvault.ui.components.validation.ValidateResult;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.Profile;
import com.bitvault.ui.utils.*;
import com.bitvault.util.Labels;
import com.bitvault.util.Messages;
import com.bitvault.util.PasswordUtils;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignR.REFRESH;


public class PasswordDetailsView extends BorderPane {

    private final PasswordDetailsVM passwordDetailsVM;

    private final ValidateForm validatedForm = new ValidateForm();

    private final SimpleDoubleProperty passBarProperty = new SimpleDoubleProperty();

    public static PasswordDetailsView test() {
        var categories = List.of(Category.createFake("TestId", "Test", "#FFFFFF"));
        var profile = new Profile("TestId", "TestProf", LocalDateTime.now(), null);
        Consumer<Password> onAction = System.out::println;

        final PasswordDetailsVM vm = new PasswordDetailsVM(null, categories, profile, Action.NEW, onAction);
        return new PasswordDetailsView(vm);
    }

    public static PasswordDetailsView editPassword(Password password, List<Category> categories, Profile profile, Consumer<Password> onAction) {
        final PasswordDetailsVM vm = new PasswordDetailsVM(password, categories, profile, Action.EDIT, onAction);
        return new PasswordDetailsView(vm);
    }

    public static PasswordDetailsView newPassword(List<Category> categories, Profile profile, Consumer<Password> onAction) {
        final PasswordDetailsVM vm = new PasswordDetailsVM(null, categories, profile, Action.NEW, onAction);
        return new PasswordDetailsView(vm);
    }

    public PasswordDetailsView(PasswordDetailsVM passwordDetailsVM) {
        this.passwordDetailsVM = passwordDetailsVM;

        final BvTextField titleTf = getTitleTf();

        final BvTextField userName = getUserNameTf(passwordDetailsVM.userNamePropertyProperty());
        validatedForm.add(userName);

        final BvPasswordInput passwordInput = getPassword(passwordDetailsVM.passwordPropertyProperty());
        validatedForm.add(passwordInput);
        final Button generatePassBtn = generatePassBtn(passwordDetailsVM.passwordPropertyProperty());
        final BvDoubleColumn passwordGenerate = BvDoubleColumn.createSingle(passwordInput, generatePassBtn);

        //bind password property string -> password complexity
        passwordDetailsVM.passwordPropertyProperty()
                .addListener((observable, oldValue, newValue) ->
                        this.passBarProperty.set(calculatePasswordComplexity(this.passwordDetailsVM.getPasswordProperty()))
                );
        //init password complexity bar
        this.passBarProperty.set(calculatePasswordComplexity(this.passwordDetailsVM.getPasswordProperty()));
        final ProgressBar progressBar = passwordBar(this.passBarProperty);

        final BvTextField domainTf = getDomainTf();

        final TextArea descriptionTf = createDescription();

        final DatePicker expiresOn = getExpiryDp();
        final TextColorComboBox<Category> categoriesDd = categoryDd();
        final BvDoubleColumn expiryCategory = BvDoubleColumn.createSingle(expiresOn, categoriesDd);

        final CheckBox masterPassword = masterPassword();


        VBox vBox = new VBox(
                titleTf,
                userName,
                passwordGenerate,
                progressBar,
                domainTf,
                descriptionTf,
                expiryCategory,
                masterPassword
        );

        vBox.setSpacing(BvSpacing.SMALL);
        vBox.setAlignment(Pos.CENTER);
        vBox.setMaxWidth(BvWidths.LARGE);
        BorderPane.setMargin(vBox, BvInsets.top10);

        final Button saveButton = saveButton();

        this.setCenter(vBox);
        this.setBottom(saveButton);

    }

    private BvTextField getTitleTf() {

        final BvTextField titleTf = new BvTextField()
                .withBinding(passwordDetailsVM.titlePropertyProperty())
                .required(true)
                .maxLength(50)
                .withDefaultSize()
                .withPromptText(Labels.i18n("title"));
        return titleTf;
    }

    private BvTextField getUserNameTf(SimpleStringProperty stringProperty) {
        final BvTextField usernameTf = new BvTextField()
                .withBinding(stringProperty)
                .required(true)
                .maxLength(50)
                .withDefaultSize()
                .withPromptText(Labels.i18n("username"));
        return usernameTf;
    }

    private BvPasswordInput getPassword(SimpleStringProperty stringProperty) {
        final BvPasswordInput passwordInput = new BvPasswordInput()
                .withBinding(stringProperty);
        return passwordInput;
    }

    private Button generatePassBtn(SimpleStringProperty stringProperty) {

        final FontIcon refresh = new FontIcon(REFRESH);

        //TODO this should be configurable - implement settings
        BvButton button = new BvButton("", refresh)
                .action(event -> stringProperty.set(PasswordUtils.generatePassString(16)));
        button.setTooltip(new Tooltip(Labels.i18n("generate")));
        return button;
    }

    private BvTextField getDomainTf() {
        final BvTextField domainTf = new BvTextField()
                .withBinding(passwordDetailsVM.domainPropertyProperty())
                .withDefaultSize()
                .withPromptText(Labels.i18n("domain"));
        return domainTf;
    }

    private TextArea createDescription() {
        TextArea descriptionTf = new TextArea();
        descriptionTf.textProperty().bindBidirectional(passwordDetailsVM.descriptionPropertyProperty());
        descriptionTf.setPromptText(Labels.i18n("description"));
        descriptionTf.setMinWidth(BvWidths.SMALL);
        descriptionTf.setPrefWidth(BvWidths.LARGE);
        descriptionTf.setMaxWidth(BvWidths.LARGE);
        descriptionTf.setPrefHeight(BvHeights.LARGE);
        descriptionTf.setWrapText(true);
        return descriptionTf;
    }

    private DatePicker getExpiryDp() {
        final DatePicker expiresOn = new DatePicker();
        expiresOn.setPromptText(Labels.i18n("expires.on"));
        JavaFxUtil.mediumSize(expiresOn);
        expiresOn.valueProperty().bindBidirectional(this.passwordDetailsVM.expiresOnProperty());
        return expiresOn;
    }

    private TextColorComboBox<Category> categoryDd() {

        ObservableList<Category> categories = FXCollections.observableArrayList(passwordDetailsVM.getCategories());
        final TextColorComboBox<Category> categoriesDd = TextColorComboBox.withCircle(categories);
        categoriesDd.valueProperty().bindBidirectional(passwordDetailsVM.selectedCatProperty());
        JavaFxUtil.mediumSize(categoriesDd);
        return categoriesDd;
    }

    private CheckBox masterPassword() {
        CheckBox checkBox = new CheckBox(Labels.i18n("master.password"));
        checkBox.selectedProperty().bindBidirectional(passwordDetailsVM.requiredMpProperty());
        checkBox.setTooltip(new Tooltip(Messages.i18n("master.password.tooltip")));
        return checkBox;
    }

    private Button saveButton() {
        final BvButton button = new BvButton(Labels.i18n("save"))
                .withDefaultSize()
                .action(event -> saveBtnAction())
                .defaultButton(true);
        BorderPane.setMargin(button, BvInsets.all10);
        BorderPane.setAlignment(button, Pos.CENTER);
        return button;
    }

    private ProgressBar passwordBar(SimpleDoubleProperty passBarProperty) {
        final var width = 300;

        final ProgressBar bar = new ProgressBar(0);
        bar.getStyleClass().add("large");
        bar.setPrefWidth(width);
        bar.setMaxWidth(width);

        bar.progressProperty().addListener((obs, old, val) -> {
            if (val == null) {
                return;
            }
            setBarProgress(bar, val.doubleValue());
        });

        bar.progressProperty().bind(passBarProperty);

        return bar;
    }

    private void setBarProgress(ProgressBar bar, double value) {
        if (value > 0.80) {
            bar.pseudoClassStateChanged(BvStyles.STATE_SUCCESS, true);
            bar.pseudoClassStateChanged(BvStyles.STATE_WARNING, false);
            bar.pseudoClassStateChanged(BvStyles.STATE_DANGER, false);
        } else if (value > 0.47) {
            bar.pseudoClassStateChanged(BvStyles.STATE_SUCCESS, false);
            bar.pseudoClassStateChanged(BvStyles.STATE_WARNING, true);
            bar.pseudoClassStateChanged(BvStyles.STATE_DANGER, false);
        } else {
            bar.pseudoClassStateChanged(BvStyles.STATE_SUCCESS, false);
            bar.pseudoClassStateChanged(BvStyles.STATE_WARNING, false);
            bar.pseudoClassStateChanged(BvStyles.STATE_DANGER, true);
        }
    }

    private double calculatePasswordComplexity(String password) {
        double value = 0.0;
        if (password == null || password.isBlank()) {
            value = 0.0;
        } else if (password.length() >= 16) {
            value = 1.0;
        } else if (password.length() >= 8) {
            value = 0.5;
        } else if (password.length() >= 3) {
            value = 0.3;
        }
        return value;
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
