package com.github.andreyrage.leftdb.config;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.github.andreyrage.leftdb.utils.CheckNullUtils.checkNotNull;

/**
 * Created by rage on 12/3/15.
 */
public class RelationshipConfig {
	public static final String NO_ACTION = "NO ACTION";
	public static final String SET_NULL = "SET NULL";
	public static final String SET_DEFAULT = "SET DEFAULT";
	public static final String CASCADE = "CASCADE";
	public static final String RESTRICT = "RESTRICT";

	@NonNull private String parentTable;
	@NonNull private String foreignKey;
	@NonNull private String parentKey;
	@Nullable private String onUpdate;
	@Nullable private String onDelete;

	private RelationshipConfig(@NonNull String parentTable, @NonNull String foreignKey, @NonNull String parentKey, @Nullable String onUpdate, @Nullable String onDelete) {
		this.parentTable = parentTable;
		this.foreignKey = foreignKey;
		this.parentKey = parentKey;
		this.onUpdate = onUpdate;
		this.onDelete = onDelete;
	}

	@NonNull
	public String parentTable() {
		return parentTable;
	}

	@NonNull
	public String foreignKey() {
		return foreignKey;
	}

	@NonNull
	public String parentKey() {
		return parentKey;
	}

	@Nullable
	public String onUpdate() {
		return onUpdate;
	}

	@Nullable
	public String onDelete() {
		return onDelete;
	}

	@NonNull
	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private String parentTable;
		private String foreignKey;
		private String parentKey;
		private String onUpdate;
		private String onDelete;

		public Builder parentTable(@NonNull String parentTable) {
			this.parentTable = parentTable;
			return this;
		}

		public Builder foreignKey(@NonNull String foreignKey) {
			this.foreignKey = foreignKey;
			return this;
		}

		public Builder parentKey(@NonNull String parentKey) {
			this.parentKey = parentKey;
			return this;
		}

		public Builder onUpdate(@Nullable String onUpdate) {
			this.onUpdate = onUpdate;
			return this;
		}

		public Builder onDelete(@Nullable String onDelete) {
			this.onDelete = onDelete;
			return this;
		}

		public RelationshipConfig build() {
			checkNotNull(parentTable, "Table name is null or empty");
			checkNotNull(foreignKey, "Foreign key is null or empty");
			checkNotNull(parentKey, "Parent key is null or empty");

			return new RelationshipConfig(
					parentTable,
					foreignKey,
					parentKey,
					onUpdate,
					onDelete
			);
		}
	}
}
