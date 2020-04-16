package ru.work.webcalendar.Service.DynamoDB;

import com.amazonaws.services.dynamodbv2.document.*;
import ru.work.webcalendar.DataModel.DAO.UsersDAO;
import ru.work.webcalendar.DataModel.DataModel;
import ru.work.webcalendar.DataModel.Entity.User;
import ru.work.webcalendar.Qualifiers.LoggableUser;
import ru.work.webcalendar.Service.WebEntityManager;
import ru.work.webcalendar.Service.TimeZone;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

public class DynomoUser implements UsersDAO {
    DynamoDB dynamoDB;
    WebEntityManager webEntityManager;
    TimeZone timeZone;
    private String USERTYPEID="user";

    @Override
    public int deleteUser(int userID) {
        return 0;
    }

    @Override
    public User getUser(String serverid) {
        Table usersTable=dynamoDB.getTable(DataModel.USERS_TABLE);
        KeyAttribute typeID=new KeyAttribute(DataModel.USERS_ID_FIELD,USERTYPEID);
        KeyAttribute servID=new KeyAttribute(DataModel.USERS_SERVERID_FIELD,serverid);
        Item item= usersTable.getItem(typeID,servID);
        if (item!=null){
            return  generateUser(item);
        }else
        return null;
    }

    private User generateUser(Item userItem){
        User newUser = webEntityManager.getNewUser();
        newUser.setServerID(userItem.getString(DataModel.USERS_SERVERID_FIELD));
        newUser.setDateLong(userItem.getLong(DataModel.USERS_BIRTHDATE_FIELD));
        newUser.setName(userItem.getString(DataModel.USERS_NAME_FIELD));
        newUser.setPhone(userItem.getString(DataModel.USERS_PHONE_FIELD));
        return newUser;
    }

    @Override
    public User getUser(int userID) {
    /*    HashMap<String,AttributeValue> getMap=new HashMap<String, AttributeValue>();
        AttributeValue attributeValue=AttributeValue.builder().n(String.valueOf(userID)).build();
        getMap.put(DataModel.USERS_ID_FIELD,attributeValue);
        GetItemRequest getItemRequest=GetItemRequest.builder().tableName(DataModel.USERS_TABLE).key(getMap).build();
         GetItemResponse response= dynamoDbClient.getItem(getItemRequest);
         User user =entityManager.getNewUser();
         if (response!=null){
             user.setId(userID);
             user.setPhone(response.item().get(DataModel.USERS_PHONE_FIELD).s());
             user.setName(response.item().get(DataModel.USERS_NAME_FIELD).s());
             user.setDateLong(Long.parseLong(response.item().get(DataModel.USERS_BIRTHDATE_FIELD).n()));
             user.setServerID(response.item().get(DataModel.USERS_SERVERID_FIELD).s());
         }*/
         return null;
    }

    public String getMaxId() {

      /*  ScanRequest scanRequest=ScanRequest.builder().tableName(DataModel.USERS_TABLE).build();
        ScanResponse scanResponse=dynamoDbClient.scan(scanRequest);
        int maxId=0;
        if (!scanResponse.items().isEmpty()) {
            for (Map<String, AttributeValue> item : scanResponse.items()) {
               int id=Integer.parseInt(item.get(DataModel.USERS_ID_FIELD).n());
               if(id >maxId){
                   maxId=id;
               }
            }
        }
        return String.valueOf(maxId);*/
      return "";
    }

    private ArrayList<User> generateUsers(ItemCollection<ScanOutcome> collection) {
        ArrayList<User> users=new ArrayList<User>();
        for (Item item:collection) {
            User user= webEntityManager.getNewUser();
            user.setPhone(item.getString(DataModel.USERS_PHONE_FIELD));
            user.setName(item.getString(DataModel.USERS_NAME_FIELD));
            user.setDateLong(item.getLong(DataModel.USERS_BIRTHDATE_FIELD));
            user.setServerID(item.getString(DataModel.USERS_SERVERID_FIELD));
            users.add(user);
        }
         Comparator<User> userComparator=new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                return u1.getServerID().compareTo(u2.getServerID());
            }
        };
        users.sort(userComparator);
        //Collections.sort(users,userComparator);
        return users;
    }
    @Override
    public ArrayList<User> getUsers()  {
      Table usersTable=dynamoDB.getTable(DataModel.USERS_TABLE);
      ScanFilter filter=new ScanFilter(DataModel.USERS_ID_FIELD).eq(USERTYPEID);
      ItemCollection<ScanOutcome> result =usersTable.scan(filter);
      return generateUsers(result);
    }

    @Override @LoggableUser
    public String insertUser(User user) {
       Table usersTable=dynamoDB.getTable(DataModel.USERS_TABLE);
       String serverID = UUID.randomUUID().toString();
       Item newUser=new Item();
       newUser.withString(DataModel.USERS_PHONE_FIELD,user.getPhone());
       newUser.withString(DataModel.USERS_SERVERID_FIELD,serverID);
       newUser.withString(DataModel.USERS_NAME_FIELD,user.getName());
       newUser.withString(DataModel.USERS_ID_FIELD,USERTYPEID);
       newUser.withLong(DataModel.USERS_BIRTHDATE_FIELD,user.getDateLong());
       PutItemOutcome result= usersTable.putItem(newUser);
       if(result.getPutItemResult().getSdkHttpMetadata().getHttpStatusCode()== HttpStatusCode.OK){
           return serverID;
       }else return "";
    }

    @Override @LoggableUser
    public int deleteUser(String serverid) {
        Table usersTable=dynamoDB.getTable(DataModel.USERS_TABLE);
        KeyAttribute typeID=new KeyAttribute(DataModel.USERS_ID_FIELD,USERTYPEID);
        KeyAttribute servID=new KeyAttribute(DataModel.USERS_SERVERID_FIELD,serverid);
        DeleteItemOutcome result= usersTable.deleteItem(typeID,servID);
         return result.getDeleteItemResult().getSdkHttpMetadata().getHttpStatusCode();
    }

    @Override @LoggableUser
    public int updateUser(User user, String serverID) {
        Table usersTable=dynamoDB.getTable(DataModel.USERS_TABLE);
        KeyAttribute typeID=new KeyAttribute(DataModel.USERS_ID_FIELD,USERTYPEID);
        KeyAttribute servID=new KeyAttribute(DataModel.USERS_SERVERID_FIELD,serverID);
        AttributeUpdate nameUpd=new  AttributeUpdate(DataModel.USERS_NAME_FIELD).put(user.getName());
        AttributeUpdate dateOpd=new AttributeUpdate(DataModel.USERS_BIRTHDATE_FIELD).put(user.getDateLong());
        AttributeUpdate phoneUpd= new AttributeUpdate(DataModel.USERS_PHONE_FIELD).put(user.getPhone());
        UpdateItemOutcome result= usersTable.updateItem(new PrimaryKey().addComponents(typeID,servID),nameUpd,dateOpd,phoneUpd);
        return result.getUpdateItemResult().getSdkHttpMetadata().getHttpStatusCode();
    }

    @Override
    public int getMaxUserId() {
        String maid=getMaxId();
        return Integer.parseInt(maid);
    }
}
