package com.justcloud.model.hibernate.jpa;

import java.util.List;

import javax.persistence.EntityManager;

import com.justcloud.model.dao.GenericDao;
import com.justcloud.model.domain.DomainObject;

/**
 * @author eduardo at just-cloud.com
 * 
 *         This is the generic dao, uses hibernate over jpa it provides the
 *         basic operations insertion, update, deletion, get all and get by id.
 * 
 *         To use this, entity beans must implent DomainObject and have a Long
 *         id
 * 
 *         Inserts and updates are handled by the save method
 * 
 * @param <T>
 *            Managed class
 */
public class GenericDaoJpa<T extends DomainObject> implements GenericDao<T> {

	private final Class<T> type;

	protected EntityManager entityManager;

	/**
	 * Child classes MUST give the type class
	 * 
	 * @param type
	 *            Managed class
	 */
	protected GenericDaoJpa(final Class<T> type) {
		this.type = type;
	}

	@Override
	public void delete(T object) {
		entityManager.remove(object);
	}

	@Override
	public T get(Long id) {
		return entityManager.find(type, id);
	}

	@Override
	public List<T> getAll() {
		return entityManager.createQuery(
				"select obj from " + type.getName() + " obj", type)
				.getResultList();
	}

	@Override
	public void save(T object) {
		entityManager.persist(object);
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
