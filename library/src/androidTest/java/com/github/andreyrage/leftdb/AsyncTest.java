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
