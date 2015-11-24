package com.github.andreyrage.leftdb.queries;

import com.github.andreyrage.leftdb.annotation.TableName;
import com.github.andreyrage.leftdb.interfaces.Func;
import com.github.andreyrage.leftdb.utils.CheckNullUtils;

import java.util.List;

/**
 * Created by Vlad on 11/19/15.
 */
public final class SelectQuery {

    private final Class<?> entity;
    private final boolean distinct;
    private final List<String> columns;
    private final String where;
    private final List<String> whereArgs;
    private final String groupBy;
    private final String having;
    private final String orderBy;
    private final String limit;

    public SelectQuery(Class<?> entity, boolean distinct,
                       List<String> columns, String where,
                       List<String> whereArgs, String groupBy,
                       String having, String orderBy, String limit) {
        this.distinct = distinct;
        this.entity = entity;
        this.columns = columns;
        this.where = where;
        this.whereArgs = whereArgs;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
    }

    public boolean distinct() {
        return distinct;
    }

    public String table() {
        if (entity.isAnnotationPresent(TableName.class)) {
            return entity.getAnnotation(TableName.class).value();
        }
        return entity.getSimpleName();
    }

    public Class<?> entity() {
        return entity;
    }

    public List<String> columns() {
        return columns;
    }

    public String where() {
        return where;
    }

    public List<String> whereArgs() {
        return whereArgs;
    }

    public String groupBy() {
        return groupBy;
    }

    public String having() {
        return having;
    }

    public String orderBy() {
        return orderBy;
    }

    public String limit() {
        return limit;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SelectQuery query = (SelectQuery) o;

        return distinct == query.distinct
                && entity.equals(query.entity)
                && columns.equals(query.columns)
                && where.equals(query.where)
                && whereArgs.equals(query.whereArgs)
                && groupBy.equals(query.groupBy)
                && having.equals(query.having)
                && orderBy.equals(query.orderBy)
                && limit.equals(query.limit);

    }

    @Override
    public int hashCode() {
        int result = (distinct ? 1 : 0);
        result = 31 * result + (entity.hashCode());
        result = 31 * result + (columns.hashCode());
        result = 31 * result + (where.hashCode());
        result = 31 * result + (whereArgs.hashCode());
        result = 31 * result + (groupBy.hashCode());
        result = 31 * result + (having.hashCode());
        result = 31 * result + (orderBy.hashCode());
        result = (31 * result) + (limit.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "SelectQuery{" +
                "entity='" + entity.getSimpleName() + '\'' +
                ", distinct=" + distinct +
                ", columns=" + columns +
                ", where='" + where + '\'' +
                ", whereArgs=" + whereArgs +
                ", groupBy='" + groupBy + '\'' +
                ", having='" + having + '\'' +
                ", orderBy='" + orderBy + '\'' +
                ", limit='" + limit + '\'' +
                '}';
    }

    public static final class Builder {

        private Class<?> entity;

        private boolean distinct;

        private List<String> columns;

        private String where;

        private List<String> whereArgs;

        private String groupBy;

        private String having;

        private String orderBy;

        private String limit;

        public Builder entity(Class<?> entity) {
            CheckNullUtils.checkNotNull(entity, "Table name is null or empty");
            this.entity = entity;
            return this;
        }

        public Builder distinct(boolean distinct) {
            this.distinct = distinct;
            return this;
        }


        public Builder columns(String... columns) {
            this.columns = CheckNullUtils.unmodifiableListOf(String.class, new Func<List<String>, String, Void>() {
                @Override
                public Void invoke(List<String> strings, String o) {
                    strings.add(o != null ? o : "null");
                    return null;
                }
            }, columns);
            return this;
        }

        public Builder where(String where) {
            this.where = where;
            return this;
        }


        public Builder whereArgs(Object... whereArgs) {
            this.whereArgs = CheckNullUtils.unmodifiableListOf(String.class, new Func<List<String>, Object, Void>() {
                @Override
                public Void invoke(List<String> strings, Object o) {
                    strings.add(o != null ? o.toString() : "null");
                    return null;
                }
            }, whereArgs);
            return this;
        }

        public Builder groupBy(String groupBy) {
            this.groupBy = groupBy;
            return this;
        }


        public Builder having(String having) {
            this.having = having;
            return this;
        }

        public Builder orderBy(String orderBy) {
            this.orderBy = orderBy;
            return this;
        }

        public Builder limit(final int limit) {
            if (limit <= 0) {
                throw new IllegalStateException("Parameter `limit` should be positive");
            }
            this.limit = String.valueOf(limit);
            return this;
        }

        public Builder limit(final int offset, final int quantity) {
            if (offset < 0) {
                throw new IllegalStateException("Parameter `offset` should not be negative");
            }
            if (quantity <= 0) {
                throw new IllegalStateException("Parameter `quantity` should be positive");
            }
            this.limit = String.valueOf(offset) + ", " + String.valueOf(quantity);
            return this;
        }

        public SelectQuery build() {
            if (where == null && whereArgs != null && !whereArgs.isEmpty()) {
                throw new IllegalStateException("You can not use whereArgs without where clause");
            }
            return new SelectQuery(entity, distinct, columns,
                    where, whereArgs, groupBy, having,
                    orderBy, limit
            );

        }

    }

}
