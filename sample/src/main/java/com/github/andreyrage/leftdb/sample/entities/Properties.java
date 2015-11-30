package com.github.andreyrage.leftdb.sample.entities;

import java.util.Date;

/**
 * eKreative
 * Created by rage on 11/27/15.
 */
public class Properties {

    private Date createdAt;

    public Properties() {
    }

    public Properties(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override public String toString() {
        return "Properties{" +
                "createdAt=" + createdAt +
                '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Properties that = (Properties) o;

        return !(createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null);

    }

    @Override public int hashCode() {
        return createdAt != null ? createdAt.hashCode() : 0;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
