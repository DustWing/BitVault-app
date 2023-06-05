package com.bitvault.database.daos;

import com.bitvault.database.models.ProfileDM;
import com.bitvault.database.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileDao implements IProfileDao {
    private static final String sGetQ = "SELECT * FROM t_profiles";
    private static final String sGetByIdQ = "SELECT * FROM t_profiles WHERE ID=?";
    private static final String sCreateQ = "INSERT INTO t_profiles VALUES (?,?,?,?,?)";
    private static final String sUpdateQ = "UPDATE t_profiles SET name=?, modified_on=? WHERE ID=?";
    private static final String sDeleteQ = "DELETE FROM t_profiles WHERE ID=?";

    private final Connection connection;

    public ProfileDao(final Connection connection) {

        this.connection = connection;
    }

    @Override
    public List<ProfileDM> get() {

        return DBUtils.select(
                sGetQ,
                connection,
                ProfileDM::create,
                Collectors.toList()
        );


    }

    @Override
    public ProfileDM get(String id) {

        return DBUtils.selectById(id, sGetByIdQ, connection, ProfileDM::create);

    }

    @Override
    public void create(ProfileDM profile) {
        try (
                PreparedStatement stm = connection.prepareStatement(sCreateQ)
        ) {


            stm.setString(1, profile.id());
            stm.setString(2, profile.name());
            stm.setString(3, profile.createdOn());
            stm.setString(4, null);
            stm.setInt(5, 0);

            stm.execute();

        } catch (SQLException e) {
            throw new RuntimeException("", e);

        }
    }

    @Override
    public void update(ProfileDM profile) {
        try (
                PreparedStatement stm = connection.prepareStatement(sUpdateQ)
        ) {

            stm.setString(1, profile.name());
            stm.setString(2, profile.modifiedOn());
            stm.setString(3, profile.id());

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
