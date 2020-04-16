package ru.work.webcalendar.Service;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import ru.work.webcalendar.DataModel.DAO.LogsDAO;
import ru.work.webcalendar.DataModel.DAO.SyncDAO;
import ru.work.webcalendar.DataModel.DAO.UsersDAO;
import ru.work.webcalendar.DataModel.DAO.WorkDayDAO;
import ru.work.webcalendar.Qualifiers.DAO;
import ru.work.webcalendar.Service.MySQL.*;

@TestConfiguration
@EnableAspectJAutoProxy @Import(MySQLPersistenceConfig.class)
public class DynamoTestConfiguration {
    @Bean
    @DAO(DAO.Type.RDSMYSQL)
    public UsersDAO usersDAO(){return new MySQLUser();}
    @Bean @DAO(DAO.Type.RDSMYSQL)
    public WorkDayDAO workDayDAO(){return new MySQLWorkDay();
    }
    @Bean
    public WebEntityManager entityManager(){return new WebEntityManager();}
   @Bean @DAO(DAO.Type.RDSMYSQL)
    public LogsDAO logsDAO(){return new MySqlLogs();}
    @Bean @DAO(DAO.Type.RDSMYSQL)
    public SyncDAO syncDAO(){return new MySqlSync();}
    @Bean @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public TimeZone timeZone(){
        return new TimeZone();
    }
    @Bean
    public AspectAudition aspectAudition(){return new AspectAudition();}



}
