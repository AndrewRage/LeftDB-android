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
import com.github.andreyrage.leftdb.annotation.ColumnChild;

import java.util.List;

/**
 * Created by rage on 11/18/15.
 */
public class ParentMany {
    @ColumnAutoInc private Long id;
    private String name;
    @ColumnChild(foreignKey = "parentId", parentKey = "id") private List<ChildMany> childs;

    public ParentMany() {
    }

    public ParentMany(String name, List<ChildMany> childs) {
        this.name = name;
        this.childs = childs;
    }

    public ParentMany(Long id, String name, List<ChildMany> childs) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
