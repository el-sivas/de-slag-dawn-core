package de.slag.dawn.core.persist;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class Credential extends PersistBean {
	
	@Basic
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Credential [name=" + name + ", getId()=" + getId() + "]";
	}
	
	

}
