package com.tad.arquillian.chapter7.drone;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

import com.tad.arquillian.chapter7.DeploymentUtils;

@RunWith(Arquillian.class)
@RunAsClient
public class ArquillianDroneTest {
	
	/*
	 * The testable=false argument for deployment forces Arquillian to run in client mode, that is not inside of the server where the application is deployed. 
	 */
	@Deployment(testable=false)
    public static WebArchive createChapter7Archive() {
        return DeploymentUtils.create();
    }
	
	@ArquillianResource
	private URL url;

}
