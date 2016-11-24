package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.annotation.ColumnAutoInc;

/**
 * Created by rage on 28.11.15.
 */
public class AutoIncId {
    @ColumnAutoInc private Long key;
    private String name;

    public AutoIncId() {
    }

    public AutoIncId(String name) {
        this.name = name;
    }

    public AutoIncId(Long primKey, String name) {
        this.key = primKey;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AutoIncId that = (AutoIncId) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AutoIncId{" +
                "key=" + key +
                ", name='" + name + '\'' +
                '}';
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
