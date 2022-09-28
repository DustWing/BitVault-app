package com.bitvault.database.utils;

import java.sql.*;
import java.util.stream.Collector;

public class DBUtils {

    public static final int FETCH_SIZE = 100;

    @FunctionalInterface
    public interface Constructor<T> {
        T create(final ResultSet rs) throws SQLException;
    }

    @FunctionalInterface
    public interface Accumulator<T> {
        void accumulate(final T object) throws SQLException;
    }

    public static <T, A, R> R select(
            final String sql,
            final Connection connection,
            final Constructor<T> constructor,
            final Collector<T, A, R> collector
    ) {

        final A a = collector.supplier().get();

        select(sql, connection, constructor,
                object -> collector.accumulator().accept(
                        a,
                        object
                )
        );

        return collector.finisher().apply(a);
    }

    public static <T> void select(
            final String sql,
            final Connection connection,
            final Constructor<T> constructor,
            final Accumulator<T> accumulator
    ) {

        select(sql, FETCH_SIZE, connection, constructor, accumulator);
    }

    public static <T> void select(
            final String sql,
            final int fetchSize,
            final Connection connection,
            final Constructor<T> constructor,
            final Accumulator<T> accumulator
    ) {

        try (
                final Statement stm = connection.createStatement()
        ) {

            stm.setFetchSize(fetchSize);
            try (final ResultSet rs = stm.executeQuery(sql)) {

                while (rs.next()) {

                    T obj = constructor.create(rs);
                    accumulator.accumulate(obj);

                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("", e);
        }
    }

    public static <T> T selectById(
            final String id,
            final String sql,
            final Connection connection,
            final Constructor<T> constructor
    ) {

        try (
                PreparedStatement stm = connection.prepareStatement(sql)
        ) {

            stm.setString(1, id);

            final ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return constructor.create(rs);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("", e);

        }
    }


}
