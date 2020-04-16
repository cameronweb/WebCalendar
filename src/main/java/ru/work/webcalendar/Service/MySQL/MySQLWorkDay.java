package ru.work.webcalendar.Service.MySQL;

import ru.work.webcalendar.DataModel.DAO.WorkDayDAO;
import ru.work.webcalendar.DataModel.Entity.WorkDay;
import ru.work.webcalendar.Qualifiers.LoggableWorkDay;
import software.amazon.awssdk.utils.Pair;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public class MySQLWorkDay implements WorkDayDAO {
    @PersistenceContext
    EntityManager entityManager;
    @Override @Transactional @LoggableWorkDay
    public Pair<String, Boolean> insertWorkDay(WorkDay workDay) {
        if(!checkExists(workDay.getWorkdate()))
        {
            String serverID= UUID.randomUUID().toString();
            workDay.setServerId(serverID);
            try {
             entityManager.persist(workDay);
                return  Pair.of(serverID,true);
            }catch (Exception e){
                return Pair.of(e.getMessage(),false);
            }
        }else return Pair.of("The Date for WorkDate already exists",false);
    }

    @Override @Transactional @LoggableWorkDay
    public int updateWorkDay(String serverID, WorkDay workDay) {
        WorkDay dbDay=getWorkDay(serverID);
        dbDay.setUserid(workDay.getUserid());
        dbDay.setWorkdate(workDay.getWorkdate());
        dbDay.setSaturday(workDay.isSaturday());
        try{
            entityManager.merge(dbDay);
            return 0;
        }catch (Exception e){
            return e.hashCode();
        }
    }

    @Override @Transactional @LoggableWorkDay
    public int deleteWorkDay(String serverID) {
        Query deleteQuery=entityManager.createNamedQuery(MySqlQueries.WORKDAY_DELETE_BYSERV_ID);
        deleteQuery.setParameter("serverid",serverID);
        try {
            return deleteQuery.executeUpdate();
        }catch (Exception e){
            return e.hashCode();
        }
    }

    @Override @Transactional @LoggableWorkDay
    public int updateWorkDay(WorkDay workDay) {
        try{
            entityManager.merge(workDay);
            return 0;
        }catch (Exception e){
            return e.hashCode();
        }
    }

    @Override @Transactional @LoggableWorkDay
    public int deleteWorkDay(int id) {
        Query deleteQuery=entityManager.createNamedQuery(MySqlQueries.WORKDAY_DELETE_BY_ID);
        deleteQuery.setParameter("id",id);
        try {
            return deleteQuery.executeUpdate();
        }catch (Exception e){
            return e.hashCode();
        }
    }

    @Override
    public WorkDay getWorkDay(int id) {
        return entityManager.find(WorkDay.class,id);
    }

    @Override
    public WorkDay getWorkDay(String serverid) {
        TypedQuery<WorkDay> query=entityManager.createNamedQuery(MySqlQueries.WORKDAY_FIND_BYSERVID,WorkDay.class);
        query.setParameter("serverid",serverid);
        WorkDay workDay=query.getSingleResult();
        return workDay;
    }

    @Override
    public List<WorkDay> getWorkDays() {
        Query findAllQuery=entityManager.createNamedQuery(MySqlQueries.WORKDAY_FINDALL);
        List<WorkDay> workDayList=findAllQuery.getResultList();
        return workDayList;
    }

    @Override
    public List<WorkDay> getByUser(String userId, Boolean sorted) {
        Query findByUserQuery=entityManager.createNamedQuery(MySqlQueries.WORKDAY_FIND_BYUSERID);
        findByUserQuery.setParameter("userid",userId);
        List<WorkDay> workDayList=findByUserQuery.getResultList();
        return workDayList;
    }

    @Override
    public List<WorkDay> getUserNextWorkDay(String user_id, long afterDate) {
        Query findByUserQuery=entityManager.createNamedQuery(MySqlQueries.WORKDAY_FIND_BYUSERID_FORNEXT_DATE);
        findByUserQuery.setParameter("userid",user_id);
        findByUserQuery.setParameter("datelong",afterDate);
        List<WorkDay> workDayList=findByUserQuery.getResultList();
        return workDayList;
    }

    @Override
    public List<WorkDay> getFutureWorkDays() {
        Query findByUserQuery=entityManager.createNamedQuery(MySqlQueries.WORKDAY_FIND_FUTURE_DATE);
        findByUserQuery.setParameter("datelong",System.currentTimeMillis());
        List<WorkDay> workDayList=findByUserQuery.getResultList();
        return workDayList;
    }

    @Override
    public boolean checkExists(long date) {
        TypedQuery<WorkDay> query=entityManager.createNamedQuery(MySqlQueries.WORKDAY_CHECK_EXISTS,WorkDay.class);
        return query.getFirstResult()==0 ? false:true;
    }
}
