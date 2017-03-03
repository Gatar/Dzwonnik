package com.wordpress.gatarblog.dzwonnik;

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

    public RingtoneState(int volumeValue, boolean vibration, int hour, int minute) {
        this.volumeValue = volumeValue;
        this.vibration = vibration;
        this.hour = hour;
        this.minute = minute;
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
}
