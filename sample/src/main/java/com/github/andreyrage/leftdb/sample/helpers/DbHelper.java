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

package com.github.andreyrage.leftdb.sample.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.andreyrage.leftdb.LeftDBUtils;
import com.github.andreyrage.leftdb.sample.entities.SimpleEntity;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by rage on 11/26/15.
 */
public class DbHelper extends LeftDBUtils {
    private static volatile DbHelper sInstance;
    private final Gson mGson;

    private DbHelper() {
        mGson = new Gson();
    }

    public static DbHelper getInstance(Context context) {
        DbHelper localInstance = sInstance;
        if (localInstance == null) {
            synchronized (DbHelper.class) {
                localInstance = sInstance;
                if (localInstance == null) {
                    sInstance = localInstance = new DbHelper();
                    localInstance.setDBContext(context, "sample.sqlite", 1);
                }
            }
        }
        return localInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        createTable(db, SimpleEntity.class);
    }

    @Override
    protected String serializeObject(Object object) {
        return mGson.toJson(object);
    }

    @Override
    protected <T> T deserializeObject(String string, Class<T> tClass, final Type genericType) {
        return mGson.fromJson(string, genericType);
    }
}
