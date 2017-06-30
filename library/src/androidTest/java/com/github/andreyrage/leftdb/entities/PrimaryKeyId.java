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

import com.github.andreyrage.leftdb.annotation.ColumnPrimaryKey;

/**
 * Created by rage on 28.11.15.
 */
public class PrimaryKeyId {
    @ColumnPrimaryKey private Long primKey;
    private String name;

    public PrimaryKeyId() {
    }

    public PrimaryKeyId(Long primKey, String name) {
        this.primKey = primKey;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrimaryKeyId that = (PrimaryKeyId) o;

        if (primKey != null ? !primKey.equals(that.primKey) : that.primKey != null) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = primKey != null ? primKey.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PrimaryKeyId{" +
                "primKey=" + primKey +
                ", name='" + name + '\'' +
                '}';
    }

    public Long getPrimKey() {
        return primKey;
    }

    public void setPrimKey(Long primKey) {
        this.primKey = primKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
