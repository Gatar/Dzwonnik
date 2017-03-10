package com.wordpress.gatarblog.dzwonnik;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Class with all data of ringtone state.
 */

public class RingtoneState {
    private long id;
    private int volumeValue;
    private boolean vibration;
    private int hour;
    private int minute;
    private boolean[] weekDays;
    private Context context;
    private TimeEvent timeEvent;

    public RingtoneState() {
    }

    public RingtoneState(int volumeValue, boolean vibration, int hour, int minute, Context context) {
        this.volumeValue = volumeValue;
        this.vibration = vibration;
        this.hour = hour;
        this.minute = minute;
        this.context = context;
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
     * Creates broadcast events for ringtone state switch (single event for each checked weekday)
     */
    public void useRingtoneState(){
        timeEvent = new TimeEvent();
        timeEvent.start();
    }

    /**
     * Stop all events, but not erase them.
     */
    public void removeRingtoneState(){
        timeEvent.stop();
    }

        private class TimeEvent{
            private Calendar calendar;
            private AlarmManager alarmManager;
            private PendingIntent alarmIntent;

            private final String EXTRA_VIBRATION = "com.wordpress.gatarblog.dzwonnik.VIBRA";
            private final String EXTRA_VOLUME = "com.wordpress.gatarblog.dzwonnik.VOLUME";

            void start(){
                setTime();
                setAlarm();
            }

            void stop(){
                alarmManager.cancel(alarmIntent);
            }

            private void setTime(){
                calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
            }

            private void setAlarm(){
                alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(context,RingtoneSwitcher.class);
                intent.putExtra(EXTRA_VIBRATION,vibration);
                intent.putExtra(EXTRA_VOLUME,volumeValue);
                alarmIntent = PendingIntent.getBroadcast(context, generateAlarmId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
            }

            private int generateAlarmId(){
                return (int)System.currentTimeMillis()*(-1);
            }

        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RingtoneState state = (RingtoneState) o;

        if (id != state.id) return false;
        if (volumeValue != state.volumeValue) return false;
        if (vibration != state.vibration) return false;
        if (hour != state.hour) return false;
        if (minute != state.minute) return false;
        return Arrays.equals(weekDays, state.weekDays);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + volumeValue;
        result = 31 * result + (vibration ? 1 : 0);
        result = 31 * result + hour;
        result = 31 * result + minute;
        result = 31 * result + Arrays.hashCode(weekDays);
        return result;
    }
}
