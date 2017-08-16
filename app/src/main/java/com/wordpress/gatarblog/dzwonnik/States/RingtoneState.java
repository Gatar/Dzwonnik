package com.wordpress.gatarblog.dzwonnik.States;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.wordpress.gatarblog.dzwonnik.Receivers.RingtoneSwitcher;
import com.wordpress.gatarblog.dzwonnik.TimeManager;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

/**
 * Class with all data of ringtone state.
 */

@Getter
@Setter
public class RingtoneState implements Serializable, Comparable<RingtoneState> {

    private static final long serialVersionUID = 6541265L;

    private long id;
    private boolean vibration;
    private boolean silent;
    private int hour;
    private int minute;
    private boolean[] weekDays;
    private VolumeValues volumeValues;

    private transient TimeEvent timeEvent;

    public RingtoneState() {
        weekDays = new boolean[7];
    }

    public RingtoneState(int ringtoneVolumeValue, boolean vibration, int hour, int minute) {
        this();
        volumeValues = new VolumeValues(ringtoneVolumeValue);
        this.vibration = vibration;
        this.hour = hour;
        this.minute = minute;
    }

    //TODO erase this two methods from usage in program, use only pure Volume Values
    public int getRingtoneVolumeValue() {
        return volumeValues.getVolumeRingtone();
    }

    public void setRingtoneVolumeValue(int ringtoneVolumeValue) {
        volumeValues = new VolumeValues(ringtoneVolumeValue);
    }


    /**
     * Stop all events, but not erase them. Method creates timeEvent if it not exist. It's necessary for stop pending alarm.
     */
    public void removeRingtoneState(Context context) {
        if (timeEvent == null) timeEvent = new TimeEvent(context, this);
        timeEvent.stop();
    }


    /**
     * Creates broadcast events for ringtone state switch (single event for each checked weekday)
     */
    public void useRingtoneState(Context context) {
        if (shouldBeAlarmStartedNow()) {
            timeEvent = new TimeEvent(context, this);
            timeEvent.start();
            String toastMessage = hour + ":" + ((minute < 10) ? ("0" + minute) : minute) + " \uD83D\uDD50";
            System.out.println(toastMessage);
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Set volume value as the same for every streams
     *
     * @param volumeValue value of volume to set up
     */
    public void setAllRingtoneSameValue(int volumeValue) {
        volumeValues.setVolumeRingtone(volumeValue);
        volumeValues.setVolumeNotification(volumeValue);
        volumeValues.setVolumeMedia(volumeValue);
        volumeValues.setVolumeSystem(volumeValue);
    }


    private boolean shouldBeAlarmStartedNow() {
        return checkDayOfWeek() && checkActualHour();
    }

    private boolean checkDayOfWeek() {
        return weekDays[TimeManager.getActualDayOfWeek()];
    }

    private boolean checkActualHour() {
        int actualHour = TimeManager.getActualHour();
        int actualMinute = TimeManager.getActualMinute();
        if (actualHour > hour) return false;
        else if (actualHour == hour && actualMinute > minute) return false;
        else return true;
    }


    @Override
    public int compareTo(RingtoneState state2) {
        if (this.getHour() != state2.getHour()) return (getHour() > state2.getHour()) ? 1 : -1;
        else return (getMinute() > state2.getMinute()) ? 1 : -1;
    }

    private class TimeEvent{

        private Calendar calendar;
        private AlarmManager alarmManager;
        private Intent intent;
        private PendingIntent alarmIntent;
        private final Context context;
        private final RingtoneState state;

        private final String EXTRA_RINGTONE_STATE = "com.wordpress.gatarblog.dzwonnik.RingtoneState";

        public TimeEvent(Context context, RingtoneState state) {
            this.context = context;
            this.state = state;
        }

        void start() {
            setTime();
            createIntentsAndAlarmManager();
            setAlarm();
        }

        void stop() {
            createIntentsAndAlarmManager();
            alarmManager.cancel(alarmIntent);
        }

        private void createIntentsAndAlarmManager() {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            intent = new Intent(context, RingtoneSwitcher.class);
            byte[] data = SerializationUtils.serialize(state);  //passing Serializable objects directly through BroadcastReceiver can case NullPointerException
            intent.putExtra(EXTRA_RINGTONE_STATE, data);
            alarmIntent = PendingIntent.getBroadcast(context, (int) id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        private void setTime() {
            calendar = TimeManager.getCalendar();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
        }

        private void setAlarm() {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        }
    }
}
