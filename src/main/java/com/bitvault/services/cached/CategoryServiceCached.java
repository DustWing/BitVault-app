package com.bitvault.services.cached;


import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.ui.model.Category;
import com.bitvault.util.Result;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CategoryServiceCached implements ICategoryService {


    private final Map<String, Category> cache = new ConcurrentHashMap<>();

    private final ICategoryService categoryService;

    public CategoryServiceCached(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public Result<List<Category>> getCategories() {
        if (this.cache.isEmpty()) {

            final Result<List<Category>> categoriesResult = categoryService.getCategories();
            if (categoriesResult.isFail()) {
                return categoriesResult;
            }

            final Map<String, Category> map = categoriesResult.get().stream()
                    .collect(Collectors.toMap(Category::id, Function.identity()));

            this.cache.putAll(map);

            return categoriesResult;
        }
        return Result.ok(List.copyOf(cache.values()));
    }

    @Override
    public Result<Category> create(Category category) {

        Result<Category> categoryResult = categoryService.create(category);

        if (categoryResult.isFail()) {
            return categoryResult;
        }

        Category newCat = categoryResult.get();
        this.cache.put(newCat.id(), newCat);

        return categoryResult;
    }

    @Override
    public Result<Category> update(Category category) {
        Result<Category> categoryResult = categoryService.update(category);

        if (categoryResult.isFail()) {
            return categoryResult;
        }

        final Category newCat = categoryResult.get();
        this.cache.put(newCat.id(), newCat);

        return categoryResult;
    }

    @Override
    public Result<Boolean> delete(String id) {
        Result<Boolean> deleteResult = categoryService.delete(id);
        if (deleteResult.isFail()) {
            return deleteResult;
        }
        this.cache.remove(id);
        return deleteResult;
    }
}
