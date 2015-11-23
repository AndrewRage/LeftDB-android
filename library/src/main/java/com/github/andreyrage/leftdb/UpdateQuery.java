package com.github.andreyrage.leftdb;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import static com.github.andreyrage.leftdb.Utils.checkNotNull;
import static com.github.andreyrage.leftdb.Utils.nonNullString;
import static com.github.andreyrage.leftdb.Utils.unmodifiableListOf;

public final class UpdateQuery {

	@NonNull private final Class<?> entity;

	@NonNull private final String where;

	@NonNull private final List<String> whereArgs;

	private UpdateQuery(@NonNull Class<?> entity, @NonNull String where, @NonNull List<String> whereArgs) {
		this.entity = entity;
		this.where = where;
		this.whereArgs = whereArgs;
	}

	@NonNull public String table() {
		return entity.getSimpleName();
	}

	@NonNull Class<?> entity() {
		return entity;
	}

	@NonNull public String where() {
		return where;
	}

	@NonNull public List<String> whereArgs() {
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

	@Override public String toString() {
		return "UpdateQuery{" +
				"entity='" + entity.getSimpleName() + '\'' +
				", where='" + where + '\'' +
				", whereArgs=" + whereArgs +
				'}';
	}

	@NonNull public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {

		private Class<?> entity;

		private String where;

		private List<String> whereArgs;

		Builder() {
		}

		@NonNull public Builder table(@NonNull Class<?> entity) {
			checkNotNull(entity, "Table name is null or empty");
			this.entity = entity;
			return this;
		}

		@NonNull public Builder where(@Nullable String where) {
			this.where = nonNullString(where);
			return this;
		}

		@NonNull public Builder whereArgs(@Nullable Object... whereArgs) {
			this.whereArgs = unmodifiableListOf(String.class, new Func<List<String>, Object, Void>() {
				@Override public Void invoke(List<String> strings, Object o) {
					strings.add(o != null ? o.toString() : "null");
					return null;
				}
			}, whereArgs);
			return this;
		}

		@NonNull public UpdateQuery build() {
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