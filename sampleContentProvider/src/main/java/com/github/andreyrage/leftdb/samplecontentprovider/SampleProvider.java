package com.github.andreyrage.leftdb.samplecontentprovider;

import com.github.andreyrage.leftdb.LeftContentProvider;
import com.github.andreyrage.leftdb.samplecontentprovider.helpers.DbHelper;

/**
 * Created by rage on 20.07.15.
 */
public class SampleProvider extends LeftContentProvider {
    @Override
    public boolean onCreate() {
        initProvider(
                DbHelper.getInstance(getContext()),
                BuildConfig.APPLICATION_ID + ".provider"
        );
        return true;
    }
}
