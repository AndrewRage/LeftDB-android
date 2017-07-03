package com.github.andreyrage.leftdb.entities;

/**
 * Created by rage on 11/30/15.
 */
public class FloatKeyChild {
    private Float primaryKey;
    private String name;

    public FloatKeyChild() {
    }

    public FloatKeyChild(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FloatKeyChild that = (FloatKeyChild) o;

        if (primaryKey != null ? !primaryKey.equals(that.primaryKey) : that.primaryKey != null)
            return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = primaryKey != null ? primaryKey.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FloatKeyChild{" +
                "primaryKey=" + primaryKey +
                ", name='" + name + '\'' +
                '}';
    }

    public Float getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Float primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
