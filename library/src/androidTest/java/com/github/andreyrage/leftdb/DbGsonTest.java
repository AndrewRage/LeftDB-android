package com.github.andreyrage.leftdb;

public class DbGsonTest extends DbJacksonTest {

	@Override
	protected void setUp() throws Exception {
		dbUtils = DBUtilsGson.newInstance(getContext(), "gson.sqlite", 1);
		assertNotNull(dbUtils.db);
	}

	@Override
	protected void tearDown() throws Exception {
		dbUtils.getDbHandler().deleteDataBase();
	}

}
