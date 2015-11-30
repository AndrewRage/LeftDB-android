package com.github.andreyrage.leftdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Anton Maniskevich on 05.03.14.
 *
 * Rightutils compatibility
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnChild {
	String parentKey() default "id";
	String foreignKey();

}
