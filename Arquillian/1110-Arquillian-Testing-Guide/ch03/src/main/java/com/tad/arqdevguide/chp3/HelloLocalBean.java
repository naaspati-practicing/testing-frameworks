package com.tad.arqdevguide.chp3;

import java.util.logging.Logger;

import javax.ejb.Remove;
import javax.ejb.Stateless;

@Stateless
public class HelloLocalBean {
	 private final String text = "Hello world !";

	public String getText() {
		return text;
	}
	@Remove
	public void removed() {
		Logger.getGlobal().info("HelloLocalBean is removed: "+hashCode());
	}
}
