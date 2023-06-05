package com.bitvault.ui.hyperlink;

import com.bitvault.ui.utils.JavaFxUtil;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class HyperLinkCell<S extends IWebLocation> implements Callback<TableColumn<S, String>, TableCell<S, String>> {
    @Override
    public TableCell<S, String> call(TableColumn<S, String> param) {
        return new TableCell<>() {

            private final Hyperlink hyperlink = new Hyperlink();

            {
                hyperlink.setOnAction(event -> {
                    String url = this.getItem();
                    JavaFxUtil.openBrowser(url);
                });
            }


            @Override
            protected void updateItem(String url, boolean empty) {
                super.updateItem(url, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    hyperlink.setText(url);
                    setGraphic(hyperlink);
                }
            }
        };

    }
}
