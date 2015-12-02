package com.github.andreyrage.leftdb.queries;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.andreyrage.leftdb.annotation.TableName;

import java.util.List;

import static com.github.andreyrage.leftdb.utils.CheckNullUtils.checkNotNull;
import static com.github.andreyrage.leftdb.utils.CheckNullUtils.nonNullString;
import static com.github.andreyrage.leftdb.utils.CheckNullUtils.unmodifiableListOfStrings;

/**
 * Created by Vlad on 11/19/15.
 */

public final class SelectQuery {

	private final @NonNull Class<?> entity;
	private final boolean distinct;
	private final @NonNull List<String> columns;
	private final @NonNull String where;
	private final @NonNull List<String> whereArgs;
	private final @NonNull String groupBy;
	private final @NonNull String having;
	private final @NonNull String orderBy;
	private final @NonNull String limit;

	public SelectQuery(@NonNull Class<?> entity, boolean distinct,
					   @NonNull List<String> columns, @NonNull String where,
					   @NonNull List<String> whereArgs, @NonNull String groupBy,
					   @NonNull String having, @NonNull String orderBy,
					   @NonNull String limit) {
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

	@NonNull
	public String table() {
		if (entity.isAnnotationPresent(TableName.class)) {
			return entity.getAnnotation(TableName.class).value();
		}
		return entity.getSimpleName();
	}

	@NonNull
	public Class<?> entity() {
		return entity;
	}

	@NonNull
	public List<String> columns() {
		return columns;
	}

	@NonNull
	public String where() {
		return where;
	}

	@NonNull
	public List<String> whereArgs() {
		return whereArgs;
	}

	@NonNull
	public String groupBy() {
		return groupBy;
	}

	@NonNull
	public String having() {
		return having;
	}

	@NonNull
	public String orderBy() {
		return orderBy;
	}

	@NonNull
	public String limit() {
		return limit;
	}

	@NonNull
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

		private String[] columns;

		private String where;

		private Object[] whereArgs;

		private String groupBy;

		private String having;

		private String orderBy;

		private String limit;

		@NonNull
		public Builder entity(@NonNull Class<?> entity) {
			checkNotNull(entity, "Table name is null or empty");
			this.entity = entity;
			return this;
		}

		@NonNull
		public Builder distinct(boolean distinct) {
			this.distinct = distinct;
			return this;
		}


		@NonNull
		public Builder columns(@Nullable String... columns) {
			this.columns = columns;
			return this;
		}

		@NonNull
		public Builder where(@Nullable String where) {
			this.where = where;
			return this;
		}


		@NonNull
		public Builder whereArgs(@Nullable Object... whereArgs) {
			this.whereArgs = whereArgs;
			return this;
		}

		@NonNull
		public Builder groupBy(@Nullable String groupBy) {
			this.groupBy = groupBy;
			return this;
		}


		@NonNull
		public Builder having(@Nullable String having) {
			this.having = having;
			return this;
		}

		@NonNull
		public Builder orderBy(@Nullable String orderBy) {
			this.orderBy = orderBy;
			return this;
		}

		@NonNull
		public Builder limit(final int limit) {
			if (limit <= 0) {
				throw new IllegalStateException("Parameter `limit` should be positive");
			}
			this.limit = String.valueOf(limit);
			return this;
		}

		@NonNull
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

		@NonNull
		public SelectQuery build() {
			if (where == null && whereArgs != null && whereArgs.length > 0) {
				throw new IllegalStateException("You can not use whereArgs without where clause");
			}
			return new SelectQuery(
					entity,
					distinct,
					unmodifiableListOfStrings(columns),
					nonNullString(where),
					unmodifiableListOfStrings(whereArgs),
					nonNullString(groupBy),
					nonNullString(having),
					nonNullString(orderBy),
					limit == null ? nonNullString(limit) : limit
			);

		}

	}

}