package com.bitvault;

import com.bitvault.ui.utils.ViewLoader;
import com.bitvault.ui.views.sync.SyncView;
import com.bitvault.util.Theme;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BitVault extends Application {
    public static void launch(String[] __) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        Platform.runLater(() -> show(stage));
    }

    private static final List<Runnable> onCloseActions = new CopyOnWriteArrayList<>();

    public static void addCloseAction(Runnable runnable) {
        onCloseActions.add(runnable);
    }

    public static void runOnCloseActions() {
        onCloseActions.forEach(Runnable::run);
    }

    private void show(Stage stage) {

        //set default css
        BitVault.setUserAgentStylesheet(Theme.DARK);

        stage.setTitle("The Vault");
        //register on close
        stage.setOnCloseRequest(event -> runOnCloseActions());

        ViewLoader.load(stage, 840, 600, SyncView::createTest);
    }

}