package com.bitvault.services.interfaces;

import com.bitvault.ui.model.Category;
import com.bitvault.util.Result;

import java.util.List;

public interface ICategoryService {

    Result<List<Category>> getCategories();

    default Result<Category> create(final Category category){
        return Result.ok(category);
    };

    default Result<Boolean> update(final Category category){
        return Result.ok(false);
    };

    default Result<Boolean> delete(final Category category){
        return Result.ok(false);
    };


}
