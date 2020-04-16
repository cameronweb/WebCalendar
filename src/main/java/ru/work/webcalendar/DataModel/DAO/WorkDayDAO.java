package ru.work.webcalendar.DataModel.DAO;

import ru.work.webcalendar.DataModel.Entity.WorkDay;
import software.amazon.awssdk.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public interface WorkDayDAO {
    public Pair<String, Boolean> insertWorkDay(WorkDay workDay);
    public int updateWorkDay(String serverID,WorkDay workDay);
    public int updateWorkDay(WorkDay workDay);
    public int deleteWorkDay(String  serverID);
    public int deleteWorkDay(int  id);
    public WorkDay getWorkDay(String serverid);
    public WorkDay getWorkDay(int id);
    public List<WorkDay> getWorkDays();

    public List<WorkDay> getByUser(String ServerID, Boolean sorted);

    public List<WorkDay> getUserNextWorkDay(String user_id, long afterDate);
    public List<WorkDay> getFutureWorkDays();
    public boolean checkExists(long date);

}
