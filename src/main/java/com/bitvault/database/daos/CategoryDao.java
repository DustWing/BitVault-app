package com.bitvault.database.daos;

import com.bitvault.database.models.CategoryDM;
import com.bitvault.database.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryDao implements ICategoryDao {

    private static final String sGetQ = "SELECT * FROM t_categories";
    private static final String sGetByIdQ = "SELECT * FROM t_categories WHERE ID=?";
    private static final String sCreateQ = "INSERT INTO t_categories VALUES (?,?,?,?,?,?)";
    private static final String sUpdateQ = "UPDATE t_categories SET name=?, color=?, modified_on=? WHERE ID=?";
    private static final String sDeleteQ = "DELETE FROM t_categories WHERE ID=?";

    private final Connection connection;

    public CategoryDao(Connection connection) {

        this.connection = connection;
    }


    @Override
    public List<CategoryDM> get() {

        return DBUtils.select(
                sGetQ,
                connection,
                CategoryDM::create,
                Collectors.toList()
        );

    }

    @Override
    public CategoryDM get(String id) {

        return DBUtils.selectById(id, sGetByIdQ, connection, CategoryDM::create);

    }

    @Override
    public void create(CategoryDM category) {

        try (
                PreparedStatement stm = connection.prepareStatement(sCreateQ)
        ) {

            stm.setString(1, category.id());
            stm.setString(2, category.name());
            stm.setString(3, category.color());
            stm.setString(4, category.createdOn());
            stm.setString(5, null);
            stm.setString(6, category.type());

            stm.execute();

        } catch (SQLException e) {
            throw new RuntimeException("", e);

        }

    }

    @Override
    public void update(CategoryDM category) {
        try (
                PreparedStatement stm = connection.prepareStatement(sUpdateQ)
        ) {

            stm.setString(1, category.name());
            stm.setString(2, category.color());
            stm.setString(3, category.modifiedOn());
            stm.setString(4, category.id());

            stm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("", e);

        }
    }

    @Override
    public void delete(String id) {
        try (
                PreparedStatement stm = connection.prepareStatement(sDeleteQ)
        ) {
            stm.setString(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("", e);

        }
    }

}
