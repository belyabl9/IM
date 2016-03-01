package dao;

import java.util.Collection;
import java.util.Map;

import javax.persistence.PersistenceException;

import model.server.DomainSuperClass;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class GenericDAO<T extends DomainSuperClass> {
	
	private SessionFactory factory;
	protected Class<T> persistentClass;
	
	private static final String QUERY_SELECT_ALL = "from %s";
	
	public GenericDAO(Class<T> persistentClass, SessionFactory factory) {
		super();
		this.persistentClass = persistentClass;
		this.factory = factory;
	}
	

	public Collection<T> findAll() throws PersistenceException {
		return executeQuery(String.format(QUERY_SELECT_ALL, persistentClass.getSimpleName()), false, null);
	}
	
	public T findById(long id) throws PersistenceException {
		Session session = factory.openSession();
		// Begin a new local transaction so that we can persist a new entity
		session.getTransaction().begin();
		T savedEntity = null;
		try {
			// Finding entity by its id
			savedEntity = (T) session.get(persistentClass, id);
			// Commit the transaction, which will cause the entity to
			// be stored in the database
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			// Catching exceptions and rollback of transaction
			session.getTransaction().rollback();
		} finally {
			// It is always good practice to close the EntityManager so that
			// resources are conserved.
			session.close();
		}
		return savedEntity;
	}
	
	public void delete(T entity) throws PersistenceException {
		// Checking for null parameters
		if (entity == null) {
			throw new PersistenceException(
					"Entity for deleting cannot be null!");
		}
		Session session = factory.openSession();

		// Begin a new local transaction so that we can persist a new entity
		session.getTransaction().begin();

		try {
			// Removing entity
			session.delete(entity);
			
			// Commit the transaction, which will cause the entity to
			// be stored in the database
			session.getTransaction().commit();

		} catch (Exception e) {

			
			// Catching exceptions and rollback of transaction
			session.getTransaction().rollback();

			// throwing exception further
			throw new PersistenceException(e);

		} finally {
			// It is always good practice to close the EntityManager so that
			// resources are conserved.
			session.close();
		}
	}
	
	public T save(T entity) throws PersistenceException {
		// Checking for null parameters
		if (entity == null) {
			throw new PersistenceException("Entity for saving cannot be null!");
		}
			Session session = factory.openSession();
			// Begin a new local transaction so that we can persist a new entity
			session.getTransaction().begin();
			T savedEntity = null;
		try {
			// Creating new entity, if id is null or merging the detached object
			// with the current state
			System.out.println("entity id: " + entity.getId());
		if (entity.getId() == 0) {
			session.save(entity);
			savedEntity = entity;
		} else {
			savedEntity = (T) session.merge(entity);
		}
			// Commit the transaction, which will cause the entity to
			// be stored in the database
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			// Catching exceptions and rollback of transaction
			session.getTransaction().rollback();
			// throwing exception further
			throw new PersistenceException(e);
		} finally {
			// It is always good practice to close the EntityManager so that
			// resources are conserved.
			session.close();
		}
			return savedEntity;
		}
	
		protected <REZ> REZ executeQuery(String queryOrQueryName, 
				boolean singleResult, Map<String, Object> args) throws PersistenceException {
			// Creating entity manager to work with entity
			// Begin a new local transaction so that we can persist a new entity
			Session session = factory.openSession();
			session.getTransaction().begin();
			REZ result;
			Query q;
			try {
				q = session.createQuery(queryOrQueryName);
				// Setting query parameters
				
				if (args != null)
					for (Map.Entry<String, Object> pair : args.entrySet()) {
						q.setParameter(pair.getKey(), pair.getValue());
					}
				// Executing query
				if (singleResult) {
					result = (REZ) q.uniqueResult();
				} else {
					result = (REZ) q.list();
				}
				// Commit the transaction, which will cause the entity to
				// be stored in the database
				session.getTransaction().commit();
			} catch (Exception e) {
				// Catching exceptions and rollback of transaction
				session.getTransaction().rollback();
				// throwing exception further
				throw new PersistenceException(e);
			} finally {
				// It is always good practice to close the EntityManager so that
				// resources are conserved.
				session.close();
			}
			return result;
		}
	
}
