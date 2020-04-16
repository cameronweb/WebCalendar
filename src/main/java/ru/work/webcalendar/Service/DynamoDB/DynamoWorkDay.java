package ru.work.webcalendar.Service.DynamoDB;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import org.springframework.beans.factory.annotation.Value;
import ru.work.webcalendar.DataModel.DAO.WorkDayDAO;
import ru.work.webcalendar.DataModel.DataModel;
import ru.work.webcalendar.DataModel.Entity.User;
import ru.work.webcalendar.DataModel.Entity.WorkDay;
import ru.work.webcalendar.Qualifiers.LoggableWorkDay;
import ru.work.webcalendar.Service.WebEntityManager;
import ru.work.webcalendar.Service.TimeZone;
import software.amazon.awssdk.http.HttpStatusCode;
import software.amazon.awssdk.utils.Pair;


import java.util.*;

public class DynamoWorkDay implements WorkDayDAO {
    @Override
    public int updateWorkDay(WorkDay workDay) {
        return 0;
    }

    @Override
    public int deleteWorkDay(int id) {
        return 0;
    }

    @Override
    public WorkDay getWorkDay(int id) {
        return null;
    }

    @Value("${webcalendar.errors.RESULT_CODE_EXIST}")
    private String RESULT_CODE_EXIST;
    @Value("${webcalendar.errors.RESULT_CODE_USER_NOT_FOUND}")
    private String RESULT_CODE_USER_NOT_FOUND;
    AmazonDynamoDB amazonDynamoDB;
    DynamoDB dynamoDB;
    WebEntityManager webEntityManager;
    TimeZone timeZone;
    private String WORKDAYTYPEID="workday";
    private String USERTYPEID="user";
    @Override @LoggableWorkDay
    public Pair<String,Boolean> insertWorkDay(WorkDay workDay) {
        Table workdayTable=dynamoDB.getTable(DataModel.WORKDAY_TABLE);
       Pair<String,Boolean> result;
        if(!checkExists(workDay.getWorkdate())) {
                String serverID = UUID.randomUUID().toString();
                User dbUser=getUser(workDay.getUser().getId());
                if (dbUser!=null) {
                    String userID = dbUser.getServerID();
                    Item workDayItem = new Item();
                    workDayItem.withString(DataModel.WORKDAY_ID_FIELD,WORKDAYTYPEID ).
                            with(DataModel.WORKDAY_DATE_FIELD, timeZone.getMoscowDateTime(workDay.getWorkdate())).
                            withString(DataModel.WORKDATE_SERVERID_FIELD, serverID).
                            withString(DataModel.WORKDAY_USERID_FIELD, userID).
                            withBoolean(DataModel.WORKDATE_ISSATURDAY_FIELD, workDay.isSaturday());
                    workdayTable.putItem(workDayItem);
                    result = Pair.of(serverID, true);
                    return result;
                }else {
                    return Pair.of(RESULT_CODE_USER_NOT_FOUND, false);
                }
        }else{
            result=Pair.of(RESULT_CODE_EXIST,false);
            return result;
        }
    }


    @Override @LoggableWorkDay
    public int updateWorkDay(String serverID,WorkDay workDay) {
        WorkDay dbWorDay=getWorkDay(serverID);
        User dbUser=getUser(workDay.getUser().getId());
        Table workDayTable=dynamoDB.getTable(DataModel.WORKDAY_TABLE);
        if(dbWorDay!=null){
            ArrayList<AttributeUpdate> attributeUpdateList=new ArrayList<>();
            attributeUpdateList.add(new AttributeUpdate(DataModel.WORKDAY_DATE_FIELD).put(timeZone.getMoscowDateTime(workDay.getWorkdate())));
            attributeUpdateList.add(new AttributeUpdate(DataModel.WORKDATE_ISSATURDAY_FIELD).put(workDay.isSaturday()));
            attributeUpdateList.add(new AttributeUpdate(DataModel.WORKDAY_USERID_FIELD).put(dbUser.getServerID()));
            UpdateItemSpec updateItemSpec=new UpdateItemSpec().
                    withPrimaryKey(new KeyAttribute(DataModel.WORKDAY_ID_FIELD,WORKDAYTYPEID),new KeyAttribute(DataModel.WORKDATE_SERVERID_FIELD,dbWorDay.getServerId())).
                    withAttributeUpdate(attributeUpdateList);
            UpdateItemOutcome updateItemOutcome= workDayTable.updateItem(updateItemSpec);
            return updateItemOutcome.getUpdateItemResult().getSdkHttpMetadata().getHttpStatusCode();
        }else return HttpStatusCode.NOT_FOUND;
    }

