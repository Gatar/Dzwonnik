package com.wordpress.gatarblog.dzwonnik;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Class use for set actual value of volume/vibration as BroadcastReceiver.
 */

public class RingtoneSwitcher extends BroadcastReceiver {

    private final String EXTRA_VIBRATION = "com.wordpress.gatarblog.dzwonnik.VIBRA";
    private final String EXTRA_VOLUME = "com.wordpress.gatarblog.dzwonnik.VOLUME";

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar;
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        String message = "CHANGES!!!! New Volume: " + intent.getIntExtra(EXTRA_VOLUME,-1) + " Vibration " + intent.getBooleanExtra(EXTRA_VIBRATION,false) + " time:" + calendar.getTime().toString();
        System.out.println(message);
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
