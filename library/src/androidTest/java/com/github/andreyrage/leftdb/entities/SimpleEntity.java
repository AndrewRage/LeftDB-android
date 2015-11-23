package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.TableName;

/**
 * Created by Vlad on 11/20/15.
 */
@TableName("Simpleee")
public class SimpleEntity {

	int id;
	String name;

	public SimpleEntity(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public SimpleEntity() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
