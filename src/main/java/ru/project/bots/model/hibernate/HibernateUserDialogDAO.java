package ru.project.bots.model.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import ru.project.bots.dto.DialogLevel;
import ru.project.bots.dto.FormDTO;
import ru.project.bots.dto.SmallFormDTO;
import ru.project.bots.model.dao.UserDialogDAO;
import ru.project.bots.model.entities.CityEntity;
import ru.project.bots.model.entities.DialogsEntity;
import ru.project.bots.model.entities.FormEntity;
import ru.project.bots.model.entities.SubstanceEntity;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class HibernateUserDialogDAO extends GenericDAO implements UserDialogDAO {

    public HibernateUserDialogDAO() {
        super();
    }

    @Override
    public DialogLevel getDialogLevel(long chatId) {

        return session().createNamedQuery(DialogsEntity.GET_DIALOG_STATUS, DialogLevel.class)
                    .setParameter("id", chatId)
                    .getResultList().stream().findFirst().orElse(DialogLevel.DIALOG_START);
    }

    @Override
    public FormDTO getForm(long chatId) {

        Session session = session();
        return session.createNamedQuery(FormEntity.GET_LAST_FORM, FormDTO.class)
                    .setParameter("dialog", session.load(DialogsEntity.class, chatId))
                    .setMaxResults(1)
                    .getResultList().stream().findFirst().orElse(null);

    }

    @Override
    public List<SmallFormDTO> existActiveForm(long chatId) {

        Session session = session();
        return session.createNamedQuery(FormEntity.GET_ACTIVE_FORM, SmallFormDTO.class)
                .setParameter("dialog", session.load(DialogsEntity.class, chatId))
                .setMaxResults(2)
                .getResultList();
    }

    @Override
    public boolean updateDialogLevel(long chatId, DialogLevel level) {
        try {
            
            return session().createNamedQuery(DialogsEntity.UPDATE_DIALOG_STATUS)
                    .setParameter("status", level)
                    .setParameter("id", chatId)
                    .executeUpdate() != 0;

        }catch (HibernateException e){
            return false;
        }
    }

    @Override
    public boolean startDialog(long chatId) {

        try {

            Session session = session();

            DialogsEntity dialog = new DialogsEntity(chatId, DialogLevel.DIALOG_START);

            FormEntity formEntity = new FormEntity();
            formEntity.setActive(true);
            formEntity.setDialog(dialog);

            List<SmallFormDTO> forms = existActiveForm(chatId);
            if(forms.size() == 0) {
                formEntity.setUuid(6631);
                session.save(dialog);
                session.save(formEntity);
                return true;
            }else if(!forms.get(0).isActive()){
                formEntity.setUuid(forms.get(0).getUuid()+ 1 +new Random().nextInt(3));
                session.save(formEntity);
            }

            return updateDialogLevel(chatId, DialogLevel.DIALOG_START);

        }catch (HibernateException e){
            return false;
        }

    }

    @Override
    public boolean endDialog(long chatId) {

        try {

           return updateDialogLevel(chatId, DialogLevel.DIALOG_PAYMENT) && completeForm(chatId);

        }catch (HibernateException e){
            return false;
        }
    }

    @Override
    public List<FormDTO> getOrders(long chatId) {

        Session session = session();
        return session.createNamedQuery(FormEntity.GET_FORM, FormDTO.class)
                .setParameter("dialog", session.load(DialogsEntity.class, chatId))
                .setParameter("active", Boolean.FALSE)
                .getResultList();
    }

    @Override
    public boolean completeForm(long chatId) {

            Session session = session();
            return session.createNamedQuery(FormEntity.COMPLETE_FORM)
                    .setParameter("dialog", session.load(DialogsEntity.class, chatId))
                    .executeUpdate() != 0;

    }

    @Override
    public boolean updateWeight(long chatId, int value) {


        try {
            Session session = session();

            boolean updated = session.createNamedQuery(FormEntity.UPDATE_WEIGHT)
                    .setParameter("weight", value)
                    .setParameter("dialog", session.load(DialogsEntity.class, chatId))
                    .executeUpdate() != 0;

            updateDialogLevel(chatId, DialogLevel.DIALOG_VALID);

            return updated;
        }catch (HibernateException e){
            return false;
        }
    }

    @Override
    public boolean updateSubstance(long chatId, long substanceId) {

        try {

            Session session = session();

            boolean updated = session.createNamedQuery(FormEntity.UPDATE_SUBSTANCE)
                    .setParameter("substance", session.load(SubstanceEntity.class, substanceId))
                    .setParameter("dialog", session.load(DialogsEntity.class, chatId))
                    .executeUpdate() != 0;

            updateDialogLevel(chatId, DialogLevel.DIALOG_WEIGHT_SELECTION);
            return updated;
        }catch (HibernateException e){
            return false;
        }
    }


    @Override
    public long getUuid(long chatId) {
        

        try {
            Session session = session();

            return session.createNamedQuery(FormEntity.GET_UUID, Long.class)
                    .setParameter("dialog", session.load(DialogsEntity.class, chatId))
                    .getResultList().stream().findFirst().orElse((long) 6631);

        }catch (HibernateException e){
            return 6631;
        }
    }

    @Override
    public boolean updateCity(long chatId, String name) {

        try {
            Session session = session();

            boolean updated = session.createNamedQuery(FormEntity.UPDATE_CITY)
                    .setParameter("city", name)
                    .setParameter("dialog", session.load(DialogsEntity.class, chatId))
                    .executeUpdate() != 0;

            updateDialogLevel(chatId, DialogLevel.DIALOG_PRICE_LIST);

            return updated;
        }catch (HibernateException e){
            return false;
        }
    }


    @Override
    public String getCityName(long chatId) {

        Session session = session();

        return session.createNamedQuery(FormEntity.GET_CITY_NAME, String.class)
                    .setParameter("dialog", session.load(DialogsEntity.class, chatId))
                    .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public boolean existCity(String name) {

        return session().createNamedQuery(CityEntity.GET_CITY_BY_NAME, CityEntity.class)
                    .setParameter("title", name)
                    .getResultList().stream().findFirst().orElse(null) != null;

    }
}
