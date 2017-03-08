package com.wordpress.gatarblog.dzwonnik;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.TimeZone;

import java.util.Calendar;
import java.util.HashSet;

/**
 * Class with all data of ringtone state.
 */

public class RingtoneState {
    private int volumeValue;
    private boolean vibration;
    private int hour;
    private int minute;
    private HashSet<WeekDays> weekDays;
    private Context context;
    private HashSet<TimeEvent> timeEvents;

    public RingtoneState(int volumeValue, boolean vibration, int hour, int minute, Context context) {
        this.volumeValue = volumeValue;
        this.vibration = vibration;
        this.hour = hour;
        this.minute = minute;
        this.context = context;
        timeEvents = new HashSet<>();
    }

    public int getVolumeValue() {
        return volumeValue;
    }

    public void setVolumeValue(int volumeValue) {
        this.volumeValue = volumeValue;
    }

    public boolean isVibration() {
        return vibration;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public HashSet<WeekDays> getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(HashSet<WeekDays> weekDays) {
        this.weekDays = weekDays;
    }

    /**
     * Creates broadcast events for ringtone state switch (single event for each checked weekday)
     */
    public void useRingtoneState(){
        removeRingtoneState();
        timeEvents.clear();

        for(WeekDays day : weekDays){
            TimeEvent event = new TimeEvent(day);
            event.start();
            timeEvents.add(event);
        }
    }

    /**
     * Stop all events, but not erase them.
     */
    public void removeRingtoneState(){
        for(TimeEvent event : timeEvents){
            event.stop();
        }
    }

    private class TimeEvent{
        private Calendar calendar;
        private AlarmManager alarmManager;
        private PendingIntent alarmIntent;
        private int dayOfWeek;

        private final String EXTRA_VIBRATION = "com.wordpress.gatarblog.dzwonnik.VIBRA";
        private final String EXTRA_VOLUME = "com.wordpress.gatarblog.dzwonnik.VOLUME";
        private final int ENUM_TO_WEEKDAY_COUNTER_SHIFT = 1;

        TimeEvent(WeekDays day) {
            dayOfWeek = day.ordinal() + ENUM_TO_WEEKDAY_COUNTER_SHIFT;
        }

        void start(){
            setTime();
            setRepeating();
        }

        void stop(){
            alarmManager.cancel(alarmIntent);
        }

        private void setTime(){
            calendar = Calendar.getInstance();
            //calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.DAY_OF_WEEK,dayOfWeek); //Sunday - 1, Monday - 2 etc
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
        }

        //TODO Tu jest coś nie tak, alarm manager włącza się o określonej godzinie, ale nie działa update
        private void setRepeating(){
            final long INTERVAL_WEEK = AlarmManager.INTERVAL_DAY * 7;

            alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context,RingtoneSwitcher.class);
            intent.putExtra(EXTRA_VIBRATION,vibration);
            intent.putExtra(EXTRA_VOLUME,volumeValue);
            int alarmId = (int)System.currentTimeMillis()*(-1);
            System.out.println("Alarm id: " + alarmId);
            alarmIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 120000, alarmIntent);
        }

    }
}
