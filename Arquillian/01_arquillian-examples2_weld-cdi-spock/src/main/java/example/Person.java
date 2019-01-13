package example;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;

public class Person {
	private String name;
	private String surname;
	
	@Inject
	Logger logger;
	
	@PostConstruct
	public void init() {
		logger.info( "A new instance of person is created with hashcode {}.", hashCode() );
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
}
