package com.wordpress.gatarblog.dzwonnik.Activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wordpress.gatarblog.dzwonnik.Database.RingtoneStatesDatabase;
import com.wordpress.gatarblog.dzwonnik.Database.RingtoneStatesDatabaseImpl;
import com.wordpress.gatarblog.dzwonnik.R;
import com.wordpress.gatarblog.dzwonnik.States.VolumeValues;
import com.wordpress.gatarblog.dzwonnik.States.RingtoneState;
import com.wordpress.gatarblog.dzwonnik.TimeManager;


/**
 * Class responsible for get from user information to create new/update existing single ringtone sets state.
 */
public class SetRingtoneStateActivity extends AppCompatActivity {

    private SeekBar volumeSeek;
    private TextView volumeTextView;
    private Button acceptButton;
    private Button deleteButton;
    private CheckBox vibrationCheck;
    private CheckBox silentCheck;
    private final CheckBox[] weekDay = new CheckBox[7];
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
        vibrationCheck.setOnCheckedChangeListener(vibrationCheckBoxBehaviour());
        silentCheck.setOnCheckedChangeListener(silentCheckBoxBehaviour());

        loadRingtoneStateObject();
        setRingtoneStateOnView(state);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Main");
        spec.setContent(R.id.main);
        spec.setIndicator("Main");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Volume");
        spec.setContent(R.id.volume);
        spec.setIndicator("Volume");
        host.addTab(spec);
    }

    /**
     * Check is there in Intent extras {@link RingtoneState} object and read them.
     */
    private void loadRingtoneStateObject(){
        RingtoneState state = (RingtoneState) getIntent().getSerializableExtra(RINGTONE_STATE_EXTRA);
        if(state != null){
            this.state = state;
        }else{
            this.state = getActualRingtoneSettings();
        }
    }

    /**
     * Get actual ringtone state values from phone settings.
     * @return actual RingtoneState object
     */
    private RingtoneState getActualRingtoneSettings(){
        AudioManager alarmManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        RingtoneState state = new RingtoneState();
        VolumeValues volumeValues = new VolumeValues(
                alarmManager.getStreamVolume(AudioManager.STREAM_RING),
                alarmManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION),
                alarmManager.getStreamVolume(AudioManager.STREAM_SYSTEM),
                alarmManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        state.setVolumeValues(volumeValues);
        state.setId(-1);
        state.setSilent(alarmManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT);
        state.setVibration(alarmManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE);
        state.getWeekDays()[TimeManager.getActualDayOfWeek()] = true;
        state.setHour(TimeManager.getActualHour());
        state.setMinute(TimeManager.getActualMinute());
        return state;
    }


    /**
     * Put {@link RingtoneState} object values into the UI view.
     * @param state object to put
     */
    private void setRingtoneStateOnView(RingtoneState state){
        volumeSeek.setProgress(state.getRingtoneVolumeValue());
        volumeTextView.setText(String.valueOf(state.getRingtoneVolumeValue()));
        for (int i = 0; i < state.getWeekDays().length; i++) {
            weekDay[i].setChecked(state.getWeekDays()[i]);
        }
        vibrationCheck.setChecked(state.isVibration());
        silentCheck.setChecked(state.isSilent());
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
        silentCheck = (CheckBox) findViewById(R.id.checkSilent);
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
                if(isStateModified()) {
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
     * Checking on vibration automatically turn volume to 0 and disable silent mode.
     * @return behariouv anonymous class CompoundButton.OnCheckedChangeListener
     */
    private CompoundButton.OnCheckedChangeListener vibrationCheckBoxBehaviour(){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    disableSilent();
                    volumeSeek.setProgress(0);
                    volumeTextView.setText("0 / 7");
                }
            }
        };
    }

    /**
     * Checking on silent automatically turn volume to 0 and disable vibration mode.
     * @return behaviour anonymous class CompoundButton.OnCheckedChangeListener
     */
    private CompoundButton.OnCheckedChangeListener silentCheckBoxBehaviour(){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    disableVibration();
                    volumeSeek.setProgress(0);
                    volumeTextView.setText("0 / 7");
                }
            }
        };
    }

    /**
     * Check does state object exist.
     * @return true - state exist, should be only updated; false - state not exist, should be created.
     */
    private boolean isStateModified(){
        final int STATE_NON_EXIST = -1;
        return state.getId() != STATE_NON_EXIST;
    }

    private void fillRingtoneState(){
        state.setHour(timePicker.getCurrentHour());
        state.setMinute(timePicker.getCurrentMinute());
        if(checkIsSetMidnight(state.getHour(),state.getMinute())) state.setMinute(1);
        state.setVibration(vibrationCheck.isChecked());
        state.setSilent(silentCheck.isChecked());
        state.setRingtoneVolumeValue(volumeValue);
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
                if(volumeValue > 0){
                    disableSilent();
                    disableVibration();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String volumeText = String.valueOf(volumeValue) + " / 7";
                volumeTextView.setText(volumeText);
            }
        };
    }

    private void disableSilent(){
        silentCheck.setChecked(false);
    }

    private void disableVibration(){
        vibrationCheck.setChecked(false);
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
        System.out.println("Silence: " + state.isSilent());
        System.out.println("Volume: " + state.getRingtoneVolumeValue());
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
