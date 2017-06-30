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
