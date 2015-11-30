package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.annotation.ColumnChild;
import com.github.andreyrage.leftdb.annotation.ColumnPrimaryKey;

/**
 * eKreative
 * Created by rage on 11/30/15.
 */
public class FloatKey {
    @ColumnPrimaryKey Float key;
    private String name;
    @ColumnChild(foreignKey = "primaryKey", parentKey = "key") private FloatKeyChild floatKeyChild;

    public FloatKey() {
    }

    public FloatKey(Float key, String name, FloatKeyChild floatKeyChild) {
        this.key = key;
        this.name = name;
        this.floatKeyChild = floatKeyChild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FloatKey floatKey = (FloatKey) o;

        if (key != null ? !key.equals(floatKey.key) : floatKey.key != null) return false;
        if (name != null ? !name.equals(floatKey.name) : floatKey.name != null) return false;
        return !(floatKeyChild != null ? !floatKeyChild.equals(floatKey.floatKeyChild) : floatKey.floatKeyChild != null);

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (floatKeyChild != null ? floatKeyChild.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FloatKey{" +
                "key=" + key +
                ", name='" + name + '\'' +
                ", floatKeyChild=" + floatKeyChild +
                '}';
    }

    public Float getKey() {
        return key;
    }

    public void setKey(Float key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FloatKeyChild getFloatKeyChild() {
        return floatKeyChild;
    }

    public void setFloatKeyChild(FloatKeyChild floatKeyChild) {
        this.floatKeyChild = floatKeyChild;
    }
}
