package com.bitvault.ui.utils;

import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Window;
import javafx.stage.*;
import javafx.util.Duration;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class JavaFxUtil {

    public static Stage popUp(final Window owner, final Parent root) {

        final Scene scene = new Scene(root);
        final Stage stage = new Stage();
        stage.initOwner(owner);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        return stage;
    }


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

    public static void changeScene(Pane parent, Node from, Node to) {
        FadeTransition fadeInTransition = new FadeTransition(Duration.millis(1000), to);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);
        changeScene(parent, from, to, fadeInTransition);
    }


    public static void changeScene(Pane parent, Node from, Node to, Transition transition) {
        parent.getChildren().remove(from);
        parent.getChildren().add(to);
        transition.play();
    }

    public static void copyToClipBoard(final String value) {
        try {
            System.out.println(value);
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

    public static void vGrowAlways(Pane pane) {
        pane.getChildren().forEach(
                node -> VBox.setVgrow(node, Priority.ALWAYS)
        );
    }

    public static void vGrowAlways(Node node) {
        VBox.setVgrow(node, Priority.ALWAYS);
    }

    public static void hGrowAlways(Pane pane) {
        pane.getChildren().forEach(
                node -> HBox.setHgrow(node, Priority.ALWAYS)
        );
    }

    public static void hGrowAlways(Node node) {
        HBox.setHgrow(node, Priority.ALWAYS);
    }

    public static void defaultSize(Region region) {
        region.setMaxWidth(BvWidths.MEDIUM);
        region.setMinWidth(BvWidths.SMALL);
        region.setPrefWidth(BvWidths.MEDIUM);
        region.setPrefHeight(BvHeights.MEDIUM);
        region.setMinHeight(BvHeights.MEDIUM);
        region.setMaxHeight(BvHeights.MEDIUM);
    }

    public static void largeSize(Region region) {
        region.setMaxWidth(BvWidths.LARGE);
        region.setMinWidth(BvWidths.SMALL);
        region.setPrefWidth(BvWidths.LARGE);
        region.setPrefHeight(BvHeights.MEDIUM);
        region.setMinHeight(BvHeights.MEDIUM);
        region.setMaxHeight(BvHeights.MEDIUM);
    }

    public static void addDebugBorder(Region region) {
        region.setBorder(Border.stroke(Paint.valueOf("#fcba03")));
    }
}
