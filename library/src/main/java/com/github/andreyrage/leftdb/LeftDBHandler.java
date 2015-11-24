package com.github.andreyrage.leftdb;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class LeftDBHandler extends SQLiteOpenHelper {

	private static final String TAG = LeftDBHandler.class.getName();
	private SQLiteDatabase dataBase;
	private Context context;
	private String name;
	private String path;
	private int version;

	private OnDbChangeCallback mCallback;

	public LeftDBHandler(Context context, String name, int version, OnDbChangeCallback mCallback) {
		super(context, name, null, version);
		this.context = context;
		this.name = name;
		this.path = context.getFilesDir() + "/databases/";
		this.version = version;
		this.mCallback = mCallback;
		try {
			createOrCopyDataBaseFromAssets();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createOrCopyDataBaseFromAssets() throws IOException {
		if (!checkDataBase()) {
			if (assetsDbExists()) {
				Log.i(TAG, "copy DataBase");
				copyDataBase();
			} else {
				Log.i(TAG, "create DataBase");
				createDataBase();
			}
		}
	}

	public void deleteDataBase() {
		if (checkDataBase()) {
			close();
			File dbFile = new File(path + name);
			dbFile.delete();
		}
	}

	protected boolean assetsDbExists() throws IOException {
		return Arrays.asList(context.getAssets().list("")).contains(name);
	}

	protected boolean checkDataBase() {
		File dbFile = new File(path + name);
		return dbFile.exists();
	}

	private void createDataBase() {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getDbFile(), null);
		db.close();
	}

	private void copyDataBase() throws IOException {
		InputStream myInput = context.getAssets().open(name);

		final File file = getDbFile();

		OutputStream myOutput = new FileOutputStream(file);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	private File getDbFile() {
		final File dir = new File(path);
		dir.mkdirs();
		return new File(dir, name);
	}

	public SQLiteDatabase openDataBase(int openType) throws SQLException {
		String myPath = path + name;
		dataBase = SQLiteDatabase.openDatabase(myPath, null, openType);
		dataBase.execSQL("PRAGMA foreign_keys=ON;");
		validateVersion(dataBase);
		return dataBase;
	}

	private void validateVersion(SQLiteDatabase db) {
		int currentVersion = db.getVersion();
		if (currentVersion != version) {
			if (mCallback != null) {
				if (db.isReadOnly()) {
					Log.e(TAG, "Can't upgrade read-only database from version " +
							currentVersion + " to " + version);
				}
				db.beginTransaction();
				try {
					if (currentVersion == 0) {
						mCallback.onCreate(db);
					} else {
						if (currentVersion > version) {
							mCallback.onDowngrade(db, currentVersion, version);
						} else if (currentVersion < version) {
							mCallback.onUpgrade(db, currentVersion, version);
						}
					}
					db.setVersion(version);
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
			} else {
				Log.e(TAG, "Can't upgrade database from version " + currentVersion
						+ " to " + version + ", cause onVersionChangeCallback is null");
			}
		}
	}

	@Override
	public synchronized void close() {
		if (dataBase != null)
			dataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	@Override
	public SQLiteDatabase getWritableDatabase() {
		return openDataBase(SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	public SQLiteDatabase getReadableDatabase() {
		return openDataBase(SQLiteDatabase.OPEN_READONLY);
	}

	public interface OnDbChangeCallback {
		void onCreate(SQLiteDatabase db);
		void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
		void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion);
	}
}
