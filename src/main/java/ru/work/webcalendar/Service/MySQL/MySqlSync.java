package ru.work.webcalendar.Service.MySQL;

import com.amazonaws.services.dynamodbv2.xspec.M;
import org.springframework.beans.factory.annotation.Autowired;
import ru.work.webcalendar.DataModel.DAO.SyncDAO;
import ru.work.webcalendar.DataModel.Entity.Sync;
import ru.work.webcalendar.Service.TimeZone;
import ru.work.webcalendar.Service.WebEntityManager;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public class MySqlSync implements SyncDAO {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    WebEntityManager webEntityManager;
    @Autowired
    TimeZone timeZone;
    @Override @Transactional
    public String insertSync(String initiatorID) {
        Sync sync=webEntityManager.getNewSync();
        sync.setInitiatorKey(initiatorID);
        String serverID= UUID.randomUUID().toString();
        sync.setServerId(serverID);
        sync.setTimeStamp(timeZone.getMoscowDateTime(System.currentTimeMillis()));
        sync.setApplyCount(0);
        entityManager.persist(sync);
        return serverID;
    }

    @Override
    public Sync getSync(String serverID) {
        TypedQuery<Sync> query=entityManager.createNamedQuery(MySqlQueries.SYNC_FIND_BY_SERVERID,Sync.class);
        query.setParameter("serverid",serverID);
        return query.getSingleResult();
    }

    @Override
    public Sync getSync(int syncID) {
        return entityManager.find(Sync.class,syncID);
    }

    @Override
    public List<Sync> getSyncAfter(Long timeStamp) {
        Query query=entityManager.createNamedQuery(MySqlQueries.SYNC_FIND_BY_AFTERDATE);
        query.setParameter("datelong",timeStamp);
        return query.getResultList();
    }

    @Override
    public List<Sync> getAll() {
        Query query=entityManager.createNamedQuery(MySqlQueries.SYNC_FINDALL);
        return query.getResultList();
    }

    @Override
    public List<Sync> getSyncByInitiator(String initiatorID) {
        Query query=entityManager.createNamedQuery(MySqlQueries.SYNC_FIND_BY_INITIATOR);
        query.setParameter("initiator",initiatorID);
        return query.getResultList();
    }

    @Override
    public Sync getLast() {
        TypedQuery<Sync> query=entityManager.createNamedQuery(MySqlQueries.SYNC_FIND_LAST,Sync.class);
        query.setMaxResults(1);
        return query.getSingleResult();
    }

    @Override @Transactional
    public void applyCount(String serverID) {
        Query query=entityManager.createNamedQuery(MySqlQueries.SYNC_APPLY_COUNT);
        query.executeUpdate();
    }

    @Override @Transactional
    public void applyCount(int syncID) {
        Sync sync=entityManager.find(Sync.class,syncID);
        int count=sync.getApplyCount()+1;
        sync.setApplyCount(count);
    }

    @Override
    public int getLastIDForZeroAplly(String initiatorID) {
        return getByNamedQuery(initiatorID,MySqlQueries.SYNC_FIND_LAST_FOR_ZEROAPPLY_BYINITIATOR);
    }

    private int getByNamedQuery(String initiatorID,String namedQuery) {
        TypedQuery<Sync> query=entityManager.createNamedQuery(namedQuery,Sync.class);
        query.setMaxResults(1);
        if (!initiatorID.isEmpty()) {
            query.setParameter("initiator", initiatorID);
        }
        int result=0;
        try {
            Sync sync = query.getSingleResult();
            if (sync != null) {
                result= sync.getId();
            }
        }catch (NoResultException e)
        {result= 0;}
        return result;
    }

    @Override
    public int getLastIDForZeroAplly() {
        return getByNamedQuery("",MySqlQueries.SYNC_FIND_LAST_FOR_ZEROAPPLY);
    }
}
