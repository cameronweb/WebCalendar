package ru.work.webcalendar.Service.DynamoDB;

import com.amazonaws.services.dynamodbv2.document.*;
import ru.work.webcalendar.DataModel.DataModel;
import ru.work.webcalendar.Service.WebEntityManager;
import ru.work.webcalendar.Service.TimeZone;


public class DynamoGlobals {
    private DynamoDB dynamoDB;
    private WebEntityManager webEntityManager;
    private TimeZone timeZone;

    private final int GLOBAL_ID=1;

    public int getLastSync() {
       return getLastID(DataModel.Globals.SyncLastID.getValue());
    }
    public void incrementSync(){
        incGlobal(DataModel.Globals.SyncLastID.getValue());
    }
    public void incrementLog(){
        incGlobal(DataModel.Globals.LogLastID.getValue());
    }

    private void incGlobal(String fieldName) {
        Table globalTable = dynamoDB.getTable(DataModel.Globals.TABLE.getValue());
        //int nextID=getLastID(fieldName)+1;
        AttributeUpdate itemUpdate=new AttributeUpdate(fieldName).addNumeric(1);
        globalTable.updateItem(new PrimaryKey().addComponent(DataModel.Globals.ID.getValue(),GLOBAL_ID),itemUpdate);
    }

    public int getLastLog(){
        return getLastID(DataModel.Globals.LogLastID.getValue());
    }

    private int getLastID(String fieldName) {
        Table globalTable = dynamoDB.getTable(DataModel.Globals.TABLE.getValue());
        Item globalItem= globalTable.getItem(new KeyAttribute(DataModel.Globals.ID.getValue(),GLOBAL_ID));
        int recID=globalItem.getInt(fieldName);
        return recID;
    }
}