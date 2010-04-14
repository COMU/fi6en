package org.red5.fi6en.util;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Hibernate utility class to get and set the hibernate session factory
 * @author cem (cemosonmez@gmail.com)
 *
 */
public class HibernateUtil {

	private static SessionFactory sessionFactory;

	static {
		try {
			//By default it will look for hibernate.cfg.xml in the class path
			sessionFactory = new AnnotationConfiguration().configure()
					.buildSessionFactory();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static void shutdown() {
		//Close caches and connection pool
		getSessionFactory().close();
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void setSessionFactory(SessionFactory sessionFactory) {
		HibernateUtil.sessionFactory = sessionFactory;
	}
}
