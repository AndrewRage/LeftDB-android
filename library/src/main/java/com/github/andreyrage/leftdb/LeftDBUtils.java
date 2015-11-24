package com.github.andreyrage.leftdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.github.andreyrage.leftdb.annotation.ColumnAutoInc;
import com.github.andreyrage.leftdb.annotation.ColumnChild;
import com.github.andreyrage.leftdb.annotation.ColumnDAO;
import com.github.andreyrage.leftdb.annotation.ColumnIgnore;
import com.github.andreyrage.leftdb.annotation.ColumnName;
import com.github.andreyrage.leftdb.annotation.TableName;
import com.github.andreyrage.leftdb.queries.DeleteQuery;
import com.github.andreyrage.leftdb.queries.SelectQuery;
import com.github.andreyrage.leftdb.queries.UpdateQuery;
import com.github.andreyrage.leftdb.utils.Serializer;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.github.andreyrage.leftdb.utils.CheckNullUtils.nullableArrayOfStrings;
import static com.github.andreyrage.leftdb.utils.CheckNullUtils.nullableString;

public abstract class LeftDBUtils implements LeftDBHandler.OnDbChangeCallback {

    private static final String TAG = LeftDBUtils.class.getName();
    protected LeftDBHandler dbHandler;
    protected SQLiteDatabase db;

