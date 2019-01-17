package com.tad.arqdevguide.chp3.cdi;

import static org.junit.Assert.*;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tad.arqdevguide.chp3.HelloWorld;

@RunWith(Arquillian.class)
public class HelloCDITest {
	@Deployment
	public static JavaArchive create() {
		JavaArchive j = ShrinkWrap.create(JavaArchive.class)
				.addClass(HelloWorld.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		
		System.out.println(j.toString(true));
		return j;
	}
	
	@Inject BeanManager maneger;
	@Inject HelloWorld hello;

	@Test
	public void testCdiBootstrap() {
		assertNotNull(maneger);
		assertNotNull(hello);
		
		assertFalse(maneger.getBeans(BeanManager.class).isEmpty());
		assertEquals("Hello world !", hello.getText());
		
	}

}
