package com.github.andreyrage.leftdb.sample.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.andreyrage.leftdb.LeftDBUtils;
import com.github.andreyrage.leftdb.sample.entities.SimpleEntity;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * eKreative
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
