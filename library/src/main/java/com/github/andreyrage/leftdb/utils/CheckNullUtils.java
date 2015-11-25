package com.github.andreyrage.leftdb.utils;

import android.text.TextUtils;

import com.github.andreyrage.leftdb.interfaces.Func;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Vlad on 11/19/15.
 */
public class CheckNullUtils {

    public static void checkNotNull(Object object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
    }

    @SafeVarargs
    public static <T, A> List<T> unmodifiableListOf(Class<T> clazz, Func<List<T>, A, Void> func, A... args) {
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

    public static String nullableString(String str) {
        return TextUtils.isEmpty(str) ? null : str;
    }

    public static String[] nullableArrayOfStrings(List<String> list) {
        return list == null || list.isEmpty()
                ? null
                : list.toArray(new String[list.size()]);
    }

    public static String nonNullString(String str) {
        return str == null ? "" : str;
    }

}
