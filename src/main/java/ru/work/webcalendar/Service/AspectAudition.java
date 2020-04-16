package ru.work.webcalendar.Service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import ru.work.webcalendar.DataModel.DAO.LogsDAO;
import ru.work.webcalendar.DataModel.DAO.UsersDAO;
import ru.work.webcalendar.DataModel.DAO.WorkDayDAO;
import ru.work.webcalendar.DataModel.DataModel;
import ru.work.webcalendar.DataModel.Entity.User;
import ru.work.webcalendar.DataModel.Entity.WorkDay;
import ru.work.webcalendar.Qualifiers.DAO;


@Aspect
public class AspectAudition {
    private String INSERT_USER="insertUser";
    private String DELETE_USER="deleteUser";
    private String UPDATE_USER="updateUser";
    private String INITIATOR_KEY="TestUser";
    private String INSERT_WORKDAY="insertWorkDay";
    private String UPDATE_WORKDAY="updateWorkDay";
    private String DELETE_WORKDAY="deleteWorkDay";


    @Autowired @DAO(DAO.Type.RDSMYSQL)
    LogsDAO logsDAO;
    @Autowired @DAO(DAO.Type.RDSMYSQL)
    UsersDAO usersDAO;
    @Autowired @DAO(DAO.Type.RDSMYSQL)
    WorkDayDAO workDayDAO;

    @Pointcut("@annotation(ru.work.webcalendar.Qualifiers.LoggableUser)")
    private void callUsersAction(){}

    @Pointcut("@annotation(ru.work.webcalendar.Qualifiers.LoggableWorkDay)")
    public void callWorkDaysAction(){}

    @Around("callUsersAction()")
    public Object logAction(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName=joinPoint.getSignature().getName();
        if(methodName.equals(INSERT_USER))
        {
            User user=(User)joinPoint.getArgs()[0];
            logsDAO.writeUser(user, DataModel.ActionType.Insert,INITIATOR_KEY);
        }else if (methodName.equals(DELETE_USER)){
            String serverID=(String) joinPoint.getArgs()[0];
            User user=usersDAO.getUser(serverID);
            logsDAO.writeUser(user, DataModel.ActionType.Delete,INITIATOR_KEY);
        }else if (methodName.equals(UPDATE_USER)){
            User user=(User)joinPoint.getArgs()[0];
            logsDAO.writeUser(user, DataModel.ActionType.Update,INITIATOR_KEY);
        }
        return joinPoint.proceed();
    }

    @Around("callWorkDaysAction()")
    public Object logDayAction(ProceedingJoinPoint joinPoint) throws Throwable{
        String methodName=joinPoint.getSignature().getName();
        if(methodName.equals(INSERT_WORKDAY)){
            WorkDay workDay=(WorkDay)joinPoint.getArgs()[0];
            logsDAO.writeWorkDay(workDay, DataModel.ActionType.Insert,INITIATOR_KEY);
        }else if (methodName.equals(DELETE_WORKDAY)){
            String serverID=(String)joinPoint.getArgs()[0];
            WorkDay dbWorkDay=workDayDAO.getWorkDay(serverID);
            logsDAO.writeWorkDay(dbWorkDay, DataModel.ActionType.Delete,INITIATOR_KEY);
        }else if(methodName.equals(UPDATE_WORKDAY)){
            WorkDay workDay=(WorkDay)joinPoint.getArgs()[1];
            logsDAO.writeWorkDay(workDay, DataModel.ActionType.Update,INITIATOR_KEY);
        }
        return joinPoint.proceed();
    }

}
