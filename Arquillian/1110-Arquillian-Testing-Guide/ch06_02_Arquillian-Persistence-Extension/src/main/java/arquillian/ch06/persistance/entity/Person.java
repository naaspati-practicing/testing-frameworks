package arquillian.ch06.persistance.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="PEOPLE")
public class Person implements Serializable {
	private static final long serialVersionUID = 533033823345115748L;

	@Id @GeneratedValue(generator="PEOPLE_ID_SEQ")
	@SequenceGenerator(name="PEOPLE_ID_SEQ", sequenceName="PEOPLE_ID_SEQ", allocationSize=5)
	private Integer id;
	
	@Version
	private long version;
	
	private String firstName;
	private String lastName;
	
	public Person() {}
	public Person(String firstname, String lastname) {
		setFirstName(firstname);
		setLastName(lastname);
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	@Override
	public String toString() {
		return "Person [id=" + id + ", version=" + version + ", firstName=" + firstName + ", lastName=" + lastName
				+ "]";
	}
}
