package com.bitvault.ui.components;

import com.bitvault.BitVault;
import javafx.application.Platform;
import javafx.scene.control.ProgressIndicator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimerBar {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final ProgressIndicator progressBar;
    private final Runnable onEnd;
    private double currentProgress;
    private ScheduledFuture<?> currentScheduledFuture;


    public TimerBar(ProgressIndicator progressBar, Runnable onEnd) {
        this.progressBar = progressBar;
        this.onEnd = onEnd;
        this.progressBar.setProgress(0);
        this.progressBar.setVisible(false);


        BitVault.addCloseAction(this::shutdown);
    }

    public void start(int duration) {

        //stop the previous schedule task
        if (currentScheduledFuture != null) {
            currentScheduledFuture.cancel(true);
        }

        this.progressBar.setVisible(true);

        progressBar.setProgress(0);

        double step = (double) 1 / duration;
        currentProgress = step;

        currentScheduledFuture = scheduler.scheduleAtFixedRate(
                () -> doProgress(step),
                0,
                1,
                TimeUnit.SECONDS
        );

    }

    private void doProgress(double step) {

        if (currentProgress >= 1) {
            currentScheduledFuture.cancel(true);
            Platform.runLater(() -> this.progressBar.setVisible(false));
            onEnd.run();
        }

        currentProgress += step;

        //update UI
        Platform.runLater(() -> progressBar.setProgress(currentProgress));

    }

    private void shutdown() {
        this.scheduler.shutdown();
    }

    public ProgressIndicator getProgressBar() {
        return progressBar;
    }
}
