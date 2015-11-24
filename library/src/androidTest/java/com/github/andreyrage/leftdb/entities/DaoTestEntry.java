package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.annotation.ColumnDAO;

import java.util.List;

/**
 * Created by rage on 24.11.15.
 */
public class DaoTestEntry {
    private long id;
    @ColumnDAO SerializableObject serializableObject;
    @ColumnDAO private List<SerializableObject> serializableObjectList;

    public DaoTestEntry() {
    }

    public DaoTestEntry(long id, SerializableObject serializableObject, List<SerializableObject> serializableObjectList) {
        this.id = id;
        this.serializableObject = serializableObject;
        this.serializableObjectList = serializableObjectList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DaoTestEntry that = (DaoTestEntry) o;

        if (id != that.id) return false;
        if (serializableObject != null ? !serializableObject.equals(that.serializableObject) : that.serializableObject != null)
            return false;
        return !(serializableObjectList != null ? !serializableObjectList.equals(that.serializableObjectList) : that.serializableObjectList != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (serializableObject != null ? serializableObject.hashCode() : 0);
        result = 31 * result + (serializableObjectList != null ? serializableObjectList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DaoTestEntry{" +
                "id=" + id +
                ", serializableObject=" + serializableObject +
                ", serializableObjectList=" + serializableObjectList +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SerializableObject getSerializableObject() {
        return serializableObject;
    }

    public void setSerializableObject(SerializableObject serializableObject) {
        this.serializableObject = serializableObject;
    }

    public List<SerializableObject> getSerializableObjectList() {
        return serializableObjectList;
    }

    public void setSerializableObjectList(List<SerializableObject> serializableObjectList) {
        this.serializableObjectList = serializableObjectList;
    }
}
