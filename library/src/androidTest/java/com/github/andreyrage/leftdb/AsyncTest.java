package com.github.andreyrage.leftdb;

import android.test.ActivityTestCase;

import com.github.andreyrage.leftdb.entities.SerializableObject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class AsyncTest extends ActivityTestCase {

	public void testAsyncCall() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final SerializableObject object = new SerializableObject(100, "simple name", null);

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				AsyncCall.make(new AsyncCall.Call<List<SerializableObject>>() {
					@Override
					public List<SerializableObject> call() {
						return Arrays.asList(object);
					}
				}, new AsyncCall.Do<List<SerializableObject>>() {
					@Override
					public void doNext(List<SerializableObject> serializableObjects) {
						assertEquals(1, serializableObjects.size());
						assertEquals(object, serializableObjects.get(0));
						signal.countDown();
					}
				}).call();
			}
		});

		signal.await();
	}
}
