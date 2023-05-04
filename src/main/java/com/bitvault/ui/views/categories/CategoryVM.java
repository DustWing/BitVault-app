package com.bitvault.ui.views.categories;

import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.ui.async.AsyncTask;
import com.bitvault.ui.components.alert.ErrorAlert;
import com.bitvault.ui.model.Category;
import com.bitvault.util.Result;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryVM {

    private final ICategoryService categoryService;
    private final SimpleBooleanProperty loading = new SimpleBooleanProperty();
    private final ObservableList<CategoryRowView> categories = FXCollections.observableArrayList();

    private List<Category> oldCategories;

    public CategoryVM(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void load() {
        AsyncTask.toRun(this::loadCategories)
                .onSuccess(this::onLoaded)
                .onFailure(Throwable::printStackTrace)
                .start();

    }

    private void onLoaded(List<Category> categories) {

        this.oldCategories = new ArrayList<>(categories);
        final List<CategoryRowView> categoryRowViews = categories
                .stream()
                .map(category -> CategoryRowView.createFromCategory(category, this::onDelete, this::onSave))
                .toList();
        this.categories.addAll(categoryRowViews);
        this.loading.set(false);
    }

    private List<Category> loadCategories() {
        loading.set(true);

        Result<List<Category>> categoriesResult = this.categoryService.getCategories();
        if (categoriesResult.isFail()) {
            //TODO handle error
            throw new RuntimeException(categoriesResult.getError());
        }


        return categoriesResult.get();
    }

    private boolean onDelete(CategoryRowView categoryRowView) {

        Optional<Category> foundInOld = oldCategories.stream()
                .filter(category -> category.id().equals(categoryRowView.getUniqueId()))
                .findAny();

        //if is not in old list then we can remove without checking service
        if (foundInOld.isEmpty()) {
            Platform.runLater(() -> this.categories.remove(categoryRowView));
            return true;
        }

        Result<Boolean> booleanResult = categoryService.delete(categoryRowView.getUniqueId());
        if (booleanResult.isFail()) {
            Platform.runLater(()->ErrorAlert.show("Category Deletion", booleanResult.getError()));
            return false;
        }
        Platform.runLater(() -> this.categories.remove(categoryRowView));
        return true;
    }

    private Result<String> onSave(CategoryRowView categoryRowView) {

        Optional<Category> foundInOld = oldCategories.stream()
                .filter(category -> category.id().equals(categoryRowView.getUniqueId()))
                .findFirst();

        //for update
        if (foundInOld.isPresent()) {
            Category updateCat = Category.createUpdate(foundInOld.get().id(), categoryRowView.getCategoryName(), categoryRowView.getColor(), "Password");
            Result<Category> updateResult = categoryService.update(updateCat);
            if (updateResult.isFail()) {
                updateResult.getError().printStackTrace();
                //TODO
                return Result.error(updateResult.getError());
            }
            return Result.ok(updateResult.get().id());
        }

        //for new
        Category category = Category.createNew(categoryRowView.getCategoryName(), categoryRowView.getColor(), "Password");
        Result<Category> categoryResult = categoryService.create(category);

        if (categoryResult.isFail()) {
            categoryResult.getError().printStackTrace();
            //TODO
            return Result.error(categoryResult.getError());
        }

        oldCategories.add(categoryResult.get());
        return Result.ok(categoryResult.get().id());

    }

    public CategoryRowView addNewCategory() {
        CategoryRowView aNew = CategoryRowView.createNew(this::onDelete, this::onSave);
        this.categories.add(aNew);
        return aNew;
    }

    public SimpleBooleanProperty loadingProperty() {
        return loading;
    }


    public ObservableList<CategoryRowView> getCategories() {
        return categories;
    }

}
