package com.bitvault.ui.views.password;

import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.components.grid.BvSimpleGrid;
import com.bitvault.ui.components.grid.GridRow;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.utils.BvInsets;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.ui.utils.ViewLoader;
import com.bitvault.util.DateTimeUtils;
import com.bitvault.util.Labels;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;
import java.util.function.Consumer;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignC.CONTENT_COPY;

public class PasswordDetailsPopUp {

    public static void showDetailsPopup(
            Scene scene,
            Password selectedItem,
            Consumer<String> onCopyUsername,
            Consumer<String> onCopyPassword
    ) {

        GridRow titleRow = new GridRow(List.of(
                new Label(Labels.i18n("title")), new Label(selectedItem.getSecureDetails().getTitle())
        ));

        Button usernameBtn = new BvButton("", new FontIcon(CONTENT_COPY))
                .action(event -> onCopyUsername.accept(selectedItem.getUsername()));

        GridRow usernameRow = new GridRow(List.of(
                new Label(Labels.i18n("username")), new Label(selectedItem.getUsername()), usernameBtn
        ));

        Button passwordBtn = new BvButton("", new FontIcon(CONTENT_COPY))
                .action(event -> onCopyPassword.accept(selectedItem.getPassword()));

        GridRow passwordRow = new GridRow(List.of(
                new Label(Labels.i18n("password")), new Label("********"), passwordBtn
        ));

        Hyperlink hyperlink = new Hyperlink(selectedItem.getSecureDetails().getDomain());

        hyperlink.setOnAction(event -> JavaFxUtil.openBrowser(selectedItem.getSecureDetails().getDomain()));

        GridRow domain = new GridRow(List.of(
                new Label(Labels.i18n("domain")), hyperlink
        ));

        Text descrText = new Text(selectedItem.getSecureDetails().getDescription());
        descrText.setWrappingWidth(300);

        GridRow description = new GridRow(List.of(
                new Label(Labels.i18n("description")), descrText)
        );

        String categoryName = selectedItem.getSecureDetails().getCategory() != null
                ? selectedItem.getSecureDetails().getCategory().name() : null;
        GridRow category = new GridRow(List.of(
                new Label(Labels.i18n("category")), new Label(categoryName)
        ));

        String expiresOn = DateTimeUtils.formatNoTime(selectedItem.getSecureDetails().getExpiresOn());
        GridRow expiry = new GridRow(List.of(
                new Label(Labels.i18n("expires.on")), new Label(expiresOn)
        ));

        String createdOn = DateTimeUtils.format(selectedItem.getSecureDetails().getCreatedOn());
        GridRow created = new GridRow(List.of(
                new Label(Labels.i18n("created.on")), new Label(createdOn)
        ));

        String updatedOn = DateTimeUtils.format(selectedItem.getSecureDetails().getModifiedOn());
        GridRow updated = new GridRow(List.of(
                new Label(Labels.i18n("modified.on")), new Label(updatedOn)
        ));

        GridRow masterP = new GridRow(List.of(
                new Label(Labels.i18n("master.password")), new Label(selectedItem.getSecureDetails().isRequiresMp() ? "Y" : "N")
        ));

        GridRow fav = new GridRow(List.of(
                new Label(Labels.i18n("favourite")), new Label(selectedItem.getSecureDetails().isFavourite() ? "Y" : "N")
        ));

        GridRow shared = new GridRow(List.of(
                new Label(Labels.i18n("shared")), new Label(selectedItem.getSecureDetails().isShared() ? "Y" : "N")
        ));


        BvSimpleGrid grid = new BvSimpleGrid(List.of(
                titleRow,
                usernameRow,
                passwordRow,
                domain,
                description,
                category,
                expiry,
                created,
                updated,
                masterP,
                fav,
                shared
        ));


        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);

        VBox vBox = new VBox(scrollPane);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(BvInsets.all10);

        ViewLoader.popUp(scene.getWindow(), vBox, Labels.i18n("details")).show();

    }
}
