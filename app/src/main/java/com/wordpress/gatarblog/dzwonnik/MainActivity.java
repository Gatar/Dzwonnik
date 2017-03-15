package com.wordpress.gatarblog.dzwonnik;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startDailyReceiver();

        final Button button = (Button) findViewById(R.id.addNewRingChange);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSetRingtoneStateActivity();
            }
        });
    }

    private void toSetRingtoneStateActivity(){
        Intent intent = new Intent(this,SetRingtoneStateActivity.class);
        startActivity(intent);
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
