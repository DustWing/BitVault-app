package com.bitvault.ui.views.sync;

import atlantafx.base.theme.Tweaks;
import com.bitvault.ui.hyperlink.HyperLinkCell;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.SyncValue;
import com.bitvault.util.Labels;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.function.Consumer;

public class PasswordSyncTable extends TableView<SyncValue<Password>> {



    public static TableView<SyncValue<Password>> createTable(
            ObservableList<SyncValue<Password>> passwords,
            List<Category> categories,
            int passLength,
            Consumer<Password> onSave
    ) {

        TableView<SyncValue<Password>> tableView = new TableView<>();

        tableView.setItems(passwords);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tableView.setRowFactory(param -> new SyncPasswordTableRowFactory(categories, passLength, onSave));

        final TableColumn<SyncValue<Password>, SyncValue.ActionState> iconC = new TableColumn<>("");
        iconC.setCellValueFactory(cellData -> cellData.getValue().actionStateProperty());
        iconC.setCellFactory(param -> new SyncTableIconCellFactory<>());
        iconC.setMaxWidth(30);
        iconC.setMinWidth(30);


        final TableColumn<SyncValue<Password>, String> titleC = new TableColumn<>(Labels.i18n("title"));
        titleC.setCellValueFactory(cellData -> cellData.getValue().getNewValue().getSecureDetails().titleProperty());

        final TableColumn<SyncValue<Password>, String> userNameC = new TableColumn<>(Labels.i18n("username"));
        userNameC.setCellValueFactory(cellData -> cellData.getValue().getNewValue().usernameProperty());

        final TableColumn<SyncValue<Password>, String> descriptionC = new TableColumn<>(Labels.i18n("description"));
        descriptionC.setCellValueFactory(cellData -> cellData.getValue().getNewValue().getSecureDetails().descriptionProperty());

        final TableColumn<SyncValue<Password>, String> domainC = new TableColumn<>(Labels.i18n("domain"));
        domainC.setCellValueFactory(cellData -> cellData.getValue().getNewValue().domainProperty());
        domainC.setCellFactory(new HyperLinkCell<>());

        tableView.getColumns().add(iconC);
        tableView.getColumns().add(titleC);
        tableView.getColumns().add(userNameC);
        tableView.getColumns().add(descriptionC);
        tableView.getColumns().add(domainC);

        //to remove border
        tableView.getStyleClass().add(Tweaks.EDGE_TO_EDGE);

        return tableView;

    }


}
