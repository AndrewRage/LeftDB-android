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

import com.github.andreyrage.leftdb.annotation.ColumnChild;
import com.github.andreyrage.leftdb.annotation.ColumnPrimaryKey;

/**
 * Created by rage on 11/30/15.
 */
public class FloatKey {
    @ColumnPrimaryKey Float key;
    private String name;
    @ColumnChild(foreignKey = "primaryKey", parentKey = "key") private FloatKeyChild floatKeyChild;

    public FloatKey() {
    }

    public FloatKey(Float key, String name, FloatKeyChild floatKeyChild) {
        this.key = key;
        this.name = name;
        this.floatKeyChild = floatKeyChild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FloatKey floatKey = (FloatKey) o;

        if (key != null ? !key.equals(floatKey.key) : floatKey.key != null) return false;
        if (name != null ? !name.equals(floatKey.name) : floatKey.name != null) return false;
        return !(floatKeyChild != null ? !floatKeyChild.equals(floatKey.floatKeyChild) : floatKey.floatKeyChild != null);

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (floatKeyChild != null ? floatKeyChild.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FloatKey{" +
                "key=" + key +
                ", name='" + name + '\'' +
                ", floatKeyChild=" + floatKeyChild +
                '}';
    }

    public Float getKey() {
        return key;
    }

    public void setKey(Float key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FloatKeyChild getFloatKeyChild() {
        return floatKeyChild;
    }

    public void setFloatKeyChild(FloatKeyChild floatKeyChild) {
        this.floatKeyChild = floatKeyChild;
    }
}
