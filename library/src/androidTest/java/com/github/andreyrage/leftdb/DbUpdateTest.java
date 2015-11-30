package com.github.andreyrage.leftdb;

import android.test.AndroidTestCase;

import com.github.andreyrage.leftdb.entities.SerializableObject;

import java.io.File;

/**
 * Created by rage on 11/19/15.
 */
public class DbUpdateTest extends AndroidTestCase {
    private static final String DB_MANE = "update.sqlite";

    @Override
    protected void setUp() throws Exception {
        File dbFile = new File(getContext().getFilesDir() + "/databases/" + DB_MANE);
        if (dbFile.exists()) {
            dbFile.delete();
        }
        assertTrue(!dbFile.exists());
    }

    public void testUpdate() throws Exception {
        {
            DBUtilsUpdate dbUtils = DBUtilsUpdate.newInstance(getContext(), DB_MANE, 1);
            assertNotNull(dbUtils.db);
            assertEquals(2, dbUtils.getAll(SerializableObject.class).size());
            dbUtils.db.close();
        }
        {
            DBUtilsUpdate dbUtils = DBUtilsUpdate.newInstance(getContext(), DB_MANE, 2);
            assertNotNull(dbUtils.db);
            assertEquals(4, dbUtils.getAll(SerializableObject.class).size());
            dbUtils.db.close();
        }
    }

}
