package com.wordpress.gatarblog.dzwonnik;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class responsible for get from user information to create new/update existing single ringtone sets state.
 */
public class RingtoneStateSetActivity extends AppCompatActivity {

    private SeekBar volumeSeek;
    private TextView volumeTextView;
    private Button acceptButton;
    private Button deleteButton;
    private CheckBox vibrationCheck;
    private CheckBox[] weekDay = new CheckBox[7];
    private TimePicker timePicker;
    private RingtoneState state;

    private int volumeValue = 0;

    @Override
    public Context getBaseContext() {
        return super.getBaseContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume_change);

        createViewObjectReferences();

        volumeSeek.setOnSeekBarChangeListener(volumeSeekBarBehaviour());
        acceptButton.setOnClickListener(acceptButtonBehaviour());
        deleteButton.setOnClickListener(deleteButtonBehaviour());
    }

    private void createViewObjectReferences(){
        volumeSeek = (SeekBar) findViewById(R.id.seekVolume);
        volumeTextView = (TextView) findViewById(R.id.textVolumePercent);
        acceptButton = (Button) findViewById(R.id.buttonAccept);
        deleteButton = (Button) findViewById(R.id.buttonDelete);
        vibrationCheck = (CheckBox) findViewById(R.id.checkVibra);
        weekDay[1] = (CheckBox) findViewById(R.id.checkMonday);
        weekDay[2] = (CheckBox) findViewById(R.id.checkTuesday);
        weekDay[3] = (CheckBox) findViewById(R.id.checkWednesday);
        weekDay[4] = (CheckBox) findViewById(R.id.checkThursday);
        weekDay[5] = (CheckBox) findViewById(R.id.checkFriday);
        weekDay[6] = (CheckBox) findViewById(R.id.checkSaturday);
        weekDay[0] = (CheckBox) findViewById(R.id.checkSunday);  //Android's Calendar start counting days of the week from Sunday as first....
        timePicker = (TimePicker) findViewById(R.id.timePicker);
    }

    private View.OnClickListener acceptButtonBehaviour(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = createRingtoneState();
                state.useRingtoneState();
                showStateInConsole(state);
                backToMainActivity();
            }
        };
    }

    private RingtoneState createRingtoneState(){
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        boolean vibration = vibrationCheck.isChecked();

        RingtoneState state = new RingtoneState(volumeValue,vibration,hour,minute,getBaseContext());
        state.setWeekDays(getWeekDays());
        return state;
    }

    private HashSet<WeekDays> getWeekDays(){
        final int WEEK_DAYS_NUMBER = 7;
        HashSet<WeekDays> days = new HashSet<>();

        for(int i = 0; i < WEEK_DAYS_NUMBER; i++){
            if(weekDay[i].isChecked()) days.add(WeekDays.value[i]);
        }
        return days;
    }

    private SeekBar.OnSeekBarChangeListener volumeSeekBarBehaviour (){
        return new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressStart, boolean fromUser) {
                volumeValue = progressStart;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String volumeText = String.valueOf(volumeValue) + " %";
                volumeTextView.setText(volumeText);
            }
        };
    }

    private void showStateInConsole(RingtoneState state){
        System.out.println("Hour: " + state.getHour());
        System.out.println("Minute: " + state.getMinute());
        System.out.println("Vibration: " + state.isVibration());
        System.out.println("Volume: " + state.getVolumeValue());
        for(WeekDays day : state.getWeekDays()){
            System.out.println("Day: " + day);
        }
    }

    private View.OnClickListener deleteButtonBehaviour(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state != null){
                    state.removeRingtoneState();
                }
                backToMainActivity();
            }
        };
    }

    private void backToMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


}
