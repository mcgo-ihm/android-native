package fr.polytech.si5.mcgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
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

    // Maximum sound stream.
    private static final int MAX_STREAMS = 5;
    private static final int STREAM_TYPE = AudioManager.STREAM_MUSIC;
    private SoundPool mSoundPool;
    private AudioManager mAudioManager;
    private boolean mLoaded;
    private float mVolume;
    private int mQuickOrderNotification;

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

        // Set up the vibrator.
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // AudioManager audio settings for adjusting the volume.
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Current volume Index of particular stream type.
        float currentVolumeIndex = (float) mAudioManager.getStreamVolume(STREAM_TYPE);

        // Get the maximum volume index for a particular stream type.
        float maxVolumeIndex = (float) mAudioManager.getStreamMaxVolume(STREAM_TYPE);

        // Volume (0 --> 1)
        this.mVolume = currentVolumeIndex / maxVolumeIndex;

        // Suggests an audio stream whose volume should be changed by the hardware volume controls.
        this.setVolumeControlStream(STREAM_TYPE);

        if (Build.VERSION.SDK_INT >= 21) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttributes).setMaxStreams(MAX_STREAMS);

            this.mSoundPool = builder.build();
        } else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        // When Sound Pool load complete.
        this.mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                mLoaded = true;
            }
        });

        this.mQuickOrderNotification = this.mSoundPool.load(this, R.raw.quick_order_notification, 1);
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
                    "Quick Order Succeeded", Snackbar.LENGTH_LONG).show();
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
        if (mLoaded) {
            float leftVolume = mVolume;
            float rightVolume = mVolume;

            // Play sound objects destroyed. Returns the ID of the new stream.
            int streamId = this.mSoundPool.play(this.mQuickOrderNotification, leftVolume, rightVolume, 1, 0, 1f);
        }
    }
}
