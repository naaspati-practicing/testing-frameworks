package com.tad.arqdevguide.chp3.junit;

import org.junit.Test;

import com.tad.arqdevguide.chp3.HelloWorld;
import static org.junit.Assert.*;

public class HelloWorldTest {
	@Test
	   public void testGetText() {
	      HelloWorld fixture = new HelloWorld();
	      assertEquals("Hello world !", fixture.getText());
	   }
}
