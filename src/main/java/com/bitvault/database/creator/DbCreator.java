package com.bitvault.database.creator;

import com.bitvault.database.provider.ConnectionProvider;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.bitvault.database.utils.DbScripts.*;

public class DbCreator implements IDbCreator {

    private final ConnectionProvider connectionProvider;

    public DbCreator(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }


    @Override
    public boolean create() {
        try {
            File file = new File(connectionProvider.getLocation());

            boolean created = file.createNewFile();
            if (!created) {
                throw new RuntimeException("Failed to make file" + file.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (
                final Connection connection = connectionProvider.connect();
                final PreparedStatement t_users_ps = connection.prepareStatement(t_users);
                final PreparedStatement t_passwords_ps = connection.prepareStatement(t_passwords);
                final PreparedStatement t_secure_details_ps = connection.prepareStatement(t_secure_details);
                final PreparedStatement t_categories_ps = connection.prepareStatement(t_categories);
                final PreparedStatement t_profiles_ps = connection.prepareStatement(t_profiles);
                final PreparedStatement t_domain_details_ps = connection.prepareStatement(t_domain_details)
        ) {
            connection.setAutoCommit(false);
            t_users_ps.execute();
            t_passwords_ps.execute();
            t_secure_details_ps.execute();
            t_categories_ps.execute();
            t_profiles_ps.execute();
            t_domain_details_ps.execute();
            connection.commit();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return true;
    }
}
