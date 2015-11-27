package com.github.andreyrage.leftdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.andreyrage.leftdb.entities.AllFields;
import com.github.andreyrage.leftdb.entities.AnnotationId;
import com.github.andreyrage.leftdb.entities.ChildMany;
import com.github.andreyrage.leftdb.entities.ChildOne;
import com.github.andreyrage.leftdb.entities.NotAnnotationId;
import com.github.andreyrage.leftdb.entities.ParentMany;
import com.github.andreyrage.leftdb.entities.ParentOne;
import com.github.andreyrage.leftdb.entities.SerializableObject;

import com.github.andreyrage.leftdb.utils.SerializeUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;

public class DBUtils extends LeftDBUtils {

	public static DBUtils newInstance(Context context, String name, int version) {
		DBUtils dbUtils = new DBUtils();
		dbUtils.setDBContext(context, name, version);
		return dbUtils;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
		createTable(db, AllFields.class);
		createTable(db, SerializableObject.class);
		createTables(db, Arrays.asList(
				ChildMany.class,
				ChildOne.class,
				ParentMany.class,
				ParentOne.class,
				AnnotationId.class,
				NotAnnotationId.class
		));
	}

	@Override
	protected String serializeObject(Object object) {
		try {
			return Arrays.toString(SerializeUtils.serialize(object));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected <T> T deserializeObject(String string, Class<T> tClass, Type genericType) {
		String[] byteValues = string.substring(1, string.length() - 1).split(",");
		byte[] bytes = new byte[byteValues.length];
		for (int i=0, len=bytes.length; i<len; i++) {
			bytes[i] = Byte.parseByte(byteValues[i].trim());
		}

		try {
			Object o = SerializeUtils.deserialize(bytes);
			if (o != null) {
				return tClass.cast(o);
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
