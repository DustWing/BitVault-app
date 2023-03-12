package com.bitvault.ui.views.password;

import com.bitvault.security.UserSession;
import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.services.interfaces.IPasswordService;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.Profile;
import com.bitvault.ui.utils.JavaFxUtil;
import com.bitvault.util.Result;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class PasswordVM {

    private final UserSession userSession;
    private final ObservableList<Password> passwords;
    private final IPasswordService passwordService;
    private final ICategoryService categoryService;

    private final Profile profile;

    private final List<Category> categories;

    public PasswordVM(
            final UserSession userSession,
            final IPasswordService passwordService,
            final ICategoryService categoryService,
            final Profile profile
    ) {
        this.userSession = userSession;
        this.passwordService = passwordService;
        this.categoryService = categoryService;
        this.profile = profile;
        this.passwords = FXCollections.observableArrayList();
        this.categories = new ArrayList<>();
        this.init();
    }

    private void init() {

        Result<List<Password>> passwordsRes = passwordService.getPasswords();
        if (passwordsRes.isFail()) {
            //TODO handle exception
            passwordsRes.getError().printStackTrace();
            return;
        }

        passwords.addAll(passwordsRes.get());


        Result<List<Category>> categoriesResult = categoryService.getCategories();

        if (categoriesResult.isFail()) {
            //TODO handle exception
            passwordsRes.getError().printStackTrace();
            return;
        }
        categories.addAll(categoriesResult.get());


    }

    public void create(Password password) {
        Result<Password> passwordResult = passwordService.create(password);

        if(passwordResult.isFail()){
            //TODO handle exception
            return;
        }

        passwords.add(passwordResult.get());
    }

    public void delete(Password password) {
        passwordService.delete(password);
        passwords.removeIf(
                oldPass -> oldPass.getId().equals(password.getId())
        );
    }

    public void update(Password password) {
        passwordService.update(password);
        passwords.replaceAll(oldPass -> {
            if (oldPass.getId().equals(password.getId())) {
                return password;
            }
            return oldPass;
        });
    }

    public void onTimeEnd() {
        JavaFxUtil.clearClipBoard();
    }
    public boolean copyPassword(Password selectedItem){
        if (selectedItem == null) return false;
        final String decrypt = userSession.decrypt(selectedItem.getPassword());
        JavaFxUtil.copyToClipBoard(decrypt);
        return true;
    }

    public ObservableList<Password> getPasswords() {
        return passwords;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Profile getProfile() {
        return profile;
    }
}
