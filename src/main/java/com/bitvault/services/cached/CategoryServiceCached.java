package com.bitvault.services.cached;


import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.ui.model.Category;
import com.bitvault.util.Result;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CategoryServiceCached implements ICategoryService {


    private final Map<String, Category> cache = new ConcurrentHashMap<>();

    private final ICategoryService categoryService;

    public CategoryServiceCached(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public Result<List<Category>> getCategories() {
        if (cache.isEmpty()) {
            return categoryService.getCategories();
        }
        return Result.ok(List.copyOf(cache.values()));
    }

    @Override
    public Result<Category> create(Category category) {

        Result<Category> categoryResult = ICategoryService.super.create(category);

        if (categoryResult.isFail()) {
            return categoryResult;
        }

        Category newCat = categoryResult.get();
        cache.put(newCat.id(), newCat);

        return categoryResult;
    }

    @Override
    public Result<Category> update(Category category) {
        Result<Category> categoryResult = ICategoryService.super.create(category);

        if (categoryResult.isFail()) {
            return categoryResult;
        }

        Category newCat = categoryResult.get();
        cache.put(newCat.id(), newCat);
        return categoryResult;
    }

    @Override
    public Result<Boolean> delete(String id) {
        cache.remove(id);
        return ICategoryService.super.delete(id);
    }
}
