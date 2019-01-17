package example

import javax.inject.Inject

import org.jboss.arquillian.container.test.api.Deployment
import org.jboss.arquillian.spock.ArquillianSputnik
import org.jboss.shrinkwrap.api.ShrinkWrap
import org.jboss.shrinkwrap.api.asset.EmptyAsset
import org.jboss.shrinkwrap.api.spec.JavaArchive
import org.junit.runner.RunWith

import spock.lang.Specification

@RunWith(ArquillianSputnik)
class PersonSpec extends Specification {
		
	@Deployment
	static JavaArchive create() {
		ShrinkWrap.create(JavaArchive)
		.addClasses(Person, LoggerProducer)
		.addAsManifestResource(EmptyAsset.INSTANCE, 'beans.xml')
	}
	
	@Inject Person person
	
		def "person should not be null"() {
			expect:
			person != null
		}

}
