package fr.polytech.si5.mcgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import fr.polytech.si5.mcgo.data.Sensors;
import fr.polytech.si5.mcgo.data.UserSettings;
import fr.polytech.si5.mcgo.sensors.ShakeDetector;

public abstract class QuickOrderActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(UserSettings.USER_PREFERENCES, MODE_MULTI_PROCESS);

        // Set up the shake sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                if (prefs.getBoolean(UserSettings.QUICK_ORDER_ENABLE, false)) {
                    handleShakeEvent(count);
                }
            }
        });

        // Set up the vibrator
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    // Sensors.

    private void handleShakeEvent(int count) {
        if (count == 5) {
            if (prefs.getBoolean(UserSettings.QUICK_ORDER_VIBRATION_FEEDBACK, false)) {
                vibrate();
            }
            if (prefs.getBoolean(UserSettings.QUICK_ORDER_AUDIO_FEEDBACK, false)) {
                playAudioFeedback();
            }

            Snackbar.make(((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0),
                    "Quick Order Feedback", Snackbar.LENGTH_LONG).show();
        }
    }

    private void vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mVibrator.vibrate(Sensors.Vibrator.QUICK_ORDER_VIBRATION_EFFECT);
        } else {
            mVibrator.vibrate(1000);
        }
    }

    private void playAudioFeedback() {

    }
}
