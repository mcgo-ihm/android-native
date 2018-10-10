package fr.polytech.si5.mcgo.data;

import android.os.VibrationEffect;

public interface Sensors {

    interface Vibrator {

        VibrationEffect DEFAULT = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE);

        VibrationEffect QUICK_ORDER_VIBRATION_EFFECT = VibrationEffect.createWaveform(
                new long[]{0, 400, 200, 400, 200, 400, 200, 400},
                new int[]{0, 255, 0, 255, 0, 170, 0, 85}, -1);

    }

}
