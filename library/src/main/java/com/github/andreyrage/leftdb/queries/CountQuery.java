/*
 * Copyright 2017 Andrii Horishnii
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.andreyrage.leftdb.queries;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.andreyrage.leftdb.annotation.TableName;

import java.util.List;

import static com.github.andreyrage.leftdb.utils.CheckNullUtils.checkNotNull;
import static com.github.andreyrage.leftdb.utils.CheckNullUtils.nonNullString;
import static com.github.andreyrage.leftdb.utils.CheckNullUtils.unmodifiableListOfStrings;

public final class CountQuery {

	@NonNull private final Class<?> entity;
	@NonNull private final String where;
	@NonNull private final List<String> whereArgs;

	private CountQuery(@NonNull Class<?> entity, @NonNull String where,
					   @NonNull List<String> whereArgs) {
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
	public Class<?> entity() {
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

		CountQuery that = (CountQuery) o;

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

	@NonNull
	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {

		private Class<?> entity;

		private String where;

		private Object[] whereArgs;

		Builder() {
		}

		@NonNull
		public Builder entity(@NonNull Class<?> entity) {
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
			this.whereArgs = whereArgs;
			return this;
		}

		@NonNull
		public CountQuery build() {
			checkNotNull(entity, "Table name is null or empty");

			if (where == null && whereArgs != null && whereArgs.length > 0) {
				throw new IllegalStateException("You can not use whereArgs without where clause");
			}

			return new CountQuery(
					entity,
					nonNullString(where),
					unmodifiableListOfStrings(whereArgs)
			);
		}

	}

}