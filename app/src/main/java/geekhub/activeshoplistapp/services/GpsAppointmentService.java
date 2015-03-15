package geekhub.activeshoplistapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import geekhub.activeshoplistapp.helpers.AppConstants;

/**
 * Created by rage on 3/14/15.
 */
public class GpsAppointmentService extends Service {
    private static final String TAG = GpsAppointmentService.class.getSimpleName();
    private GpsAppointment gpsAppointment;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        gpsAppointment = new GpsAppointment(getApplicationContext());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long id = 0;
        if (intent != null) {
            id = intent.getLongExtra(AppConstants.EXTRA_GPS_APPOINTMENT, 0);
        }
        if (id == 0) {
            gpsAppointment.update();
        }
        Log.d(TAG, "onStartCommand: id = " + id);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
