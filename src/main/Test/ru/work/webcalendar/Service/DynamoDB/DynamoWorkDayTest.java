package ru.work.webcalendar.Service.DynamoDB;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.work.webcalendar.DataModel.DAO.UsersDAO;
import ru.work.webcalendar.DataModel.DAO.WorkDayDAO;
import ru.work.webcalendar.DataModel.Entity.User;
import ru.work.webcalendar.DataModel.Entity.WorkDay;
import ru.work.webcalendar.Qualifiers.DAO;
import ru.work.webcalendar.Service.DynamoTestConfiguration;
import ru.work.webcalendar.Service.WebEntityManager;

import java.util.UUID;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DynamoTestConfiguration.class)
public class DynamoWorkDayTest {
    @Autowired
    WebEntityManager webEntityManager;
    @Autowired @DAO(DAO.Type.DynamoDB)
    WorkDayDAO workDayDAO;
    @Autowired @DAO(DAO.Type.DynamoDB)
    UsersDAO usersDAO;
    private WorkDay workDay;
    private User user;

    @Before
    public void init(){
        String serverID= UUID.randomUUID().toString();
        user=usersDAO.getUser("008676ad-4fba-41db-8ff2-9b0a24ddd7b1");
        workDay= webEntityManager.getNewWorkDay();
        workDay.setWorkdate(System.currentTimeMillis());
        workDay.setSaturday(false);
        workDay.setUser(user);
        workDay.setServerId(serverID);
        workDay.setUserid(user.getId());
    }

    @Test
    public void insertWorkDay() {
        workDayDAO.insertWorkDay(workDay);
    }

    @Test
    public void updateWorkDay() {
        workDay.setSaturday(true);
        workDay.setWorkdate(System.currentTimeMillis()+10000);
        workDay.setServerId("ed2db176-388f-47bd-baa6-efe6e8525f08");
        user.setServerID("008676ad-4fba-41db-8ff2-9b0a24ddd7b1");
        workDayDAO.updateWorkDay(workDay.getServerId(),workDay);
    }

    @Test
    public void deleteWorkDay() {
        workDay.setServerId("d3685fbf-707d-4b05-a74f-2565855f38d1");
        workDayDAO.deleteWorkDay(workDay.getServerId());
    }
}