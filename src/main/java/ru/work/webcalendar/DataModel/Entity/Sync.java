package ru.work.webcalendar.DataModel.Entity;

import ru.work.webcalendar.Service.MySQL.MySqlQueries;

import javax.persistence.*;
import java.util.List;
@NamedQueries({@NamedQuery(name = MySqlQueries.SYNC_FINDALL,query = MySqlQueries.SYNC_FINDALL_QUERY),
                @NamedQuery(name = MySqlQueries.SYNC_FIND_BY_AFTERDATE,query = MySqlQueries.SYNC_FIND_BY_AFTERDATE_QUERY),
                @NamedQuery(name = MySqlQueries.SYNC_FIND_BY_INITIATOR,query = MySqlQueries.SYNC_FIND_BY_INITIATOR_QUERY),
                @NamedQuery(name = MySqlQueries.SYNC_FIND_BY_SERVERID,query = MySqlQueries.SYNC_FIND_BY_SERVERID_QUERY),
                @NamedQuery(name = MySqlQueries.SYNC_FIND_LAST,query = MySqlQueries.SYNC_FIND_LAST_QUERY),
                @NamedQuery(name = MySqlQueries.SYNC_FIND_LAST_FOR_ZEROAPPLY,query = MySqlQueries.SYNC_FIND_LAST_FOR_ZEROAPPLY_QUERY),
                @NamedQuery(name=MySqlQueries.SYNC_APPLY_COUNT,query = MySqlQueries.SYNC_APPLY_COUNT_QUERY),
                @NamedQuery(name = MySqlQueries.SYNC_FIND_LAST_FOR_ZEROAPPLY_BYINITIATOR,query = MySqlQueries.SYNC_FIND_LAST_FOR_ZEROAPPLY_BYINITIATOR_QUERY)})
@Entity @Table(name="sync")
public class Sync {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "serverid")
    private String serverId;
    @Column(name = "initiatorkey")
    private String initiatorKey;
    private long timeStamp;
    private int applyCount;
    @OneToMany(mappedBy = "sync", fetch = FetchType.EAGER)
    private List<Logs> logs;


    public int getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(int applyCount) {
        this.applyCount = applyCount;
    }

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

    public String getInitiatorKey() {
        return initiatorKey;
    }

    public void setInitiatorKey(String initiatorKey) {
        this.initiatorKey = initiatorKey;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<Logs> getLogs() {
        return logs;
    }

    public void setLogs(List<Logs> logs) {
        this.logs = logs;
    }
}
