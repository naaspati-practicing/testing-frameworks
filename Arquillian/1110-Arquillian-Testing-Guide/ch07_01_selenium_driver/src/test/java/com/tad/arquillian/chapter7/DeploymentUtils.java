package com.tad.arquillian.chapter7;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import com.tad.arquillian.chapter7.model.Addresses;
import com.tad.arquillian.chapter7.model.People;
import com.tad.arquillian.chp7.controller.JaxRsActivator;
import com.tad.arquillian.chp7.controller.PeopleController;
import com.tad.arquillian.chp7.controller.PeopleResource;
import com.tad.arquillian.chp7.dao.PeopleDAO;

public class DeploymentUtils {
	public static WebArchive create() {
		WebArchive archive = ShrinkWrap.create(WebArchive.class)
				.addClasses(Addresses.class,People.class,
						PeopleDAO.class,PeopleController.class,
						PeopleResource.class,JaxRsActivator.class)
				.addAsResource("META-INF/persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml")
				.addAsWebInfResource("WEB-INF/web.xml","web.xml");	

		for (String s : new String[] {"editPerson.xhtml","graphenePeople.html","jquery.js","people.xhtml"}) 
			archive.addAsWebResource(s, s);

		return archive;
	}
}
