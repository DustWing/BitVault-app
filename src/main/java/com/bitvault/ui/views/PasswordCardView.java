package com.bitvault.ui.views;

import com.bitvault.ui.components.BitVaultCard;
import com.bitvault.ui.components.BitVaultFlatButton;
import com.bitvault.ui.model.Password;
import com.bitvault.util.Labels;
import com.bitvault.ui.viewmodel.PasswordCardVM;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.function.Consumer;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignD.DELETE_FOREVER;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignF.FILE_DOCUMENT_EDIT;

public class PasswordCardView extends BitVaultCard {

    private final PasswordCardVM passwordCardVM;
    private final Consumer<Password> onEdit;
    private final Consumer<Password> onDelete;


    public PasswordCardView(PasswordCardVM passwordCardVM, Consumer<Password> onEdit, Consumer<Password> onDelete) {
        super();
        this.passwordCardVM = passwordCardVM;
        this.onEdit = onEdit;
        this.onDelete = onDelete;
        this.setId(passwordCardVM.getPassword().id());
        init();
    }


    private void init() {

        final Password password = passwordCardVM.getPassword();

        var editIc = new FontIcon(FILE_DOCUMENT_EDIT);
        editIc.setIconSize(20);
        var editBtn = new BitVaultFlatButton(
                Labels.i18n("edit"),
                editIc);
        editBtn.setOnAction(
                event -> onEdit.accept(password)
        );

        var deleteIc = new FontIcon(DELETE_FOREVER);
        deleteIc.setIconSize(20);
        var deleteBtn = new BitVaultFlatButton(
                Labels.i18n("delete"),
                deleteIc
        );
        deleteBtn.setOnAction(
                event -> onDelete.accept(password)
        );

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.addRow(0,
                new Label(Labels.i18n("username")), new Label(password.username()));
        gridPane.addRow(2,
                new Label(Labels.i18n("domain")), new Label(password.secureDetails().domain()));
        gridPane.addRow(3,
                new Label(Labels.i18n("description")), new Label(password.secureDetails().description()));
        gridPane.addRow(4, editBtn, deleteBtn);


        gridPane.getChildren().forEach(
                node -> {
                    GridPane.setVgrow(node, Priority.ALWAYS);
                    GridPane.setHgrow(node, Priority.ALWAYS);
                }
        );

        this.getChildren().addAll(
                gridPane
        );

    }

    public Password getPassword() {
        return passwordCardVM.getPassword();
    }

}
