package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.annotation.ColumnName;
import com.github.andreyrage.leftdb.annotation.ColumnPrimaryKey;

/**
 * Created by rage on 26.07.16.
 */

public abstract class BaseEntity {
    @ColumnPrimaryKey
    private long id;

    @ColumnName("base")
    private String baseField;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBaseField() {
        return baseField;
    }

    public void setBaseField(String baseField) {
        this.baseField = baseField;
    }
}
