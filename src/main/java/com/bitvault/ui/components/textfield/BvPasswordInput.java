package com.bitvault.ui.components.textfield;

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
public class BvPasswordInput extends StackPane {

    /**
     * @param password bind string property to UI fields
     */
    public BvPasswordInput(SimpleStringProperty password) {


        FontIcon eyeOn = new FontIcon(EYE);
        Button eyeOnBtn = new Button("", eyeOn);
        eyeOnBtn.setFocusTraversable(false);

        FontIcon eyeOff = new FontIcon(EYE_OFF);
        Button eyeOffBtn = new Button("", eyeOff);
        eyeOffBtn.setFocusTraversable(false);


        final BvTextField passwordTf = new BvTextField()
                .withBinding(password)
                .withDefaultSize()
                .withPromptText(Labels.i18n("password"))
                .withRight(eyeOnBtn);
        passwordTf.setVisible(false);

        final BvPasswordField passwordPf = new BvPasswordField()
                .withBinding(password)
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

        this.getChildren().addAll(
                passwordTf,
                passwordPf
        );
    }
}
