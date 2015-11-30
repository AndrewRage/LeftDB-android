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