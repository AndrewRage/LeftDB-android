package com.github.andreyrage.leftdb.queries;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.andreyrage.leftdb.annotation.TableName;
import com.github.andreyrage.leftdb.interfaces.Func;

import java.util.List;

import static com.github.andreyrage.leftdb.utils.CheckNullUtils.*;

/**
 * Created by Vlad on 11/20/15.
 */
public final class DeleteQuery {

    @NonNull private final Class<?> entity;
    @NonNull private final String where;
    @NonNull private final List<String> whereArgs;

    private DeleteQuery(@NonNull Class<?> entity, @NonNull String where, @NonNull List<String> whereArgs) {
        this.entity = entity;
        this.where = where;
        this.whereArgs = whereArgs;
    }

    @NonNull
    public String table() {
        if (entity.isAnnotationPresent(TableName.class)) {
            return entity.getAnnotation(TableName.class).value();
        }
        return entity.getSimpleName();
    }

    @NonNull
    Class<?> entity() {
        return entity;
    }

    @NonNull
    public String where() {
        return where;
    }

    @NonNull
    public List<String> whereArgs() {
        return whereArgs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeleteQuery that = (DeleteQuery) o;

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
        return "DeleteQuery{" +
                "entity='" + entity.getSimpleName() + '\'' +
                ", where='" + where + '\'' +
                ", whereArgs=" + whereArgs +
                '}';
    }

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Class<?> entity;

        private String where;

        private List<String> whereArgs;

        Builder() {
        }

        @NonNull
        public Builder entity(@NonNull Class<?> entity) {
            checkNotNull(entity, "Table name is null or empty");
            this.entity = entity;
            return this;
        }

        @NonNull
        public Builder where(@Nullable String where) {
            this.where = nonNullString(where);
            return this;
        }

        @NonNull
        public Builder whereArgs(@Nullable Object... whereArgs) {
            this.whereArgs = unmodifiableListOf(String.class, new Func<List<String>, Object, Void>() {
                @Override
                public Void invoke(List<String> strings, Object o) {
                    strings.add(o != null ? o.toString() : "null");
                    return null;
                }
            }, whereArgs);
            return this;
        }

        @NonNull
        public DeleteQuery build() {
            if (where == null && whereArgs != null && !whereArgs.isEmpty()) {
                throw new IllegalStateException("You can not use whereArgs without where clause");
            }

            return new DeleteQuery(
                    entity,
                    where,
                    whereArgs
            );
        }

    }

}