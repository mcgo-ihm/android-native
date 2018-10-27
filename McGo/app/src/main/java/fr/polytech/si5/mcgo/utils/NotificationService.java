package fr.polytech.si5.mcgo.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import fr.polytech.si5.mcgo.data.local.ItemsDataSource;

public class NotificationService extends Service {

    private Timer timer;
    private TimerTask timerTask;
    private String TAG = "Timers";
    private int WAIT = 5;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        startTimer();

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stopTimerTask();
        super.onDestroy();
    }

    // We are going to use a handler to be able to run in our TimerTask.
    final Handler handler = new Handler();

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 5000, WAIT * 1000);
    }

    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                // Use a handler to run a toast that shows the current timestamp for example.
                handler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void run() {
                        //TODO CALL NOTIFICATION FUNC
                        if (!ItemsDataSource.ORDERS_IN_PROGRESS.isEmpty()) {
                            Toast toast = Toast.makeText(NotificationService.this,
                                    String.format(Locale.ENGLISH, "%s is ready",
                                            ItemsDataSource.ORDERS_IN_PROGRESS.get(0).toString()),
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                    }
                });
            }
        };
    }
}
