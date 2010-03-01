package org.red5.fi6en.userservice;

import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.red5.fi6en.util.HibernateUtil;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.IScope;
import org.slf4j.Logger;

public class DatabaseOperation {
	private static Logger log = Red5LoggerFactory.getLogger(
			DatabaseOperation.class, "fi6en");

	IScope appScope;

	static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	/**
	 * store user information to the database
	 * @param params user information object
	 * @return void
	 */
	public void saveToDatabase(Object[] params) {
		log
				.info("saving user informations to the fi6en database under users table");
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
		Transaction tx = session.beginTransaction();
		try {
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
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			log.error("error during database operation for user : "
					+ e.getMessage());
		} finally {
			session.close();
		}

	}//saveToDatabase

	/**
	 * method to fetch the password from fi6en database
	 * @param username username for the password
	 * @return String
	 */
	public String fetchUserPassword(String username) {
		Session session = sessionFactory.openSession();
		String password = null;
		try {
			Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.eq("username", username));
			List users = criteria.list();
			/*if (users == null)
				return "";
			else {*/
			Iterator iterator = users.iterator();
			while (iterator.hasNext()) {
				User user = (User) iterator.next();
				log.info("username : " + user.getUsername());
				log.info("password : " + user.getPassword());
				log.info("email : " + user.getEmail() + "\n");
				password = user.getPassword();
			}
			//}
		} catch (Exception e) {
			log
					.error("An error occured while fetching password data from database "
							+ e.getMessage());
			System.err.println("error fetching password : " + e.getMessage());
		}
		return password;
	}

	/**
	 * compares password that is coming from client and password in the database
	 * @param username username
	 * @param password password
	 * @return boolean;
	 */
	public boolean comparePasswords(String username, String password) {
		String databasePassword = fetchUserPassword(username);
		if (databasePassword != null && databasePassword.equals(password)) {
			log.info("authentication successful");
			return true;
		} else {
			log.error("authentication failed");
			return false;
		}
	}

	public boolean checkDuplicateUser(String username) {
		log.info("check duplicate username method, username : {}", username);
		Session session = sessionFactory.openSession();
		boolean isDuplicate= true;
		try {
			Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.eq("username", username));
			List users = criteria.list();
			if (users.isEmpty()) {
				log.info("username : {} is available to use",username);
				return false;
			} else {
				log.error("username : {} is already in use",username);
				return true;
			}
		} catch (Exception e) {
			log.error("An error occured while checking duplicate username "
					+ e.getMessage());
		}
		return isDuplicate;
	}
}