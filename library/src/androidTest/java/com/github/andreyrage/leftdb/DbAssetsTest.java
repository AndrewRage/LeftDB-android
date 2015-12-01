package com.github.andreyrage.leftdb;

import android.content.ContentValues;
import android.test.AndroidTestCase;

import com.github.andreyrage.leftdb.entities.AllFields;
import com.github.andreyrage.leftdb.entities.AnnotationId;
import com.github.andreyrage.leftdb.entities.ChildMany;
import com.github.andreyrage.leftdb.entities.ChildOne;
import com.github.andreyrage.leftdb.entities.FloatKey;
import com.github.andreyrage.leftdb.entities.FloatKeyChild;
import com.github.andreyrage.leftdb.entities.NotAnnotationId;
import com.github.andreyrage.leftdb.entities.StringKeyChild;
import com.github.andreyrage.leftdb.entities.StringKey;
import com.github.andreyrage.leftdb.entities.ParentMany;
import com.github.andreyrage.leftdb.entities.ParentOne;
import com.github.andreyrage.leftdb.entities.PrimaryKeyId;
import com.github.andreyrage.leftdb.entities.SerializableObject;
import com.github.andreyrage.leftdb.queries.DeleteQuery;
import com.github.andreyrage.leftdb.queries.SelectQuery;
import com.github.andreyrage.leftdb.queries.UpdateQuery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DbAssetsTest extends AndroidTestCase {

	public DBUtils dbUtils;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		dbUtils = DBUtils.newInstance(getContext(), "test.sqlite", 1);
		assertNotNull(dbUtils.db);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		dbUtils.getDbHandler().deleteDataBase();
	}

	public void testAddEntity() throws Exception {
		SerializableObject child = new SerializableObject(1, "child", null);
		SerializableObject parent = new SerializableObject(1, "parrent", child);
		ArrayList<SerializableObject> list = new ArrayList<>();
		list.add(child);
		list.add(parent);
		AllFields allFields = new AllFields(
				1,
				(short) 10,
				(short) 10,
				20,
				20,
				(long) 30,
				(long) 30,
				0.40f,
				0.40f,
				0.50d,
				0.50d,
				true,
				true,
				"simple string",
				new BigDecimal(1345892734),
				new Date(),
				Calendar.getInstance(),
				parent,
				parent,
				list
		);

		long row = dbUtils.add(allFields);
		List<AllFields> dbList = dbUtils.getAll(AllFields.class);

		assertEquals(1, row);
		assertEquals(1, dbUtils.count(AllFields.class));
		assertEquals(1, dbList.size());
		assertEquals(allFields, dbList.get(0));
	}

	public void testAddEntityWithNull() throws Exception {
		AllFields allFields = new AllFields(
				1,
				(short) 10,
				null,
				20,
				null,
				(long) 30,
				null,
				0.40f,
				null,
				0.50d,
				null,
				true,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null
		);

		dbUtils.add(allFields);
		List<AllFields> dbList = dbUtils.getAll(AllFields.class);

		assertEquals(1, dbUtils.count(AllFields.class));
		assertEquals(1, dbList.size());
		assertEquals(allFields, dbList.get(0));
	}

	public void testAddEntityAutoInc() throws Exception {
		SerializableObject object1 = new SerializableObject();
		dbUtils.add(object1);
		assertEquals(1, object1.getId());

		SerializableObject object2 = new SerializableObject();
		dbUtils.add(object2);
		assertEquals(2, object2.getId());

		List<SerializableObject> dbList = dbUtils.getAll(SerializableObject.class);

		assertEquals(2, dbUtils.count(SerializableObject.class));
		assertEquals(2, dbList.size());
		assertEquals(1, dbUtils.count(SerializableObject.class, "id = 1"));
		assertEquals(1, dbList.get(0).getId());
		assertEquals(2, dbList.get(1).getId());
	}

	public void testColumnName() throws Exception {
		SerializableObject object = new SerializableObject(100, "simple name", null);

		long row = dbUtils.add(object);
		List<SerializableObject> dbList = dbUtils.getAll(SerializableObject.class);

		assertEquals(100, row);
		assertEquals(1, dbList.size());
		assertEquals("simple name", dbList.get(0).getName());
	}

	public void testColumnIgnore() throws Exception {
		SerializableObject object = new SerializableObject(100, "simple name", new SerializableObject());

		dbUtils.add(object);
		List<SerializableObject> dbList = dbUtils.getAll(SerializableObject.class);

		assertEquals(1, dbList.size());
		assertNotSame(object, dbList.get(0));
		assertEquals("simple name", dbList.get(0).getName());
		assertNull(dbList.get(0).getObject());
	}

	public void testAddListWithTransaction() throws Exception {
		SerializableObject object1 = new SerializableObject(100, "simple name", new SerializableObject());
		SerializableObject object2 = new SerializableObject(101, "simple name", new SerializableObject());

		List<SerializableObject> list = new ArrayList<>();
		list.add(object1);
		list.add(object2);

		int count = dbUtils.add(list);
		List<SerializableObject> dbList = dbUtils.getAll(SerializableObject.class);

		assertEquals(2, count);
		assertEquals(2, dbList.size());
	}

	public void testAddListWithoutTransaction() throws Exception {
		SerializableObject object1 = new SerializableObject(100, "simple name", new SerializableObject());
		SerializableObject object2 = new SerializableObject(101, "simple name", new SerializableObject());

		List<SerializableObject> list = new ArrayList<>();
		list.add(object1);
		list.add(object2);

		int count = dbUtils.add(list, false);
		List<SerializableObject> dbList = dbUtils.getAll(SerializableObject.class);

		assertEquals(2, count);
		assertEquals(2, dbList.size());
	}

	public void testGetAllWhere() throws Exception {
		SerializableObject object1 = new SerializableObject(100, "simple name", null);
		SerializableObject object2 = new SerializableObject(101, "simple name", null);

		List<SerializableObject> list = new ArrayList<>();
		list.add(object1);
		list.add(object2);

		dbUtils.add(list);
		List<SerializableObject> dbList = dbUtils.getAllWhere("id = 101", SerializableObject.class);

		assertEquals(1, dbList.size());
		assertEquals(object2, dbList.get(0));
	}

	public void testGetAllLimit() throws Exception {
		SerializableObject object1 = new SerializableObject(100, "simple name1", null);
		SerializableObject object2 = new SerializableObject(101, "simple name2", null);
		SerializableObject object3 = new SerializableObject(103, "simple name3", null);

		List<SerializableObject> list = new ArrayList<>();
		list.add(object1);
		list.add(object2);
		list.add(object3);

		dbUtils.add(list);
		assertEquals(3, dbUtils.count(SerializableObject.class));

		assertEquals(1, dbUtils.getAllLimited(SerializableObject.class, 1).size());
		assertEquals(2, dbUtils.getAllLimited(SerializableObject.class, 2).size());
		assertEquals(3, dbUtils.getAllLimited(SerializableObject.class, 3).size());
		assertEquals(3, dbUtils.getAllLimited(SerializableObject.class, 4).size());
	}

	public void testDeleteAll() throws Exception {
		SerializableObject object1 = new SerializableObject(100, "simple name", null);
		SerializableObject object2 = new SerializableObject(101, "simple name", null);

		List<SerializableObject> list = new ArrayList<>();
		list.add(object1);
		list.add(object2);

		dbUtils.add(list);
		assertEquals(2, dbUtils.count(SerializableObject.class));

		int count = dbUtils.deleteAll(SerializableObject.class);
		List<SerializableObject> dbList = dbUtils.getAll(SerializableObject.class);

		assertEquals(0, dbUtils.count(SerializableObject.class));
		assertEquals(2, count);
		assertTrue(dbList.isEmpty());
	}

	public void testDeleteObject() throws Exception {
		SerializableObject object1 = new SerializableObject(100, "simple name", null);
		SerializableObject object2 = new SerializableObject(101, "simple name", null);

		List<SerializableObject> list = new ArrayList<>();
		list.add(object1);
		list.add(object2);

		dbUtils.add(list);
		boolean isDeleted = dbUtils.delete(object1);
		List<SerializableObject> dbList = dbUtils.getAll(SerializableObject.class);

		assertEquals(1, dbList.size());
		assertTrue(isDeleted);
		assertEquals(object2, dbList.get(0));
	}

	public void testDeleteAnnotationObject() throws Exception {
		AnnotationId object1 = new AnnotationId(100L, "simple name");
		AnnotationId object2 = new AnnotationId(101L, "simple name");

		List<AnnotationId> list = new ArrayList<>();
		list.add(object1);
		list.add(object2);

		dbUtils.add(list);
		boolean isDeleted = dbUtils.delete(object1);
		List<AnnotationId> dbList = dbUtils.getAll(AnnotationId.class);

		assertEquals(1, dbList.size());
		assertTrue(isDeleted);
		assertEquals(object2, dbList.get(0));
	}

	public void testDeleteNotAnnotationObject() throws Exception {
		NotAnnotationId object1 = new NotAnnotationId(100L, "simple name");
		NotAnnotationId object2 = new NotAnnotationId(101L, "simple name");

		List<NotAnnotationId> list = new ArrayList<>();
		list.add(object1);
		list.add(object2);

		dbUtils.add(list);
		boolean isDeleted = dbUtils.delete(object1);
		List<NotAnnotationId> dbList = dbUtils.getAll(NotAnnotationId.class);

		assertEquals(1, dbList.size());
		assertTrue(isDeleted);
		assertEquals(object2, dbList.get(0));
	}

	public void testDeletePrimaryKeyObject() throws Exception {
		PrimaryKeyId object1 = new PrimaryKeyId(100L, "simple name");
		PrimaryKeyId object2 = new PrimaryKeyId(101L, "simple name");

		List<PrimaryKeyId> list = new ArrayList<>();
		list.add(object1);
		list.add(object2);

		dbUtils.add(list);
		boolean isDeleted = dbUtils.delete(object1);
		List<PrimaryKeyId> dbList = dbUtils.getAll(PrimaryKeyId.class);

		assertEquals(1, dbList.size());
		assertTrue(isDeleted);
		assertEquals(object2, dbList.get(0));
	}

	public void testDeleteListObject() throws Exception {
		SerializableObject object1 = new SerializableObject(100, "simple name", null);
		SerializableObject object2 = new SerializableObject(101, "simple name", null);
		SerializableObject object3 = new SerializableObject(102, "simple name", null);

		List<SerializableObject> list = new ArrayList<>();
		list.add(object1);
		list.add(object2);
		list.add(object3);
		dbUtils.add(list);

		List<SerializableObject> deleteList = new ArrayList<>();
		deleteList.add(object1);
		deleteList.add(object3);
		int count = dbUtils.delete(deleteList);

		List<SerializableObject> dbList = dbUtils.getAll(SerializableObject.class);

		assertEquals(1, dbList.size());
		assertEquals(2, count);
		assertEquals(object2, dbList.get(0));
	}

	public void testDeleteSetObject() throws Exception {
		SerializableObject object1 = new SerializableObject(100, "simple name", null);
		SerializableObject object2 = new SerializableObject(101, "simple name", null);
		SerializableObject object3 = new SerializableObject(102, "simple name", null);

		List<SerializableObject> list = new ArrayList<>();
		list.add(object1);
		list.add(object2);
		list.add(object3);
		dbUtils.add(list);

		Set<SerializableObject> deleteList = new HashSet<>();
		deleteList.add(object1);
		deleteList.add(object3);
		int count = dbUtils.delete(deleteList);

		List<SerializableObject> dbList = dbUtils.getAll(SerializableObject.class);

		assertEquals(1, dbList.size());
		assertEquals(2, count);
		assertEquals(object2, dbList.get(0));
	}

	public void testDeleteWhere() throws Exception {
		SerializableObject object1 = new SerializableObject(100, "simple name", null);
		SerializableObject object2 = new SerializableObject(101, "simple name", null);

		List<SerializableObject> list = new ArrayList<>();
		list.add(object1);
		list.add(object2);

		dbUtils.add(list);
		int count = dbUtils.deleteWhere(SerializableObject.class, "id = 100");
		List<SerializableObject> dbList = dbUtils.getAll(SerializableObject.class);

		assertEquals(1, dbList.size());
		assertEquals(1, count);
		assertEquals(object2, dbList.get(0));
	}

	public void testDeleteListIds() throws Exception {
		SerializableObject object1 = new SerializableObject(100, "simple name", null);
		SerializableObject object2 = new SerializableObject(101, "simple name", null);
		SerializableObject object3 = new SerializableObject(102, "simple name", null);

		List<SerializableObject> list = new ArrayList<>();
		list.add(object1);
		list.add(object2);
		list.add(object3);

		dbUtils.add(list);
		assertEquals(3, dbUtils.count(SerializableObject.class));

		int count = dbUtils.delete(SerializableObject.class, "id", Arrays.asList((long) 101, (long) 102));
		List<SerializableObject> dbList = dbUtils.getAll(SerializableObject.class);

		assertEquals(2, count);
		assertEquals(1, dbUtils.count(SerializableObject.class));
		assertEquals(1, dbList.size());
		assertEquals(object1, dbList.get(0));
	}

	public void testOneToOne() throws Exception {
		ParentOne parentOne1 = new ParentOne(200, "parent1", new ChildOne("child1"));
		ParentOne parentOne2 = new ParentOne(201, "parent2", new ChildOne("child2"));
		List<ParentOne> list = new ArrayList<>();
		list.add(parentOne1);
		list.add(parentOne2);

		dbUtils.add(list);
		List<ParentOne> dbList = dbUtils.getAll(ParentOne.class);

		assertEquals(2, dbList.size());
		assertEquals("child1", dbList.get(0).getChild().getName());
		assertEquals("child2", dbList.get(1).getChild().getName());
	}

	public void testOneToOneAutoInc() throws Exception {
		ParentOne parentOne1 = new ParentOne("parent1", new ChildOne("child1"));
		ParentOne parentOne2 = new ParentOne("parent2", new ChildOne("child2"));
		List<ParentOne> list = new ArrayList<>();
		list.add(parentOne1);
		list.add(parentOne2);

		dbUtils.add(list);
		List<ParentOne> dbList = dbUtils.getAll(ParentOne.class);

		assertEquals(2, dbList.size());
		assertEquals("child1", dbList.get(0).getChild().getName());
		assertEquals("child2", dbList.get(1).getChild().getName());
	}

	public void testOneToMany() throws Exception {
		List<ChildMany> childList1 = new ArrayList<>();
		childList1.add(new ChildMany("child1"));
		childList1.add(new ChildMany("child2"));
		ParentMany parentMany1 = new ParentMany(200L, "parent1", childList1);
		List<ChildMany> childList2 = new ArrayList<>();
		childList2.add(new ChildMany("child3"));
		childList2.add(new ChildMany("child4"));
		childList2.add(new ChildMany("child5"));
		ParentMany parentMany2 = new ParentMany(201L, "parent2", childList2);
		List<ParentMany> list = new ArrayList<>();
		list.add(parentMany1);
		list.add(parentMany2);

		dbUtils.add(list);
		List<ParentMany> dbList = dbUtils.getAll(ParentMany.class);

		assertEquals(2, dbList.size());
		assertEquals(2, dbList.get(0).getChilds().size());
		assertEquals("child1", dbList.get(0).getChilds().get(0).getName());
		assertEquals("child2", dbList.get(0).getChilds().get(1).getName());
		assertEquals(3, dbList.get(1).getChilds().size());
		assertEquals("child3", dbList.get(1).getChilds().get(0).getName());
		assertEquals("child4", dbList.get(1).getChilds().get(1).getName());
		assertEquals("child5", dbList.get(1).getChilds().get(2).getName());
	}

	public void testOneToManyAutoInc() throws Exception {
		List<ChildMany> childList1 = new ArrayList<>();
		childList1.add(new ChildMany("child1"));
		childList1.add(new ChildMany("child2"));
		ParentMany parentMany1 = new ParentMany("parent1", childList1);
		List<ChildMany> childList2 = new ArrayList<>();
		childList2.add(new ChildMany("child3"));
		childList2.add(new ChildMany("child4"));
		childList2.add(new ChildMany("child5"));
		ParentMany parentMany2 = new ParentMany("parent2", childList2);
		List<ParentMany> list = new ArrayList<>();
		list.add(parentMany1);
		list.add(parentMany2);

		dbUtils.add(list);
		List<ParentMany> dbList = dbUtils.getAll(ParentMany.class);

		assertEquals(2, dbList.size());
		assertEquals(2, dbList.get(0).getChilds().size());
		assertEquals("child1", dbList.get(0).getChilds().get(0).getName());
		assertEquals("child2", dbList.get(0).getChilds().get(1).getName());
		assertEquals(3, dbList.get(1).getChilds().size());
		assertEquals("child3", dbList.get(1).getChilds().get(0).getName());
		assertEquals("child4", dbList.get(1).getChilds().get(1).getName());
		assertEquals("child5", dbList.get(1).getChilds().get(2).getName());
	}

	public void testOneToOneStringKey() throws Exception {
		StringKey parent1 = new StringKey("key1", "parent1", new StringKeyChild("child1"));
		StringKey parent2 = new StringKey("key2", "parent2", new StringKeyChild("child2"));
		StringKey parent3 = new StringKey(null, "parent2", new StringKeyChild("child3"));
		List<StringKey> list = new ArrayList<>();
		list.add(parent1);
		list.add(parent2);
		list.add(parent3);

		dbUtils.add(list);
		List<StringKey> dbList = dbUtils.getAll(StringKey.class);

		assertEquals(3, dbList.size());
		assertEquals("child1", dbList.get(0).getStringKeyChild().getName());
		assertEquals("child2", dbList.get(1).getStringKeyChild().getName());
		assertEquals("child3", dbList.get(2).getStringKeyChild().getName());
	}

	public void testOneToOneFloatKey() throws Exception {
		FloatKey parent1 = new FloatKey(3.4f, "parent1", new FloatKeyChild("child1"));
		FloatKey parent2 = new FloatKey(697.0f, "parent2", new FloatKeyChild("child2"));
		FloatKey parent3 = new FloatKey(null, "parent2", new FloatKeyChild("child3"));
		List<FloatKey> list = new ArrayList<>();
		list.add(parent1);
		list.add(parent2);
		list.add(parent3);

		dbUtils.add(list);
		List<FloatKey> dbList = dbUtils.getAll(FloatKey.class);

		assertEquals(3, dbList.size());
		assertEquals("child1", dbList.get(0).getFloatKeyChild().getName());
		assertEquals("child2", dbList.get(1).getFloatKeyChild().getName());
		assertEquals("child3", dbList.get(2).getFloatKeyChild().getName());
	}

	public void testSelect() throws Exception {
		SerializableObject object1 = new SerializableObject(100, "simple name", null);
		SerializableObject object2 = new SerializableObject(101, "simple name", null);

		List<SerializableObject> list = new ArrayList<>();
		list.add(object1);
		list.add(object2);

		dbUtils.add(list);
		List<SerializableObject> dbList = dbUtils.select(
				SelectQuery.builder()
						.entity(SerializableObject.class)
						.where("id = ?")
						.whereArgs("101")
						.build()
		);

		assertEquals(1, dbList.size());
		assertNotSame(object2, dbList.get(0));
	}

	public void testDelete() throws Exception {
		SerializableObject object1 = new SerializableObject(100, "simple name", null);
		SerializableObject object2 = new SerializableObject(101, "simple name", null);

		List<SerializableObject> list = new ArrayList<>();
		list.add(object1);
		list.add(object2);

		dbUtils.add(list);
		dbUtils.delete(
				DeleteQuery.builder()
						.entity(SerializableObject.class)
						.where("id = ?").whereArgs("100")
						.build()
		);
		List<SerializableObject> dbList = dbUtils.getAll(SerializableObject.class);

		assertEquals(1, dbList.size());
		assertEquals(1, dbUtils.count(SelectQuery.builder().entity(SerializableObject.class).build()));
		assertEquals(object2, dbList.get(0));
	}

	public void testUpdate() throws Exception {
		SerializableObject object = new SerializableObject(100, "simple name", null);

		dbUtils.add(object);
		ContentValues contentValues = new ContentValues();
		contentValues.put("otherName", "New name");
		dbUtils.update(
				UpdateQuery.builder().table(SerializableObject.class).where("id = 100").build(),
				contentValues
		);
		List<SerializableObject> dbList = dbUtils.getAll(SerializableObject.class);

		assertEquals(1, dbList.size());
		assertNotSame(object, dbList.get(0));
		assertEquals("New name", dbList.get(0).getName());
	}
}
