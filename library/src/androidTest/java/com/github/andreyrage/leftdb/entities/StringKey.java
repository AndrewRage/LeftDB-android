package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.annotation.ColumnChild;
import com.github.andreyrage.leftdb.annotation.ColumnPrimaryKey;

/**
 * eKreative
 * Created by rage on 11/30/15.
 */
public class StringKey {
    @ColumnPrimaryKey private String key;
    private String name;
    @ColumnChild(foreignKey = "parentKey", parentKey = "key")
    private StringKeyChild stringKeyChild;

    public StringKey() {
    }

    public StringKey(String key, String name, StringKeyChild stringKeyChild) {
        this.key = key;
        this.name = name;
        this.stringKeyChild = stringKeyChild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringKey that = (StringKey) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return !(stringKeyChild != null ? !stringKeyChild.equals(that.stringKeyChild) : that.stringKeyChild != null);

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (stringKeyChild != null ? stringKeyChild.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NotLongKey{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", notLongChild=" + stringKeyChild +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StringKeyChild getStringKeyChild() {
        return stringKeyChild;
    }

    public void setStringKeyChild(StringKeyChild stringKeyChild) {
        this.stringKeyChild = stringKeyChild;
    }
}
