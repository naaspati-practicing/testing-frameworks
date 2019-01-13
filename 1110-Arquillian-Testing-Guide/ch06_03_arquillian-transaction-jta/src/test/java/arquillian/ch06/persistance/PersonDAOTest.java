package arquillian.ch06.persistance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import arquillian.ch06.persistance.entity.Person;

/*
 * We use @InSequence to order the tests, to ensure that the creation occurs before the
 * second test. The frst test creates a person and saves them; we assert this occurred by
 * checking that the Id feld is populated in the result. The second test though looks to
 * fnd all possible people and checks that none of them are the created person. This test
 * passing will show that the results were rolled back in the database. This would help
 * a bit with automatically cleaning up the code that was created in your test
 *
 */

/*
 * if deployement failed, try cleaning C:\Users\anie\_SERVER\glassfish4\glassfish\domains\domain1\applications
 */

@RunWith(Arquillian.class)
public class PersonDAOTest {

	@Deployment
	public static Archive<?> deployment(){
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
				.addClasses(Person.class, PersonDAO.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsManifestResource("test-persistence.xml", "persistence.xml");
		
		Logger.getGlobal().info("\n"+archive.toString(true));
		return archive;
	}
	
	@EJB
	private PersonDAO dao;
	
	@Test
	@InSequence(1)
	@Transactional(TransactionMode.ROLLBACK)
	public void setupTestData(){
		Person p = new Person("aam", "achar");
		assertNull(p.getId());
		
		dao.persist(p);
		
		assertNotNull(p.getId());
	}
	
	@Test
	@InSequence(2)
	public void test() {
		List<Person> allPeople = dao.findAll();
		assertTrue(allPeople.isEmpty());
	}
	
}
