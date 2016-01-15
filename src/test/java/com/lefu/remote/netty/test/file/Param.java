package com.lefu.remote.netty.test.file;

import java.io.Serializable;

public class Param implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String value;
	
	public Param() {
		
	}
	
	public Param(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "name = " + this.name + ", value = " + this.value;
	}
	
}