    @Override @LoggableWorkDay
    public int deleteWorkDay(String  serverID) {
        Table workDayTable=dynamoDB.getTable(DataModel.WORKDAY_TABLE);

        DeleteItemOutcome deleteItemOutcome= workDayTable.deleteItem(new KeyAttribute(DataModel.WORKDAY_ID_FIELD,WORKDAYTYPEID),new KeyAttribute(DataModel.WORKDATE_SERVERID_FIELD,serverID));
         DeleteItemResult result= deleteItemOutcome.getDeleteItemResult();
         if(result!=null){ return result.getSdkHttpMetadata().getHttpStatusCode();}
         else return HttpStatusCode.NOT_FOUND;
    }

    @Override
    public WorkDay getWorkDay(String serverID) {
        Table workDayTable=dynamoDB.getTable(DataModel.WORKDAY_TABLE);
        Item workDayItem = workDayTable.getItem(new KeyAttribute(DataModel.WORKDAY_ID_FIELD,WORKDAYTYPEID),new KeyAttribute(DataModel.WORKDATE_SERVERID_FIELD,serverID));
        if (workDayItem != null) {
                return generateWorkDay(workDayItem);
        } else return null;
    }
    private WorkDay generateWorkDay(Item workDayItem){
        WorkDay workDay= webEntityManager.getNewWorkDay();
        workDay.setSaturday(workDayItem.getBoolean(DataModel.WORKDATE_ISSATURDAY_FIELD));
        workDay.setWorkdate(workDayItem.getLong(DataModel.WORKDAY_DATE_FIELD));
        workDay.setServerId(workDayItem.getString(DataModel.WORKDATE_SERVERID_FIELD));
        workDay.setUserid(workDayItem.getInt(DataModel.WORKDAY_USERID_FIELD));
        workDay.setUser(getUser(workDay.getUserid()));
        return workDay;
    }
    private User getUser(int userID)
    {
        Table userTable=dynamoDB.getTable(DataModel.USERS_TABLE);
        Item userItem=userTable.getItem(new KeyAttribute(DataModel.USERS_ID_FIELD,USERTYPEID),new KeyAttribute(DataModel.USERS_SERVERID_FIELD,userID));
        if(userItem!=null){
           return generateUser(userItem);
        }else return null;
    }
    private User generateUser(Item userItem) {
        User user= webEntityManager.getNewUser();
        user.setDateLong(userItem.getLong(DataModel.USERS_BIRTHDATE_FIELD));
        user.setServerID(userItem.getString(DataModel.USERS_SERVERID_FIELD));
        user.setPhone(userItem.getString(DataModel.USERS_PHONE_FIELD));
        user.setName(userItem.getString(DataModel.USERS_NAME_FIELD));
        return user;
    }
    @Override
    public List<WorkDay> getWorkDays() {
        Table workDay=dynamoDB.getTable(DataModel.WORKDAY_TABLE);
        ScanSpec scanSpec=new ScanSpec().withProjectionExpression(DataModel.WORKDATE_FIELDS_SELECT);
        ItemCollection<ScanOutcome> result=workDay.scan(scanSpec);
        return generateList(result);
    }
    private List<WorkDay> generateList(ItemCollection<ScanOutcome> result){
        Table usersTable=dynamoDB.getTable(DataModel.USERS_TABLE);
        ArrayList<WorkDay> workDayArrayList=new ArrayList<>();

            for (Item item : result) {
                WorkDay day = webEntityManager.getNewWorkDay();
                int userID = item.getInt(DataModel.WORKDAY_USERID_FIELD);
                KeyAttribute userKey = new KeyAttribute(DataModel.USERS_SERVERID_FIELD, userID);
                KeyAttribute userType = new KeyAttribute(DataModel.USERS_ID_FIELD, USERTYPEID);
                day.setUserid(userID);
                Item userItem = usersTable.getItem(userType,userKey);
                if (userItem != null) {
                    User user = webEntityManager.getNewUser();
                    user.setName(userItem.getString(DataModel.USERS_NAME_FIELD));
                    user.setPhone(userItem.getString(DataModel.USERS_PHONE_FIELD));
                    user.setServerID(userItem.getString(DataModel.USERS_SERVERID_FIELD));
                    user.setDateLong(userItem.getLong(DataModel.USERS_BIRTHDATE_FIELD));
                    day.setUser(user);
                }
                day.setWorkdate(item.getLong(DataModel.WORKDAY_DATE_FIELD));
                day.setSaturday(item.getBoolean(DataModel.WORKDATE_ISSATURDAY_FIELD));
                day.setServerId(item.getString(DataModel.WORKDATE_SERVERID_FIELD));
                workDayArrayList.add(day);
            }
            return workDayArrayList;
    }
    @Override
    public List<WorkDay> getByUser(String ServerID, Boolean sorted) {
        Table workDay=dynamoDB.getTable(DataModel.WORKDAY_TABLE);
        ScanFilter scanFilter=new ScanFilter(DataModel.WORKDAY_USERID_FIELD).eq(ServerID);
        ItemCollection<ScanOutcome> result=workDay.scan(scanFilter);
        return generateList(result);
    }

