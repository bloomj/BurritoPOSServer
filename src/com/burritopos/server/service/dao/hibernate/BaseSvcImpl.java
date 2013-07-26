package com.burritopos.server.service.dao.hibernate;

import java.io.File;

import org.hibernate.*;
import org.hibernate.cfg.*;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.apache.log4j.*;

/**
 *
 * @author james.bloom
 */
public class BaseSvcImpl {
	private static Logger dLog = Logger.getLogger(BaseSvcImpl.class);
	private static final Configuration config = new Configuration();
	private static SessionFactory sessionFactory = null;
	private static ServiceRegistry serviceRegistry = null;

	//Hibernate
	private static SessionFactory getSessionFactory() {
		try {
			if (sessionFactory == null) {
				config.configure("hibernate" + File.separator + "hibernate.cfg.xml");
				serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
				sessionFactory = config.buildSessionFactory(serviceRegistry);
			}
		}
		catch(Exception e) {
			dLog.error("Exception in getSessionFactory: ", e);
		}

		return sessionFactory;
	}

	protected static Session getSession() {
		SessionFactory factory = getSessionFactory();
		return (factory != null) ? factory.openSession() : null;
	}
}
