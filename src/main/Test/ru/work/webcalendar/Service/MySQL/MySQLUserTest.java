package ru.work.webcalendar.Service.MySQL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.work.webcalendar.DataModel.DAO.UsersDAO;
import ru.work.webcalendar.DataModel.Entity.User;
import ru.work.webcalendar.Qualifiers.DAO;
import ru.work.webcalendar.Service.DynamoTestConfiguration;
import ru.work.webcalendar.Service.WebEntityManager;

import javax.persistence.PersistenceContext;
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DynamoTestConfiguration.class)
public class MySQLUserTest {
    @Autowired @DAO(DAO.Type.RDSMYSQL)
    UsersDAO usersDAO;

    @Autowired
    WebEntityManager webEntityManager;
    private User user;

    @Before
    public void createUser(){
        user= webEntityManager.getNewUser();
        user.setName("Fedorova Alevtina Alexandrovna");
        user.setPhone("+11111111111");
        user.setDateLong(Long.parseLong("426816000000"));
    }

    @Test
    public void getUser() {
        User user= usersDAO.getUser(5);
        System.out.println(user.getName()+" ==== "+user.getPhone()+"------------------------------------------");
    }

    @Test
    public void testGetUser() {
    }

    @Test
    public void getUsers() {

    }

    @Test
    public void insertUser() {
        String serverID=usersDAO.insertUser(user);
        System.out.println(serverID+"--------------------------------------------------------------------------------------------");
    }

    @Test
    public void deleteUser() {
        int result= usersDAO.deleteUser("23f97891-b3a9-4dd8-97b4-f404c836a474");
        System.out.println(result+"--------------------------------------------------------------------------------------------");
    }

    @Test
    public void updateUser() {
        user.setPhone("+79374170225");
        user.setName("Федорова Алевтина Александровна");
        user.setServerID("23f97891-b3a9-4dd8-97b4-f404c836a474");
        int result= usersDAO.updateUser(user,user.getServerID());
        System.out.println(result+"--------------------------------------------------------------------------------------------");
    }
}