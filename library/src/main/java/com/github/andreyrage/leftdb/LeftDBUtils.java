package com.github.andreyrage.leftdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.github.andreyrage.leftdb.annotation.ColumnAutoInc;
import com.github.andreyrage.leftdb.annotation.ColumnChild;
import com.github.andreyrage.leftdb.annotation.ColumnDAO;
import com.github.andreyrage.leftdb.annotation.ColumnIgnore;
import com.github.andreyrage.leftdb.annotation.ColumnName;
import com.github.andreyrage.leftdb.annotation.ColumnPrimaryKey;
import com.github.andreyrage.leftdb.annotation.TableName;
import com.github.andreyrage.leftdb.queries.DeleteQuery;
import com.github.andreyrage.leftdb.queries.SelectQuery;
import com.github.andreyrage.leftdb.queries.UpdateQuery;
import com.github.andreyrage.leftdb.utils.SerializeUtils;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.github.andreyrage.leftdb.utils.CheckNullUtils.nullableArrayOfStrings;
import static com.github.andreyrage.leftdb.utils.CheckNullUtils.nullableString;

public abstract class LeftDBUtils implements LeftDBHandler.OnDbChangeCallback {

    private static final String TAG = LeftDBUtils.class.getName();

    protected static final int FIELD_TYPE_BLOB = 4;
    protected static final int FIELD_TYPE_FLOAT = 2;
    protected static final int FIELD_TYPE_INTEGER = 1;
    protected static final int FIELD_TYPE_NULL = 0;
    protected static final int FIELD_TYPE_STRING = 3;

    protected LeftDBHandler dbHandler;
    protected SQLiteDatabase db;

    /**
     * Initialize DBHandler
     *
     * @param context to use to open or create the database
     * @param name of the database file; if the file exists in the directory it will be
     *             copied, or if the file does not exist {@link #onCreate} will be used
     *             to create database
     * @param version number of the database (starting at 1); if the database is older,
     *     {@link #onUpgrade} will be used to upgrade the database; if the database is
     *     newer, {@link #onDowngrade} will be used to downgrade the database
     *
     * Rightutils compatibility
     * */
    protected void setDBContext(@NonNull Context context, @NonNull String name, int version) {
        dbHandler = new LeftDBHandler(context, name, version, this);
        if (db == null || !db.isOpen()) {
            db = dbHandler.getWritableDatabase();
        }
    }

    /**
     * Called for the first time if the file does not exist in assets and need
     * to create a database. This is where the creation of tables and the initial
     * population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Called when the database needs to be downgraded. This is strictly similar to
     * {@link #onUpgrade} method, but is called whenever current version is newer than requested one.
     * However, this method is not abstract, so it is not mandatory for a customer to
     * implement it. If not overridden, default implementation will reject downgrade and
     * throws SQLiteException
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Called when need serialize object to string
     *
     * @param object the object that need serialize.
     *
     * @return string of serialized object
     *
     * */
    protected abstract String serializeObject(Object object);

    /**
     * Called when need deserialize string to object
     *
     * @param string the string that need deserialize.
     * @param tClass the class of object that need deserialize
     * @param genericType the generic type of object that need deserialize
     *
     * @return the object that was deserialize
     *
     * */
    protected abstract <T> T deserializeObject(String string, Class<T> tClass, Type genericType);

    /**
     * Convenience method for deleting rows in the database.
     *
     * @param type the class of table to delete
     * @param where the optional WHERE clause to apply when deleting.
     *            Passing null will delete all rows.
     *
     * @return the number of deletes rows
     *
     * Rightutils compatibility
     * */
    public <T> int deleteWhere(@NonNull Class<T> type, @Nullable String where) {
        return db.delete(getTableName(type), where, null);
    }

    /**
     * Convenience method for deleting all table rows in the database.
     *
     * @param type the class of table to delete
     *
     * @return the number of deletes rows
     *
     * Rightutils compatibility
     * */
    public <T> int deleteAll(@NonNull Class<T> type) {
        return db.delete(getTableName(type), null, null);
    }

    /**
     * Convenience method for deleting rows in the database.
     *
     * @param type the class of table to delete
     * @param columnId the column name
     * @param ids values of column row that need to delete
     *
     * @return the number of deletes rows
     *
     * Rightutils compatibility
     * */
    public <T> int delete(@NonNull Class<T> type, @NonNull String columnId, @NonNull List<Long> ids) {
        return deleteWhere(type, String.format("%s IN (%s)", columnId, TextUtils.join(",", ids)));
    }

