package com.wordpress.gatarblog.dzwonnik.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.wordpress.gatarblog.dzwonnik.Database.RingtoneStatesDatabase;
import com.wordpress.gatarblog.dzwonnik.Database.RingtoneStatesDatabaseImpl;
import com.wordpress.gatarblog.dzwonnik.R;
import com.wordpress.gatarblog.dzwonnik.RingtoneState;
import com.wordpress.gatarblog.dzwonnik.TimeManager;

import java.util.ArrayList;

/**
 * Receiver started everyday at 00:00 or after restart the phone (provided by {@link OnBootReceiver})
 */
public class DailyReceiver extends BroadcastReceiver {

    private Context context;
    private RingtoneStatesDatabase database;
    private ArrayList<RingtoneState> ringtoneStates;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        database = createDatabaseConnection(context);
        ringtoneStates = loadRingtoneStatesFromDB();
        setTodayRingtoneStatesAlarms();
        makeToast(context.getString(R.string.today_states_set));
        System.out.println("The bells shall ring like bloody hell!!!");
    }

    private ArrayList<RingtoneState> loadRingtoneStatesFromDB(){
        return database.getAllStates();
    }

    private RingtoneStatesDatabase createDatabaseConnection(Context context){
        return new RingtoneStatesDatabaseImpl(context);
    }

    private void setTodayRingtoneStatesAlarms(){
        for (RingtoneState ringtoneState : ringtoneStates) {
            if(shouldBeAlarmStartedNow(ringtoneState)){
                ringtoneState.useRingtoneState(context);
            }
        }
    }

    /**
     * Check is today weekday set as true in ringtone state object (to start the alarm).
     * @param state received state
     * @return true - alarm should be started today, false - alarm mustn't be started today
     */
    private boolean shouldBeAlarmStartedNow(RingtoneState state){
        return state.getWeekDays()[TimeManager.getActualDayOfWeek()];
    }

    private void makeToast(String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
