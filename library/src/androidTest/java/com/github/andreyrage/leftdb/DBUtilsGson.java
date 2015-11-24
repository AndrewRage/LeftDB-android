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