    protected void setDBContext(Context context, String name, int version) {
        dbHandler = new LeftDBHandler(context, name, version, this);
        if (db == null || !db.isOpen()) {
            db = dbHandler.getWritableDatabase();
        }
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    protected abstract String serializeObject(Object object);

    protected abstract <T> T deserializeObject(String string, Class<T> tClass, Type genericType);

    public <T> void deleteWhere(Class<T> type, String where) {
        db.delete(getTableName(type), where, null);
    }

    public <T> void deleteAll(Class<T> type) {
        db.delete(getTableName(type), null, null);
    }

    public <T> void delete(Class<T> type, String columnId, List<Long> ids) {
        deleteWhere(type, String.format("%s IN (%s)", columnId, TextUtils.join(",", ids)));
    }

    public <T> int delete(DeleteQuery query) {
        return byQuery(query);
    }

    public int countResultsByQuery(String query) {
        return countResultsByCursor(db.rawQuery(query, null));
    }

    private int countResultsByCursor(Cursor cursor) {
        int count = 0;
        if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int count(SelectQuery query) {
        return countResultsByCursor(byQuery(query));
    }

    public <T> int count(Class<T> type, String where) {
        return countResultsByQuery(String.format("SELECT * FROM %s", getTableName((Class) type))
                + (TextUtils.isEmpty(where) ? "" : " WHERE " + where));
    }

    public <T> int count(Class<T> type) {
        return count(type, null);
    }

    public <T> List<T> executeQuery(String query, Class<T> type) {
        return queryListMapper(query, type);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> select(SelectQuery query) {
        try {
            Class clazz = Class.forName(query.entity().getCanonicalName());
            return queryListMapper(query, clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public <T> List<T> getAll(Class<T> type) {
        String query = String.format("select * from `%s`", getTableName(type));
        return queryListMapper(query, type);
    }

    public <T> List<T> getAllLimited(Class<T> type, long limit) {
        String query = String.format("select * from `%s` limit %d", getTableName(type), limit);
        return queryListMapper(query, type);
    }

    public <T> List<T> getAllWhere(String where, Class<T> type) {
        String query = String.format("select * from `%s` where %s", getTableName(type), where);
        return queryListMapper(query, type);
    }

    public <T> void add(List<T> elements, boolean useTransaction) {
        if (useTransaction) {
            try {
                db.beginTransaction();
                for (T value : elements) {
                    add(value);
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(TAG, "add list, use transaction", e);
            } finally {
                db.endTransaction();
            }
        } else {
            for (T value : elements) {
                add(value);
            }
        }
    }

    public <T> void add(List<T> elements) {
        add(elements, true);
    }

    public <T> long add(final T element) {
        final ContentValues values = new ContentValues();
        boolean isColumnChild = false;
        for (Field value : element.getClass().getDeclaredFields()) {
            if (!value.isAnnotationPresent(ColumnIgnore.class)
                    && !Modifier.isStatic(value.getModifiers())) {
                if (value.isAnnotationPresent(ColumnAutoInc.class)) {
                    valueAutoIncMapper(values, value, element);
                } else if (value.isAnnotationPresent(ColumnDAO.class)) {
                    valueDAOMapper(values, value, element);
                } else if (value.isAnnotationPresent(ColumnChild.class)) {
                    isColumnChild = true;
                } else {
                    valueMapper(values, value, element);
                }
            }
        }
        long row = db.insertWithOnConflict(getTableName(element.getClass()),
                null, values, SQLiteDatabase.CONFLICT_REPLACE);
        values.clear();
        if (isColumnChild) {
            addColumnChild(element);
        }
        return row;
    }

    private <T> void addColumnChild(final T element) {
        for (Field value : element.getClass().getDeclaredFields()) {
            if (value.isAnnotationPresent(ColumnChild.class)) {
                value.setAccessible(true);
                try {
                    Field parentKeyField = element.getClass().getDeclaredField(getParentKey(value));
                    parentKeyField.setAccessible(true);
                    Long parentKeyValue = (Long) parentKeyField.get(element);
                    String foreignKey = getForeignKey(value);
                    if (value.getType().isAssignableFrom(List.class)) {
                        List list = (List) value.get(element);
                        for (Object o : list) {
                            Field foreignKeyField = o.getClass().getDeclaredField(foreignKey);
                            foreignKeyField.setAccessible(true);
                            foreignKeyField.set(o, parentKeyValue);
                        }
                        add(list, false);
                    } else {
                        Object childObject = value.get(element);
                        Field foreignKeyField = childObject.getClass().getDeclaredField(foreignKey);
                        foreignKeyField.setAccessible(true);
                        foreignKeyField.set(childObject, parentKeyValue);
                        add(childObject);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "valueMapper", e);
                }
            }
        }
    }

    public int update(UpdateQuery query, ContentValues values) {
        return db.update(
                query.table(),
                values,
                nullableString(query.where()),
                nullableArrayOfStrings(query.whereArgs()));
    }

    //INNER METHODS

    private Cursor byQuery(SelectQuery query) {
        return db.query(
                query.distinct(),
                query.table(),
                nullableArrayOfStrings(query.columns()),
                nullableString(query.where()),
                nullableArrayOfStrings(query.whereArgs()),
                nullableString(query.groupBy()),
                nullableString(query.having()),
                nullableString(query.orderBy()),
                nullableString(query.limit()));
    }

    private int byQuery(DeleteQuery query) {
        return db.delete(
                query.table(),
                nullableString(query.where()),
                nullableArrayOfStrings(query.whereArgs()));
    }

    private <T> void valueAutoIncMapper(ContentValues values, Field field, T element) {
        field.setAccessible(true);
        try {
            long id = (Long) field.get(element);
            if (id > 0) {
                values.put(getColumnName(field), id);
            }
        } catch (IllegalAccessException e) {
            Log.e(TAG, "valueAutoIncMapper", e);
        }
    }

    private <T> void valueDAOMapper(ContentValues values, Field field, T element) {
        field.setAccessible(true);
        try {
            values.put(getColumnName(field), field.get(element) != null ? serializeObject(field.get(element)) : null);
        } catch (Exception e) {
            Log.e(TAG, "valueDaoMapper", e);
        }
    }


    private <T> void valueMapper(ContentValues values, Field field, T element) {
        field.setAccessible(true);
		Class<?> fieldType = field.getType();
        try {
            if (fieldType.isAssignableFrom(String.class)) {
                values.put(getColumnName(field), (String) field.get(element));
            } else if (fieldType.isAssignableFrom(long.class) || fieldType.isAssignableFrom(Long.class)) {
                values.put(getColumnName(field), (Long) field.get(element));
            } else if (fieldType.isAssignableFrom(int.class) || fieldType.isAssignableFrom(Integer.class)) {
				values.put(getColumnName(field), (Integer) field.get(element));
			} else if (fieldType.isAssignableFrom(short.class) || fieldType.isAssignableFrom(Short.class)) {
				values.put(getColumnName(field), (Short) field.get(element));
			} else if (fieldType.isAssignableFrom(boolean.class) || fieldType.isAssignableFrom(Boolean.class)) {
                values.put(getColumnName(field), field.get(element) == null ? null : ((Boolean) field.get(element)) ? 1 : 0);
            } else if (fieldType.isAssignableFrom(float.class) || fieldType.isAssignableFrom(Float.class)) {
                values.put(getColumnName(field), (Float) field.get(element));
            } else if (fieldType.isAssignableFrom(double.class) || fieldType.isAssignableFrom(Double.class)) {
                values.put(getColumnName(field), (Double) field.get(element));
            } else if (fieldType.isAssignableFrom(BigDecimal.class)) {
				try {
					values.put(getColumnName(field), field.get(element).toString());
				} catch (NullPointerException e) {
					values.putNull(getColumnName(field));
				}
			} else if (fieldType.isAssignableFrom(Date.class)) {
                values.put(getColumnName(field), field.get(element) == null ? null : ((Date) field.get(element)).getTime());
            } else if (fieldType.isAssignableFrom(Calendar.class)) {
				try {
					values.put(getColumnName(field), ((Calendar) field.get(element)).getTimeInMillis());
				} catch (NullPointerException e) {
					values.put(getColumnName(field), (Long) null);
				}
			} else if (Serializable.class.isAssignableFrom(fieldType.getClass())) {
				byte[] bytes = null;
                if (field.get(element) != null) {
                    try {
                        bytes = Serializer.serialize(field.get(element));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
				if (bytes == null) {
					values.put(getColumnName(field), "".getBytes());
				} else {
					values.put(getColumnName(field), bytes);
				}
			} else {
                Log.w(TAG, String.format("In class '%s' type '%s' of field '%s' not supported.", element.getClass().getSimpleName(), field.getType().toString(), field.getName()));
            }
        } catch (IllegalAccessException e) {
            Log.e(TAG, "valueMapper", e);
        }
    }

    private <T> List<T> queryListMapper(Cursor cursor, Class<T> type) {
        List<T> results = new ArrayList<>();
        if (cursor == null || cursor.isClosed()) {
            return results;
        }
        if (cursor.moveToFirst()) {
            do {
                results.add(cursorMapper(cursor, type));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return results;
    }

    private <T> List<T> queryListMapper(String query, Class<T> type) {
        return queryListMapper(db.rawQuery(query, null), type);
    }

    private <T> List<T> queryListMapper(SelectQuery query, Class<T> type) {
        return queryListMapper(byQuery(query), type);
    }

    private <T> T cursorMapper(Cursor cursor, Class<T> type) {
        T result = null;
        try {
            result = type.newInstance();
            for (Field field : result.getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(ColumnIgnore.class)) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        fieldMapper(result, cursor, field, getColumnName(field));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "cursorMapper", e);
        }
        return result;
    }

    private <T> void fieldMapper(T result, Cursor cursor, final Field field, String columnName) {
        field.setAccessible(true);
		Class<?> fieldType = field.getType();
        try {
            if (field.isAnnotationPresent(ColumnChild.class)) {
                String foreignKey = getForeignKey(field);
                long parentKeyValue = cursor.getLong(cursor.getColumnIndex(getParentKey(field)));
                if (fieldType.isAssignableFrom(List.class)) {
                    field.set(result, getAllWhere(String.format("%s = %d", foreignKey, parentKeyValue), (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]));
                } else {
                    List resultList = getAllWhere(String.format("%s = %d", foreignKey, parentKeyValue), fieldType);
                    field.set(result, resultList.isEmpty() ? null : resultList.get(0));
                }
                return;
            }
            if (cursor.isNull(cursor.getColumnIndex(columnName))) {
                return;
            }
            if (fieldType.isAssignableFrom(String.class)) {
				field.set(result, cursor.getString(cursor.getColumnIndex(columnName)));
            } else if (fieldType.isAssignableFrom(long.class) || fieldType.isAssignableFrom(Long.class)) {
                field.set(result, cursor.getLong(cursor.getColumnIndex(columnName)));
            } else if (fieldType.isAssignableFrom(int.class) || fieldType.isAssignableFrom(Integer.class)) {
				field.set(result, cursor.getInt(cursor.getColumnIndex(columnName)));
            } else if (fieldType.isAssignableFrom(short.class) || fieldType.isAssignableFrom(Short.class)) {
				field.set(result, cursor.getShort(cursor.getColumnIndex(columnName)));
			} else if (fieldType.isAssignableFrom(boolean.class) || fieldType.isAssignableFrom(Boolean.class)) {
                field.set(result, cursor.getLong(cursor.getColumnIndex(columnName)) == 1);
			} else if (fieldType.isAssignableFrom(float.class) || fieldType.isAssignableFrom(Float.class)) {
                field.set(result, cursor.getFloat(cursor.getColumnIndex(columnName)));
			} else if (fieldType.isAssignableFrom(double.class) || fieldType.isAssignableFrom(Double.class)) {
                field.set(result, cursor.getDouble(cursor.getColumnIndex(columnName)));
            } else if (fieldType.isAssignableFrom(BigDecimal.class)) {
				String val = cursor.getString(cursor.getColumnIndex(columnName));
				field.set(result, val == null || val.equals("null") ? null : new BigDecimal(val));
			} else if (fieldType.isAssignableFrom(Date.class)) {
                field.set(result, cursor.getLong(cursor.getColumnIndex(columnName)) == 0 ? null : new Date(cursor.getLong(cursor.getColumnIndex(columnName))));
            } else if (fieldType.isAssignableFrom(Calendar.class)) {
				long l = cursor.getLong(cursor.getColumnIndex(columnName));
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(l);
				field.set(result, c);
			} else if (field.isAnnotationPresent(ColumnDAO.class)) {
                String value = cursor.getString(cursor.getColumnIndex(columnName));
                field.set(result, value != null ? deserializeObject(value, fieldType, field.getGenericType()) : null);
            } else if (Serializable.class.isAssignableFrom(fieldType.getClass())) {
				byte[] bytes = cursor.getBlob(cursor.getColumnIndex(columnName));
				if (bytes == null) {
					field.set(result, null);
				} else {
					field.set(result, Serializer.deserialize(bytes));
				}
			} else {
                Log.w(TAG, String.format("In class '%s' type '%s' of field '%s' not supported.", result.getClass().getSimpleName(), fieldType.toString(), field.getName()));
            }
        } catch (IllegalAccessException e) {
            Log.e(TAG, "fieldMapper", e);
        } catch (Exception e) {
            Log.e(TAG, "fieldMapper", e);
        }
    }

    private <T> String getTableName(Class<T> type) {
        String tableName = type.getSimpleName();
        if (type.isAnnotationPresent(TableName.class)) {
            tableName = type.getAnnotation(TableName.class).value();
        }
        return tableName;
    }

    private String getColumnName(Field field) {
        String columnName = field.getName();
        if (field.isAnnotationPresent(ColumnName.class)) {
            columnName = field.getAnnotation(ColumnName.class).value();
        }
        return columnName;
    }

    private String getForeignKey(Field field) {
        String columnName = field.getName();
        if (field.isAnnotationPresent(ColumnChild.class)) {
            columnName = field.getAnnotation(ColumnChild.class).foreignKey();
        }
        return columnName;
    }

    private String getParentKey(Field field) {
        String columnName = field.getName();
        if (field.isAnnotationPresent(ColumnChild.class)) {
            columnName = field.getAnnotation(ColumnChild.class).parentKey();
        }
        return columnName;
    }

    public LeftDBHandler getDbHandler() {
        return dbHandler;
    }

    //SQL UTILS
    public <T> void createTables(SQLiteDatabase db, List<T> elements) {
        for (T element : elements) {
            createTable(db, element);
        }
    }

    public <T> void createTable(SQLiteDatabase db, T element){
        db.execSQL(createTableSQL(element));
    }

    public <T> String createTableSQL(T element) throws IllegalArgumentException {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ");
        sqlBuilder.append(getTableName((Class) element));
        sqlBuilder.append(" (");
        int columnCount = 0;
        for (Field field : ((Class) element).getDeclaredFields()) {
            if (!field.isAnnotationPresent(ColumnIgnore.class)
                    && !field.isAnnotationPresent(ColumnChild.class)) {
                StringBuilder builder = new StringBuilder();
                if (columnCount > 0) {
                    builder.append(", ");
                }

                Class<?> fieldType = field.getType();
                if (fieldType.isAssignableFrom(String.class)) {
                    builder.append(getColumnName(field));
                    builder.append(" TEXT");
                } else if (fieldType.isAssignableFrom(long.class) || fieldType.isAssignableFrom(Long.class)) {
                    builder.append(getColumnName(field));
                    builder.append(" INTEGER");
                    if (field.isAnnotationPresent(ColumnAutoInc.class)) {
                        builder.append(" PRIMARY KEY AUTOINCREMENT NOT NULL");
                    }
                } else if (fieldType.isAssignableFrom(int.class) || fieldType.isAssignableFrom(Integer.class)) {
                    builder.append(getColumnName(field));
                    builder.append(" INTEGER");
                    if (field.isAnnotationPresent(ColumnAutoInc.class)) {
                        builder.append(" PRIMARY KEY AUTOINCREMENT NOT NULL");
                    }
                } else if (fieldType.isAssignableFrom(short.class) || fieldType.isAssignableFrom(Short.class)) {
                    builder.append(getColumnName(field));
                    builder.append(" INTEGER");
                    if (field.isAnnotationPresent(ColumnAutoInc.class)) {
                        builder.append(" PRIMARY KEY AUTOINCREMENT NOT NULL");
                    }
                } else if (fieldType.isAssignableFrom(boolean.class) || fieldType.isAssignableFrom(Boolean.class)) {
                    builder.append(getColumnName(field));
                    builder.append(" INTEGER");
                } else if (fieldType.isAssignableFrom(float.class) || fieldType.isAssignableFrom(Float.class)) {
                    builder.append(getColumnName(field));
                    builder.append(" REAL");
                } else if (fieldType.isAssignableFrom(double.class) || fieldType.isAssignableFrom(Double.class)) {
                    builder.append(getColumnName(field));
                    builder.append(" REAL");
                } else if (fieldType.isAssignableFrom(BigDecimal.class)) {
                    builder.append(getColumnName(field));
                    builder.append(" TEXT");
                } else if (fieldType.isAssignableFrom(Date.class)) {
                    builder.append(getColumnName(field));
                    builder.append(" INTEGER");
                } else if (fieldType.isAssignableFrom(Calendar.class)) {
                    builder.append(getColumnName(field));
                    builder.append(" INTEGER");
                } else if (field.isAnnotationPresent(ColumnDAO.class)) {
                    builder.append(getColumnName(field));
                    builder.append(" TEXT");
                } else if (Serializable.class.isAssignableFrom(fieldType.getClass())) {
                    builder.append(getColumnName(field));
                    builder.append(" BLOB");
                }

                if (builder.length() > 2) {
                    sqlBuilder.append(builder);
                    columnCount++;
                }
            }
        }
        if (columnCount == 0) {
            throw new IllegalArgumentException("Cannot create a table without at least one column.");
        }
        sqlBuilder.append(" );");
        return sqlBuilder.toString();
    }

    public <T> void deleteTables(SQLiteDatabase db, List<T> elements) {
        for (T element : elements) {
            deleteTable(db, element);
        }
    }

    public <T> void deleteTable(SQLiteDatabase db, T element) {
        db.execSQL(deleteTableSQL(element));
    }

    public <T> String deleteTableSQL(T element) {
        return String.format("DROP TABLE IF EXISTS %s;", getTableName((Class) element));
    }

    public <T> boolean isTableExists(T element) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", getTableName((Class) element)});
        if (!cursor.moveToFirst()) {
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
}
