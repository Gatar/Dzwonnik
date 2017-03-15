package com.wordpress.gatarblog.dzwonnik;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Receiver started everyday at 00:00 or after restart the phone.
 */

public class DailyReceiver extends BroadcastReceiver {

    private Context context;
    private RingtoneStatesDatabase database;
    private ArrayList<RingtoneState> ringtoneStates;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("~~~~~~~~Zmiana dzwonków na dziś zostanie ustawiona");
        this.context = context;
        database = createDatabaseConnection(context);
        ringtoneStates = loadRingtoneStatesFromDB();
        setTodayRingtoneStatesAlarms();
        makeToast("Zmiana dzwonków na dziś ustawiona");
        System.out.println("~~~~~~~~~~Zmiana dzwonków na dziś jest ustawiona");
    }

    private ArrayList<RingtoneState> loadRingtoneStatesFromDB(){
        return database.getAllStates();
    }

    private RingtoneStatesDatabase createDatabaseConnection(Context context){
        return new RingtoneStatesDatabaseImpl(context);
    }

    private void setTodayRingtoneStatesAlarms(){
        int actualDayOfWeek = getActualDayOfWeek();
        for (RingtoneState ringtoneState : ringtoneStates) {
            if(shouldBeAlarmStartedNow(ringtoneState,actualDayOfWeek)){
                ringtoneState.useRingtoneState(context);
            }
        }
    }

    private boolean shouldBeAlarmStartedNow(RingtoneState state, int dayOfWeek){
        return state.getWeekDays()[dayOfWeek];
    }

    private int getActualDayOfWeek(){
        final int CALENDAR_DAY_OF_WEEK_SHIFT = 2;
        Calendar calendar;
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(day == Calendar.SUNDAY) return 6;
        else return day - CALENDAR_DAY_OF_WEEK_SHIFT;
    }

    private void makeToast(String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
