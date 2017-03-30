package com.wordpress.gatarblog.dzwonnik.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Class use for set actual value of volume/vibration as BroadcastReceiver.
 */
public class RingtoneSwitcher extends BroadcastReceiver {

    private final String EXTRA_VIBRATION = "com.wordpress.gatarblog.dzwonnik.VIBRA";
    private final String EXTRA_VOLUME = "com.wordpress.gatarblog.dzwonnik.VOLUME";
    private final String EXTRA_SILENT = "com.wordpress.gatarblog.dzwonnik.SILENT";
    private final int USER_VOLUME_CHANGE_RANGE = 7;
    private AudioManager audioManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        String message = setSelectedStream(intent);
        System.out.println(message);
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    /**
     * Set stream selected in intent and in case RINGER_MODE_NORMAL start setStreamVolume method.
     * @param intent received intent
     * @return message for toast to user and write it in logs
     */
    private String setSelectedStream(Intent intent){
        boolean isVibration = intent.getBooleanExtra(EXTRA_VIBRATION,false);
        boolean isSilent = intent.getBooleanExtra(EXTRA_SILENT,false);
        int volumeValue = intent.getIntExtra(EXTRA_VOLUME,0);

        if(isSilent) audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        else if(isVibration) audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        else{
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            setStreamVolume(volumeValue, AudioManager.STREAM_RING);
            setStreamVolume(volumeValue, AudioManager.STREAM_NOTIFICATION);
            setStreamVolume(volumeValue, AudioManager.STREAM_ALARM);
            setStreamVolume(volumeValue, AudioManager.STREAM_MUSIC);
        }

        return "Zmiana głośności na: " + volumeValue + "/7" + " Vibra: " + isVibration + " Cisza: " + isSilent;
    }

    /**
     * Set colume value of selected stream adapting volume range from user (0 to 7) to range specific to chosen stream (for example for music is 0 to 14).
     * @param newVolume value of new volume, have to be from 0 to 7
     * @param streamType AudioManager stream type
     */
    private void setStreamVolume(int newVolume, int streamType){
        float multiply = audioManager.getStreamMaxVolume(streamType) / USER_VOLUME_CHANGE_RANGE;
        newVolume = (int)(newVolume*multiply);
        System.out.println("Stream type: " + streamType + " actual new volume " + newVolume);
        audioManager.setStreamVolume(streamType,newVolume,0);
    }
}
