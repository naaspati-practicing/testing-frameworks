package arquillian.ch06.persistance;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import arquillian.ch06.persistance.entity.Person;

@Stateless
public class PersonDAO {
	
	@PersistenceContext
	private EntityManager em;
	
	public void deleteAllPeople() {
		em.createQuery("delete from Person p").executeUpdate();
	}
	public Person persist(Person p) {
		em.persist(p);
		return p;
	}
	public Person merge(Person p) {
		em.merge(p);
		return p;
	}
	public Person find(int id) {
		return em.find(Person.class, id);
	}
	public List<Person> findAll() {
		return em.createQuery("select p from Person p order by p.id", Person.class).getResultList();
	}
}
