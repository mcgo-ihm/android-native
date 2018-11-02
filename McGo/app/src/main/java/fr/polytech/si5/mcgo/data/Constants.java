package fr.polytech.si5.mcgo.data;

import android.app.NotificationManager;
import android.os.VibrationEffect;

public interface Constants {

    /* Notifications */
    String BUZZER_CHANNEL_ID = "buzzer";
    String BUZZER_CHANNEL_HIGH_ID = "buzzer_high";
    String BUZZER_CHANNEL_DEFAULT_ID = "buzzer_default";
    String BUZZER_CHANNEL_LOW_ID = "buzzer_low";
    String BUZZER_CHANNEL_MIN_ID = "buzzer_min";
    String BUZZER_CHANNEL_NONE_ID = "buzzer_none";
    String FRAGMENT_BUNDLE_QUICK_ORDER_KEY = "quickOrderFragment";
    String FRAGMENT_BUNDLE_CAN_SEE_CART_KEY = "canSeeCart";

    interface UserSettings {

        String USER_PREFERENCES = "Preferences";
        String QUICK_ORDER_ENABLE = "quickOrderEnable";
        String QUICK_ORDER_VIBRATION_FEEDBACK = "quickOrderVibrationFeedback";
        String QUICK_ORDER_AUDIO_FEEDBACK = "quickOrderAudioFeedback";
        String NOTIFICATION_PRIORITY_VALUE = "notificationPriority";

        enum Priority {
            HIGH("High"), DEFAULT("Default"), LOW("Low"), MIN("Min"), NONE("None");

            private String name;

            Priority(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public static int match(String priority) {
                switch (priority) {
                    case "High":
                        return NotificationManager.IMPORTANCE_HIGH;
                    case "Default":
                        return NotificationManager.IMPORTANCE_DEFAULT;
                    case "Low":
                        return NotificationManager.IMPORTANCE_LOW;
                    case "Min":
                        return NotificationManager.IMPORTANCE_MIN;
                    case "None":
                        return NotificationManager.IMPORTANCE_NONE;
                    default:
                        return NotificationManager.IMPORTANCE_HIGH;
                }
            }
        }

    }

    interface Sensors {

        interface Vibrator {

            VibrationEffect DEFAULT = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE);

            VibrationEffect QUICK_ORDER_VIBRATION_EFFECT = VibrationEffect.createWaveform(
                    new long[]{0, 400, 200, 400, 200, 400, 200, 400},
                    new int[]{0, 255, 0, 255, 0, 170, 0, 85}, -1);

        }

        interface GPS {

            // The minimum distance to change Updates in meters
            long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
            // The minimum time between updates in milliseconds
            long MIN_TIME_BW_UPDATES = 1000;

        }

    }
}
