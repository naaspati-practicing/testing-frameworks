package com.tad.arqdevguide.chp3;

import javax.enterprise.context.Dependent;

@Dependent
public class Calculator {
	public int add(int x, int y) {
		return Math.addExact(x, y);
	}
	public int subtract(int x, int y) {
		return Math.subtractExact(x, y);
	}
}
