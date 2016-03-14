package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.annotation.ColumnAutoInc;
import com.github.andreyrage.leftdb.annotation.ColumnChild;
import com.github.andreyrage.leftdb.annotation.ColumnName;
import com.github.andreyrage.leftdb.annotation.TableName;

import java.util.ArrayList;

/**
 * Created by rage on 11/18/15.
 */
@TableName("ParentMany")
public class ParentManyArrayCustomName {
    @ColumnAutoInc @ColumnName("id") private Long mId;
    private String name;
    @ColumnChild(foreignKey = "parent", parentKey = "mId") private ArrayList<ChildManyCustomName> childs;

    public ParentManyArrayCustomName() {
    }

    public ParentManyArrayCustomName(String name, ArrayList<ChildManyCustomName> childs) {
        this.name = name;
        this.childs = childs;
    }

    public ParentManyArrayCustomName(Long id, String name, ArrayList<ChildManyCustomName> childs) {
        this.mId = id;
        this.name = name;
        this.childs = childs;
    }

    @Override
    public String toString() {
        return "ParentMany{" +
                "id=" + mId +
                ", name='" + name + '\'' +
                ", childs=" + childs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParentManyArrayCustomName that = (ParentManyArrayCustomName) o;

        if (mId != that.mId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return !(childs != null ? !childs.equals(that.childs) : that.childs != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (mId ^ (mId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (childs != null ? childs.hashCode() : 0);
        return result;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        this.mId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ChildManyCustomName> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<ChildManyCustomName> childs) {
        this.childs = childs;
    }
}
