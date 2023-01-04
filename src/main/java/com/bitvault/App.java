package com.bitvault;

import com.bitvault.ui.views.ServerView;
import com.bitvault.ui.views.WelcomeView;
import com.bitvault.util.Theme;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {

        Platform.runLater(
                () -> show(stage)
        );


    }

    private void show(Stage stage) {
//        final WelcomeView view = new WelcomeView();
        final ServerView view = new ServerView();

        final Scene scene = new Scene(view, 840, 700);
        //set Theme - TODO read from external properties
        Theme.toDark(scene);
        stage.setTitle("The Vault");
        stage.setScene(scene);
        stage.show();
    }


    public static void launch(String[] args) {
        launch();
    }
}