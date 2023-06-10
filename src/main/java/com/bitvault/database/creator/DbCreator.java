package com.bitvault.database.creator;

import com.bitvault.database.provider.ConnectionProvider;
import com.bitvault.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.bitvault.database.utils.DbScripts.*;

public class DbCreator implements IDbCreator {

    private static final Logger logger = LoggerFactory.getLogger(DbCreator.class);

    private final ConnectionProvider connectionProvider;

    public DbCreator(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }


    @Override
    public Result<Boolean> create() {
        try {
            File file = new File(connectionProvider.getLocation());

            if (file.exists()) {
                return Result.error(new RuntimeException("File already exists " + file.getAbsolutePath()));
            }

            boolean created = file.createNewFile();
            if (!created) {
                return Result.error(new RuntimeException("Failed to make file " + file.getAbsolutePath()));
            }
        } catch (Exception ex) {
            return Result.error(ex);
        }

        try (
                final Connection connection = connectionProvider.connect();
                final PreparedStatement t_users_ps = connection.prepareStatement(t_users);
                final PreparedStatement t_passwords_ps = connection.prepareStatement(t_passwords);
                final PreparedStatement t_secure_details_ps = connection.prepareStatement(t_secure_details);
                final PreparedStatement t_categories_ps = connection.prepareStatement(t_categories);
                final PreparedStatement t_profiles_ps = connection.prepareStatement(t_profiles);
                final PreparedStatement t_domain_details_ps = connection.prepareStatement(t_domain_details);
                final PreparedStatement t_passwords_audit_ps = connection.prepareStatement(t_passwords_audit);
                final PreparedStatement t_secure_details_audit_ps = connection.prepareStatement(t_secure_details_audit);
                final PreparedStatement t_settings_ps = connection.prepareStatement(t_settings);
        ) {
            connection.setAutoCommit(false);

            t_users_ps.execute();
            t_passwords_ps.execute();
            t_secure_details_ps.execute();
            t_categories_ps.execute();
            t_profiles_ps.execute();
            t_domain_details_ps.execute();
            t_passwords_audit_ps.execute();
            t_secure_details_audit_ps.execute();
            t_settings_ps.execute();

            connection.commit();
        } catch (SQLException ex) {

            return Result.error(ex);
        }

        return Result.Success;
    }

//    private void deleteFileOnFail() {
//        try {
//            File file = new File(connectionProvider.getLocation());
//            boolean delete = file.delete();
//            if (!delete) {
//                logger.error("[deleteFileOnFail] could not delete file");
//            }
//        } catch (Exception exception) {
//            logger.error("[deleteFileOnFail]", exception);
//        }
//    }
}
