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

package com.github.andreyrage.leftdb.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Vlad on 11/19/15.
 */
public class CheckNullUtils {

	public static void checkNotNull(@Nullable Object object, @NonNull String message) {
		if (object == null) {
			throw new NullPointerException(message);
		}
	}

	@SafeVarargs
	@NonNull
	public static <A> List<String> unmodifiableListOfStrings(@Nullable A... args) {
		if (args == null || args.length == 0) {
			return Collections.emptyList();
		} else {
			final List<String> list = new ArrayList<>(args.length);
			for (A arg : args) {
				list.add((arg != null ? arg.toString() : "null"));
			}
			return Collections.unmodifiableList(list);
		}
	}

	@Nullable
	public static String nullableString(@Nullable String str) {
		return TextUtils.isEmpty(str) ? null : str;
	}

	@Nullable
	public static String[] nullableArrayOfStrings(@Nullable List<String> list) {
		return list == null || list.isEmpty()
				? null
				: list.toArray(new String[list.size()]);
	}

	@NonNull
	public static String nonNullString(@Nullable String str) {
		return str == null ? "" : str;
	}

}