package com.bitvault.database.daos;

import com.bitvault.database.models.UserDM;
import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.database.utils.DBUtils;
import com.bitvault.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserDao implements IUserDao {

    private static final String sGetQ = "SELECT * FROM t_users";
    private static final String sCreateQ = "INSERT INTO t_users VALUES (?,?,?)";


    private final Connection connection;

    public UserDao(final Connection connection) {

        this.connection = connection;
    }


    @Override
    public UserDM get() {

        final List<UserDM> users = DBUtils.select(
                sGetQ,
                connection,
                UserDM::create,
                Collectors.toList()
        );

        if (users.isEmpty()) {
            throw new RuntimeException("No user found");
        }

        return users.get(0);


    }

    @Override
    public void create(UserDM user) {
        try (
                final PreparedStatement stm = connection.prepareStatement(sCreateQ)
        ) {
            stm.setString(1, user.id());
            stm.setString(2, user.name());
            stm.setString(3, user.credentials());
            stm.execute();

        } catch (SQLException e) {
            throw new RuntimeException("", e);
        }
    }


}
