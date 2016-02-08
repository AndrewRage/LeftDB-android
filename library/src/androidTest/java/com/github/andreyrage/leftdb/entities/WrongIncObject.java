package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.annotation.ColumnAutoInc;
import com.github.andreyrage.leftdb.annotation.ColumnIgnore;
import com.github.andreyrage.leftdb.annotation.ColumnName;
import com.github.andreyrage.leftdb.annotation.TableName;

import java.io.Serializable;

/**
 * Created by rage on 11/18/15.
 */

@TableName("Object")
public class WrongIncObject implements Serializable {

    @ColumnAutoInc private int id;
    @ColumnName("otherName") private String name;
    @ColumnIgnore private WrongIncObject mObject;

    public WrongIncObject() {
    }

    public WrongIncObject(int id, String name, WrongIncObject object) {
        this.id = id;
        this.name = name;
        mObject = object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WrongIncObject that = (WrongIncObject) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return !(mObject != null ? !mObject.equals(that.mObject) : that.mObject != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (mObject != null ? mObject.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SerializableObject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mObject=" + mObject +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WrongIncObject getObject() {
        return mObject;
    }

    public void setObject(WrongIncObject object) {
        mObject = object;
    }
}
