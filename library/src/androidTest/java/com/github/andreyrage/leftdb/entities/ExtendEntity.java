package com.github.andreyrage.leftdb.entities;

/**
 * Created by rage on 26.07.16.
 */

public class ExtendEntity extends BaseEntity {
    private String field;

    public ExtendEntity() {
    }

    public ExtendEntity(long id, String baseField, String field) {
        setId(id);
        setBaseField(baseField);
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
