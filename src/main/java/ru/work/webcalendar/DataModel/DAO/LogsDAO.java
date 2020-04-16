package ru.work.webcalendar.DataModel.DAO;

import ru.work.webcalendar.DataModel.DataModel;
import ru.work.webcalendar.DataModel.Entity.Logs;
import ru.work.webcalendar.DataModel.Entity.User;
import ru.work.webcalendar.DataModel.Entity.WorkDay;

import java.util.List;

public interface LogsDAO {

    public void writeUser(User user, DataModel.ActionType actionType,String initiatorID);
    void writeWorkDay(WorkDay workDay, DataModel.ActionType actionType,String initiatorID);
    List<Logs> getLogs(String syncID);
    List<Logs> getAll();
}
