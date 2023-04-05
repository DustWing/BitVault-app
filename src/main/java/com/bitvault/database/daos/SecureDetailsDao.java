package com.bitvault.database.daos;

import com.bitvault.database.models.SecureDetailsDM;
import com.bitvault.database.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SecureDetailsDao implements ISecureDetailsDao {

    private static final String sGetQ = "SELECT * FROM t_secure_details";
    private static final String sCreateQ = "INSERT INTO t_secure_details VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String sUpdateQ = "UPDATE t_secure_details " +
            "SET category_id=?, domain=?, title=?, description=?, favourite=?, modified_on=?, expires_on=?, requires_mp=?, shared=? " +
            "WHERE ID=?";
    private static final String sDeleteQ = "DELETE FROM t_secure_details WHERE ID=?";

    private static final String sCountByCategoryIdQ = "SELECT COUNT(*) FROM t_secure_details WHERE category_id=?";

    private final Connection connection;

    public SecureDetailsDao(Connection connection) {

        this.connection = connection;
    }


    @Override
    public List<SecureDetailsDM> get() {
        return DBUtils.select(
                sGetQ,
                connection,
                SecureDetailsDM::create,
                Collectors.toList()
        );
    }

    @Override
    public void create(SecureDetailsDM secureDetails) {

        try (
                PreparedStatement stm = connection.prepareStatement(sCreateQ)
        ) {

            int index = 0;

            stm.setString(++index, secureDetails.id());
            stm.setString(++index, secureDetails.categoryId());
            stm.setString(++index, secureDetails.profileId());
            stm.setString(++index, secureDetails.domain());
            stm.setString(++index, secureDetails.title());
            stm.setString(++index, secureDetails.description());
            stm.setBoolean(++index, secureDetails.favourite());
            stm.setString(++index, secureDetails.createdOn());
            stm.setString(++index, secureDetails.modifiedOn());
            stm.setString(++index, secureDetails.expiresOn());
            stm.setString(++index, secureDetails.importedOn());
            stm.setBoolean(++index, secureDetails.requiresMp());
            stm.setBoolean(++index, secureDetails.shared());

            stm.execute();

        } catch (SQLException e) {
            throw new RuntimeException("", e);

        }

    }

    @Override
    public int update(SecureDetailsDM secureDetails) {
        try (
                PreparedStatement stm = connection.prepareStatement(sUpdateQ)
        ) {
            int index = 0;
            stm.setString(++index, secureDetails.categoryId());
            stm.setString(++index, secureDetails.domain());
            stm.setString(++index, secureDetails.title());
            stm.setString(++index, secureDetails.description());
            stm.setBoolean(++index, secureDetails.favourite());
            stm.setString(++index, secureDetails.modifiedOn());
            stm.setString(++index, secureDetails.expiresOn());
            stm.setBoolean(++index, secureDetails.requiresMp());
            stm.setBoolean(++index, secureDetails.shared());

            //where
            stm.setString(++index, secureDetails.id());

            return stm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("", e);

        }
    }

    @Override
    public int delete(String id) {
        try (
                PreparedStatement stm = connection.prepareStatement(sDeleteQ)
        ) {
            stm.setString(1, id);
            return stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("", e);
        }
    }


    @Override
    public int countByCategoryId(String id) {
        try (
                PreparedStatement stm = connection.prepareStatement(sCountByCategoryIdQ)
        ) {
            stm.setString(1, id);
            ResultSet resultSet = stm.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("", e);
        }
    }
}
