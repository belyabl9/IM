package dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class DaoFactory {
	  
	private static SessionFactory factory;

	private static SessionFactory getSessionFactory() {
		if (factory == null) {
	          Configuration configuration = new Configuration();
	          configuration.configure("hibernate.cfg.xml");
	          StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
	          factory = configuration.buildSessionFactory(ssrb.build());
		}
		return factory;
	}
	
	private Session createSession() {
		return getSessionFactory().openSession();
	}
	  
	public static MessagesDAO getMessagesDAO() {
		return new MessagesDAO(getSessionFactory());
	}
	
	public static UsersDAO getUsersDAO() {
		return new UsersDAO(getSessionFactory());
	}
	
	public static DepartmentsDAO getDepartmentsDAO() {
		return new DepartmentsDAO(getSessionFactory());
	}
	
}
