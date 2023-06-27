package com.bitvault;

import atlantafx.base.theme.Dracula;
import atlantafx.base.theme.PrimerDark;
import com.bitvault.ui.utils.BvSceneSize;
import com.bitvault.ui.utils.IconUtils;
import com.bitvault.ui.utils.ViewLoader;
import com.bitvault.ui.views.WelcomeView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        String userAgentStylesheet = new Dracula().getUserAgentStylesheet();
        BitVault.setUserAgentStylesheet(userAgentStylesheet);

        stage.setTitle("The Vault");
        stage.getIcons().add(IconUtils.STAGE_ICON);

        //register on close
        stage.setOnCloseRequest(event -> runOnCloseActions());
        BvSceneSize aDefault = BvSceneSize.Default;
        ViewLoader.load(stage, aDefault.width(), aDefault.height(), WelcomeView::new);

//        ViewLoader.loadFxml(stage, "Spotify.fxml");
    }

}