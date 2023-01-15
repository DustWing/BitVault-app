package com.bitvault.ui.views;

import com.bitvault.server.cache.ImportCache;
import com.bitvault.server.endpoints.EndpointResolver;
import com.bitvault.server.endpoints.SecureItemController;
import com.bitvault.server.http.ServerListener;
import com.bitvault.server.http.HttpServer;
import com.bitvault.ui.components.BitVaultFlatButton;
import com.bitvault.ui.components.BitVaultVBox;
import com.bitvault.ui.model.LocalServerInfo;
import com.bitvault.util.*;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.sqlite.util.StringUtils;

import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;

public class ServerView extends BitVaultVBox {


    private HttpServer httpServer;
    private final TextArea textArea;

    private ImportCache importCache;


    private final int port = 8080;

    public ServerView() {


        BitVaultFlatButton start = new BitVaultFlatButton(Labels.i18n("start"));
        start.setOnAction(event -> start());
        start.setDefaultButton(true);

        BitVaultFlatButton stop = new BitVaultFlatButton(Labels.i18n("stop"));
        stop.setOnAction(event -> stop());
        stop.setDefaultButton(false);

        BitVaultFlatButton clearLog = new BitVaultFlatButton(Labels.i18n("clear"));
        clearLog.setOnAction(event -> clear());
        clearLog.setDefaultButton(false);

        BitVaultFlatButton showCache = new BitVaultFlatButton("Show Cache");
        showCache.setOnAction(event -> showCache());
        showCache.setDefaultButton(false);

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(start, stop, clearLog, showCache);


        textArea = new TextArea();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);


        InetAddress localHost = getLocalHost();
        String hostAddress = localHost.getHostAddress();

        final LocalServerInfo localServerInfo = new LocalServerInfo(
                "v1.0",
                hostAddress,
                port
        );

        Result<String> res = Json.serialize(localServerInfo);

        if (res.isFail()) {
            //TODO handle error
        }
        String serialize = res.get();

        appendText("Host: %s".formatted(serialize));

        final Result<BufferedImage> bufferedImageResult = QrUtils.generateQRCode(serialize, 200, 200);

        if (bufferedImageResult.isFail()) {
            //TODO handle error
        }

        final BufferedImage bufferedImage = bufferedImageResult.get();

        final Result<Image> imageResult = QrUtils.createImage(bufferedImage);

        if (imageResult.isFail()) {
            //TODO handle error
        }

        final Image image = imageResult.get();

        final ImageView imageView = new ImageView(image);

        this.getChildren().addAll(
                imageView,
                scrollPane,
                buttonBar
        );

        setAlignment(Pos.CENTER);
        vGrowAlways();

    }

    private InetAddress getLocalHost() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    public void start() {

        importCache = ImportCache.create();

        EndpointResolver endpointResolver = EndpointResolver.create(importCache);
        httpServer = HttpServer.create(port, endpointResolver);

        httpServer.addListener(
                new ServerListener() {
                    @Override
                    public void onMessage(String msg) {
                        appendText(msg);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                }
        );


        new Thread(() -> httpServer.start())
                .start();

        appendText("SERVER STARTED");


    }

    String newLine = System.getProperty("line.separator");

    private void appendText(String text) {
        var log = "%s-->%s".formatted(DateTimeUtils.getTimeNow(), text) + newLine;
        textArea.appendText(log);
    }

    public void stop() {

        if (httpServer == null) {
            return;
        }

        httpServer.stop();

    }

    public void clear() {
        textArea.clear();
    }

    public void showCache() {

        if (importCache == null) {
            appendText("You need to start the server to initialize the cache. FU");
            return;
        }


        List<String> strings = importCache.getCache().stream().map(Object::toString).toList();
        String join = StringUtils.join(strings, newLine);
        appendText("===CACHE====" + newLine + join);
    }

}
