package com.bitvault.database.daos;

import com.bitvault.database.models.CategoryDM;

import java.util.List;

public interface ICategoryDao {

    List<CategoryDM> get();

    CategoryDM get(final String id);

    void create(CategoryDM category);

    void update(CategoryDM category);

    void delete(String id);

}
