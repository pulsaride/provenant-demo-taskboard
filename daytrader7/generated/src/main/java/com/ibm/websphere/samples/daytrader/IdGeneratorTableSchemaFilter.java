package com.ibm.websphere.samples.daytrader;

import java.util.Locale;
import java.util.Set;
import org.hibernate.boot.model.relational.Namespace;
import org.hibernate.boot.model.relational.Sequence;
import org.hibernate.mapping.Table;
import org.hibernate.tool.schema.internal.DefaultSchemaFilter;
import org.hibernate.tool.schema.spi.SchemaFilter;
import org.hibernate.tool.schema.spi.SchemaFilterProvider;

/**
 * Validation filter of the id generator tables (engine-authored, listed in
 * reserved_to_emitter.engine_files of modernization-contract.json, RFC-040 ruling 5):
 * the legacy schema declares the generator's counter column as an integer type narrower
 * than bigint (RFC-040 ruling 5 amendment 1). Only the declared generator tables are left out, and only of the
 * validation; named in application-legacydb.properties only.
 */
public class IdGeneratorTableSchemaFilter implements SchemaFilterProvider {

    private static final Set<String> GENERATOR_TABLES = Set.of("KEYGENEJB");

    private static final SchemaFilter VALIDATE = new SchemaFilter() {
        @Override
        public boolean includeNamespace(Namespace namespace) {
            return true;
        }

        @Override
        public boolean includeTable(Table table) {
            return !GENERATOR_TABLES.contains(table.getName().toUpperCase(Locale.ROOT));
        }

        @Override
        public boolean includeSequence(Sequence sequence) {
            return true;
        }
    };

    @Override
    public SchemaFilter getCreateFilter() {
        return DefaultSchemaFilter.INSTANCE;
    }

    @Override
    public SchemaFilter getDropFilter() {
        return DefaultSchemaFilter.INSTANCE;
    }

    @Override
    public SchemaFilter getMigrateFilter() {
        return DefaultSchemaFilter.INSTANCE;
    }

    @Override
    public SchemaFilter getTruncatorFilter() {
        return DefaultSchemaFilter.INSTANCE;
    }

    @Override
    public SchemaFilter getValidateFilter() {
        return VALIDATE;
    }
}
