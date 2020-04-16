package ru.work.webcalendar.DataModel.DAO;

import ru.work.webcalendar.DataModel.Entity.User;

import java.util.ArrayList;
import java.util.List;

public interface UsersDAO {
    public User getUser(String serverid);
    public User getUser(int userID);
    public List<User> getUsers();
    public String insertUser(User user);
    public int deleteUser(String serverid);
    int deleteUser(int userID);
    public int updateUser(User user, String serverID);
    int getMaxUserId();

}
