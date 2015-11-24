package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.annotation.ColumnChild;

import java.util.List;

/**
 * Created by rage on 11/18/15.
 */
public class ParentMany {
    private long id;
    @ColumnChild(foreignKey = "parentId", parentKey = "id") private List<ChildMany> childs;

    public ParentMany() {
    }

    public ParentMany(long id, List<ChildMany> childs) {
        this.id = id;
        this.childs = childs;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParentMany that = (ParentMany) o;

        if (id != that.id) return false;
        return !(childs != null ? !childs.equals(that.childs) : that.childs != null);

    }

    @Override public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (childs != null ? childs.hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "ParentMany{" +
                "id=" + id +
                ", childs=" + childs +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<ChildMany> getChilds() {
        return childs;
    }

    public void setChilds(List<ChildMany> childs) {
        this.childs = childs;
    }
}
