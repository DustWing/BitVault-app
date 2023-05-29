package com.bitvault.ui.components.textfield;

import com.bitvault.ui.components.validation.ValidateField;
import com.bitvault.ui.components.validation.ValidateResult;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.Labels;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.kordamp.ikonli.javafx.FontIcon;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignE.EYE;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignE.EYE_OFF;

/**
 * PasswordInput is a stack pane that had two TextField. It will render one field at a time. This will change from normal
 * Text field to a password text field.
 */
public class BvPasswordInput extends StackPane implements ValidateField {

    private final BvTextField passwordTf;
    private final BvPasswordField passwordPf;


    public BvPasswordInput() {

        FontIcon eyeOn = new FontIcon(EYE);
        Button eyeOnBtn = new Button("", eyeOn);
        eyeOnBtn.setFocusTraversable(false);

        FontIcon eyeOff = new FontIcon(EYE_OFF);
        Button eyeOffBtn = new Button("", eyeOff);
        eyeOffBtn.setFocusTraversable(false);

        this.passwordTf = new BvTextField()
                .withDefaultSize()
                .required(true)
                .maxLength(100)
                .withPromptText(Labels.i18n("password"))
                .withRight(eyeOnBtn);
        passwordTf.setVisible(false);

        this.passwordPf = new BvPasswordField()
                .withDefaultSize()
                .required(true)
                .maxLength(100)
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

        this.getChildren().addAll(
                passwordTf,
                passwordPf
        );
    }

    public BvPasswordInput withText(String password) {
        this.passwordTf.setText(password);
        this.passwordPf.setText(password);
        return this;
    }

    public BvPasswordInput withBinding(SimpleStringProperty password) {
        this.passwordTf.withBinding(password);
        this.passwordPf.withBinding(password);
        return this;
    }


    @Override
    public ValidateResult validate() {
        this.passwordPf.validate();
        return this.passwordTf.validate();
    }


}
