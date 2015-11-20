package com.github.andreyrage.leftdb;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public final class UpdateQuery {

	@NonNull private final String table;

	@NonNull private final String where;

	@NonNull private final List<String> whereArgs;

	private UpdateQuery(@NonNull String table, @NonNull String where, @NonNull List<String> whereArgs) {
		this.table = table;
		this.where = where;
		this.whereArgs = whereArgs;
	}

	@NonNull public String table() {
		return table;
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

		return table.equals(that.table)
				&& where.equals(that.where)
				&& whereArgs.equals(that.whereArgs);
	}

	@Override
	public int hashCode() {
		int result = table.hashCode();
		result = 31 * result + where.hashCode();
		result = 31 * result + whereArgs.hashCode();
		return result;
	}

	@Override public String toString() {
		return "UpdateQuery{" +
				"table='" + table + '\'' +
				", where='" + where + '\'' +
				", whereArgs=" + whereArgs +
				'}';
	}

	@NonNull public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {

		private String table;

		private String where;

		private List<String> whereArgs;

		Builder() {
		}

		@NonNull public Builder table(@NonNull String table) {
			Utils.checkNotNullOrEmpty(table, "Table name is null or empty");
			this.table = table;
			return this;
		}

		@NonNull public Builder where(@Nullable String where) {
			this.where = Utils.nonNullString(where);
			return this;
		}

		@NonNull public Builder whereArgs(@Nullable Object... whereArgs) {
			this.whereArgs = Utils.unmodifiableListOf(String.class, new Func<List<String>, Object, Void>() {
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
					table,
					where,
					whereArgs
			);
		}

	}

}