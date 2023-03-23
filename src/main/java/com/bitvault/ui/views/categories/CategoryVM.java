package com.bitvault.ui.views.categories;

import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.ui.model.Category;
import com.bitvault.util.Result;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.List;

public class CategoryVM {


    private final ICategoryService categoryService;
    private final ObservableList<Category> categories = FXCollections.observableArrayList();

    public CategoryVM(ICategoryService categoryService) {
        this.categoryService = categoryService;

        Result<List<Category>> categoriesResult = this.categoryService.getCategories();

        if (categoriesResult.isFail()) {
            //TODO handle error
            throw new RuntimeException(categoriesResult.getError());
        }

        categories.addAll(categoriesResult.get());

    }


    public ObservableList<Category> getCategories() {
        return categories;
    }
}
