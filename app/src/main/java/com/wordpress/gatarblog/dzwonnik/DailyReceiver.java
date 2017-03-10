package com.wordpress.gatarblog.dzwonnik;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Receiver started everyday at 00:00 or after restart the phone.
 */

public class DailyReceiver extends BroadcastReceiver {

    //TODO Ma być włączany raz na dobę, odczytywać z bazy danych WSZYSTKIE RingStaty i odpalać je

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
