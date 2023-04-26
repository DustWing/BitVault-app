package com.bitvault.ui.views.sync;

import com.bitvault.BitVault;
import com.bitvault.security.UserSession;
import com.bitvault.server.cache.ImportCache;
import com.bitvault.server.dto.SecureItemRqDto;
import com.bitvault.server.endpoints.EndpointResolver;
import com.bitvault.server.http.HttpServer;
import com.bitvault.ui.exceptions.ViewLoadException;
import com.bitvault.ui.model.*;
import com.bitvault.util.DateTimeUtils;
import com.bitvault.util.Json;
import com.bitvault.util.QrUtils;
import com.bitvault.util.Result;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.sqlite.util.StringUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SyncViewModel {

    private static final String newLine = System.getProperty("line.separator");
    private HttpServer httpServer;
    private ImportCache importCache;
    private final ObservableList<SyncValue<Password>> passwords = FXCollections.observableList(new ArrayList<>());
    private final SimpleStringProperty log = new SimpleStringProperty();
    private final UserSession userSession;
    private final List<Password> oldPasswords;

    public SyncViewModel(UserSession userSession) {
        this.userSession = userSession;

        Result<List<Password>> passwords = this.userSession.getServiceFactory()
                .getPasswordService()
                .getPasswords();

        if (passwords.isFail()) {
            throw new RuntimeException(passwords.getError());
        }
        oldPasswords = passwords.get();
    }

    public Result<Image> createQrdImage(int port) {

        final Result<String> localHostResult = getLocalHost();
        if (localHostResult.isFail()) {
            return Result.error(localHostResult.getError());
        }

        final LocalServerInfo localServerInfo = new LocalServerInfo("v1.0", localHostResult.get(), port);

        final Result<String> serialized = Json.serialize(localServerInfo);
        if (serialized.isFail()) {
            throw new ViewLoadException(serialized.getError());
        }

        appendText("Host: %s".formatted(serialized.get()));

        final Result<BufferedImage> bufferedImageResult = QrUtils.generateQRCode(serialized.get(), 200, 200);

        if (bufferedImageResult.isFail()) {
            return Result.error(bufferedImageResult.getError());
        }
        final BufferedImage bufferedImage = bufferedImageResult.get();
        final Result<Image> imageResult = QrUtils.createImage(bufferedImage);

        return imageResult;
    }

    private Result<Integer> getRandomPort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return Result.ok(socket.getLocalPort());
        } catch (IOException e) {
            return Result.error(e);
        }
    }

    private Result<String> getLocalHost() {
        try {
            return Result.ok(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            return Result.error(e);
        }
    }

    /**
     * @return the Port of the local server
     */
    public Result<Integer> startServer() {

        stopServer();

        final Result<Integer> portResult = getRandomPort();
        if (portResult.isFail()) {
            return Result.error(portResult.getError());
        }

        importCache = ImportCache.createDefault(this::onAddPassword);

        EndpointResolver endpointResolver = EndpointResolver.create(importCache);
        httpServer = HttpServer.create(portResult.get(), endpointResolver);
        httpServer.addListener(
                msg -> {
                    if (msg.isFail()) {
                        appendText(msg.getError().getMessage());
                    } else {
                        appendText(msg.get());
                    }
                }
        );

        Thread thread = new Thread(() -> httpServer.start());
        thread.start();
        BitVault.addCloseAction(this::stopServer);

        appendText("SERVER STARTED");

        return Result.ok(portResult.get());
    }

    private void appendText(String text) {
        var log = "%s-->%s".formatted(DateTimeUtils.getTimeNow(), text) + newLine;
        this.log.set(log);
    }

    public void stopServer() {
        if (httpServer != null) {
            httpServer.stop();
        }
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


    private void onAddPassword(SecureItemRqDto.LocalPasswordDto localPasswordDto) {
        final Password passwordNew = fromDto(localPasswordDto);

        final Optional<Password> passwordOpt = oldPasswords.stream()
                .filter(old -> passwordNew.getUsername().equals(old.getUsername())
                        && passwordNew.getSecureDetails().getDomain().equals(old.getSecureDetails().getDomain())
                        && passwordNew.getSecureDetails().getProfile().id().equals(old.getSecureDetails().getProfile().id())
                )
                .findAny();

        final SyncValue<Password> syncValue;
        if (passwordOpt.isPresent()) {
            appendText("Already found CHANGED:" + passwordNew);
            appendText("Already found OLD:" + passwordOpt.get());
            syncValue = SyncValue.createConflict(passwordOpt.get(), passwordNew);
        } else {
            syncValue = SyncValue.createNew(passwordNew);
        }

        this.passwords.add(syncValue);
    }

    private Password fromDto(SecureItemRqDto.LocalPasswordDto localPasswordDto) {

        final Category category = new Category(
                localPasswordDto.category(),
                localPasswordDto.category(),
                "",
                null,
                null,
                null
        );

        final SecureDetails secureDetails = new SecureDetails(
                localPasswordDto.id(),
                category,
                null,
                localPasswordDto.domainDetails() == null ? null : localPasswordDto.domainDetails().domain(),
                "No title",
                localPasswordDto.description(),
                localPasswordDto.isFavourite(),
                DateTimeUtils.parseToLocal(localPasswordDto.createdOn()),
                DateTimeUtils.parseToLocal(localPasswordDto.modifiedOn()),
                DateTimeUtils.parseToLocal(localPasswordDto.expiresOn()),
                LocalDateTime.now(),
                localPasswordDto.requiresMasterPassword(),
                false
        );

        final Password password = new Password(
                localPasswordDto.id(),
                localPasswordDto.username(),
                localPasswordDto.password(),
                secureDetails
        );
        return password;
    }


    public SimpleStringProperty logTestProperty() {
        return log;
    }

    public ObservableList<SyncValue<Password>> getPasswords() {
        return passwords;
    }
}
