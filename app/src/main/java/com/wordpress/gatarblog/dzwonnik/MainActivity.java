package com.wordpress.gatarblog.dzwonnik;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button buttonAddNew;
    private ListView listStates;
    private RingtoneStatesDatabase database;

    private final String RINGTONE_STATE_EXTRA = "RingtoneState";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startDailyReceiver();

        buttonAddNew = (Button) findViewById(R.id.addNewRingChange);
        listStates = (ListView) findViewById(R.id.ringChangeList);
        database = new RingtoneStatesDatabaseImpl(getBaseContext());

        buttonAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSetRingtoneStateActivity();
            }
        });
        listStates.setAdapter(new StateListAdapter(getBaseContext(), R.layout.state_list_row, getStatesFromDatabase()));
        listStates.setOnItemClickListener(setListViewListener());
    }

    private AdapterView.OnItemClickListener setListViewListener(){
        System.out.println("1111111`");
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("2222222222");
                RingtoneState state = (RingtoneState) listStates.getItemAtPosition(position);
                toSetRingtoneStateActivity(state);
            }
        };
    }

    private void toSetRingtoneStateActivity(){
        Intent intent = new Intent(this,SetRingtoneStateActivity.class);
        startActivity(intent);
    }

    private void toSetRingtoneStateActivity(RingtoneState state){
        Intent intent = new Intent(this,SetRingtoneStateActivity.class);
        intent.putExtra(RINGTONE_STATE_EXTRA,state);
        startActivity(intent);
    }

    private ArrayList<RingtoneState> getStatesFromDatabase(){
        return database.getAllStates();
    }

    private void startDailyReceiver(){
        Context context = this.getBaseContext();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,20);
        calendar.set(Calendar.MINUTE,20);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,DailyReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, alarmIntent);
    }


}
