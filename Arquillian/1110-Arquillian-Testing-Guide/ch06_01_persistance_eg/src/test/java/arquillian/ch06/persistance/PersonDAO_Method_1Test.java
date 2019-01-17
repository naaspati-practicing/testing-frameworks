package arquillian.ch06.persistance;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import arquillian.ch06.persistance.entity.Person;

@RunWith(Arquillian.class)
public class PersonDAO_Method_1Test {

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
	
	@Before
	public void setupTestData(){
		dao.deleteAllPeople();
		
		dao.persist(new Person("sameer", "veda"));
		dao.persist(new Person("jameel", "ahmad"));
	}
	@Test
	public void test() {
		List<Person> allPeople = dao.findAll();
		
		assertEquals(2, allPeople.size());
		
		assertPerson(allPeople.get(0), "sameer", "veda");
		assertPerson(allPeople.get(1), "jameel", "ahmad");
		
	}
	private void assertPerson(Person person, String firstname, String lastname) {
		assertEquals(person.getFirstName(), firstname);
		assertEquals(person.getLastName(), lastname);
	}
}
