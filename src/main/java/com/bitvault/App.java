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
                ()->{
                    final WelcomeView view = new WelcomeView();
                    final Scene scene = new Scene(view, 640, 400);
                    //set Theme - TODO read from external properties
                    Theme.toDark(scene);
                    stage.setTitle("The Vault");
                    stage.setScene(scene);
                    stage.show();
                }
        );


    }


    public static void launch(String[] args) {
        launch();
    }
}