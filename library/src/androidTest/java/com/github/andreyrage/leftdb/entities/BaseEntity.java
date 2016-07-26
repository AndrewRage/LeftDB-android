package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.annotation.ColumnPrimaryKey;

/**
 * Created by rage on 26.07.16.
 */

public abstract class BaseEntity {
    @ColumnPrimaryKey
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
