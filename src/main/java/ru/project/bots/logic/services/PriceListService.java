package ru.project.bots.logic.services;

import org.hibernate.HibernateException;
import ru.project.bots.model.configs.SubstancesConfigFile;
import ru.project.bots.model.dao.SubstancesDAO;
import ru.project.bots.model.hibernate.GenericDAO;
import ru.project.bots.model.hibernate.SubstancesDAOImpl;
import ru.project.bots.model.logs.SimpleLogger;

import java.util.*;
import java.util.stream.Collectors;

public class PriceListService implements Runnable{

    private static List<Set<Long>> priceLists;

    private final SimpleLogger logger;

    private List<Long> requiredIDS;
    private static int listsNum;
    private boolean updating = true;

    public PriceListService(int listsNum, List<Long> requiredIDS, SimpleLogger logger) {

        this.logger = logger;
        this.requiredIDS = requiredIDS;
        PriceListService.listsNum = listsNum;
        priceLists = new ArrayList<>(listsNum);

        new Thread(this).start();
    }

    public Set<Long> getPriceList(String city){
        synchronized (priceLists){
            return priceLists.get(getHash(city));
        }
    }

    private Set<Long> generateSet(int size){

        Random random = new Random();

        Set<Long> set = new HashSet<>(requiredIDS);
        set.addAll(random.ints(random.nextInt(5) + 5, 17, size)
                .mapToObj(i-> (long) (i + 1))
                .collect(Collectors.toList()));

        return Collections.synchronizedSet(set);
    }

    private int getHash(String str){
        int res = 0;
        for (int i = 0; i < str.length(); i++) {
            res = (res + ((str.charAt(i) * 31) << 4));
        }
        return res % listsNum;
    }


    /*
    * Update lists every fixed time
    * */
    @Override
    public void run() {

        while (updating){
            SubstancesDAO dao = null;
            try {

                dao = new SubstancesDAOImpl();

                GenericDAO genericDAO = (GenericDAO) dao;
                genericDAO.beginTx();

                int size = (int) dao.getSubstancesCount();

                genericDAO.commitTx();

                List<Set<Long>> tempMap = new ArrayList<>(listsNum);

                for (int i = 0; i < listsNum; i++) {
                    tempMap.add(generateSet(size));
                }

                PriceListService.priceLists = tempMap;
                logger.log("Price lists updated!");
                /*
                 * Sleep 3 days
                 * */

                Thread.sleep(259_200_000L);
            }catch (HibernateException e) {
                if(dao == null){
                    try {
                        Thread.sleep(3_600_000);
                    }catch (InterruptedException exc){
                        logger.log(exc);
                        new Thread(this).start();
                        Thread.currentThread().interrupt();
                    }
                }else{
                    ((GenericDAO) dao).rollback();
                    logger.log(e);
                }

            }catch (Exception e){
                logger.log(e);
                new Thread(this).start();
                Thread.currentThread().interrupt();
            }

        }

    }

    public void setUpdating(boolean updating) {
        this.updating = updating;
    }
}
