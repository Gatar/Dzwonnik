package com.wordpress.gatarblog.dzwonnik.Activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.wordpress.gatarblog.dzwonnik.Receivers.DailyReceiver;
import com.wordpress.gatarblog.dzwonnik.Database.RingtoneStatesDatabase;
import com.wordpress.gatarblog.dzwonnik.Database.RingtoneStatesDatabaseImpl;
import com.wordpress.gatarblog.dzwonnik.R;
import com.wordpress.gatarblog.dzwonnik.RingtoneState;
import com.wordpress.gatarblog.dzwonnik.Activities.ListAdapter.StateListAdapter;
import com.wordpress.gatarblog.dzwonnik.TimeManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements DatabaseEraseDialog.EntityDelete {

    private Button buttonAddNew;
    private Button buttonErase;
    private ListView listStates;
    private RingtoneStatesDatabase database;

    private final String RINGTONE_STATE_EXTRA = "RingtoneState";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startDailyReceiver();
        database = new RingtoneStatesDatabaseImpl(getBaseContext());

        buttonAddNew = (Button) findViewById(R.id.addNewRingChange);
        buttonErase = (Button) findViewById(R.id.eraseDatabaseButton);
        listStates = (ListView) findViewById(R.id.ringChangeList);


        buttonAddNew.setOnClickListener(newRingtoneStateButtonBehaviour());
        buttonErase.setOnClickListener(deleteButtonBehaviour());
        listStates.setAdapter(new StateListAdapter(getBaseContext(), R.layout.state_list_row, getStatesFromDatabase()));
        listStates.setOnItemClickListener(setListViewListener());

        grantDoNotDisturbAccess();
    }

    private AdapterView.OnItemClickListener setListViewListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RingtoneState state = (RingtoneState) listStates.getItemAtPosition(position);
                toSetRingtoneStateActivity(state);
            }
        };
    }

    private View.OnClickListener newRingtoneStateButtonBehaviour(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSetRingtoneStateActivity();
            }
        };
    }

    private View.OnClickListener deleteButtonBehaviour(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DatabaseEraseDialog dialog = new DatabaseEraseDialog();
                dialog.setEntityDelete(MainActivity.this);
                dialog.show(getSupportFragmentManager(),"delete");
            }
        };
    }

    private void toSetRingtoneStateActivity() {
        Intent intent = new Intent(this, SetRingtoneStateActivity.class);
        startActivity(intent);
    }

    private void toSetRingtoneStateActivity(RingtoneState state) {
        Intent intent = new Intent(this, SetRingtoneStateActivity.class);
        intent.putExtra(RINGTONE_STATE_EXTRA, state);
        startActivity(intent);
    }

    private ArrayList<RingtoneState> getStatesFromDatabase() {
        ArrayList<RingtoneState> states = database.getAllStates();
        Collections.sort(states);
        return states;
    }

    /**
     * Start DailyReceiver, which set on all alarms on this day. Could be started multiple times because alarms are updated if exists before.
     */
    private void startDailyReceiver() {
        Context context = this.getBaseContext();
        Calendar calendar = TimeManager.getCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    /**
     * Ask user for permission to change DoNotDisturb mode.
     */
    private void grantDoNotDisturbAccess() {
        NotificationManager notificationManager =
                (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !notificationManager.isNotificationPolicyAccessGranted())
        {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    public void eraseDatabaseEntities() {
        database.cleanDatabase();
        listStates.setAdapter(new StateListAdapter(getBaseContext(), R.layout.state_list_row, getStatesFromDatabase()));
        Toast.makeText(this,"Lista zmiany głośności została usunięta",Toast.LENGTH_SHORT).show();
    }
}
