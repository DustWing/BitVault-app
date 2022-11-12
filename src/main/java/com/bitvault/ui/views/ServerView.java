package com.bitvault.ui.views;

import com.bitvault.server.ServerListener;
import com.bitvault.server.http.HttpServer;
import com.bitvault.ui.components.BitVaultFlatButton;
import com.bitvault.ui.components.BitVaultVBox;
import com.bitvault.ui.model.LocalServerInfo;
import com.bitvault.util.Json;
import com.bitvault.util.Labels;
import com.bitvault.util.QrUtils;
import com.bitvault.util.Result;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerView extends BitVaultVBox {


    HttpServer httpServer;
    private final TextArea textArea;

    private final int port = 8080;

    public ServerView() {


        BitVaultFlatButton start = new BitVaultFlatButton(Labels.i18n("start"));
        start.setOnAction(event -> start());
        start.setDefaultButton(true);

        BitVaultFlatButton stop = new BitVaultFlatButton(Labels.i18n("stop"));
        stop.setOnAction(event -> stop());
        stop.setDefaultButton(false);

        textArea = new TextArea();

        InetAddress localHost = getLocalHost();

        String hostAddress = localHost.getHostAddress();


        final LocalServerInfo localServerInfo = new LocalServerInfo(
                "v1.0",
                hostAddress,
                port
        );

        String serialize = Json.serialize(localServerInfo);

        textArea.appendText("Host: %s\n".formatted(serialize));

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
                start,
                stop,
                imageView,
                textArea
        );

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

        httpServer = new HttpServer(port);

        httpServer.addListener(
                new ServerListener() {
                    @Override
                    public void onMessage(String msg) {
                        textArea.appendText(msg);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                }
        );


        new Thread(() -> httpServer.start())
                .start();

        textArea.appendText("SERVER STARTED\n");


    }


    public void stop() {

        httpServer.stop();

    }

}
