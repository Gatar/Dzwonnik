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

public class RingtoneState implements Serializable {
    private long id;
    private int volumeValue;
    private boolean vibration;
    private int hour;
    private int minute;
    private boolean[] weekDays;
    private TimeEvent timeEvent;

    public RingtoneState() {
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
            String toastMessage = hour + ":" + minute + " ringtone set";
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean shouldBeAlarmStartedNow(){
        return checkDayOfWeek() && checkActualHour();
    }

    private boolean checkDayOfWeek(){
        return weekDays[getActualDayOfWeek()];
    }

    private boolean checkActualHour(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int actualHour = calendar.get(Calendar.HOUR_OF_DAY);
        int actualMinute = calendar.get(Calendar.MINUTE);
        if(actualHour > hour) return false;
        else if(actualHour == hour && actualMinute > minute) return false;
        else return true;
    }

    private int getActualDayOfWeek() {
        final int CALENDAR_DAY_OF_WEEK_SHIFT = 2;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SUNDAY) return 6;
        else return day - CALENDAR_DAY_OF_WEEK_SHIFT;
    }

        private class TimeEvent{
            private Calendar calendar;
            private AlarmManager alarmManager;
            private Intent intent;
            private PendingIntent alarmIntent;
            private Context context;

            private final String EXTRA_VIBRATION = "com.wordpress.gatarblog.dzwonnik.VIBRA";
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
                alarmIntent = PendingIntent.getBroadcast(context, (int)id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            private void setTime(){
                calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
            }

            private void setAlarm(){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
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
