package org.red5.fi6en.userservice;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.red5.fi6en.util.HibernateUtil;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.slf4j.Logger;

public class DatabaseOperation {
	private static Logger log = Red5LoggerFactory.getLogger(DatabaseOperation.class,
			"fi6en");

	IScope appScope;

	SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	public void saveToDatabase(Object[] params) {
		log.info("saving user informations to the fi6en database under users table");
		IConnection conn = Red5.getConnectionLocal();
		appScope = conn.getScope();

		String username = params[0].toString();
		String password = params[1].toString();
		String firstname = params[2].toString();
		String lastname = params[3].toString();
		String email = params[4].toString();
		String location = params[5].toString();

		/*for (int i = 0; i < params.length; i++) {
			log.info(i + " : " + params[i].toString() + "\n");
		}*/

		Session session = sessionFactory.openSession();
		try {
			Transaction tx = session.beginTransaction();
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			user.setFirstname(firstname);
			user.setLastname(lastname);
			user.setEmail(email);
			user.setLocation(location);
			Long userId = (Long) session.save(user);
			log.info("users id : " + userId);
			tx.commit();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		} finally {
			session.close();
		}

	}//saveToDatabase

}
