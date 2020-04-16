package ru.work.webcalendar.DataModel.Entity;

import ru.work.webcalendar.DataModel.DataModel;
import ru.work.webcalendar.DataModel.DataModel.ActionType;
import ru.work.webcalendar.Service.MySQL.MySqlQueries;

import javax.persistence.*;
@NamedQueries({@NamedQuery(name = MySqlQueries.LOGS_FIND_ALL,query = MySqlQueries.LOGS_FIND_ALL_QUERY),
                @NamedQuery(name = MySqlQueries.LOGS_FIND_BY_SYNCID,query = MySqlQueries.LOGS_FIND_BY_SYNCID_QUERY)})
@Entity @Table(name = "logs")
public class Logs {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "serverid")
    private String serverId;
    @Column(name = "actiontype") @Enumerated(EnumType.STRING)
    private ActionType actionType;
    @Column(name = "initiatorkey")
    private  String initiatorKey;
    private String jsonItemOld;
    private String jsonItemNew;
    @Column(name = "objecttype") @Enumerated(EnumType.STRING)
    private DataModel.ObjectType objectType;
    private long timeStamp;
    private int syncid;
    @JoinColumn(name = "syncid",insertable = false,updatable = false) @ManyToOne(fetch = FetchType.EAGER)
    private Sync sync;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getInitiatorKey() {
        return initiatorKey;
    }

    public void setInitiatorKey(String initiatorKey) {
        this.initiatorKey = initiatorKey;
    }

    public String getJsonItemOld() {
        return jsonItemOld;
    }

    public void setJsonItemOld(String jsonItemOld) {
        this.jsonItemOld = jsonItemOld;
    }

    public String getJsonItemNew() {
        return jsonItemNew;
    }

    public void setJsonItemNew(String jsonItemNew) {
        this.jsonItemNew = jsonItemNew;
    }

    public DataModel.ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(DataModel.ObjectType objectType) {
        this.objectType = objectType;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getSyncid() {
        return syncid;
    }

    public void setSyncid(int syncid) {
        this.syncid = syncid;
    }

    public Sync getSync() {
        return sync;
    }

    public void setSync(Sync sync) {
        this.sync = sync;
    }
}
