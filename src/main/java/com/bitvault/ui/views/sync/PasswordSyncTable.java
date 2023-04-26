package com.bitvault.ui.views.sync;

import com.bitvault.ui.hyperlink.HyperLinkCell;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.SyncValue;
import com.bitvault.ui.utils.BvStyles;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PasswordSyncTable extends TableView<SyncValue<Password>> {



    public PasswordSyncTable(ObservableList<SyncValue<Password>> passwords) {

        this.setItems(passwords);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.setRowFactory(param -> new SyncTableRowFactory<>());

        final TableColumn<SyncValue<Password>, Boolean> iconC = new TableColumn<>("");
        iconC.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().warning()));
        iconC.setCellFactory(param -> new SyncTableIconCellFactory<>());

        final TableColumn<SyncValue<Password>, String> titleC = new TableColumn<>("Title");
        titleC.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getNewValue().getSecureDetails().getTitle())
        );

        final TableColumn<SyncValue<Password>, String> userNameC = new TableColumn<>("Username");
        userNameC.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNewValue().getUsername()));

        final TableColumn<SyncValue<Password>, String> descriptionC = new TableColumn<>("Description");
        descriptionC.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNewValue().getSecureDetails().getDescription()));

        final TableColumn<SyncValue<Password>, String> domainC = new TableColumn<>("Domain");
        domainC.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNewValue().getUrl()));
        domainC.setCellFactory(new HyperLinkCell<>());

        this.getColumns().add(iconC);
        this.getColumns().add(titleC);
        this.getColumns().add(userNameC);
        this.getColumns().add(descriptionC);
        this.getColumns().add(domainC);

        //to remove border
        this.getStyleClass().add(BvStyles.EDGE_TO_EDGE);

    }

}
