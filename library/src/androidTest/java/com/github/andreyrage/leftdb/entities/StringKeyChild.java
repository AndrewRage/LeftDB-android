package com.github.andreyrage.leftdb.entities;

/**
 * Created by rage on 11/30/15.
 */
public class StringKeyChild {
    private String parentKey;
    private String name;

    public StringKeyChild() {
    }

    public StringKeyChild(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringKeyChild that = (StringKeyChild) o;

        if (parentKey != null ? !parentKey.equals(that.parentKey) : that.parentKey != null)
            return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = parentKey != null ? parentKey.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NotLongChild{" +
                "parentKey='" + parentKey + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
