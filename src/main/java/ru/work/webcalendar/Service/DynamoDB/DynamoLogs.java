package ru.work.webcalendar.Service.DynamoDB;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import ru.work.webcalendar.DataModel.DAO.LogsDAO;
import ru.work.webcalendar.DataModel.DAO.SyncDAO;
import ru.work.webcalendar.DataModel.DAO.UsersDAO;
import ru.work.webcalendar.DataModel.DAO.WorkDayDAO;
import ru.work.webcalendar.DataModel.DataModel;
import ru.work.webcalendar.DataModel.Entity.Logs;
import ru.work.webcalendar.DataModel.Entity.User;
import ru.work.webcalendar.DataModel.Entity.WorkDay;
import ru.work.webcalendar.Service.WebEntityManager;
import ru.work.webcalendar.Service.TimeZone;

import java.util.*;

@Component
public class DynamoLogs implements LogsDAO {

    AmazonDynamoDB amazonDynamoDB;

    DynamoDB dynamoDB;

    WebEntityManager webEntityManager;

    TimeZone timeZone;

    SyncDAO dynamoSync;

    UsersDAO usersDAO;

    WorkDayDAO workDayDAO;
    private String EMPTY_VALUE="Empty";
    private String logTypeID="log";
    @Override
    public void writeUser(User newUser, DataModel.ActionType actionType, String initiatorID) {
        writeLog(newUser, actionType, initiatorID, DataModel.ObjectType.User);
    }

    @Override
    public List<Logs> getAll() {
        return null;
    }

    private void writeLog(Object objToLog, DataModel.ActionType actionType, String initiatorID, DataModel.ObjectType objectType) {
       /* Gson jsonDoc=new Gson();
        String syncID=dynamoSync.getLastIDForZeroAplly(initiatorID);
        String serverID= UUID.randomUUID().toString();
        String jsonObj=jsonDoc.toJson(objToLog);
        Table logTable=dynamoDB.getTable(DataModel.Logs.logsTable.getLogsItem());
        Item newItem=new Item().withString(DataModel.Logs.TypeID.getLogsItem(),logTypeID).withString(DataModel.Logs.ActionType.getLogsItem(),actionType.getAction()).
                withString(DataModel.Logs.InitiatorKEY.getLogsItem(),initiatorID).withString(DataModel.Logs.SyncID.getLogsItem(),syncID).
                withLong(DataModel.Logs.TimeStamp.getLogsItem(),timeZone.getMoscowDateTime(System.currentTimeMillis())).
                withString(DataModel.Logs.Object.getLogsItem(),objectType.getObject()).
                withString(DataModel.Logs.ServerID.getLogsItem(),serverID);

        if(actionType== DataModel.ActionType.Insert){
            newItem.withString(DataModel.Logs.JsonItemNew.getLogsItem(),jsonObj).
            withString(DataModel.Logs.JsonItemOld.getLogsItem(),EMPTY_VALUE);
        }else if (actionType== DataModel.ActionType.Delete){
            newItem.withString(DataModel.Logs.JsonItemOld.getLogsItem(),jsonObj).
            withString(DataModel.Logs.JsonItemNew.getLogsItem(),EMPTY_VALUE);
        }else if(actionType== DataModel.ActionType.Update){
            newItem.withString(DataModel.Logs.JsonItemNew.getLogsItem(),jsonObj);
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
            newItem.withString(DataModel.Logs.JsonItemOld.getLogsItem(),oldJson);
        }
        logTable.putItem(newItem);*/
    }

    @Override
    public void writeWorkDay(WorkDay workDay, DataModel.ActionType actionType,String initiatorID) {
        writeLog(workDay,actionType,initiatorID, DataModel.ObjectType.WorkDay);
    }

    @Override
    public List<Logs> getLogs(String syncID) {
        Table logTable=dynamoDB.getTable(DataModel.Logs.logsTable.getLogsItem());
        KeyAttribute typeID=new KeyAttribute(DataModel.Logs.TypeID.getLogsItem(),"log");
        KeyAttribute serverID=new KeyAttribute(DataModel.Logs.ServerID.getLogsItem(),"db32a9da-ecc1-4804-8266-f29c7d6d6425");
        Item result=logTable.getItem(typeID,serverID);
        ArrayList<Logs> logsArrayList =new ArrayList<>();
        Logs logs=new Logs();
        if(result!=null){
            logs.setServerId(result.getString(DataModel.Logs.ServerID.getLogsItem()));
            logs.setActionType(DataModel.ActionType.valueOf(result.getString(DataModel.Logs.ActionType.getLogsItem())));
            logs.setInitiatorKey(result.getString(DataModel.Logs.InitiatorKEY.getLogsItem()));
            logs.setJsonItemNew(result.getString(DataModel.Logs.JsonItemNew.getLogsItem()));
            logs.setJsonItemOld(result.getString(DataModel.Logs.JsonItemOld.getLogsItem()));
            logs.setObjectType(DataModel.ObjectType.valueOf(result.getString(DataModel.Logs.Object.getLogsItem())));
            logs.setTimeStamp(result.getLong(DataModel.Logs.TimeStamp.getLogsItem()));
        }
        logsArrayList.add(logs);
        return logsArrayList;
    }
}
