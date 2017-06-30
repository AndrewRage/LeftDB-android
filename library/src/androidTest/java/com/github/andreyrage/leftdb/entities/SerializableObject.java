/*
 * Copyright 2017 Andrii Horishnii
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
public class SerializableObject implements Serializable {

    @ColumnAutoInc private long id;
    @ColumnName("otherName") private String name;
    @ColumnIgnore private SerializableObject mObject;

    public SerializableObject() {
    }

    public SerializableObject(long id, String name, SerializableObject object) {
        this.id = id;
        this.name = name;
        mObject = object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SerializableObject that = (SerializableObject) o;

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

    public SerializableObject getObject() {
        return mObject;
    }

    public void setObject(SerializableObject object) {
        mObject = object;
    }
}
