package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.ColumnChild;

/**
 * Created by rage on 11/18/15.
 */
public class ParentOne {
    private long id;
    @ColumnChild(foreignKey = "parentId", parentKey = "id") private ChildOne child;

    public ParentOne() {
    }

    public ParentOne(long id, ChildOne child) {
        this.id = id;
        this.child = child;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParentOne parentOne = (ParentOne) o;

        if (id != parentOne.id) return false;
        return !(child != null ? !child.equals(parentOne.child) : parentOne.child != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (child != null ? child.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ParentOne{" +
                "id=" + id +
                ", child=" + child +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ChildOne getChild() {
        return child;
    }

    public void setChild(ChildOne child) {
        this.child = child;
    }
}
