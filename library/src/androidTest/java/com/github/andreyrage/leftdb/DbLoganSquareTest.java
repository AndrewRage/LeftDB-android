package com.github.andreyrage.leftdb;

public class DbLoganSquareTest extends DbJacksonTest {

	@Override
	protected void setUp() throws Exception {
		dbUtils = DBUtilsLoganSquare.newInstance(getContext(), "logansquare.sqlite", 1);
		assertNotNull(dbUtils.db);
	}

	@Override
	protected void tearDown() throws Exception {
		dbUtils.getDbHandler().deleteDataBase();
	}

}
