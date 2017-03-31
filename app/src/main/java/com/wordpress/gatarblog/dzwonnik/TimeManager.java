package com.wordpress.gatarblog.dzwonnik;

import java.util.Calendar;

/**
 * Created by bgata on 31.03.2017.
 */

public class TimeManager {

    private static Calendar calendar;

    private static void refreshTime(){
        calendar.setTimeInMillis(System.currentTimeMillis());
    }

    public static int getActualDayOfWeek(){
        final int CALENDAR_DAY_OF_WEEK_SHIFT = 2;
        refreshTime();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(day == Calendar.SUNDAY) return 6;
        else return day - CALENDAR_DAY_OF_WEEK_SHIFT;
    }

    public static int getActualHour(){
        refreshTime();
        return calendar.get(Calendar.HOUR);
    }

    public static int getActualMinute(){
        refreshTime();
        return calendar.get(Calendar.MINUTE);
    }

    public static Calendar getCalendar(){
        refreshTime();
        return calendar;
    }
}
