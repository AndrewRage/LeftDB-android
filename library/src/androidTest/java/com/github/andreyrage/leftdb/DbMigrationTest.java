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
