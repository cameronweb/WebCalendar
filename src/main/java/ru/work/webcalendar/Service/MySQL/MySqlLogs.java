package ru.work.webcalendar.Service.MySQL;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import ru.work.webcalendar.DataModel.DAO.LogsDAO;
import ru.work.webcalendar.DataModel.DAO.SyncDAO;
import ru.work.webcalendar.DataModel.DAO.UsersDAO;
import ru.work.webcalendar.DataModel.DAO.WorkDayDAO;
import ru.work.webcalendar.DataModel.DataModel;
import ru.work.webcalendar.DataModel.Entity.Logs;
import ru.work.webcalendar.DataModel.Entity.User;
import ru.work.webcalendar.DataModel.Entity.WorkDay;
import ru.work.webcalendar.Qualifiers.DAO;
import ru.work.webcalendar.Service.TimeZone;
import ru.work.webcalendar.Service.WebEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public class MySqlLogs implements LogsDAO {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    WebEntityManager webEntityManager;
    @Autowired @DAO(DAO.Type.RDSMYSQL)
    UsersDAO usersDAO;
    @Autowired @DAO(DAO.Type.RDSMYSQL)
    WorkDayDAO workDayDAO;
    @Autowired @DAO(DAO.Type.RDSMYSQL)
    SyncDAO syncDAO;
    @Autowired
    TimeZone timeZone;

    private String EMPTY_VALUE="Empty";
    private String logTypeID="log";
    private String initiator="Cameron";
    @Override @Transactional
    public void writeUser(User user, DataModel.ActionType actionType, String initiatorID) {
        writeToLog(user,actionType,initiatorID, DataModel.ObjectType.User);
    }

    @Override @Transactional
    public void writeWorkDay(WorkDay workDay, DataModel.ActionType actionType, String initiatorID) {
        writeToLog(workDay,actionType,initiatorID, DataModel.ObjectType.WorkDay);

    }

    private void writeToLog(Object objToLog, DataModel.ActionType actionType, String initiatorID, DataModel.ObjectType objectType){
        Logs newItem=webEntityManager.getNewLogs();
        Gson jsonDoc=new Gson();
        int syncID=syncDAO.getLastIDForZeroAplly(initiatorID);
        if (syncID==0){
            String serverID= syncDAO.insertSync(initiatorID);
            syncID=syncDAO.getSync(serverID).getId();
        }
        String jsonObj=jsonDoc.toJson(objToLog);
        newItem.setSyncid(syncID);
        newItem.setTimeStamp(timeZone.getMoscowDateTime(System.currentTimeMillis()));
        newItem.setInitiatorKey(initiatorID);
        newItem.setActionType(actionType);
        newItem.setServerId(UUID.randomUUID().toString());
        newItem.setObjectType(objectType);
        if(actionType== DataModel.ActionType.Insert){
            newItem.setJsonItemNew(jsonObj);
            newItem.setJsonItemOld(EMPTY_VALUE);
        }else if (actionType== DataModel.ActionType.Delete){
            newItem.setJsonItemOld(jsonObj);
            newItem.setJsonItemNew(EMPTY_VALUE);
        }else if(actionType== DataModel.ActionType.Update){
            newItem.setJsonItemNew(jsonObj);
            String oldJson="";
            if (objectType== DataModel.ObjectType.User) {
                User newUser=(User)objToLog;
                User oldUser = usersDAO.getUser(newUser.getServerID());
                oldJson=jsonDoc.toJson(oldUser);
            }else if(objectType== DataModel.ObjectType.WorkDay){
                WorkDay newDay=(WorkDay)objToLog;
                WorkDay oldDay=workDayDAO.getWorkDay(newDay.getServerId());
                oldJson=jsonDoc.toJson(oldDay);
            }
            newItem.setJsonItemOld(oldJson);
        }
        entityManager.persist(newItem);
    }

    @Override
    public List<Logs> getLogs(String syncID) {
        Query query=entityManager.createNamedQuery(MySqlQueries.LOGS_FIND_BY_SYNCID);
        query.setParameter("syncid",syncID);
        return query.getResultList();
    }

    @Override
    public List<Logs> getAll() {
        Query query=entityManager.createNamedQuery(MySqlQueries.LOGS_FIND_ALL_QUERY);
        return query.getResultList();
    }
}
