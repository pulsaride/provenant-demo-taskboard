package com.github.hantsy.ee8sample;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserVersionType;

/**
 * Version seed of the legacy provider (engine-authored, listed in
 * reserved_to_emitter.engine_files of modernization-contract.json, RFC-040
 * section 8): EclipseLink writes 1 on the first insert and current + 1 on every
 * update, Hibernate's own seed is 0. The field stays null until the insert.
 */
public class EclipseLinkLongVersionType implements UserVersionType<Long> {

    @Override
    public int getSqlType() {
        return Types.BIGINT;
    }

    @Override
    public Class<Long> returnedClass() {
        return Long.class;
    }

    @Override
    public boolean equals(Long x, Long y) {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Long x) {
        return Objects.hashCode(x);
    }

    @Override
    public Long nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner)
            throws SQLException {
        Long value = rs.getLong(position);
        return rs.wasNull() ? null : value;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Long value, int index, SharedSessionContractImplementor session)
            throws SQLException {
        if (value == null) {
            st.setNull(index, Types.BIGINT);
        } else {
            st.setLong(index, value);
        }
    }

    @Override
    public Long deepCopy(Long value) {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Long value) {
        return value;
    }

    @Override
    public Long assemble(Serializable cached, Object owner) {
        return (Long) cached;
    }

    @Override
    public Long replace(Long detached, Long managed, Object owner) {
        return detached;
    }

    @Override
    public Long seed(SharedSessionContractImplementor session) {
        return 1L;
    }

    @Override
    public Long next(Long current, SharedSessionContractImplementor session) {
        return current + 1;
    }

    @Override
    public int compare(Long x, Long y) {
        return x.compareTo(y);
    }
}
