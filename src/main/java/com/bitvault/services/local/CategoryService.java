package com.bitvault.services.local;

import com.bitvault.database.daos.CategoryDao;
import com.bitvault.database.daos.ICategoryDao;
import com.bitvault.database.daos.ISecureDetailsDao;
import com.bitvault.database.daos.SecureDetailsDao;
import com.bitvault.database.models.CategoryDM;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.ui.model.Category;
import com.bitvault.util.Result;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoryService implements ICategoryService {


    private final ConnectionProvider connectionProvider;

    public CategoryService(final ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }


    @Override
    public Result<List<Category>> getCategories() {

        try (Connection connection = connectionProvider.connect()) {

            final List<CategoryDM> categories = new CategoryDao(connection).get();

            final List<Category> list = categories.stream()
                    .map(CategoryDM::convert)
                    .toList();

            return Result.ok(list);

        } catch (SQLException e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<Category> create(final Category category) {

        try (Connection connection = connectionProvider.connect()) {

            final ICategoryDao categoryDao = new CategoryDao(connection);

            final CategoryDM categoryDM = CategoryDM.createNew(category);

            //save
            categoryDao.create(categoryDM);

            final Category catResult = CategoryDM.convert(categoryDM);
            return Result.ok(catResult);

        } catch (SQLException e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<Category> update(Category category) {

        try (Connection connection = connectionProvider.connect()) {

            final ICategoryDao categoryDao = new CategoryDao(connection);

            final CategoryDM categoryDM = categoryDao.get(category.id());

            if (categoryDM == null) {
                return Result.error(new Exception("No category found"));
            }

            final CategoryDM tpUpdate = CategoryDM.convertUpdate(category);
            categoryDao.update(tpUpdate);

            final Category catResult = CategoryDM.convert(categoryDM);

            return Result.ok(catResult);

        } catch (SQLException e) {
            return Result.error(e);
        }

    }

    @Override
    public Result<Boolean> delete(String id) {
        try (Connection connection = connectionProvider.connect()) {

            final ISecureDetailsDao secureDetailsDao = new SecureDetailsDao(connection);

            int count = secureDetailsDao.countByCategoryId(id);

            if (count > 0) {
                return Result.error(new Exception("Category must be empty to delete."));
            }

            final ICategoryDao categoryDao = new CategoryDao(connection);

            final CategoryDM categoryDM = categoryDao.get(id);

            if (categoryDM == null) {
                return Result.error(new Exception("No category found"));
            }

            categoryDao.delete(id);

            return Result.Success;

        } catch (SQLException e) {
            return Result.error(e);
        }
    }
}
