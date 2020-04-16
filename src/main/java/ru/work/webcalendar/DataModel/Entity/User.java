package ru.work.webcalendar.DataModel.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.work.webcalendar.Service.MySQL.MySqlQueries;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity @Table(name = "users")
@NamedQueries({@NamedQuery(name = MySqlQueries.USER_FINDALL,query = MySqlQueries.USER_FINDALL_QUERY),
        @NamedQuery(name=MySqlQueries.USER_FIND_BY_SERVERID,query = MySqlQueries.USER_FIND_BYSERVERID_QUERY),
        @NamedQuery(name=MySqlQueries.USER_DELETE_BY_SERVID,query = MySqlQueries.USER_DELETE_BY_SERVID_QUERY),
        @NamedQuery(name=MySqlQueries.USER_DELETE_BUYID,query = MySqlQueries.USER_DELETEU_BUYID_QUERY)})
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private  String name;
    private  long birthDate;
    private  String phone;
    private String serverID;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<WorkDay> workDayList;

    public List<WorkDay> getWorkDayList() {
        return workDayList;
    }

    public String getServerID() {
        return serverID;
    }
    public long getDateLong()
    {
        return birthDate;
    }
    public void setDateLong(long dateLong)
    {
        birthDate=dateLong;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public Date getBirthDate() {
        return new Date(birthDate);
    }

    public String getPhone() {
        return phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate.getTime();
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }
}
