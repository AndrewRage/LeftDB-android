package com.github.andreyrage.leftdb;

import android.test.AndroidTestCase;

import com.github.andreyrage.leftdb.entities.DaoTestEntry;
import com.github.andreyrage.leftdb.entities.SerializableObject;

import java.util.ArrayList;
import java.util.List;

public class DbJacksonTest extends AndroidTestCase {

	public LeftDBUtils dbUtils;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		dbUtils = DBUtilsJackson.newInstance(getContext(), "jackson.sqlite", 1);
		assertNotNull(dbUtils.db);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		dbUtils.getDbHandler().deleteDataBase();
	}

	public void testNullDao() throws Exception {
		DaoTestEntry daoTestEntry = new DaoTestEntry(1, null, null);

		dbUtils.add(daoTestEntry);
		List<DaoTestEntry> dbList = dbUtils.getAll(DaoTestEntry.class);

		assertEquals(1, dbList.size());
		assertNull(dbList.get(0).getSerializableObject());
		assertNull(dbList.get(0).getSerializableObjectList());
	}

	public void testObjectDao() throws Exception {
		SerializableObject object = new SerializableObject(20, "single", new SerializableObject(200, "child", null));
		DaoTestEntry daoTestEntry = new DaoTestEntry(1, object, null);

		dbUtils.add(daoTestEntry);
		List<DaoTestEntry> dbList = dbUtils.getAll(DaoTestEntry.class);

		assertEquals(1, dbList.size());
		assertEquals(object, dbList.get(0).getSerializableObject());
	}

	public void testObjectChangedDao() throws Exception {
		SerializableObject object = new SerializableObject(20, "single", new SerializableObject(200, "child", null));
		DaoTestEntry daoTestEntry = new DaoTestEntry(1, object, null);

		dbUtils.add(daoTestEntry);
		object.getObject().setName("another");
		List<DaoTestEntry> dbList = dbUtils.getAll(DaoTestEntry.class);

		assertEquals(1, dbList.size());
		assertNotSame(object, dbList.get(0).getSerializableObject());
	}

	public void testListDao() throws Exception {
		SerializableObject object1 = new SerializableObject(21, "single", new SerializableObject(201, "child", null));
		SerializableObject object2 = new SerializableObject(22, "single", new SerializableObject(202, "child", null));
		SerializableObject object3 = new SerializableObject(23, "single", new SerializableObject(203, "child", null));
		List<SerializableObject> list = new ArrayList<>();
		list.add(object1);
		list.add(object2);
		list.add(object3);
		DaoTestEntry daoTestEntry = new DaoTestEntry(1, null, list);

		dbUtils.add(daoTestEntry);
		List<DaoTestEntry> dbList = dbUtils.getAll(DaoTestEntry.class);

		assertEquals(1, dbList.size());
		assertEquals(list, dbList.get(0).getSerializableObjectList());
	}

}
