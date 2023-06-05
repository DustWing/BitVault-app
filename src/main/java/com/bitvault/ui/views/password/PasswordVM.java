package com.bitvault.ui.views.password;

import com.bitvault.security.UserSession;
import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.services.interfaces.IPasswordService;
import com.bitvault.ui.components.alert.ConfirmAlert;
import com.bitvault.ui.components.alert.ErrorAlert;
import com.bitvault.ui.exceptions.AuthenticateException;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.Profile;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.Labels;
import com.bitvault.util.Messages;
import com.bitvault.util.Result;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PasswordVM {

    private final UserSession userSession;
    private final ObservableList<Password> passwords = FXCollections.observableArrayList();
    private final FilteredList<Password> filteredList = new FilteredList<>(passwords, s -> true);
    private final ObservableList<Category> categories = FXCollections.observableArrayList();
    private final SimpleObjectProperty<Category> selectedCategory = new SimpleObjectProperty<>();
    private final List<Category> categoriesList = new ArrayList<>();  //to pass to new category window
    private final Category fakeCategory = Category.createFake("ALL", "ALL", "#000000");

    private final IPasswordService passwordService;
    private final ICategoryService categoryService;

    public PasswordVM(final UserSession userSession) {
        this.userSession = userSession;
        this.passwordService = userSession.getServiceFactory().getPasswordService();
        this.categoryService = userSession.getServiceFactory().getCategoryService();

        this.init();
    }

    public void init() {

        Result<List<Password>> passwordsRes = passwordService.getPasswords();
        if (passwordsRes.hasError()) {
            ErrorAlert.show(Labels.i18n("error"), passwordsRes.getError());
            return;
        }

        passwords.clear();
        passwords.addAll(passwordsRes.get());

        Result<List<Category>> categoriesResult = categoryService.getCategories();

        if (categoriesResult.hasError()) {
            ErrorAlert.show(Labels.i18n("error"), passwordsRes.getError());
            return;
        }


        categoriesList.clear();
        categoriesList.addAll(categoriesResult.get());

        categories.clear();
        categories.add(fakeCategory);
        categories.addAll(categoriesResult.get());
        selectedCategory.set(fakeCategory);//reset to all, order matters

    }

    public void create(Password password) {
        Result<Password> passwordResult = passwordService.create(password);

        if (passwordResult.hasError()) {
            ErrorAlert.show(Labels.i18n("error"), passwordResult.getError());
            return;
        }

        passwords.add(passwordResult.get());
    }


    public void delete(Password password) {

        boolean confirm = ConfirmAlert.deleteConfirm(userSession, password.getSecureDetails().isRequiresMp());

        if (confirm) {
            passwordService.delete(password);
            passwords.removeIf(oldPass -> oldPass.getId().equals(password.getId()));
        }

    }

    public void update(Password password) {
        Result<Password> update = passwordService.update(password);

        if (update.hasError()) {
            ErrorAlert.show(Labels.i18n("error"), update.getError());
        }

        final Password updatedPassword = update.get();

        Optional<Password> passwordOpt = passwords
                .stream()
                .filter(password1 -> password1.getId().equals(updatedPassword.getId()))
                .findAny();

        if(passwordOpt.isEmpty()){
            ErrorAlert.show(Labels.i18n("error"), Messages.i18n("no.record.found"));
            return;
        }

        Password passwordOld = passwordOpt.get();
        passwordOld.update(update.get());

    }

    public boolean copyPassword(String password) {
        final String decrypt = userSession.getEncryptionProvider().decrypt(password);
        JavaFxUtil.copyToClipBoard(decrypt);
        return true;
    }

    public boolean copyUsername(String username) {
        JavaFxUtil.copyToClipBoard(username);
        return true;
    }

    public Result<Password> prepareForEdit(Password oldPass) {
        boolean confirm = ConfirmAlert.editAuthenticate(userSession, oldPass.getSecureDetails().isRequiresMp());

        if (!confirm) {
            return Result.error(AuthenticateException.INSTANCE);
        }

        final String decrypt = userSession.getEncryptionProvider().decrypt(oldPass.getPassword());

        final Password password = new Password(
                oldPass.getId(),
                oldPass.getUsername(),
                decrypt,
                oldPass.getSecureDetails()
        );

        return Result.ok(password);
    }

    public ObservableList<Password> getPasswords() {
        return passwords;
    }

    public FilteredList<Password> getFilteredList() {
        return filteredList;
    }

    public SimpleObjectProperty<Category> selectedCategoryProperty() {
        return selectedCategory;
    }

    public ObservableList<Category> getCategories() {
        return categories;
    }

    public List<Category> getCategoriesList() {
        return categoriesList;
    }

    public Profile getProfile() {
        return userSession.getProfile();
    }

    public ICategoryService getCategoryService() {
        return categoryService;
    }
}
