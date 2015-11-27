package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.annotation.ColumnName;

/**
 * Created by rage on 27.11.15.
 */
public class AnnotationId {
    @ColumnName("_id") private Long mId;
    private String name;

    public AnnotationId() {
    }

    public AnnotationId(Long id, String name) {
        this.mId = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotationId that = (AnnotationId) o;

        if (mId != null ? !mId.equals(that.mId) : that.mId != null) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NotAnnotationId{" +
                "mId=" + mId +
                ", name='" + name + '\'' +
                '}';
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
}
