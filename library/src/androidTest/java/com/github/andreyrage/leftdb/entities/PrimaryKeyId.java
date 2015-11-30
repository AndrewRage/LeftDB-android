package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.annotation.ColumnPrimaryKey;

/**
 * Created by rage on 28.11.15.
 */
public class PrimaryKeyId {
    @ColumnPrimaryKey private Long primKey;
    private String name;

    public PrimaryKeyId() {
    }

    public PrimaryKeyId(Long primKey, String name) {
        this.primKey = primKey;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrimaryKeyId that = (PrimaryKeyId) o;

        if (primKey != null ? !primKey.equals(that.primKey) : that.primKey != null) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = primKey != null ? primKey.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PrimaryKeyId{" +
                "primKey=" + primKey +
                ", name='" + name + '\'' +
                '}';
    }

    public Long getPrimKey() {
        return primKey;
    }

    public void setPrimKey(Long primKey) {
        this.primKey = primKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
