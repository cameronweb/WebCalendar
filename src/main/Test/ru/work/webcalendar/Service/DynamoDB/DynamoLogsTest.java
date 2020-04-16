package ru.work.webcalendar.Service.DynamoDB;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.work.webcalendar.DataModel.DAO.LogsDAO;
import ru.work.webcalendar.DataModel.Entity.Logs;
import ru.work.webcalendar.Qualifiers.DAO;
import ru.work.webcalendar.Service.DynamoTestConfiguration;

import java.util.ArrayList;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DynamoTestConfiguration.class)
public class DynamoLogsTest {
    @Autowired @DAO(DAO.Type.DynamoDB)
    LogsDAO logsDAO;
    @Test
    public void writeUser() {
    }

    @Test
    public void writeWorkDay() {
    }

    @Test
    public void getLogs() {
        ArrayList<Logs> logsArrayList=(ArrayList<Logs>) logsDAO.getLogs("321");
        Logs log=logsArrayList.get(0);
        System.out.println(log.getActionType().getAction()+"  "+log.getJsonItemNew()+" "+log.getServerId()+" -----------------------------");
    }
}