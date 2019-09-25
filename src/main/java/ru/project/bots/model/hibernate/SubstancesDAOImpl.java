package ru.project.bots.model.hibernate;

import org.hibernate.Session;
import ru.project.bots.dto.SubstanceDTO;
import ru.project.bots.dto.SubstanceType;
import ru.project.bots.model.dao.SubstancesDAO;
import ru.project.bots.model.entities.SubstanceEntity;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SubstancesDAOImpl extends GenericDAO implements SubstancesDAO {

    public SubstancesDAOImpl() {
        super();
    }

    @Override
    public List<SubstanceDTO> getSubstancesByIDs(Set<Long> ids) {

        return session().createNamedQuery(SubstanceEntity.GET_SUBSTANCES_BY_IDS, SubstanceDTO.class)
                    .setParameter("ids", ids)
                    .getResultList();
    }

    @Override
    public void removeSubstances(Set<Long> ids) {

        session().createNamedQuery(SubstanceEntity.REMOVE_SUBSTANCES)
                .setParameter("ids", ids)
                .executeUpdate();

    }

    @Override
    public long getSubstancesCount() {

        return session().createNamedQuery(SubstanceEntity.COUNT_SUBSTANCES, Long.class)
                    .getResultList().stream().findFirst().orElse((long) 0);

    }

    @Override
    public long getIdByName(String substance) {

        return session().createNamedQuery(SubstanceEntity.GET_SUBSTANCE_ID_BY_NAME, Long.class)
                    .setParameter("title", substance)
                    .getResultList().stream().findFirst().orElse((long) -1);

    }

    @Override
    public List<SubstanceDTO> getAllSubstances() {

        return session().createNamedQuery(SubstanceEntity.GET_ALL_SUBSTANCES, SubstanceDTO.class)
                .getResultList();
    }

    @Override
    public List<Long> getAllSubstanceIDs() {

        return session().createNamedQuery(SubstanceEntity.GET_ALL_SUBSTANCE_IDS, Long.class)
                .getResultList();
    }

    @Override
    public SubstanceType getSubstanceFormByChatId(long chatId) {

        return session().createNamedQuery(SubstanceEntity.GET_SUBSTANCE_FORM_BY_CHAT, SubstanceType.class)
                .setParameter("id", chatId)
                .getResultList().stream().findFirst().orElse(SubstanceType.DUST);
    }

    @Override
    public void updateSubstances(Collection<SubstanceDTO> substances) {

        Session session = session();
        int counter = 0;
        for (SubstanceDTO substance: substances){

            session.saveOrUpdate(new SubstanceEntity(substance.getId(),
                    substance.getName(),
                    substance.getPrice(),
                    substance.getType()));

            if(counter++ % 20 == 0)
                session.flush();
        }

        session.flush();
    }
}
