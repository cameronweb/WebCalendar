package ru.work.webcalendar.Service.MySQL;

public  class MySqlQueries {
    public static final String USER_FINDALL ="findAll";
    public static final String USER_FINDALL_QUERY ="from User";
    public static final String USER_FIND_BY_SERVERID ="findByServerID";
    public static final String USER_FIND_BYSERVERID_QUERY ="from User u  where u.serverID=:serverId";
    public static final String USER_DELETE_BY_SERVID ="deleteByServerID";
    public static final String USER_DELETE_BY_SERVID_QUERY ="Delete from User u  where u.serverID=:serverId";
    public static final String USER_DELETE_BUYID ="deleteById";
    public static final String USER_DELETEU_BUYID_QUERY ="Delete from User u  where u.id=:userid";
    //---------------------------------------------------------
    public static final String WORKDAY_FINDALL ="findAllWorkDay";
    public static final String WORKDAY_FINDALL_QUERY ="from WorkDay";
    public static final String WORKDAY_FIND_BYSERVID ="findWorkDayByServerID";
    public static final String WORKDAY_FIND_BYSERVID_QUERY ="from WorkDay w where w.serverId=:serverid";
    public static final String WORKDAY_FIND_BYUSERID ="findByUser";
    public static final String WORKDAY_FIND_BYUSERID_QUERY ="from WorkDay w where w.userid=:userid";
    public static final String WORKDAY_FIND_BYUSERID_FORNEXT_DATE ="findByUserIdNextDate";
    public static final String WORKDAY_FIND_BYUSERID_FORNEXT_DATE_QUERY ="from WorkDay w where w.userid=:userid and w.workdate>=:datelong";
    public static final String WORKDAY_FIND_FUTURE_DATE ="findFutureDate";
    public static final String WORKDAY_FIND_FUTURE_DATE_QUERY ="from WorkDay w where w.workdate>=:datelong";
    public static final String WORKDAY_CHECK_EXISTS ="checkWorkdayExists";
    public static final String WORKDAY_CHECK_EXISTS_QUERY ="from WorkDay w where w.workdate=:datelong";

    public static final String WORKDAY_DELETE_BYSERV_ID ="deleteWorDayByServerId";
    public static final String WORKDAY_DELETE_BYSERV_ID_QUERY ="delete from WorkDay d where d.serverId=:serverid";
    public static final String WORKDAY_DELETE_BY_ID ="deleteWorDayById";
    public static final String WORKDAY_DELETE_BY_ID_QUERY ="delete from WorkDay d where d.id=:id";

    public static final String SYNC_FINDALL="syncFindAall";
    public static final String SYNC_FINDALL_QUERY="from Sync";
    public static final String SYNC_FIND_BY_SERVERID="syncFindByServerId";
    public static final String SYNC_FIND_BY_SERVERID_QUERY="from Sync s where s.serverId=:serverid";
    public static final String SYNC_FIND_BY_AFTERDATE="syncFindAfterDate";
    public static final String SYNC_FIND_BY_AFTERDATE_QUERY="from Sync s where s.timeStamp>=:datelong";
    public static final String SYNC_FIND_BY_INITIATOR="syncFindByInitiator";
    public static final String SYNC_FIND_BY_INITIATOR_QUERY="from Sync s where s.initiatorKey=:initiator";
    public static final String SYNC_FIND_LAST="syncFindLast";
    public static final String SYNC_FIND_LAST_QUERY="from Sync s order by s.id desc";
    public static final String SYNC_FIND_LAST_FOR_ZEROAPPLY="syncFindLastForZeroApply";
    public static final String SYNC_FIND_LAST_FOR_ZEROAPPLY_QUERY="from Sync s where s.applyCount=0 order by s.id desc";
    public static final String SYNC_FIND_LAST_FOR_ZEROAPPLY_BYINITIATOR ="syncFindLastForZeroApplyByInitiator";
    public static final String SYNC_FIND_LAST_FOR_ZEROAPPLY_BYINITIATOR_QUERY="from Sync s where s.applyCount=0 and s.initiatorKey=:initiator order by s.id desc";
    public static final String SYNC_APPLY_COUNT="syncApplyCount";
    public static final String SYNC_APPLY_COUNT_QUERY="update Sync s set s.applyCount=s.applyCount+1 where s.serverId=:serverid";

    public static final String LOGS_FIND_BY_SYNCID="logsFindBySyncId";
    public static final String LOGS_FIND_BY_SYNCID_QUERY="from Logs l where l.syncid=:syncid";
    public static final String LOGS_FIND_ALL="logsFindAll";
    public static final String LOGS_FIND_ALL_QUERY="from Logs";
}
