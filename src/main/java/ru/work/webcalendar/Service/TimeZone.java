package ru.work.webcalendar.Service;

import java.util.Calendar;

public class TimeZone {
    private Calendar calendar;
    public TimeZone(){
        java.util.TimeZone tz= java.util.TimeZone.getTimeZone("Europe/Moscow");
        calendar = Calendar.getInstance(tz);
    }
    public  long getMoscowDateTime()
    {
        return calendar.getTimeInMillis();
    }
    public  long getMoscowDateTime(long dateLong)
    {
        calendar.setTimeInMillis(dateLong);
        return calendar.getTimeInMillis();
    }
    public void setTimeMillis(long dateTime)
    {
        calendar.setTimeInMillis(dateTime);
    }
    public long getTimeMillis()
    {
        return calendar.getTimeInMillis();
    }
}