    /**
     * Method for deleting row in the database.
     *
     * @param o the object that need to delete
     *
     * @return return true if row is deletes
     * */
    public boolean delete(@NonNull Object o) {
        String idFieldName = getIdFieldName(o.getClass());
        if (idFieldName == null) {
            return false;
        }
        Field idField = null;
        Long id = null;
        try {
            idField = o.getClass().getDeclaredField(idFieldName);
            idField.setAccessible(true);
            id = (Long) idField.get(o);
        } catch (Exception e) {
            Log.e(TAG, "delete", e);
        }
        if (idField == null || id == null) {
            return false;
        }
        int count = deleteWhere(o.getClass(), String.format("%s=%d", getColumnName(idField), id));
        return count > 0;
    }

    /**
     * Method for deleting rows in the database.
     *
     * @param collection the collection of similar objects that need to delete
     *
     * @return @return the number of deletes rows
     * */
    public <T extends Collection<?>> int delete(@NonNull T collection) {
        if (collection.size() == 0) {
            return 0;
        }
        Class<?> clazz = null;
        String idFieldName = null;
        Field idField = null;
        List<Long> idList = new ArrayList<>();
        try {
            for (Object o : collection) {
                if (o != null) {
                    if (idFieldName == null) {
                        idFieldName = getIdFieldName(o.getClass());
                        clazz = o.getClass();
                        if (idFieldName == null) {
                            break;
                        }
                    }
                    Field field = o.getClass().getDeclaredField(idFieldName);
                    field.setAccessible(true);
                    Long id = (Long) field.get(o);
                    idList.add(id);
                    if (idField == null) {
                        idField = field;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "delete", e);
        }
        if (clazz == null || idFieldName == null || idField == null || idList.size() == 0) {
            return 0;
        }
        return delete(clazz, getColumnName(idField), idList);
    }

    /**
     * Method for deleting rows in the database.
     *
     * @param query {@link DeleteQuery}
     *
     * @return @return the number of deletes rows
     * */
    public int delete(@NonNull DeleteQuery query) {
        return byQuery(query);
    }

    /**
     * Method that return count of rows
     *
     * @param query the SQL query
     *
     * @return count
     *
     * Rightutils compatibility
     * */
    public int countResultsByQuery(@NonNull String query) {
        return countResultsByCursor(db.rawQuery(query, null));
    }

    private int countResultsByCursor(@Nullable Cursor cursor) {
        int count = 0;
        if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    /**
     * Method that return count of rows
     *
     * @param query {@link SelectQuery}
     *
     * @return count or rows
     * */
    public int count(@NonNull SelectQuery query) {
        return countResultsByCursor(byQuery(query));
    }

    /**
     * Method that return count of rows
     *
     * @param type the class of table
     * @param where the where query
     *
     * @return count of rows
     * */
    public <T> int count(@NonNull Class<T> type, @Nullable String where) {
        return countResultsByQuery(String.format("SELECT * FROM %s", getTableName(type))
                + (TextUtils.isEmpty(where) ? "" : " WHERE " + where));
    }

    /**
     * Method that return count of rows
     *
     * @param type the class of table
     *
     * @return count of rows
     * */
    public <T> int count(@NonNull Class<T> type) {
        return count(type, null);
    }

    /**
     * Get list of objects from database
     *
     * @param query the SQL query
     * @param type the class to which you want to map result
     *
     * @return list of mapped objects
     *
     * Rightutils compatibility
     * */
    @NonNull
    public <T> List<T> executeQuery(@NonNull String query, @NonNull Class<T> type) {
        return queryListMapper(query, type);
    }

    /**
     * Get list of objects from database
     *
     * @param query {@link SelectQuery}
     *
     * @return list of mapped objects
     * */
    @SuppressWarnings("unchecked")
    @NonNull
    public <T> List<T> select(@NonNull SelectQuery query) {
        try {
            Class clazz = Class.forName(query.entity().getCanonicalName());
            return queryListMapper(query, clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Get list of objects from database
     *
     * @param type the class of table
     *
     * @return list of mapped objects
     *
     * Rightutils compatibility
     * */
    @NonNull
    public <T> List<T> getAll(@NonNull Class<T> type) {
        String query = String.format("select * from `%s`", getTableName(type));
        return queryListMapper(query, type);
    }

    /**
     * Get list of objects from database
     *
     * @param type the class of table
     * @param limit the limit the data amount
     *
     * @return list of mapped objects
     *
     * Rightutils compatibility
     * */
    @NonNull
    public <T> List<T> getAllLimited(@NonNull Class<T> type, long limit) {
        String query = String.format("select * from `%s` limit %d", getTableName(type), limit);
        return queryListMapper(query, type);
    }

    /**
     * Get list of objects from database
     *
     * @param where the where query
     * @param type the class of table
     *
     * @return list of mapped objects
     *
     * Rightutils compatibility
     * */
    @NonNull
    public <T> List<T> getAllWhere(@NonNull String where, @NonNull Class<T> type) {
        String query = String.format("select * from `%s` where %s", getTableName(type), where);
        return queryListMapper(query, type);
    }

    /**
     * Method for add rows in the database.
     *
     * @param elements the list of object that need to add in the database
     * @param useTransaction is need use transaction?
     *
     * @return the number of added rows OR -1 if any error
     * */
    public <T> int add(@NonNull List<T> elements, boolean useTransaction) {
        int count = 0;
        if (useTransaction) {
            try {
                db.beginTransaction();
                for (T value : elements) {
                    if (value != null) {
                        long raw = add(value);
                        if (raw > 0) {
                            count++;
                        }
                    }
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                count = -1;
                Log.e(TAG, "add list, use transaction", e);
            } finally {
                db.endTransaction();
            }
        } else {
            for (T value : elements) {
                if (value != null) {
                    long raw = add(value);
                    if (raw > 0) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Method for add rows in the database.
     *
     * @param elements the list of object that need to add in the database
     *
     * @return the number of added rows OR -1 if any error
     *
     * Rightutils compatibility
     * */
    public <T> int add(@NonNull List<T> elements) {
        return add(elements, true);
    }

    /**
     * Method for add row in the database.
     *
     * @param element the object that need to add in the database
     *
     * @return the row ID of the newly inserted row
     *
     * Rightutils compatibility
     * */
    public <T> long add(@NonNull final T element) {
        final ContentValues values = new ContentValues();
        boolean isColumnChild = false;
        Field fieldAutoInc = null;
        for (Field value : element.getClass().getDeclaredFields()) {
            if (!value.isAnnotationPresent(ColumnIgnore.class)
                    && !Modifier.isStatic(value.getModifiers())) {
                if (value.isAnnotationPresent(ColumnAutoInc.class)) {
                    valueAutoIncMapper(values, value, element);
                    fieldAutoInc = value;
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
        if (row > 0 && fieldAutoInc != null) {
            try {
                fieldAutoInc.setAccessible(true);
                fieldAutoInc.set(element, row);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (isColumnChild) {
            addColumnChild(element);
        }
        return row;
    }

    private <T> void addColumnChild(@NonNull final T element) {
        for (Field value : element.getClass().getDeclaredFields()) {
            if (value.isAnnotationPresent(ColumnChild.class)) {
                value.setAccessible(true);
                try {
                    Field parentKeyField = element.getClass().getDeclaredField(getParentKey(value));
                    parentKeyField.setAccessible(true);
                    Object parentKeyValue = parentKeyField.get(element);
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

    /**
     * Method for updating rows in the database.
     *
     * @param query {@link UpdateQuery}
     * @param values a map from column names to new column values. null is a
     *            valid value that will be translated to NULL.
     *
     * @return the row ID of the newly inserted row
     * */
    public int update(@NonNull UpdateQuery query, @NonNull ContentValues values) {
        return db.update(
                query.table(),
                values,
                nullableString(query.where()),
                nullableArrayOfStrings(query.whereArgs()));
    }

    //INNER METHODS

    private Cursor byQuery(@NonNull SelectQuery query) {
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

    private int byQuery(@NonNull DeleteQuery query) {
        return db.delete(
                query.table(),
                nullableString(query.where()),
                nullableArrayOfStrings(query.whereArgs()));
    }

    private <T> void valueAutoIncMapper(@NonNull ContentValues values, @NonNull Field field, @NonNull T element) {
        field.setAccessible(true);
        try {
            Long id = (Long) field.get(element);
            if (id != null && id > 0) {
                values.put(getColumnName(field), id);
            }
        } catch (IllegalAccessException e) {
            Log.e(TAG, "valueAutoIncMapper", e);
        }
    }

    private <T> void valueDAOMapper(@NonNull ContentValues values, @NonNull Field field, @NonNull T element) {
        field.setAccessible(true);
        try {
            values.put(getColumnName(field), field.get(element) != null ? serializeObject(field.get(element)) : null);
        } catch (Exception e) {
            Log.e(TAG, "valueDaoMapper", e);
        }
    }


    private <T> void valueMapper(@NonNull ContentValues values, @NonNull Field field, @NonNull T element) {
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
                        bytes = SerializeUtils.serialize(field.get(element));
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

    @NonNull
    private <T> List<T> queryListMapper(@Nullable Cursor cursor, @NonNull Class<T> type) {
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

    @NonNull
    private <T> List<T> queryListMapper(@NonNull String query, @NonNull Class<T> type) {
        return queryListMapper(db.rawQuery(query, null), type);
    }

    @NonNull
    private <T> List<T> queryListMapper(@NonNull SelectQuery query, @NonNull Class<T> type) {
        return queryListMapper(byQuery(query), type);
    }

    private <T> T cursorMapper(@NonNull Cursor cursor, @NonNull Class<T> type) {
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

    private <T> void fieldMapper(@NonNull T result, @NonNull Cursor cursor, final Field field, @NonNull String columnName) {
        field.setAccessible(true);
		Class<?> fieldType = field.getType();
        try {
            if (field.isAnnotationPresent(ColumnChild.class)) {
                childFieldMapper(result, cursor, field, fieldType);
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
					field.set(result, SerializeUtils.deserialize(bytes));
				}
			} else {
                Log.w(TAG, String.format("In class '%s' type '%s' of field '%s' not supported.", result.getClass().getSimpleName(), fieldType.toString(), field.getName()));
            }
        } catch (Exception e) {
            Log.e(TAG, "fieldMapper", e);
        }
    }

    private <T> void childFieldMapper(@NonNull T result, @NonNull Cursor cursor, Field field, Class<?> fieldType) throws IllegalAccessException {
        String foreignKey = getForeignKey(field);
        int columnIndex = cursor.getColumnIndex(getParentKey(field));
        final Object parentKeyValue;
        if (getType(cursor, columnIndex) == FIELD_TYPE_BLOB) {
            parentKeyValue = cursor.getBlob(columnIndex);
        } else if (getType(cursor, columnIndex) == FIELD_TYPE_FLOAT) {
            parentKeyValue = cursor.getDouble(columnIndex);
        } else if (getType(cursor, columnIndex) == FIELD_TYPE_INTEGER) {
            parentKeyValue = cursor.getLong(columnIndex);
        } else if (getType(cursor, columnIndex) == FIELD_TYPE_STRING) {
            parentKeyValue = cursor.getString(columnIndex);
        } else {
            parentKeyValue = null;
        }
        if (fieldType.isAssignableFrom(List.class)) {
            field.set(result, getAllWhere(formatParentKeyValue(foreignKey, parentKeyValue), (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]));
        } else {
            List resultList = getAllWhere(formatParentKeyValue(foreignKey, parentKeyValue), fieldType);
            field.set(result, resultList.isEmpty() ? null : resultList.get(0));
        }
    }

    private String formatParentKeyValue(String foreignKey, Object parentKeyValue) {
        if (parentKeyValue == null) {
            return String.format("%s is null", foreignKey);
        } else if (parentKeyValue instanceof String){
            return String.format("%s = '%s'", foreignKey, parentKeyValue);
        } else {
            return String.format("%s = %s", foreignKey, String.valueOf(parentKeyValue));
        }
    }

    private int getType(Cursor cursor, int column) {
        int type = -1;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            SQLiteCursor sqLiteCursor = (SQLiteCursor) cursor;
            CursorWindow cursorWindow = sqLiteCursor.getWindow();
            int pos = cursor.getPosition();
            if (cursorWindow.isNull(pos, column)) {
                type = FIELD_TYPE_NULL;
            } else if (cursorWindow.isLong(pos, column)) {
                type = FIELD_TYPE_INTEGER;
            } else if (cursorWindow.isFloat(pos, column)) {
                type = FIELD_TYPE_FLOAT;
            } else if (cursorWindow.isString(pos, column)) {
                type = FIELD_TYPE_STRING;
            } else if (cursorWindow.isBlob(pos, column)) {
                type = FIELD_TYPE_BLOB;
            }
        } else {
            if (cursor.getType(column) == Cursor.FIELD_TYPE_NULL) {
                type = FIELD_TYPE_NULL;
            } else if (cursor.getType(column) == Cursor.FIELD_TYPE_INTEGER) {
                type = FIELD_TYPE_INTEGER;
            } else if (cursor.getType(column) == Cursor.FIELD_TYPE_FLOAT) {
                type = FIELD_TYPE_FLOAT;
            } else if (cursor.getType(column) == Cursor.FIELD_TYPE_STRING) {
                type = FIELD_TYPE_STRING;
            } else if (cursor.getType(column) == Cursor.FIELD_TYPE_BLOB) {
                type = FIELD_TYPE_BLOB;
            }
        }

        return type;
    }

    @NonNull
    private <T> String getTableName(@NonNull Class<T> type) {
        String tableName = type.getSimpleName();
        if (type.isAnnotationPresent(TableName.class)) {
            tableName = type.getAnnotation(TableName.class).value();
        }
        return tableName;
    }

    @NonNull
    private String getColumnName(@NonNull Field field) {
        String columnName = field.getName();
        if (field.isAnnotationPresent(ColumnName.class)) {
            columnName = field.getAnnotation(ColumnName.class).value();
        }
        return columnName;
    }

    @NonNull
    private String getForeignKey(@NonNull Field field) {
        String columnName = field.getName();
        if (field.isAnnotationPresent(ColumnChild.class)) {
            columnName = field.getAnnotation(ColumnChild.class).foreignKey();
        }
        return columnName;
    }

    @NonNull
    private String getParentKey(@NonNull Field field) {
        String columnName = field.getName();
        if (field.isAnnotationPresent(ColumnChild.class)) {
            columnName = field.getAnnotation(ColumnChild.class).parentKey();
        }
        return columnName;
    }

    @Nullable
    private <T> String getIdFieldName(Class<T> type) {
        String id = null;
        String possibleId = null;
        String possibleRealId = null;
        for (Field field : type.getDeclaredFields()) {
            if (field.getType().isAssignableFrom(long.class) || field.getType().isAssignableFrom(Long.class)) {
                if (field.isAnnotationPresent(ColumnPrimaryKey.class) || field.isAnnotationPresent(ColumnAutoInc.class)) {
                    id = field.getName();
                    break;
                }
                if ("id".equalsIgnoreCase(getColumnName(field)) || "_id".equalsIgnoreCase(getColumnName(field))) {
                    possibleId = field.getName();
                    break;
                }
                if ("id".equalsIgnoreCase(field.getName()) || "_id".equalsIgnoreCase(field.getName())) {
                    possibleRealId = field.getName();
                    break;
                }
            }
        }
        return id != null ? id : possibleId != null ? possibleId : possibleRealId;
    }

    public LeftDBHandler getDbHandler() {
        return dbHandler;
    }

    //SQL UTILS

    /**
     * Create tables in database
     *
     * @param db The database.
     * @param elements The list of the classes of objects that need to create
     * */
    public void createTables(@NonNull SQLiteDatabase db, @NonNull List<Class<?>> elements) {
        for (Class<?> element : elements) {
            createTable(db, element);
        }
    }

    /**
     * Create table in database
     *
     * @param db The database.
     * @param type The class of object that need to create
     * */
    public void createTable(@NonNull SQLiteDatabase db, @NonNull Class<?> type){
        db.execSQL(createTableSQL(type));
    }

    private String createTableSQL(@NonNull Class<?> type) throws IllegalArgumentException {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ");
        sqlBuilder.append(getTableName(type));
        sqlBuilder.append(" (");
        int columnCount = 0;
        for (Field field : type.getDeclaredFields()) {
            if (!field.isAnnotationPresent(ColumnIgnore.class)
                    && !field.isAnnotationPresent(ColumnChild.class)) {
                String columnName = getColumnName(field);
                if (!columnName.contains("$")) {
                    StringBuilder builder = new StringBuilder();
                    if (columnCount > 0) {
                        builder.append(", ");
                    }

                    Class<?> fieldType = field.getType();
                    if (fieldType.isAssignableFrom(String.class)) {
                        builder.append(columnName);
                        builder.append(" TEXT");
                        if (field.isAnnotationPresent(ColumnPrimaryKey.class)) {
                            builder.append(" PRIMARY KEY");
                        }
                    } else if (fieldType.isAssignableFrom(long.class) || fieldType.isAssignableFrom(Long.class)) {
                        builder.append(columnName);
                        builder.append(" INTEGER");
                        if (field.isAnnotationPresent(ColumnAutoInc.class)) {
                            builder.append(" PRIMARY KEY AUTOINCREMENT");
                        } else if (field.isAnnotationPresent(ColumnPrimaryKey.class)) {
                            builder.append(" PRIMARY KEY");
                        }
                    } else if (fieldType.isAssignableFrom(int.class) || fieldType.isAssignableFrom(Integer.class)) {
                        builder.append(columnName);
                        builder.append(" INTEGER");
                        if (field.isAnnotationPresent(ColumnPrimaryKey.class)) {
                            builder.append(" PRIMARY KEY");
                        }
                    } else if (fieldType.isAssignableFrom(short.class) || fieldType.isAssignableFrom(Short.class)) {
                        builder.append(columnName);
                        builder.append(" INTEGER");
                        if (field.isAnnotationPresent(ColumnPrimaryKey.class)) {
                            builder.append(" PRIMARY KEY");
                        }
                    } else if (fieldType.isAssignableFrom(boolean.class) || fieldType.isAssignableFrom(Boolean.class)) {
                        builder.append(columnName);
                        builder.append(" INTEGER");
                        if (field.isAnnotationPresent(ColumnPrimaryKey.class)) {
                            builder.append(" PRIMARY KEY");
                        }
                    } else if (fieldType.isAssignableFrom(float.class) || fieldType.isAssignableFrom(Float.class)) {
                        builder.append(columnName);
                        builder.append(" REAL");
                        if (field.isAnnotationPresent(ColumnPrimaryKey.class)) {
                            builder.append(" PRIMARY KEY");
                        }
                    } else if (fieldType.isAssignableFrom(double.class) || fieldType.isAssignableFrom(Double.class)) {
                        builder.append(columnName);
                        builder.append(" REAL");
                        if (field.isAnnotationPresent(ColumnPrimaryKey.class)) {
                            builder.append(" PRIMARY KEY");
                        }
                    } else if (fieldType.isAssignableFrom(BigDecimal.class)) {
                        builder.append(columnName);
                        builder.append(" TEXT");
                        if (field.isAnnotationPresent(ColumnPrimaryKey.class)) {
                            builder.append(" PRIMARY KEY");
                        }
                    } else if (fieldType.isAssignableFrom(Date.class)) {
                        builder.append(columnName);
                        builder.append(" INTEGER");
                        if (field.isAnnotationPresent(ColumnPrimaryKey.class)) {
                            builder.append(" PRIMARY KEY");
                        }
                    } else if (fieldType.isAssignableFrom(Calendar.class)) {
                        builder.append(columnName);
                        builder.append(" INTEGER");
                        if (field.isAnnotationPresent(ColumnPrimaryKey.class)) {
                            builder.append(" PRIMARY KEY");
                        }
                    } else if (field.isAnnotationPresent(ColumnDAO.class)) {
                        builder.append(columnName);
                        builder.append(" TEXT");
                        if (field.isAnnotationPresent(ColumnPrimaryKey.class)) {
                            builder.append(" PRIMARY KEY");
                        }
                    } else if (Serializable.class.isAssignableFrom(fieldType.getClass())) {
                        builder.append(columnName);
                        builder.append(" BLOB");
                        if (field.isAnnotationPresent(ColumnPrimaryKey.class)) {
                            builder.append(" PRIMARY KEY");
                        }
                    }

                    if (builder.length() > 2) {
                        sqlBuilder.append(builder);
                        columnCount++;
                    }
                }
            }
        }
        if (columnCount == 0) {
            throw new IllegalArgumentException("Cannot create a table without at least one column.");
        }
        sqlBuilder.append(" );");
        return sqlBuilder.toString();
    }

    /**
     * Delete tables in database
     *
     * @param db The database.
     * @param elements The list of the classes of objects that need to delete
     * */
    public void deleteTables(@NonNull SQLiteDatabase db, @NonNull List<Class<?>> elements) {
        for (Class<?> element : elements) {
            deleteTable(db, element);
        }
    }

    /**
     * Delete table in database
     *
     * @param db The database.
     * @param type The class of object that need to delete
     * */
    public void deleteTable(@NonNull SQLiteDatabase db, @NonNull Class<?> type) {
        db.execSQL(deleteTableSQL(type));
    }

    private String deleteTableSQL(@NonNull Class<?> type) {
        return String.format("DROP TABLE IF EXISTS %s;", getTableName(type));
    }

    /**
     * Check is table exist in database
     *
     * @return true if table exist
     * */
    public boolean isTableExists(@NonNull Class<?> type) {
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?",
                new String[] {"table", getTableName(type)});
        if (!cursor.moveToFirst()) {
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
}
