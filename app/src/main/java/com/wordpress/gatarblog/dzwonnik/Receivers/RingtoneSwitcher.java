package com.wordpress.gatarblog.dzwonnik.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;

import com.wordpress.gatarblog.dzwonnik.R;
import com.wordpress.gatarblog.dzwonnik.States.VolumeValues;

/**
 * Class use for set actual value of volume/vibration as BroadcastReceiver.
 */
public class RingtoneSwitcher extends BroadcastReceiver {

    private final String EXTRA_VIBRATION = "com.wordpress.gatarblog.dzwonnik.VIBRA";
    private final String EXTRA_VOLUME = "com.wordpress.gatarblog.dzwonnik.VOLUME";
    private final String EXTRA_SILENT = "com.wordpress.gatarblog.dzwonnik.SILENT";
    private final float USER_VOLUME_CHANGE_RANGE = 7f;
    private AudioManager audioManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        String message = setSelectedStream(context, intent);
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    /**
     * Set stream selected in intent and in case RINGER_MODE_NORMAL start setStreamVolume method.
     * @param intent received intent
     * @return message for toast to user and write it in logs
     */
    private String setSelectedStream(Context context, Intent intent){
        boolean isVibration = intent.getBooleanExtra(EXTRA_VIBRATION,false);
        boolean isSilent = intent.getBooleanExtra(EXTRA_SILENT,false);
        VolumeValues volumeValues = (VolumeValues) intent.getSerializableExtra(EXTRA_VOLUME);

        if(isSilent) audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        else if(isVibration) audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        else{
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            setStreamVolume(volumeValues.getVolumeRingtone(), AudioManager.STREAM_RING);
            setStreamVolume(volumeValues.getVolumeNotification(), AudioManager.STREAM_NOTIFICATION);
            setStreamVolume(volumeValues.getVolumeSystem(), AudioManager.STREAM_SYSTEM);
        }

        setStreamVolume(volumeValues.getVolumeMedia(), AudioManager.STREAM_MUSIC);    //Stream music volume have to be changed always

        return context.getString(R.string.volume_rate) + ": " + volumeValues.getVolumeRingtone() + "/7 " +
                context.getString(R.string.vibration_mode) + ": " + isVibration + " " +
                context.getString(R.string.silent_mode) + ": " + isSilent;
    }

    /**
     * Set colume value of selected stream adapting volume range from user (0 to 7) to range specific to chosen stream (for example for music is 0 to 14).
     * @param volume value of new volume, have to be from 0 to 7
     * @param streamType AudioManager stream type
     */
    private int setStreamVolume(int volume, int streamType){
        int realMaxVolume = audioManager.getStreamMaxVolume(streamType);
        volume = calculateNewVolume(volume,realMaxVolume);
        audioManager.setStreamVolume(streamType,volume,0);
        return volume;
    }

    /**
     *
     * @param volume value of new volume, have to be from 0 to 7
     * @param realMaxVolume maximum volume value depending on stream type
     * @return new calculated volume for set on stream rounded down to closest multiply of 7
     */
    private int calculateNewVolume(int volume, int realMaxVolume){
        float multiply = (float)realMaxVolume / USER_VOLUME_CHANGE_RANGE;
        return (int)(volume*multiply);
    }
}
