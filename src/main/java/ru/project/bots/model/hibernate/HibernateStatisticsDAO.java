package ru.project.bots.model.hibernate;

import ru.project.bots.model.dao.StatisticsDAO;
import ru.project.bots.model.entities.DialogsEntity;
import ru.project.bots.model.entities.FormEntity;

public class HibernateStatisticsDAO extends GenericDAO implements StatisticsDAO {

    @Override
    public long countDialogs() {

        return session().createNamedQuery(DialogsEntity.COUNT_DIALOGS, Long.class)
                .getResultList().stream().findFirst().orElse((long) 0);
    }

    @Override
    public long countOrders() {

        return session().createNamedQuery(FormEntity.COUNT_ORDERS, Long.class)
                .getResultList().stream().findFirst().orElse((long) 0);
    }

}
