package ru.work.webcalendar.Service.DynamoDB;


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

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DynamoTestConfiguration.class)
public class DynomoUserTest {
   @Autowired @DAO(DAO.Type.DynamoDB)
    UsersDAO usersDAO;
   @Autowired
   WebEntityManager webEntityManager;
   private User user;
   private  String ServerID;

   @Before
   public void createUser(){
       user= webEntityManager.getNewUser();
       user.setName("Fedorova Alevtina Alexandrovna");
       user.setPhone("+11111111111");
       user.setDateLong(Long.parseLong("426816000000"));
   }

    @Test
    public void insertUser() {
      String serverid= usersDAO.insertUser(user);
      System.out.println(serverid+"-------------------------------------------");
    }

    @Test
    public void deleteUser() {
        usersDAO.deleteUser("008676ad-4fba-41db-8ff2-9b0a24ddd7b1");
    }

    @Test
    public void updateUser() {
       user.setName("Федорова Алевтина Александровна");
       user.setPhone("+79374170225");
       user.setServerID("008676ad-4fba-41db-8ff2-9b0a24ddd7b1");
       usersDAO.updateUser(user,user.getServerID());
    }
}
