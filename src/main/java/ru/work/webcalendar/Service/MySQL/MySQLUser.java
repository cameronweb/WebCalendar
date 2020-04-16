package ru.work.webcalendar.Service.MySQL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import ru.work.webcalendar.DataModel.DAO.UsersDAO;
import ru.work.webcalendar.DataModel.Entity.User;
import ru.work.webcalendar.Qualifiers.LoggableUser;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;


public class MySQLUser implements UsersDAO {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    JpaTransactionManager transactionManager;
    @Override
    public User getUser(String serverid) {
        TypedQuery<User> query=entityManager.createNamedQuery(MySqlQueries.USER_FIND_BY_SERVERID,User.class);
        query.setParameter("serverId",serverid);
        return query.getSingleResult();
    }

    @Override
    public User getUser(int userID) {
        return entityManager.find(User.class,userID);
    }

    @Override
    public List<User> getUsers() {
        Query findAll=entityManager.createNamedQuery(MySqlQueries.USER_FINDALL);
        List<User> users=findAll.getResultList();
        return users;
    }

    @Override @Transactional @LoggableUser
    public String insertUser(User user) {
        String serverID= UUID.randomUUID().toString();
        user.setServerID(serverID);
        try {
            entityManager.persist(user);
            return serverID;
        }catch (Exception e){
            return  e.getMessage();
        }


    }

    @Override @Transactional @LoggableUser
    public int deleteUser(String serverid) {
        TypedQuery<User> query=entityManager.createNamedQuery(MySqlQueries.USER_DELETE_BY_SERVID_QUERY,User.class);
        query.setParameter("serverId",serverid);
        return query.executeUpdate();
    }

    @Override @LoggableUser
    public int deleteUser(int userID) {
        TypedQuery<User> query=entityManager.createNamedQuery(MySqlQueries.USER_DELETEU_BUYID_QUERY,User.class);
        query.setParameter("userid",userID);
        return query.executeUpdate();
    }

    @Override @Transactional @LoggableUser
    public int updateUser(User user, String serverID) {
        User dbUser=getUser(serverID);
        dbUser.setName(user.getName());
        dbUser.setPhone(user.getPhone());
        dbUser.setDateLong(user.getDateLong());
        entityManager.merge(dbUser);
        return 0;
    }

    @Override
    public int getMaxUserId() {
        return 0;
    }
}
