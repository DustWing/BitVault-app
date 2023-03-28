package com.bitvault.ui.views.categories;

import com.bitvault.ui.async.BvService;
import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.ui.model.Category;
import com.bitvault.util.Result;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

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
