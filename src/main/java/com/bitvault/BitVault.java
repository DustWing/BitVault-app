package com.bitvault;

import com.bitvault.ui.utils.BvSceneSize;
import com.bitvault.ui.utils.ViewLoader;
import com.bitvault.ui.views.WelcomeView;
import com.bitvault.util.ResourceLoader;
import com.bitvault.util.Theme;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BitVault extends Application {

    private static final Logger logger = LoggerFactory.getLogger(BitVault.class);

    public static void launch(String[] __) {
        logger.info("Starting application...");
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
        final URL themeUrl = ResourceLoader.loadURL(Theme.DARK);
        BitVault.setUserAgentStylesheet(themeUrl.toExternalForm());

        stage.setTitle("The Vault");

        // Set the icon of the primary stage
        final URL iconUrl = ResourceLoader.loadURL("/com.bitvault/icons/32moth.png");
        stage.getIcons().add(new Image(iconUrl.toExternalForm()));

        //register on close
        stage.setOnCloseRequest(event -> runOnCloseActions());
        BvSceneSize aDefault = BvSceneSize.Default;
        ViewLoader.load(stage, aDefault.width(), aDefault.height(), WelcomeView::new);
    }

}