package arquillian.ch06.persistance;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

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

// unrecommened method
@RunWith(Arquillian.class)
public class PersonDAO_Method_2_Test {

	@Deployment
	public static Archive<?> deployment(){
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
				.addClasses(Person.class, PersonDAO.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsManifestResource("test-persistence.xml", "persistence.xml");
		
		Logger.getGlobal().info("\n"+archive.toString(true));
		return archive;
	}
	
	@PersistenceContext
	EntityManager em;
	
	@Inject
	UserTransaction utx;
	
	@EJB
	private PersonDAO dao;
	
	@Before
	public void setupTestData() throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		utx.begin();
		em.joinTransaction();
		em.createQuery("delete from Person p").executeUpdate();
		
		em.persist(new Person("sameer", "veda"));
		em.persist(new Person("jameel", "ahmad"));
		
		utx.commit();
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
