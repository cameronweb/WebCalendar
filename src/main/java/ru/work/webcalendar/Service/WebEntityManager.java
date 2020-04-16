package ru.work.webcalendar.Service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.work.webcalendar.DataModel.DAO.UsersDAO;
import ru.work.webcalendar.DataModel.DAO.WorkDayDAO;
import ru.work.webcalendar.DataModel.Entity.Logs;
import ru.work.webcalendar.DataModel.Entity.Sync;
import ru.work.webcalendar.DataModel.Entity.User;
import ru.work.webcalendar.DataModel.Entity.WorkDay;
import ru.work.webcalendar.Qualifiers.DAO;
import software.amazon.awssdk.utils.Pair;

import java.util.List;

public class WebEntityManager {
    @Autowired @DAO(DAO.Type.RDSMYSQL)
    UsersDAO usersDAO;
    @Autowired @DAO(DAO.Type.RDSMYSQL)
    WorkDayDAO workDayDAO;


    public User getNewUser()
    {
        return  new User();
    }
    public WorkDay getNewWorkDay()
    {
        return  new WorkDay();
    }
    public Pair<String, Boolean> insertEntity(Object entity){
        Class entityClass=entity.getClass();
        String serverID="";
        Pair<String, Boolean> pair=null;
        if(entityClass==User.class)
        {
            User newUser=(User)entity;
            serverID=usersDAO.insertUser(newUser);
            if (serverID.isEmpty()){
                pair=Pair.of("",false);
            }else {
                pair=Pair.of(serverID,true);
            }
        }else if(entityClass==WorkDay.class){
            WorkDay newDay=(WorkDay)entity;
            pair=workDayDAO.insertWorkDay(newDay);
        }
        return pair;
    }
    public int updateEntity(Object entity,String serverID){
        int result_code=0;
        Class entityClass=entity.getClass();
        if(entity instanceof User){
            User user=(User)entity;
           result_code= usersDAO.updateUser(user,serverID);
        }else if(entityClass==WorkDay.class){
            WorkDay workDay=(WorkDay)entity;
            result_code=workDayDAO.updateWorkDay(serverID,workDay);
        }
        return result_code;
    }
    public int deleteEntity(String serverID,Class entityClass){
        int result_code=0;
        if(entityClass==User.class){
            result_code=usersDAO.deleteUser(serverID);
        }else if(entityClass==WorkDay.class){
            result_code=workDayDAO.deleteWorkDay(serverID);
        }
        return result_code;
    }
    public <T> List<T> getAllEntities(Class<T> clazz){
        if (clazz==User.class){
            return (List<T>) usersDAO.getUsers();
        }else if(clazz==WorkDay.class){
            return (List<T>) workDayDAO.getWorkDays();
        }else  return null;
    }
    public <T> T getById(Class<T> clazz,String serverID){
        if(clazz==User.class){
            return (T) usersDAO.getUser(serverID);
        }else if(clazz==WorkDay.class){
            return (T) workDayDAO.getWorkDay(serverID);
        }else return null;
    }
    public Sync getNewSync(){return new Sync();}
    public Logs getNewLogs(){return new Logs();}
}
