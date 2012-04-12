package com.justcloud.test.model;

import javax.persistence.EntityManager;

import com.justcloud.model.hibernate.jpa.GenericDaoJpa;

public class PersonDaoImpl extends GenericDaoJpa<Person> implements
		PersonDao {

	public PersonDaoImpl() {
		super(Person.class);
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
