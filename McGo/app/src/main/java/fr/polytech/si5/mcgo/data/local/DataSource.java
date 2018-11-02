package fr.polytech.si5.mcgo.data.local;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.location.Location;
import android.os.Build;

import static fr.polytech.si5.mcgo.data.Constants.BUZZER_CHANNEL_DEFAULT_ID;
import static fr.polytech.si5.mcgo.data.Constants.BUZZER_CHANNEL_HIGH_ID;
import static fr.polytech.si5.mcgo.data.Constants.BUZZER_CHANNEL_LOW_ID;
import static fr.polytech.si5.mcgo.data.Constants.BUZZER_CHANNEL_MIN_ID;
import static fr.polytech.si5.mcgo.data.Constants.BUZZER_CHANNEL_NONE_ID;

public final class DataSource {

    public static final NotificationChannel[] CHANNELS;
    public static String CURRENT_NOTIFICATION_CHANNEL = BUZZER_CHANNEL_HIGH_ID;
    public static long CHANNEL_OFFSET = 0;
    private static NotificationChannel CHANNEL_HIGH;
    private static NotificationChannel CHANNEL_DEFAULT;
    private static NotificationChannel CHANNEL_LOW;
    private static NotificationChannel CHANNEL_MIN;

    public static Location RESTAURANT_1 = new Location("");
    public static Location RESTAURANT_2 = new Location("");
    public static Location RESTAURANT_3 = new Location("");
    public static Location RESTAURANT_4 = new Location("");
    public static Location RESTAURANT_5 = new Location("");
    public static Location[] locations = {RESTAURANT_1, RESTAURANT_2, RESTAURANT_3, RESTAURANT_4, RESTAURANT_5};

    public static boolean BUZZER = false;
    private static NotificationChannel CHANNEL_NONE;

    static {
        RESTAURANT_1.setLatitude(43.6178685);
        RESTAURANT_1.setLongitude(7.0749743);

        RESTAURANT_2.setLatitude(43.6183568);
        RESTAURANT_2.setLongitude(7.0750616);

        RESTAURANT_3.setLatitude(43.6186174);
        RESTAURANT_3.setLongitude(7.0751608);

        RESTAURANT_4.setLatitude(43.6180467);
        RESTAURANT_4.setLongitude(7.0750095);

        RESTAURANT_5.setLatitude(43.6184772);
        RESTAURANT_5.setLongitude(7.0756282);

        CharSequence name = "BuzzerChannel";
        String description = "Buzzer system";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CHANNEL_HIGH = new NotificationChannel(BUZZER_CHANNEL_HIGH_ID, name, NotificationManager.IMPORTANCE_HIGH);
            CHANNEL_HIGH.setDescription(description);

            CHANNEL_DEFAULT = new NotificationChannel(BUZZER_CHANNEL_DEFAULT_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            CHANNEL_DEFAULT.setDescription(description);

            CHANNEL_LOW = new NotificationChannel(BUZZER_CHANNEL_LOW_ID, name, NotificationManager.IMPORTANCE_LOW);
            CHANNEL_LOW.setDescription(description);

            CHANNEL_MIN = new NotificationChannel(BUZZER_CHANNEL_MIN_ID, name, NotificationManager.IMPORTANCE_MIN);
            CHANNEL_MIN.setDescription(description);

            CHANNEL_NONE = new NotificationChannel(BUZZER_CHANNEL_NONE_ID, name, NotificationManager.IMPORTANCE_NONE);
            CHANNEL_NONE.setDescription(description);
        }

        CHANNELS = new NotificationChannel[]{CHANNEL_NONE, CHANNEL_MIN, CHANNEL_LOW, CHANNEL_DEFAULT, CHANNEL_HIGH};
    }
}
