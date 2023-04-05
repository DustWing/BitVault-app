package com.bitvault;

import atlantafx.base.theme.PrimerDark;
import com.bitvault.ui.utils.ViewLoader;
import com.bitvault.ui.views.WelcomeView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BitVault extends Application {
    public static void launch(String[] not_used) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        Platform.runLater(() -> show(stage));
    }

    private static final List<Runnable> onCloseActions = new CopyOnWriteArrayList<>();

    public static void addCloseAction(Runnable runnable){
        onCloseActions.add(runnable);
    }
    public static void runOnCloseActions(){
        onCloseActions.forEach(Runnable::run);
    }

    private void show(Stage stage) {

        //set default css
        BitVault.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        stage.setTitle("The Vault");
        stage.setWidth(840);
        stage.setHeight(600);

        //register on close
        stage.setOnCloseRequest(event -> runOnCloseActions());

        ViewLoader.load(stage, WelcomeView::new);

    }


}