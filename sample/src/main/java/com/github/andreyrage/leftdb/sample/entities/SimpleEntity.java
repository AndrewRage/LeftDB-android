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

package com.github.andreyrage.leftdb.sample.entities;

import com.github.andreyrage.leftdb.annotation.ColumnAutoInc;
import com.github.andreyrage.leftdb.annotation.ColumnDAO;
import com.github.andreyrage.leftdb.annotation.ColumnName;
import com.github.andreyrage.leftdb.annotation.TableName;

import java.util.Date;

/**
 * eKreative
 * Created by rage on 11/27/15.
 */

@TableName("entities")
public class SimpleEntity {

    @ColumnAutoInc private Long id;
    @ColumnName("name") private String entityName;
    @ColumnDAO private Properties properties;

    public SimpleEntity() {
    }

    public SimpleEntity(String entityName) {
        this.entityName = entityName;
        this.properties = new Properties(new Date());
    }

    @Override public String toString() {
        return "SimpleEntity{" +
                "id=" + id +
                ", entityName='" + entityName + '\'' +
                ", properties=" + properties +
                '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleEntity that = (SimpleEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (entityName != null ? !entityName.equals(that.entityName) : that.entityName != null)
            return false;
        return !(properties != null ? !properties.equals(that.properties) : that.properties != null);

    }

    @Override public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (entityName != null ? entityName.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
