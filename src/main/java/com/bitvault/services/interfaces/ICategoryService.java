package com.bitvault.services.interfaces;

import com.bitvault.model.Category;
import com.bitvault.util.Result;

import java.util.List;

public interface ICategoryService {

    Result<List<Category>> getCategories();

    Result<Category> create(final Category category);

    Result<Boolean> update(final Category category);

    Result<Boolean> delete(final Category category);


}
