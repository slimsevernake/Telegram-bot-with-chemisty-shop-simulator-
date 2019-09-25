package ru.project.bots.logic.services;

import org.hibernate.Session;
import ru.project.bots.dto.SubstanceDTO;
import ru.project.bots.model.configs.SubstancesConfigFile;
import ru.project.bots.model.dao.SubstancesDAO;
import ru.project.bots.model.hibernate.GenericDAO;
import ru.project.bots.model.hibernate.SubstancesDAOImpl;
import ru.project.bots.model.logs.SimpleLogger;

import java.util.*;

public class SubstanceUpdaterService implements Runnable {

    private boolean updating = true;
    private final SimpleLogger logger;
    private final String substancesFilePath;

    public SubstanceUpdaterService(SimpleLogger logger, String substancesFilePath) {
        this.logger = logger;
        this.substancesFilePath = substancesFilePath;
        new Thread(this).start();
    }

    @Override
    public void run() {


        while (updating){
            SubstancesDAO dao = null;
            try {

                dao = new SubstancesDAOImpl();

                Session session = ((GenericDAO) dao).session();
                if(!session.getTransaction().isActive())
                    session.beginTransaction();

                final List<Long> databaseSubstances = dao.getAllSubstanceIDs();
                final Set<Long> extraSubstances = new HashSet<>();
                final Map<Long, SubstanceDTO> updatedSubstances = new SubstancesConfigFile(logger, substancesFilePath)
                        .uploadSubstances();

                for(long old: databaseSubstances){
                    if(!updatedSubstances.containsKey(old))
                        extraSubstances.add(old);
                }
                if(extraSubstances.size() > 0)
                    dao.removeSubstances(extraSubstances);

                dao.updateSubstances(updatedSubstances.values());

                session.getTransaction().commit();

                logger.log("Substances updated!");
                /*
                * Update every day
                * */

                Thread.sleep(86_400_000);
            }catch (Exception e){
                logger.log(e);
                if(dao != null) {
                    ((GenericDAO) dao).rollback();
                }
                try {
                    Thread.sleep(3_600_000);
                } catch (InterruptedException interrupted) {
                    logger.log(interrupted);
                    new Thread(this).start();
                    Thread.currentThread().interrupt();
                }
            }
        }


    }

    public boolean isUpdating() {
        return updating;
    }

    public void setUpdating(boolean updating) {
        this.updating = updating;
    }
}
