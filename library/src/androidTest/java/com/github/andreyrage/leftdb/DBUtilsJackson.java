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

package com.github.andreyrage.leftdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.andreyrage.leftdb.entities.DaoTestEntry;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.lang.reflect.Type;

public class DBUtilsJackson extends LeftDBUtils {
	private ObjectMapper mapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	public static DBUtilsJackson newInstance(Context context, String name, int version) {
		DBUtilsJackson dbUtils = new DBUtilsJackson();
		dbUtils.setDBContext(context, name, version);
		return dbUtils;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
		createTable(db, DaoTestEntry.class);
	}

	@Override
	protected String serializeObject(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected <T> T deserializeObject(String string, Class<T> tClass, final Type genericType) {
		try {
			return mapper.readValue(string, new TypeReference<Object>() {
                @Override public Type getType() {
                    return genericType;
                }
            });
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
