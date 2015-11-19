package com.github.andreyrage.leftdb;

public class DbNotAssetsTest extends DbAssetsTest {

    @Override
    protected void setUp() throws Exception {
        //super.setUp();
        dbUtils = DBUtils.newInstance(getContext(), "notassets.sqlite", 1);
        assertNotNull(dbUtils.db);
    }
}
