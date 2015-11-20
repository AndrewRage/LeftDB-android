package com.github.andreyrage.leftdb;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import static com.github.andreyrage.leftdb.Utils.checkNotNullOrEmpty;
import static com.github.andreyrage.leftdb.Utils.nonNullString;
import static com.github.andreyrage.leftdb.Utils.unmodifiableListOf;

/**
 * Created by Vlad on 11/20/15.
 */
public final class DeleteQuery {

	@NonNull private final String table;

	@NonNull private final String where;

	@NonNull private final List<String> whereArgs;

	private DeleteQuery(@NonNull String table, @NonNull String where, @NonNull List<String> whereArgs) {
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

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DeleteQuery that = (DeleteQuery) o;

		return table.equals(that.table)
				&& where.equals(that.where)
				&& whereArgs.equals(that.whereArgs);
	}

	@Override public int hashCode() {
		int result = table.hashCode();
		result = 31 * result + where.hashCode();
		result = 31 * result + whereArgs.hashCode();
		return result;
	}

	@Override public String toString() {
		return "DeleteQuery{" +
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
			checkNotNullOrEmpty(table, "Table name is null or empty");
			this.table = table;
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

		@NonNull public DeleteQuery build() {
			if (where == null && whereArgs != null && !whereArgs.isEmpty()) {
				throw new IllegalStateException("You can not use whereArgs without where clause");
			}

			return new DeleteQuery(
					table,
					where,
					whereArgs
			);
		}

	}

}