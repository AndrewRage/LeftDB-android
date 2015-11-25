package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.annotation.ColumnAutoInc;
import com.github.andreyrage.leftdb.annotation.ColumnChild;

import java.util.List;

/**
 * Created by rage on 11/18/15.
 */
public class ParentMany {
    @ColumnAutoInc private long id;
    private String name;
    @ColumnChild(foreignKey = "parentId", parentKey = "id") private List<ChildMany> childs;

    public ParentMany() {
    }

    public ParentMany(String name, List<ChildMany> childs) {
        this.name = name;
        this.childs = childs;
    }

    public ParentMany(long id, String name, List<ChildMany> childs) {
        this.id = id;
        this.name = name;
        this.childs = childs;
    }

    @Override
    public String toString() {
        return "ParentMany{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", childs=" + childs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParentMany that = (ParentMany) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return !(childs != null ? !childs.equals(that.childs) : that.childs != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (childs != null ? childs.hashCode() : 0);
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

    public List<ChildMany> getChilds() {
        return childs;
    }

    public void setChilds(List<ChildMany> childs) {
        this.childs = childs;
    }
}
