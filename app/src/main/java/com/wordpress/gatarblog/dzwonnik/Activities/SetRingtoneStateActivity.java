package com.wordpress.gatarblog.dzwonnik.Activities;

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

import com.wordpress.gatarblog.dzwonnik.Database.RingtoneStatesDatabase;
import com.wordpress.gatarblog.dzwonnik.Database.RingtoneStatesDatabaseImpl;
import com.wordpress.gatarblog.dzwonnik.R;
import com.wordpress.gatarblog.dzwonnik.RingtoneState;


/**
 * Class responsible for get from user information to create new/update existing single ringtone sets state.
 */
public class SetRingtoneStateActivity extends AppCompatActivity {

    private SeekBar volumeSeek;
    private TextView volumeTextView;
    private Button acceptButton;
    private Button deleteButton;
    private CheckBox vibrationCheck;
    private CheckBox[] weekDay = new CheckBox[7];
    private TimePicker timePicker;

    private int volumeValue = 0;
    private RingtoneState state;
    private RingtoneStatesDatabase database;

    private final String RINGTONE_STATE_EXTRA = "RingtoneState";

    @Override
    public Context getBaseContext() {
        return super.getBaseContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume_change);
        database = createDatabaseConnection(getBaseContext());

        createViewObjectReferences();

        volumeSeek.setOnSeekBarChangeListener(volumeSeekBarBehaviour());
        acceptButton.setOnClickListener(acceptButtonBehaviour());
        deleteButton.setOnClickListener(deleteButtonBehaviour());

        checkRingtoneStatePresence();
    }

    /**
     * Check is there in Intent extras {@link RingtoneState} object and read them.
     */
    private void checkRingtoneStatePresence(){
        RingtoneState state = (RingtoneState) getIntent().getSerializableExtra(RINGTONE_STATE_EXTRA);
        if(state != null){
            this.state = state;
            loadRingtoneStateToView(state);
        }
    }

    /**
     * Put {@link RingtoneState} object values into the UI view.
     * @param state object to put
     */
    private void loadRingtoneStateToView(RingtoneState state){
        volumeSeek.setProgress(state.getVolumeValue());
        volumeTextView.setText(String.valueOf(state.getVolumeValue()));
        for (int i = 0; i < state.getWeekDays().length; i++) {
            weekDay[i].setChecked(state.getWeekDays()[i]);
        }
        vibrationCheck.setChecked(state.isVibration());
        timePicker.setCurrentHour(state.getHour());
        timePicker.setCurrentMinute(state.getMinute());
    }

    private RingtoneStatesDatabase createDatabaseConnection(Context context){
        return new RingtoneStatesDatabaseImpl(context);
    }

    private void createViewObjectReferences(){
        volumeSeek = (SeekBar) findViewById(R.id.seekVolume);
        volumeTextView = (TextView) findViewById(R.id.textVolumePercent);
        acceptButton = (Button) findViewById(R.id.buttonAccept);
        deleteButton = (Button) findViewById(R.id.buttonDelete);
        vibrationCheck = (CheckBox) findViewById(R.id.checkVibra);
        weekDay[0] = (CheckBox) findViewById(R.id.checkMonday);
        weekDay[1] = (CheckBox) findViewById(R.id.checkTuesday);
        weekDay[2] = (CheckBox) findViewById(R.id.checkWednesday);
        weekDay[3] = (CheckBox) findViewById(R.id.checkThursday);
        weekDay[4] = (CheckBox) findViewById(R.id.checkFriday);
        weekDay[5] = (CheckBox) findViewById(R.id.checkSaturday);
        weekDay[6] = (CheckBox) findViewById(R.id.checkSunday);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
    }

    private View.OnClickListener acceptButtonBehaviour(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStateAlreadyExist()) {
                    fillRingtoneState();
                    database.updateState(state);
                    showStateInConsole(state);
                    state.useRingtoneState(getBaseContext());
                }else {
                    state = new RingtoneState();
                    fillRingtoneState();
                    database.addState(state);
                    showStateInConsole(state);
                    state.useRingtoneState(getBaseContext());
                }
                backToMainActivity();
            }
        };
    }

    /**
     * Check does state object exist.
     * @return true - state exist, should be only updated; false - state not exist, should be created.
     */
    private boolean isStateAlreadyExist(){
        return state != null;
    }

    private void fillRingtoneState(){
        state.setHour(timePicker.getCurrentHour());
        state.setMinute(timePicker.getCurrentMinute());
        if(checkIsSetMidnight(state.getHour(),state.getMinute())) state.setMinute(1);
        state.setVibration(vibrationCheck.isChecked());
        state.setVolumeValue(volumeValue);
        state.setWeekDays(getWeekDaysFromUser());
    }

    private boolean checkIsSetMidnight(int hour, int minute){
        return (hour == 0) && (minute == 0);
    }

    /**
     * Read states of week days check boxes from UI.
     * @return array of booleans where 0 - monday, 1 - tuesday....
     */
    private boolean[] getWeekDaysFromUser(){
        boolean[] week = new boolean[7];
        for(int i = 0; i < weekDay.length; i++){
            week[i] = weekDay[i].isChecked();
        }
        return week;
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

    /**
     * Function used only for see all State parameters in console.
     * @param state Ringtone State object to show
     */
    private void showStateInConsole(RingtoneState state){
        System.out.println("id: " + state.getId());
        System.out.println("Hour: " + state.getHour());
        System.out.println("Minute: " + state.getMinute());
        System.out.println("Vibration: " + state.isVibration());
        System.out.println("Volume: " + state.getVolumeValue());
        for (int i = 0; i < state.getWeekDays().length; i++) {
            System.out.println("Day " + (i + 1) + " " + state.getWeekDays()[i]);
        }
    }

    private View.OnClickListener deleteButtonBehaviour(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state != null){
                    state.removeRingtoneState(getBaseContext());
                    database.deleteState(state);
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
