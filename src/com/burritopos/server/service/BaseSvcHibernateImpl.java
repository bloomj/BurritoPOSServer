/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.burritopos.server.service;

import org.hibernate.*;
import org.hibernate.cfg.*;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.apache.log4j.*;
import java.util.Date;

/**
 *
 * @author james.bloom
 */
public class BaseSvcHibernateImpl {
        private static Logger dLog = Logger.getLogger(BaseSvcHibernateImpl.class);
        private static final Configuration config = new Configuration();
        private static SessionFactory sessionFactory = null;
        private static ServiceRegistry serviceRegistry = null;

        //Hibernate
        private static SessionFactory getSessionFactory() {
            try {
				if (sessionFactory == null) {
					config.configure("hibernate.cfg.xml");
				    serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
				    sessionFactory = config.buildSessionFactory(serviceRegistry);
				}
            }
            catch(Exception e) {
                dLog.error(new Date() + " | Exception in getSessionFactory: "+e.getMessage());
                System.out.println(new Date() + " | Exception in getSessionFactory: "+e.getMessage());
            }

            return sessionFactory;
	}

        protected static Session getSession() {
            SessionFactory factory = getSessionFactory();
            return (factory != null) ? factory.openSession() : null;
        }
}
