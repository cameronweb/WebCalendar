package ru.work.webcalendar.DataModel;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
import ru.work.webcalendar.DataModel.DAO.LogsDAO;
import ru.work.webcalendar.DataModel.DAO.SyncDAO;
import ru.work.webcalendar.DataModel.DAO.UsersDAO;
import ru.work.webcalendar.DataModel.DAO.WorkDayDAO;
import ru.work.webcalendar.Qualifiers.DAO;
import ru.work.webcalendar.Service.AspectAudition;
import ru.work.webcalendar.Service.MySQL.*;
import ru.work.webcalendar.Service.WebEntityManager;
import ru.work.webcalendar.Service.TimeZone;

@Configuration @Import(MySQLPersistenceConfig.class) @EnableAspectJAutoProxy
public class DAOFactory {
    @Bean @DAO(DAO.Type.RDSMYSQL)
    public UsersDAO usersDAO(){
       return new MySQLUser();
    }
    @Bean @DAO(DAO.Type.RDSMYSQL)
    public WorkDayDAO workDayDAO()
    {
       return  new MySQLWorkDay();
    }
    @Bean
    public WebEntityManager entityManager()
    {
        return new WebEntityManager();
    }
    @Bean @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public TimeZone timeZone(){
        return new TimeZone();
    }
    @Bean
    public AspectAudition aspectAudition(){return new AspectAudition();}
    @Bean @DAO(DAO.Type.RDSMYSQL)
    public SyncDAO syncDAO(){return  new MySqlSync();}
    @Bean @DAO(DAO.Type.RDSMYSQL)
    public LogsDAO logsDAO(){return  new MySqlLogs();}

}
