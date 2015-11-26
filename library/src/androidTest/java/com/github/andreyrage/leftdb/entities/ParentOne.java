package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.annotation.ColumnAutoInc;
import com.github.andreyrage.leftdb.annotation.ColumnChild;

/**
 * Created by rage on 11/18/15.
 */
public class ParentOne {
    @ColumnAutoInc private long id;
    private String name;
    @ColumnChild(foreignKey = "parentId", parentKey = "id") private ChildOne child;

    public ParentOne() {
    }

    public ParentOne(String name, ChildOne child) {
        this.name = name;
        this.child = child;
    }

    public ParentOne(long id, String name, ChildOne child) {
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

        ParentOne parentOne = (ParentOne) o;

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

    public ChildOne getChild() {
        return child;
    }

    public void setChild(ChildOne child) {
        this.child = child;
    }
}
