package com.github.andreyrage.leftdb;

/**
 * Created by Vlad on 11/19/15.
 */
public interface Func<A, B, C> {
	C invoke(A a, B b);
}
