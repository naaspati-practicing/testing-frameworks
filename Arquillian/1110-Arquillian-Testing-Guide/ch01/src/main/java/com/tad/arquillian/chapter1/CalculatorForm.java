package com.tad.arquillian.chapter1;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

/**
 * @author JohnAment
 * 
 */
@RequestScoped
public class CalculatorForm {
	@PostConstruct
	public void init() {
		Logger.getGlobal().info("created: "+getClass()+"  "+hashCode());
	}

    private int x, y, z, sum;

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getSum() {
        return this.sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

}
