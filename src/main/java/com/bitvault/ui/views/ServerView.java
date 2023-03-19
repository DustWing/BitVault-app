package com.bitvault.ui.views;

import com.bitvault.enums.Action;
import com.bitvault.server.cache.ImportCache;
import com.bitvault.server.dto.SecureItemRqDto;
import com.bitvault.server.endpoints.EndpointResolver;
import com.bitvault.server.http.HttpServer;
import com.bitvault.server.http.ServerListener;
import com.bitvault.ui.components.BitVaultVBox;
import com.bitvault.ui.components.BvButton;
import com.bitvault.ui.exceptions.ViewLoadException;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.LocalServerInfo;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.SecureDetails;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.*;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

        ButtonBar buttonBar = createButtonBar();

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
            throw new ViewLoadException(res.getError());
        }
        String serialize = res.get();

        appendText("Host: %s".formatted(serialize));

        final Result<BufferedImage> bufferedImageResult = QrUtils.generateQRCode(serialize, 200, 200);

        if (bufferedImageResult.isFail()) {
            throw new ViewLoadException(res.getError());
        }

        final BufferedImage bufferedImage = bufferedImageResult.get();

        final Result<Image> imageResult = QrUtils.createImage(bufferedImage);

        if (imageResult.isFail()) {
            throw new ViewLoadException(res.getError());
        }

        final Image image = imageResult.get();

        final ImageView imageView = new ImageView(image);

        this.getChildren().addAll(
                imageView,
                scrollPane,
                buttonBar
        );

        setAlignment(Pos.CENTER);
        JavaFxUtil.vGrowAlways(this);


    }

    private ButtonBar createButtonBar() {
        final BvButton start = new BvButton(Labels.i18n("start"))
                .action(event -> start())
                .defaultButton(true)
                .withDefaultSize();


        final BvButton stop = new BvButton(Labels.i18n("stop"))
                .action(event -> stop())
                .defaultButton(false)
                .withDefaultSize();

        final BvButton clearLog = new BvButton(Labels.i18n("clear"))
                .action(event -> clear())
                .defaultButton(false)
                .withDefaultSize();

        BvButton showCache = new BvButton("Show Cache")
                .action(event -> showCache())
                .defaultButton(false)
                .withDefaultSize();

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons()
                .addAll(start, stop, clearLog, showCache);

        return buttonBar;
    }

    private InetAddress getLocalHost() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    public void start() {

        importCache = ImportCache.createDefault(
                password -> System.out.printf("printing pass %s%n", password)
        );

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
            appendText("You need to start the server to initialize the cache.");
            return;
        }


        List<String> strings = importCache.getCache()
                .stream()
                .map(Object::toString)
                .toList();
        String join = StringUtils.join(strings, newLine);
        appendText("===CACHE====" + newLine + join);
    }

    private Password fromDto(SecureItemRqDto.LocalPasswordDto localPasswordDto) {
        SecureDetails secureDetails = new SecureDetails(
                localPasswordDto.id(),
                new Category(localPasswordDto.category(), localPasswordDto.category(), "", null, null, null),
                null,
                localPasswordDto.domainDetails() == null ? null : localPasswordDto.domainDetails().domain(),
                "No title",
                localPasswordDto.description(),
                localPasswordDto.isFavourite(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                localPasswordDto.requiresMasterPassword(),
                false

        );

        Password password = new Password(
                localPasswordDto.id(),
                localPasswordDto.username(),
                localPasswordDto.password(),
                secureDetails,
                Action.NEW
        );
        return password;
    }

}
