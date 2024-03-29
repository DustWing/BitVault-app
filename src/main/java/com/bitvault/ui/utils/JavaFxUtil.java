package com.bitvault.ui.utils;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class JavaFxUtil {


    public static File chooseFile(final Window owner, final String title) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        return chooser.showOpenDialog(
                owner
        );
    }

    public static File chooseDir(final Window owner, final String title) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        return chooser.showDialog(owner);
    }

    public static <T> void scrollToLast(final ListView<T> listView) {
        listView.scrollTo(listView.getItems().size() - 1);
    }

    private static void scrollTo(ScrollPane pane, Node node) {
        double width = pane.getContent().getBoundsInLocal().getWidth();
        double height = pane.getContent().getBoundsInLocal().getHeight();

        double x = node.getBoundsInParent().getMaxX();
        double y = node.getBoundsInParent().getMaxY();

        // scrolling values range from 0 to 1
        pane.setVvalue(y / height);
        pane.setHvalue(x / width);

        // just for usability
        node.requestFocus();
    }

    public static void fadeIn(Node node) {
        FadeTransition fadeInTransition = new FadeTransition(Duration.millis(1000), node);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);
        fadeInTransition.play();

//        Timeline timeline = new Timeline(
//                new KeyFrame(Duration.ZERO, new KeyValue(node.opacityProperty(), 0)),
//                new KeyFrame(Duration.seconds(1), new KeyValue(node.opacityProperty(), 1))
//        );
//        timeline.play();
    }

    public static void copyToClipBoard(final String value) {
        try {
            StringSelection selection = new StringSelection(value);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void clearClipBoard() {

        try {

            StringSelection stringSelection = new StringSelection("");
            Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(stringSelection, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void focus(Node node) {
        if (!node.isFocused()) {
            node.requestFocus();
        }
    }

    public static void openBrowser(String url) {

        if (!Desktop.isDesktopSupported()) {
            return;
        }

        Desktop desktop = Desktop.getDesktop();

        if (!desktop.isSupported(Desktop.Action.BROWSE)) {
            return;
        }

        try {
            desktop.browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    public static void vGrowAlways(VBox pane) {
        pane.getChildren().forEach(
                node -> VBox.setVgrow(node, Priority.ALWAYS)
        );
    }


    public static void hGrowAlways(HBox pane) {
        pane.getChildren().forEach(
                node -> HBox.setHgrow(node, Priority.ALWAYS)
        );
    }

    public static void hGrowAlways(Node node) {
        HBox.setHgrow(node, Priority.ALWAYS);
    }

    public static void borderPaneCenterAll(BorderPane borderPane) {
        borderPane.getChildren().forEach(
                node -> BorderPane.setAlignment(node, Pos.CENTER)
        );
    }

    public static void borderPaneSpacing(BorderPane borderPane, double spacing) {
        Insets insets = new Insets(0, 0, spacing, 0);
        borderPane.getChildren().forEach(
                node -> BorderPane.setMargin(node, insets)
        );
    }

    public static void tinySize(Region region) {
        region.setMinWidth(BvWidths.TINY);
        region.setPrefWidth(BvWidths.TINY);
        region.setMaxWidth(BvWidths.TINY);

        region.setMinHeight(BvHeights.MEDIUM);
        region.setPrefHeight(BvHeights.MEDIUM);
        region.setMaxHeight(BvHeights.MEDIUM);

    }

    public static void mediumSize(Region region) {
        region.setMinWidth(BvWidths.SMALL);
        region.setPrefWidth(BvWidths.MEDIUM);
        region.setMaxWidth(BvWidths.MEDIUM);

        region.setMinHeight(BvHeights.MEDIUM);
        region.setPrefHeight(BvHeights.MEDIUM);
        region.setMaxHeight(BvHeights.MEDIUM);
    }

    public static void mediumIconSize(Region region) {
        region.setMinWidth(BvWidths.SMALL);

        region.setMinHeight(BvHeights.MEDIUM);
        region.setPrefHeight(BvHeights.MEDIUM);
        region.setMaxHeight(BvHeights.MEDIUM);
    }


    public static void largeSize(Region region) {
        region.setMinWidth(BvWidths.SMALL);
        region.setPrefWidth(BvWidths.LARGE);
        region.setMaxWidth(BvWidths.LARGE);

        region.setMinHeight(BvHeights.MEDIUM);
        region.setPrefHeight(BvHeights.MEDIUM);
        region.setMaxHeight(BvHeights.MEDIUM);

    }

    public static void vboxArrangement(VBox vBox) {
        vBox.setFillWidth(true);
        vBox.setPadding(BvInsets.all10);
        vBox.setSpacing(BvSpacing.SMALL);
    }

    public static void addDebugBorder(Region region) {
        region.setBorder(Border.stroke(Paint.valueOf("#fcba03")));
    }
}
