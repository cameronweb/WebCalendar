package ru.work.webcalendar.DataModel.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.work.webcalendar.Service.MySQL.MySqlQueries;

import javax.persistence.*;
import java.util.Date;
@NamedQueries({@NamedQuery(name = MySqlQueries.WORKDAY_FINDALL,query = MySqlQueries.WORKDAY_FINDALL_QUERY),
                @NamedQuery(name=MySqlQueries.WORKDAY_FIND_BYSERVID,query = MySqlQueries.WORKDAY_FIND_BYSERVID_QUERY),
                @NamedQuery(name=MySqlQueries.WORKDAY_FIND_BYUSERID,query = MySqlQueries.WORKDAY_FIND_BYUSERID_QUERY),
                @NamedQuery(name = MySqlQueries.WORKDAY_FIND_BYUSERID_FORNEXT_DATE,query = MySqlQueries.WORKDAY_FIND_BYUSERID_FORNEXT_DATE_QUERY),
                @NamedQuery(name=MySqlQueries.WORKDAY_FIND_FUTURE_DATE,query = MySqlQueries.WORKDAY_FIND_FUTURE_DATE_QUERY),
                @NamedQuery(name=MySqlQueries.WORKDAY_DELETE_BYSERV_ID,query = MySqlQueries.WORKDAY_DELETE_BYSERV_ID_QUERY),
                @NamedQuery(name=MySqlQueries.WORKDAY_DELETE_BY_ID,query = MySqlQueries.WORKDAY_DELETE_BY_ID_QUERY),
                @NamedQuery(name=MySqlQueries.WORKDAY_CHECK_EXISTS,query = MySqlQueries.WORKDAY_CHECK_EXISTS_QUERY)})
@Entity @Table(name = "workdays")
public class WorkDay {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private long workdate;
    private boolean saturday;
    private int userid;
    private String serverId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid",insertable = false,updatable = false)
    private User user;

    public int getId() {
        return id;
    }

    public long getWorkdate() {
        return workdate;
    }
    @JsonIgnore
    public Date getDate(){
        return new Date(workdate);
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public int getUserid() {
        return userid;
    }

    public User getUser() {
        return user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWorkdate(long workdate) {
        this.workdate = workdate;
    }

    public void setDate(Date date){
        workdate =date.getTime();
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setUser(User user) {
        this.user = user;
        userid =user.getId();
    }

    public String getServerId() {
        return serverId;
    }
}
