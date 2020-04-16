package ru.work.webcalendar.DataModel.DAO;

import ru.work.webcalendar.DataModel.Entity.Sync;

import java.sql.Timestamp;
import java.util.List;

public interface SyncDAO {
    String insertSync(String initiatorID);
    Sync getSync(String serverID);
    Sync getSync(int syncID);
    List<Sync> getAll();
    List<Sync> getSyncAfter(Long timeStamp);
    List<Sync> getSyncByInitiator(String initiatorID);
    Sync getLast();
    void applyCount(String serverID);
    void applyCount(int syncID);
    int getLastIDForZeroAplly(String InitiatorID);
    int getLastIDForZeroAplly();
}
