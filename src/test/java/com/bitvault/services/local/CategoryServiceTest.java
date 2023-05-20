package com.bitvault.services.local;

import com.bitvault.consts.Consts;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.database.provider.LocalDB;
import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.utils.BvColors;
import com.bitvault.util.Result;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Order(3)
class CategoryServiceTest {

    static ICategoryService categoryService;


    @BeforeAll
    static void init() {
        final String location = Consts.location;

        if (location.isBlank()) {
            throw new RuntimeException("Set up location for test file");
        }

        final ConnectionProvider connectionProvider = new LocalDB(location);

        categoryService = new CategoryService(connectionProvider);
    }

    @Test
    void create() {
        final Category category1 = new Category(
                UUID.randomUUID().toString(),
                "category",
                BvColors.randomHex(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Password"
        );

        Result<Category> categoryResult = categoryService.create(category1);

        if (categoryResult.hasError()) {
            fail(categoryResult.getError());
        }

        Category category = categoryResult.get();

        assertEquals(category.name(), category1.name());


        final Category category2 = new Category(
                UUID.randomUUID().toString(),
                "category2",
                BvColors.randomHex(),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                "Password"
        );


        Result<Category> categoryResult2 = categoryService.create(category2);

        if (categoryResult2.hasError()) {
            fail(categoryResult2.getError());
        }

        Category categoryRes2 = categoryResult2.get();

        assertEquals(categoryRes2.name(), category2.name());

    }

    @Test
    void getCategories() {
        Result<List<Category>> resultCat = categoryService.getCategories();

        if (resultCat.hasError()) {
            fail(resultCat.getError());
        }

        System.out.println(resultCat.get());

    }

    @Test
    void update() {
        Result<List<Category>> resultCat = categoryService.getCategories();

        if (resultCat.hasError()) {
            fail(resultCat.getError());
            return;
        }

        List<Category> categories = resultCat.get();
        if (categories.isEmpty()) {
            fail("No Categories found");
            return;
        }

        Category category = categories.get(0);

        Result<Category> update = categoryService.update(
                new Category(
                        category.id(),
                        category.name(),
                        category.color(),
                        category.createdOn(),
                        LocalDateTime.now(),
                        category.type()
                )
        );

        if (update.hasError()) {
            fail(update.getError());
        }


    }

    @Test
    void delete() {

        Result<List<Category>> resultCat = categoryService.getCategories();

        if (resultCat.hasError()) {
            fail(resultCat.getError());
            return;
        }

        List<Category> categories = resultCat.get();
        if (categories.isEmpty()) {
            fail("No Categories found");
            return;
        }

        Category category = categories.get(0);
        Result<Boolean> delete = categoryService.delete(category.id());
        if (delete.hasError()) {
            fail(delete.getError());
        }

    }
}