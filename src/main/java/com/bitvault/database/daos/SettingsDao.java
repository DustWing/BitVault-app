package com.bitvault.database.daos;

import com.bitvault.database.models.SettingsDM;
import com.bitvault.database.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SettingsDao implements ISettingsDao {

    private static final String sFindAllAllQ = "Select name,value from t_settings";

    private static final String sInsertQ = "INSERT INTO t_settings (?, ?, ?, ?)";
    private static final String sUpdateQ = "UPDATE t_settings SET value=?, modified_on=? WHERE name=?";

    private final Connection connection;

    public SettingsDao(Connection connection) {
        this.connection = connection;
    }


    @Override
    public List<SettingsDM> findAll() {

        return DBUtils.select(
                sFindAllAllQ,
                connection,
                SettingsDM::create,
                Collectors.toList()
        );
    }

    @Override
    public int insert(Collection<SettingsDM> values) {

        try (PreparedStatement statement = connection.prepareStatement(sInsertQ)) {

            return DBUtils.batchExecute(
                    values.stream(),
                    statement,
                    (statement1, settingsDM) -> {
                        statement1.setString(1, settingsDM.name());
                        statement1.setString(2, settingsDM.value());
                        statement1.setString(3, settingsDM.createdOn());
                        statement1.setString(4, settingsDM.modifiedOn());
                    }
            );

        } catch (SQLException ex) {
            throw new RuntimeException("Error in SettingsDao Insert", ex);
        }
    }

    @Override
    public int update(Collection<SettingsDM> values) {
        try (PreparedStatement statement = connection.prepareStatement(sUpdateQ)) {

            return DBUtils.batchExecute(
                    values.stream(),
                    statement,
                    (statement1, settingsDM) -> {
                        statement1.setString(1, settingsDM.value());
                        statement1.setString(2, settingsDM.createdOn());
                        statement1.setString(3, settingsDM.name());
                    }
            );

        } catch (SQLException ex) {
            throw new RuntimeException("Error in SettingsDao Update", ex);
        }
    }
}
