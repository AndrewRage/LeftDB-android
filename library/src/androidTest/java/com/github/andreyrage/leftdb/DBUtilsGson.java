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
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class DBUtilsGson extends LeftDBUtils {
	private Gson mapper = new Gson();

	public static DBUtilsGson newInstance(Context context, String name, int version) {
		DBUtilsGson dbUtils = new DBUtilsGson();
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
		return mapper.toJson(object);
	}

	@Override
	protected <T> T deserializeObject(String string, Class<T> tClass, final Type genericType) {
		return mapper.fromJson(string, genericType);
	}
}
