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
