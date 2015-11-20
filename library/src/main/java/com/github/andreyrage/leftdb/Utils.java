package com.github.andreyrage.leftdb;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Vlad on 11/19/15.
 */
public class Utils {

	public static void checkNotNull(@Nullable Object object, @NonNull String message) {
		if (object == null) {
			throw new NullPointerException(message);
		}
	}

	public static void checkNotNullOrEmpty(@Nullable String string, @NonNull String message) {
		if (string == null) {
			throw new NullPointerException(message);
		} else if (string.isEmpty()) {
			throw new IllegalStateException(message);
		}
	}

	@SafeVarargs
	@NonNull public static <T, A> List<T> unmodifiableListOf(Class<T> clazz, Func<List<T>, A, Void> func, @Nullable A... args) {
		if (args == null || args.length == 0) {
			return Collections.emptyList();
		} else {
			final List<T> list = new ArrayList<>(args.length);
			for (A arg : args) {
				func.invoke(list, arg);
			}
			return Collections.unmodifiableList(list);
		}
	}

	@Nullable public static String nullableString(@Nullable String str) {
		return str == null || str.isEmpty() ? null : str;
	}

	@Nullable public static String[] nullableArrayOfStrings(@Nullable List<String> list) {
		return list == null || list.isEmpty()
				? null
				: list.toArray(new String[list.size()]);
	}

	@NonNull public static String nonNullString(@Nullable String str) {
		return str == null ? "" : str;
	}

}
