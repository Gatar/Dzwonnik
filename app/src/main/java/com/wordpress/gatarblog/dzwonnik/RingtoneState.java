package com.wordpress.gatarblog.dzwonnik;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.wordpress.gatarblog.dzwonnik.Receivers.RingtoneSwitcher;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Class with all data of ringtone state.
 */

public class RingtoneState implements Serializable, Comparable<RingtoneState> {
    private long id;
    private int volumeValue;
    private boolean vibration;
    private boolean silent;
    private int hour;
    private int minute;
    private boolean[] weekDays;
    private TimeEvent timeEvent;

    public RingtoneState() {
        weekDays = new boolean[7];
    }

    public RingtoneState(int volumeValue, boolean vibration, int hour, int minute) {
        this.volumeValue = volumeValue;
        this.vibration = vibration;
        this.hour = hour;
        this.minute = minute;
        weekDays = new boolean[7];
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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


    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
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

    public boolean[] getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(boolean[] weekDays) {
        this.weekDays = weekDays;
    }

    /**
     * Stop all events, but not erase them. Method creates timeEvent if it not exist. It's necessary for stop pending alarm.
     */
    public void removeRingtoneState(Context context){
        if(timeEvent == null) timeEvent = new TimeEvent(context);
        timeEvent.stop();
    }


    /**
     * Creates broadcast events for ringtone state switch (single event for each checked weekday)
     */
    public void useRingtoneState(Context context){
        if(shouldBeAlarmStartedNow()) {
            timeEvent = new TimeEvent(context);
            timeEvent.start();
            String toastMessage = hour + ":" + ((minute < 10) ? ("0" + minute) : minute) + " ringtone set";
            System.out.println(toastMessage);
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean shouldBeAlarmStartedNow(){
        return checkDayOfWeek() && checkActualHour();
    }

    private boolean checkDayOfWeek(){
        return weekDays[TimeManager.getActualDayOfWeek()];
    }

    private boolean checkActualHour(){
        int actualHour = TimeManager.getActualHour();
        int actualMinute = TimeManager.getActualMinute();
        System.out.println("Actual hour: " + actualHour + " vs saved time: " + hour);
        if(actualHour > hour) return false;
        else if(actualHour == hour && actualMinute > minute) return false;
        else return true;
    }


    @Override
    public int compareTo(RingtoneState state2){
        if(this.getHour() != state2.getHour()) return (getHour() > state2.getHour()) ? 1 : -1;
        else return (getMinute() > state2.getMinute()) ? 1 : -1;
    }

        private class TimeEvent{
            private Calendar calendar;
            private AlarmManager alarmManager;
            private Intent intent;
            private PendingIntent alarmIntent;
            private Context context;

            private final String EXTRA_VIBRATION = "com.wordpress.gatarblog.dzwonnik.VIBRA";
            private final String EXTRA_SILENT = "com.wordpress.gatarblog.dzwonnik.SILENT";
            private final String EXTRA_VOLUME = "com.wordpress.gatarblog.dzwonnik.VOLUME";

            public TimeEvent(Context context) {
                this.context = context;
            }

            void start(){
                setTime();
                createIntentsAndAlarmManager();
                setAlarm();
            }

            void stop(){
                createIntentsAndAlarmManager();
                alarmManager.cancel(alarmIntent);
            }

            private void createIntentsAndAlarmManager(){
                alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                intent = new Intent(context,RingtoneSwitcher.class);
                intent.putExtra(EXTRA_VIBRATION,vibration);
                intent.putExtra(EXTRA_VOLUME,volumeValue);
                intent.putExtra(EXTRA_SILENT,silent);
                alarmIntent = PendingIntent.getBroadcast(context, (int)id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            private void setTime(){
                calendar = TimeManager.getCalendar();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
            }

            private void setAlarm(){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
            }
        }
}
