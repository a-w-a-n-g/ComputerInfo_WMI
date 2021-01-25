package com.awang.hibernate.session;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**

* @author awang
* @version 1.0
* @date 2020年12月29日 下午2:47:29
* 
*/

public class HibernateSessionFactory {

	private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";
	private static final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<Session>();
	private static Configuration configuration = new Configuration();
	private static SessionFactory sessionFactory;
	private static String configFile = CONFIG_FILE_LOCATION;

	static {
		try {
			configuration.configure();
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
			sessionFactory = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

	private HibernateSessionFactory() {

	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void rebuildSessionFactory() {
		synchronized (sessionFactory) {
			try {
				configuration.configure(configFile);
				StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties()).build();
				sessionFactory = configuration.buildSessionFactory(standardServiceRegistry);
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
	}

	public static Session getSession() {
		Session session = (Session) sessionThreadLocal.get();
		try {
			if (session == null || !session.isOpen()) {
				if (sessionFactory == null) {
					rebuildSessionFactory();
				}
				session = (sessionFactory != null) ? sessionFactory.openSession() : null;
				sessionThreadLocal.set(session);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return session;
	}

	public static void closeSession() {
		Session session = (Session) sessionThreadLocal.get();
		sessionThreadLocal.set(null);
		try {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

	public static void setConfigFile(String configFile) {
		HibernateSessionFactory.configFile = configFile;
		sessionFactory = null;
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

}
