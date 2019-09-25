package ru.project.bots.model.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private final static SessionFactory sessionFactory;

    static {

        sessionFactory = new Configuration()
                .configure(HibernateUtil.class.getClassLoader().getResource("hibernate.cfg.xml"))
                .buildSessionFactory();

    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
