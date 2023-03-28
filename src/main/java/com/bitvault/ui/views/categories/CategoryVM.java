package com.bitvault.ui.views.categories;

import com.bitvault.BvService;
import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.utils.BvColors;
import com.bitvault.util.Result;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryVM {

    private final ICategoryService categoryService;
    private final SimpleBooleanProperty loading = new SimpleBooleanProperty();
    private final ObservableList<CategoryRowView> categories = FXCollections.observableArrayList();

    public CategoryVM(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void load() {
        BvService.create(this::loadCategories)
                .onSuccess(this::onLoaded)
                .onFailure(Throwable::printStackTrace)
                .start();

    }

    private void onLoaded(List<Category> categories) {
        final List<CategoryRowView> categoryRowViews = categories
                .stream()
                .map(category -> CategoryRowView.createFromCategory(category, this::onDelete))
                .toList();
        this.categories.addAll(categoryRowViews);
        this.loading.set(false);
    }

    private List<Category> loadCategories() {

        loading.set(true);

        try {
            System.out.println("Paused");
            Thread.sleep(1000 * 3);
            System.out.println("Continue");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Result<List<Category>> categoriesResult = this.categoryService.getCategories();

        if (categoriesResult.isFail()) {
            //TODO handle error
            throw new RuntimeException(categoriesResult.getError());
        }


        return categoriesResult.get();
    }

    private Result<Boolean> onDelete(CategoryRowView categoryRowView) {
        Result<Boolean> booleanResult = categoryService.delete(categoryRowView.getUniqueId());
        if (booleanResult.isFail()) {
            return booleanResult;
        }
        this.categories.remove(categoryRowView);
        return Result.Success;
    }
    
    public CategoryRowView addNewCategory() {
        CategoryRowView aNew = CategoryRowView.createNew(this::onDelete);
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
