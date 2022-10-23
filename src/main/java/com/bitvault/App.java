package com.bitvault;

import com.bitvault.util.Theme;
import com.bitvault.ui.views.WelcomeView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {

        final WelcomeView view = new WelcomeView();
        final Scene scene = new Scene(view, 640, 400);
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