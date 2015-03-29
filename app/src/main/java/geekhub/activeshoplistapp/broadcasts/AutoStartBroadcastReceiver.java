package geekhub.activeshoplistapp.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import geekhub.activeshoplistapp.services.GpsAppointmentService;

/**
 * Created by rage on 3/29/15.
 */
public class AutoStartBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startGps = new Intent(context, GpsAppointmentService.class);
        context.startService(startGps);
    }
}