    @Override
    public List<WorkDay> getUserNextWorkDay(String user_id, long afterDate) {
        Table workDay=dynamoDB.getTable(DataModel.WORKDAY_TABLE);
        ScanFilter userFilter=new ScanFilter(DataModel.WORKDAY_USERID_FIELD).eq(user_id);
        ScanFilter dateFilter=new ScanFilter(DataModel.WORKDAY_DATE_FIELD).ge(afterDate);
        ScanSpec querySpec=new ScanSpec().withScanFilters(userFilter,dateFilter);
         ItemCollection<ScanOutcome> result=workDay.scan(querySpec);
         return generateList(result);
    }

    @Override
    public List<WorkDay> getFutureWorkDays() {
        Table workDay=dynamoDB.getTable(DataModel.WORKDAY_TABLE);
        long currentDate= timeZone.getMoscowDateTime();
        ScanFilter scanFilter=new ScanFilter(DataModel.WORKDAY_DATE_FIELD).ge(currentDate);
        ItemCollection<ScanOutcome> result=workDay.scan(scanFilter);
        return generateList(result);
    }

    @Override
    public boolean checkExists(long date) {
        Table workTable=dynamoDB.getTable(DataModel.WORKDAY_TABLE);
        ScanFilter scanFilter=new ScanFilter(DataModel.WORKDAY_DATE_FIELD).eq(date);
        ItemCollection<ScanOutcome> result=workTable.scan(scanFilter);
        boolean isPresent=false;
        for (Item item:result) {
            isPresent= item.isPresent(DataModel.WORKDATE_SERVERID_FIELD);
        }
        return isPresent;
    }

    private int getWorkDayMaxID()
    {
        int maxID=0;
        Table workDayTable=dynamoDB.getTable(DataModel.WORKDAY_TABLE);
        Index index =workDayTable.getIndex(DataModel.WORKDATE_INDEX);
        ItemCollection<ScanOutcome> result= index.scan(new ScanSpec().withProjectionExpression(DataModel.WORKDAY_ID_FIELD));
        for (Item item:result) {

            int id=item.getInt(DataModel.WORKDAY_ID_FIELD);
            if(id>maxID)
            {
                maxID=id;
            }
        }
        return maxID;
    }
}
