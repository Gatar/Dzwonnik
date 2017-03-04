package com.wordpress.gatarblog.dzwonnik;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Class use for set actual value of volume/vibration as BroadcastReceiver.
 */

public class RingtoneSwitcher extends BroadcastReceiver {

    private final String EXTRA_VIBRATION = "com.wordpress.gatarblog.dzwonnik.VIBRA";
    private final String EXTRA_VOLUME = "com.wordpress.gatarblog.dzwonnik.VOLUME";

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = "Volume: " + intent.getIntExtra(EXTRA_VOLUME,-1) + " Vibration " + intent.getBooleanExtra(EXTRA_VIBRATION,false);
        System.out.println(message);
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
