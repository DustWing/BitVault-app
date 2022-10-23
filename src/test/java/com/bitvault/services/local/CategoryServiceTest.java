package com.bitvault.services.local;

import com.bitvault.consts.Consts;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.database.provider.LocalDB;
import com.bitvault.ui.model.Category;
import com.bitvault.services.interfaces.ICategoryService;
import com.bitvault.ui.utils.BvColors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
        categoryService.create(category1)
                .apply(
                        category -> assertEquals(category.name(), category1.name()),
                        Assertions::fail
                );

        final Category category2 = new Category(
                UUID.randomUUID().toString(),
                "category2",
                BvColors.randomHex(),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                "Password"
        );
        categoryService.create(category2)
                .apply(
                        category -> assertEquals(category.name(), category2.name()),
                        Assertions::fail
                );
    }

    @Test
    void getCategories() {
        categoryService.getCategories().apply(
                System.out::println,
                Assertions::fail
        );
    }

    @Test
    void update() {

        categoryService.getCategories().apply(
                categories -> {

                    if (categories.isEmpty()) {
                        fail("No Categories found");
                    }

                    Category category = categories.get(0);

                    categoryService.update(
                            new Category(
                                    category.id(),
                                    category.name(),
                                    category.color(),
                                    category.createdOn(),
                                    LocalDateTime.now(),
                                    category.type()
                            )
                    ).apply(
                            Assertions::assertTrue,
                            Assertions::fail
                    );

                },
                Assertions::fail
        );
    }

    @Test
    void delete() {

        categoryService.getCategories().apply(
                categories -> {
                    if (categories.isEmpty()) {
                        fail("No Categories found");
                    }
                    Category category = categories.get(0);
                    categoryService.delete(category)
                            .apply(
                                    Assertions::assertTrue,
                                    Assertions::fail
                            );
                },
                Assertions::fail
        );
    }
}