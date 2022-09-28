package com.bitvault.viewmodel;

import com.bitvault.model.Category;
import com.bitvault.model.Password;
import com.bitvault.model.Profile;
import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.services.interfaces.IPasswordService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class PasswordVM {

    private final ObservableList<Password> passwords;
    private final IPasswordService passwordService;
    private final ICategoryService categoryService;

    private final Profile profile;

    private final List<Category> categories;

    public PasswordVM(
            final IPasswordService passwordService,
            final ICategoryService categoryService,
            final Profile profile
    ) {
        this.passwordService = passwordService;
        this.categoryService = categoryService;
        this.profile = profile;
        this.passwords = FXCollections.observableArrayList();
        this.categories = new ArrayList<>();
        this.init();
    }

    private void init() {

        passwordService.getPasswords().apply(
                this.passwords::addAll,
                exception -> {
                    //TODO handle exception
                }
        );

        categoryService.getCategories().apply(
                categories::addAll,
                exception -> {
                    //TODO handle exception
                }
        );

    }

    public void create(Password password) {
        passwordService.create(password);
        passwords.add(password);
    }

    public void delete(Password password) {
        passwordService.delete(password);
        passwords.removeIf(
                oldPass -> oldPass.id().equals(password.id())
        );
    }

    public void update(Password password) {
        passwordService.update(password);
        passwords.replaceAll(oldPass -> {
            if (oldPass.id().equals(password.id())) {
                return password;
            }
            return oldPass;
        });
    }

    public void reload() {
        passwordService.getPasswords().apply(
                passwordList -> {
                    passwords.clear();
                    passwords.addAll(passwordList);
                },
                exception -> {
                    //TODO need exception
                }
        );
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
