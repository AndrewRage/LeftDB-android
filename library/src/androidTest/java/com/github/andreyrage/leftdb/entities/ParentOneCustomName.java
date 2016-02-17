package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.annotation.ColumnAutoInc;
import com.github.andreyrage.leftdb.annotation.ColumnChild;
import com.github.andreyrage.leftdb.annotation.TableName;

/**
 * Created by rage on 11/18/15.
 */
@TableName("ParentOne")
public class ParentOneCustomName {
    @ColumnAutoInc private long id;
    private String name;
    @ColumnChild(foreignKey = "parent", parentKey = "id") private ChildOneCustomName child;

    public ParentOneCustomName() {
    }

    public ParentOneCustomName(String name, ChildOneCustomName child) {
        this.name = name;
        this.child = child;
    }

    public ParentOneCustomName(long id, String name, ChildOneCustomName child) {
        this.id = id;
        this.name = name;
        this.child = child;
    }

    @Override
    public String toString() {
        return "ParentOne{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", child=" + child +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParentOneCustomName parentOne = (ParentOneCustomName) o;

        if (id != parentOne.id) return false;
        if (name != null ? !name.equals(parentOne.name) : parentOne.name != null) return false;
        return !(child != null ? !child.equals(parentOne.child) : parentOne.child != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (child != null ? child.hashCode() : 0);
        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChildOneCustomName getChild() {
        return child;
    }

    public void setChild(ChildOneCustomName child) {
        this.child = child;
    }
}
