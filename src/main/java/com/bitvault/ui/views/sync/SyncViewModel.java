package com.bitvault.ui.views.sync;

import com.bitvault.BitVault;
import com.bitvault.security.EncryptionProvider;
import com.bitvault.security.UserSession;
import com.bitvault.server.cache.ImportCache;
import com.bitvault.server.dto.SecureItemRqDto;
import com.bitvault.server.endpoints.EndpointResolver;
import com.bitvault.server.http.HttpServer;
import com.bitvault.services.interfaces.ISyncService;
import com.bitvault.ui.components.alert.ErrorAlert;
import com.bitvault.ui.exceptions.ViewLoadException;
import com.bitvault.ui.model.*;
import com.bitvault.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

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
    private HttpServer httpServer;
    private final UserSession userSession;
    private final List<Password> oldPasswords;
    private final List<Category> categories;

    private final ObservableList<SyncValue<Password>> passwords = FXCollections.observableList(new ArrayList<>());


    public SyncViewModel(UserSession userSession) {
        this.userSession = userSession;

        final Result<List<Password>> passwords = this.userSession.getServiceFactory()
                .getPasswordService()
                .getPasswords();

        if (passwords.hasError()) {
            throw new RuntimeException(passwords.getError());
        }

        oldPasswords = passwords.get();

        final Result<List<Category>> categoriesRes = this.userSession.getServiceFactory()
                .getCategoryService()
                .getCategories();

        if (categoriesRes.hasError()) {
            throw new RuntimeException(passwords.getError());
        }
        categories = categoriesRes.get();

    }

    public Result<Image> createQrdImage(int port) {

        final Result<String> localHostResult = getLocalHost();
        if (localHostResult.hasError()) {
            return Result.error(localHostResult.getError());
        }

        final LocalServerInfo localServerInfo = new LocalServerInfo("v1.0", localHostResult.get(), port);

        final Result<String> serialized = Json.serialize(localServerInfo);
        if (serialized.hasError()) {
            throw new ViewLoadException(serialized.getError());
        }

        final Result<BufferedImage> bufferedImageResult = QrUtils.generateQRCode(serialized.get(), 200, 200);

        if (bufferedImageResult.hasError()) {
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


    public Result<Integer> startServer() {

        stopServer();

        final Result<Integer> portResult = getRandomPort();
        if (portResult.hasError()) {
            return Result.error(portResult.getError());
        }

        final ImportCache importCache = ImportCache.createDefault(this::onAddPassword);
        final EndpointResolver endpointResolver = EndpointResolver.create(importCache);
        httpServer = HttpServer.create(portResult.get(), endpointResolver);

        Thread thread = new Thread(() -> httpServer.start());
        thread.start();
        BitVault.addCloseAction(this::stopServer);

        return Result.ok(portResult.get());
    }

    public void stopServer() {
        if (httpServer != null) {
            httpServer.stop();
        }
    }


    public void onAddPassword(SecureItemRqDto.LocalPasswordDto localPasswordDto) {
        final Password passwordNew = fromDto(localPasswordDto);

        final Optional<Password> passwordOpt = oldPasswords.stream()
                .filter(old -> passwordNew.getUsername().equals(old.getUsername())
                        && passwordNew.getSecureDetails().getDomain().equals(old.getSecureDetails().getDomain())
                )
                .findAny();

        final SyncValue<Password> syncValue;

        if (passwordOpt.isPresent()) {
            syncValue = SyncValue.createConflict(passwordOpt.get(), passwordNew);
        } else if (passwordNew.getSecureDetails().getCategory() == null) {
            syncValue = SyncValue.createWarning(passwordNew, Messages.i18n("category.not.found"));
        } else {
            syncValue = SyncValue.createNew(passwordNew);
        }

        this.passwords.add(syncValue);
    }

    private Password fromDto(SecureItemRqDto.LocalPasswordDto localPasswordDto) {

        Optional<Category> categoryOpt = categories.stream()
                .filter(category -> category.name().equals(localPasswordDto.category()))
                .findAny();

        final Category category = categoryOpt.orElse(null);

        final SecureDetails secureDetails = new SecureDetails(
                localPasswordDto.id(),
                category,
                userSession.getProfile(),
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

    public void save() {

        //TODO revisit
        List<String> list = this.passwords.stream().map(SyncValue::getWarningMsg).toList();
        if (!list.isEmpty()) {
            ErrorAlert.show("Sync error", list);
            return;
        }

        ISyncService syncService = this.userSession.getServiceFactory().getSyncService();

        List<Password> passwordToSave = this.passwords.stream().map(SyncValue::getNewValue).toList();
        List<Result<Password>> results = syncService.savePasswords(passwordToSave);
    }


    public ObservableList<SyncValue<Password>> getPasswords() {
        return passwords;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public EncryptionProvider getEncryptionProvider() {
        return userSession.getEncryptionProvider();
    }

    public int getPassLength(){
        return this.userSession.getSettings().passwordGenerateLength();
    }
}
