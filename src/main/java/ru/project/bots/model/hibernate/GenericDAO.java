package ru.project.bots.model.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.resource.transaction.spi.TransactionStatus;

public abstract class GenericDAO{

    private final SessionFactory sessionFactory;

    public GenericDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public Session session(){
        return sessionFactory.getCurrentSession();
    }

    public void beginTx() throws IllegalStateException{
        Session session = session();
        if(session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE)
            session.beginTransaction();
    }

    public void commitTx(){
        Session session = session();
        if(session.getTransaction().getStatus() == TransactionStatus.ACTIVE)
            session.getTransaction().commit();
    }

    public void flush(){
        Session session = session();
        if(session.getTransaction().getStatus() == TransactionStatus.ACTIVE)
            session.flush();
    }

    public void rollback(){
        Session session = session();
        if(session.getTransaction().getStatus() == TransactionStatus.ACTIVE)
            session.getTransaction().rollback();
    }
}
