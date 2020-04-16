package ru.work.webcalendar.Service.MySQL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.work.webcalendar.DataModel.DAO.WorkDayDAO;
import ru.work.webcalendar.DataModel.Entity.WorkDay;
import ru.work.webcalendar.Qualifiers.DAO;
import ru.work.webcalendar.Service.DynamoTestConfiguration;
import ru.work.webcalendar.Service.WebEntityManager;
import software.amazon.awssdk.utils.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DynamoTestConfiguration.class)
public class MySQLWorkDayTest {
    @Autowired
    @DAO(DAO.Type.RDSMYSQL)
    WorkDayDAO workDayDAO;
    private WorkDay workDay;

    @Autowired
    WebEntityManager webEntityManager;
    @Before
    public void init() throws ParseException {
        workDay=webEntityManager.getNewWorkDay();
        workDay.setSaturday(false);
        workDay.setUserid(5);
        SimpleDateFormat format=new SimpleDateFormat("dd.MM.yyyy");
        String stringDate="21.12.2019";
        Date date=format.parse(stringDate);
        workDay.setWorkdate(date.getTime());
    }

    @Test
    public void insertWorkDay() {
        Pair<String,Boolean> result =workDayDAO.insertWorkDay(workDay);
        System.out.println(result.left()+"-----------------------------------------------------------------");
    }

    @Test
    public void updateWorkDay() {
        workDay.setId(2);
        workDay.setServerId("3d81199f-ac73-4c59-9570-10dea814509e");
        workDay.setSaturday(true);
       int result= workDayDAO.updateWorkDay(workDay);
       System.out.println(result+" ----------------------------------------------------------");
    }

    @Test
    public void deleteWorkDay() {
    }

    @Test
    public void testUpdateWorkDay() {

    }

    @Test
    public void testDeleteWorkDay() {
    }

    @Test
    public void getWorkDay() {
        WorkDay dbworkday=workDayDAO.getWorkDay(1);
        System.out.println(dbworkday.getServerId()+"  "+dbworkday.getUser().getName()+" -------------------------------------------------");
    }

    @Test
    public void testGetWorkDay() {

    }

    @Test
    public void getWorkDays() {
    }

    @Test
    public void getByUser() {
    }

    @Test
    public void getUserNextWorkDay() {
    }

    @Test
    public void getFutureWorkDays() {
    }

    @Test
    public void checkExists() {
    }
}