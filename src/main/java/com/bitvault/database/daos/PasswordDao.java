package com.bitvault.database.daos;

import com.bitvault.database.models.PasswordDM;
import com.bitvault.database.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordDao implements IPasswordDao {

    private static final String sGetQ = "SELECT * FROM t_passwords";
    private static final String sGetByIdQ = "SELECT * FROM t_passwords WHERE ID=?";
    private static final String sCreateQ = "INSERT INTO t_passwords VALUES (?,?,?,?)";
    private static final String sUpdateQ = "UPDATE t_passwords SET username=?, password=? WHERE ID=?";
    private static final String sDeleteQ = "DELETE FROM t_passwords WHERE ID=?";

    private final Connection connection;

    public PasswordDao(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<PasswordDM> get() {


        return DBUtils.select(
                sGetQ,
                connection,
                PasswordDM::create,
                Collectors.toList()
        );

    }

    @Override
    public PasswordDM get(String id) {

        return DBUtils.selectById(id, sGetByIdQ, connection, PasswordDM::create);

    }

    @Override
    public void create(PasswordDM password) {
        try (
                PreparedStatement stm = connection.prepareStatement(sCreateQ)
        ) {


            stm.setString(1, password.id());
            stm.setString(2, password.username());
            stm.setString(3, password.password());
            stm.setString(4, password.secureDetailsId());

            stm.execute();

        } catch (SQLException e) {
            throw new RuntimeException("", e);

        }
    }

    @Override
    public int update(PasswordDM password) {
        try (
                PreparedStatement stm = connection.prepareStatement(sUpdateQ)
        ) {

            stm.setString(1, password.username());
            stm.setString(2, password.password());
            stm.setString(3, password.id());

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

}
