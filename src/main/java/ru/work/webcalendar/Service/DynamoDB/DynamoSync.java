package ru.work.webcalendar.Service.DynamoDB;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import ru.work.webcalendar.DataModel.DAO.SyncDAO;
import ru.work.webcalendar.DataModel.DataModel;
import ru.work.webcalendar.DataModel.Entity.Sync;
import ru.work.webcalendar.Service.WebEntityManager;
import ru.work.webcalendar.Service.TimeZone;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class DynamoSync implements SyncDAO {

    private AmazonDynamoDB amazonDynamoDB;

    private DynamoDB dynamoDB;

    private WebEntityManager webEntityManager;
    private TimeZone timeZone;
    private String typeIDSync="sync";
    @Override
    public String insertSync(String initiatorID){
        String serverID=UUID.randomUUID().toString();
        Table syncTable=dynamoDB.getTable(DataModel.Sync.SyncTable.getSynkItem());
        Item newItem=new Item();
        newItem.withString(DataModel.Sync.TypeID.getSynkItem(),typeIDSync);
        newItem.withString(DataModel.Sync.InitiatorKEY.getSynkItem(),initiatorID);
        newItem.withLong(DataModel.Sync.TimeStamp.getSynkItem(),timeZone.getMoscowDateTime());
        newItem.withInt(DataModel.Sync.ApplyCount.getSynkItem(),0);
        newItem.withString(DataModel.Sync.ServerID.getSynkItem(), serverID);
        PutItemOutcome itemOutcome= syncTable.putItem(newItem);
        PutItemResult result=itemOutcome.getPutItemResult();
        if(result!=null){
            return serverID;
        }else return "";
    }

    @Override
    public Sync getSync(String serverID) {
        Table syncTable=dynamoDB.getTable(DataModel.Sync.SyncTable.getSynkItem());
        KeyAttribute typeID=new KeyAttribute(DataModel.Sync.TypeID.getSynkItem(),typeIDSync);
        KeyAttribute servID=new KeyAttribute(DataModel.Sync.ServerID.getSynkItem(),serverID);
        Item syncItem=syncTable.getItem(typeID,servID);
        Sync newSync=null;
        if(syncItem!=null){
            newSync = genereateSync(syncItem);
        }
        return newSync;
    }

    @Override
    public Sync getSync(int syncID) {
        return null;
    }

    @Override
    public List<Sync> getSyncAfter(Long timeStamp) {
        Table syncTable = dynamoDB.getTable(DataModel.Sync.SyncTable.getSynkItem());
        ScanFilter scanFilter=new ScanFilter(DataModel.Sync.TimeStamp.getSynkItem()).ge(timeStamp);
        ItemCollection<ScanOutcome> scanOutcome=syncTable.scan(scanFilter);
        ArrayList<Sync> syncArrayList=new ArrayList<>();
        for (Item item:scanOutcome) {
            Sync sync=genereateSync(item);
            syncArrayList.add(sync);
        }
        return syncArrayList;
    }

    @Override
    public List<Sync> getSyncByInitiator(String initiatorID) {
        Table syncTable = dynamoDB.getTable(DataModel.Sync.SyncTable.getSynkItem());
        ScanFilter scanFilter=new ScanFilter(DataModel.Sync.InitiatorKEY.getSynkItem()).eq(initiatorID);
        ItemCollection<ScanOutcome> scanOutcome=syncTable.scan(scanFilter);
        ArrayList<Sync> syncArrayList=new ArrayList<>();
        for (Item item:scanOutcome) {
            Sync sync=genereateSync(item);
            syncArrayList.add(sync);
        }
        return syncArrayList;
    }

    @Override
    public void applyCount(String serverID) {
        Sync sync= getSync(serverID);
        Table syncTable = dynamoDB.getTable(DataModel.Sync.SyncTable.getSynkItem());
        AttributeUpdate attributeUpdate=new AttributeUpdate(DataModel.Sync.ApplyCount.getSynkItem()).put(sync.getApplyCount()+1);
        KeyAttribute typeID=new KeyAttribute(DataModel.Sync.TypeID.getSynkItem(),typeIDSync);
        KeyAttribute servID=new KeyAttribute(DataModel.Sync.ServerID.getSynkItem(),serverID);
        syncTable.updateItem(new PrimaryKey().addComponents(typeID,servID),attributeUpdate);
    }

    @Override
    public void applyCount(int syncID) {

    }

    @Override
    public List<Sync> getAll() {
        return null;
    }

    @Override
    public Sync getLast() {
        Table syncTable=dynamoDB.getTable(DataModel.Sync.SyncTable.getSynkItem());
        ScanFilter scanFilter=new ScanFilter(DataModel.Sync.ApplyCount.getSynkItem()).eq(0);
        ItemCollection<ScanOutcome> result= syncTable.scan(scanFilter);
        long timestamp=0;
        Sync sync= webEntityManager.getNewSync();
        for (Item item:result) {
            long dbtimestamp=item.getLong(DataModel.Sync.TimeStamp.getSynkItem());
            if(dbtimestamp>timestamp){
                timestamp=dbtimestamp;
                sync=genereateSync(item);
            }
        }
        return sync;
    }

    private Sync genereateSync(Item syncItem) {
        Sync newSync;
        newSync = webEntityManager.getNewSync();
        //newSync.setId(syncItem.getInt(DataModel.Sync.TypeID.getSynkItem()));
        newSync.setInitiatorKey(syncItem.getString(DataModel.Sync.InitiatorKEY.getSynkItem()));
        newSync.setTimeStamp(syncItem.getLong(DataModel.Sync.TimeStamp.getSynkItem()));
        newSync.setServerId(syncItem.getString(DataModel.Sync.ServerID.getSynkItem()));
        return newSync;
    }

    @Override
    public int getLastIDForZeroAplly(String inititatorID) {
       return 0;
    }

    @Override
    public int getLastIDForZeroAplly() {
        return 0;
    }
}
