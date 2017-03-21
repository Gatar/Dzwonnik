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
    private AudioManager audioManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isVibration = intent.getBooleanExtra(EXTRA_VIBRATION,false);
        boolean isSilent = intent.getBooleanExtra(EXTRA_SILENT,false);
        int volumeValue = intent.getIntExtra(EXTRA_VOLUME,0);
        String message = "Zmiana głośności na: " + volumeValue + "/7" + " Vibra: " + isVibration + " Cisza: " + isSilent;

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if(isSilent) audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        else if(isVibration) audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        else{
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            setStreamVolume(volumeValue, AudioManager.STREAM_RING);
            setStreamVolume(volumeValue, AudioManager.STREAM_ALARM);
            setStreamVolume(volumeValue, AudioManager.STREAM_MUSIC);
        }

        System.out.println(message);
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    private void setStreamVolume(int newVolume, int streamType) {
        int actualVolume = audioManager.getStreamVolume(streamType);
        if (isMusicStream(streamType))
            newVolume *= 2; //maximum value for MusicStream is 15, not 7 as other.
        System.out.println("Stream type: " + streamType + " actual new volume " + newVolume);
        while (actualVolume != newVolume) {
            if (actualVolume < newVolume)
                audioManager.adjustStreamVolume(streamType, AudioManager.ADJUST_RAISE, 0);
            else if (actualVolume > newVolume)
                audioManager.adjustStreamVolume(streamType, AudioManager.ADJUST_LOWER, 0);
            actualVolume = audioManager.getStreamVolume(streamType);
        }
    }

    private boolean isMusicStream(int streamType) {
        return streamType == AudioManager.STREAM_MUSIC;
    }
}
