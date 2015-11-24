package com.github.andreyrage.leftdb.queries;

import com.github.andreyrage.leftdb.annotation.TableName;
import com.github.andreyrage.leftdb.interfaces.Func;

import java.util.List;

import static com.github.andreyrage.leftdb.utils.CheckNullUtils.checkNotNull;
import static com.github.andreyrage.leftdb.utils.CheckNullUtils.nonNullString;
import static com.github.andreyrage.leftdb.utils.CheckNullUtils.unmodifiableListOf;

public final class UpdateQuery {

    private final Class<?> entity;
    private final String where;
    private final List<String> whereArgs;

    private UpdateQuery(Class<?> entity, String where, List<String> whereArgs) {
        this.entity = entity;
        this.where = where;
        this.whereArgs = whereArgs;
    }

    public String table() {
        if (entity.isAnnotationPresent(TableName.class)) {
            return entity.getAnnotation(TableName.class).value();
        }
        return entity.getSimpleName();
    }

    Class<?> entity() {
        return entity;
    }

    public String where() {
        return where;
    }

    public List<String> whereArgs() {
        return whereArgs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateQuery that = (UpdateQuery) o;

        return entity.equals(that.entity)
                && where.equals(that.where)
                && whereArgs.equals(that.whereArgs);
    }

    @Override
    public int hashCode() {
        int result = entity.hashCode();
        result = 31 * result + where.hashCode();
        result = 31 * result + whereArgs.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UpdateQuery{" +
                "entity='" + entity.getSimpleName() + '\'' +
                ", where='" + where + '\'' +
                ", whereArgs=" + whereArgs +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Class<?> entity;

        private String where;

        private List<String> whereArgs;

        Builder() {
        }

        public Builder table(Class<?> entity) {
            checkNotNull(entity, "Table name is null or empty");
            this.entity = entity;
            return this;
        }

        public Builder where(String where) {
            this.where = nonNullString(where);
            return this;
        }

        public Builder whereArgs(Object... whereArgs) {
            this.whereArgs = unmodifiableListOf(String.class, new Func<List<String>, Object, Void>() {
                @Override
                public Void invoke(List<String> strings, Object o) {
                    strings.add(o != null ? o.toString() : "null");
                    return null;
                }
            }, whereArgs);
            return this;
        }

        public UpdateQuery build() {
            if (where == null && whereArgs != null && !whereArgs.isEmpty()) {
                throw new IllegalStateException("You can not use whereArgs without where clause");
            }

            return new UpdateQuery(
                    entity,
                    where,
                    whereArgs
            );
        }

    }

}