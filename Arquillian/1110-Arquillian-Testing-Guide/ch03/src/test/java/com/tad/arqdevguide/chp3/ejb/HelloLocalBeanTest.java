package com.tad.arqdevguide.chp3.ejb;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import com.tad.arqdevguide.chp3.HelloLocalBean;

@RunWith(Arquillian.class)
public class HelloLocalBeanTest {
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(JavaArchive.class)
				.addClass(HelloLocalBean.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "ejb-jar.xml");
	}
	
	@EJB
	HelloLocalBean bean;
	
	@Test
	public void testEJBBootstrap() {
		assertNotNull(bean);
		assertEquals("Hello world !", bean.getText());
	}

}
