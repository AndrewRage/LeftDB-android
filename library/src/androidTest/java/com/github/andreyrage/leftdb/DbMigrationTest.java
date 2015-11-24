package com.github.andreyrage.leftdb;

import android.test.AndroidTestCase;

import com.github.andreyrage.leftdb.entities.AllFields;
import com.github.andreyrage.leftdb.entities.ChildMany;
import com.github.andreyrage.leftdb.entities.ChildOne;

import java.io.File;

/**
 * Created by rage on 11/19/15.
 */
public class DbMigrationTest extends AndroidTestCase {
    private static final String DB_MANE = "migration.sqlite";

    @Override
    protected void setUp() throws Exception {
        File dbFile = new File(getContext().getFilesDir() + "/databases/" + DB_MANE);
        if (dbFile.exists()) {
            dbFile.delete();
        }
        assertTrue(!dbFile.exists());
    }

    public void testMigration() throws Exception {
        {
            DBUtilsMigration dbUtils = DBUtilsMigration.newInstance(getContext(), DB_MANE, 1);
            assertNotNull(dbUtils.db);
            assertTrue(dbUtils.isTableExists(AllFields.class));
            assertTrue(!dbUtils.isTableExists(ChildOne.class));
            assertTrue(!dbUtils.isTableExists(ChildMany.class));
            dbUtils.db.close();
        }
        {
            DBUtilsMigration dbUtils = DBUtilsMigration.newInstance(getContext(), DB_MANE, 2);
            assertNotNull(dbUtils.db);
            assertTrue(dbUtils.isTableExists(AllFields.class));
            assertTrue(dbUtils.isTableExists(ChildOne.class));
            assertTrue(!dbUtils.isTableExists(ChildMany.class));
            dbUtils.db.close();
        }
        {
            DBUtilsMigration dbUtils = DBUtilsMigration.newInstance(getContext(), DB_MANE, 3);
            assertNotNull(dbUtils.db);
            assertTrue(dbUtils.isTableExists(AllFields.class));
            assertTrue(dbUtils.isTableExists(ChildOne.class));
            assertTrue(dbUtils.isTableExists(ChildMany.class));
            dbUtils.db.close();
        }
        {
            DBUtilsMigration dbUtils = DBUtilsMigration.newInstance(getContext(), DB_MANE, 2);
            assertNotNull(dbUtils.db);
            assertTrue(dbUtils.isTableExists(AllFields.class));
            assertTrue(dbUtils.isTableExists(ChildOne.class));
            assertTrue(!dbUtils.isTableExists(ChildMany.class));
            dbUtils.db.close();
        }
        {
            DBUtilsMigration dbUtils = DBUtilsMigration.newInstance(getContext(), DB_MANE, 1);
            assertNotNull(dbUtils.db);
            assertTrue(dbUtils.isTableExists(AllFields.class));
            assertTrue(!dbUtils.isTableExists(ChildOne.class));
            assertTrue(!dbUtils.isTableExists(ChildMany.class));
            dbUtils.db.close();
        }
    }

}
