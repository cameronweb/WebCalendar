package ru.work.webcalendar.DataModel;

public interface DataModel {
    public static final String USERS_TABLE="workcalendar";
    public static final String USERS_ID_FIELD="typeid";
    public static final String USERS_NAME_FIELD="name";
    public static final String USERS_PHONE_FIELD="phone";
    public static final String USERS_BIRTHDATE_FIELD="birthdate";
    public static final String USERS_SERVERID_FIELD="serverid";
    public static final String USERS_SERVERID_INDEX="serverid-index";
    public static final String WORKDAY_TABLE="workcalendar";
    public static final String WORKDAY_ID_FIELD="typeid";
    public static final String WORKDAY_USERID_FIELD="userid";
    public static final String WORKDAY_DATE_FIELD="workdate";
    public static final String WORKDATE_ISSATURDAY_FIELD="saturday";
    public static final String WORKDATE_SERVERID_FIELD="serverid";
    public static final String WORKDATE_INDEX="serverid-index";
    public static final String WORKDATE_DATE_INDEX="workdate-index";
    public static final String WORKDATE_FIELDS_SELECT="typeid,userid,workdate,saturday,serverid";

    public enum Sync{
        SyncTable("Synclog"), TypeID("typeid"),ServerID("serverid"),InitiatorKEY("initiatorkey"),
        TimeStamp("timestamp"),ApplyCount("applycount"),Index("timestamp-index");
        String synkItem;
        Sync(String synkItem){
            this.synkItem=synkItem;
        }
        public String getSynkItem(){
            return synkItem;
        }
    }
    public enum Logs{
        logsTable("Synclog"), TypeID("typeid"),SyncID("syncid"),ActionType("actiontype"),
        InitiatorKEY("initiatorkey"),JsonItemNew("jsonitemnew"),JsonItemOld("jsonitemold"),
        Object("object"),ServerID("serverid"),TimeStamp("timestamp");
        String logsItem;
        Logs(String logsItem){
            this.logsItem=logsItem;
        }
        public String getLogsItem(){
            return logsItem;
        }
    }
    public enum ActionType{
        Insert("insert"),Update("update"),Delete("delete");
        String action;
        public String getAction(){return action;}
        ActionType(String action){
            this.action=action;
        }
    }
    public  enum ObjectType{
        User("user"),WorkDay("workday");
        String object;
        public String getObject(){return object;}
        ObjectType(String object){
            this.object=object;
        }
    }
    public enum Globals{
        UserLastID("userlastid"),WorkDayLastID("workdaylastid"),SyncLastID("synclastid"),
        LogLastID("loglastid"),ID("id"),TABLE("globals");
        Globals(String lastId){this.lastId=lastId;}
        String lastId;
        public String getValue(){ return  lastId;}
    }

}
