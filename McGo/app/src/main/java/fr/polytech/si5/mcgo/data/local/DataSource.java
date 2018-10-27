package fr.polytech.si5.mcgo.data.local;

import android.location.Location;

public final class DataSource {

    public static Location RESTAURANT_1 = new Location("");
    public static Location RESTAURANT_2 = new Location("");
    public static Location RESTAURANT_3 = new Location("");
    public static Location RESTAURANT_4 = new Location("");
    public static Location RESTAURANT_5 = new Location("");
    public static Location[] locations = {RESTAURANT_1, RESTAURANT_2, RESTAURANT_3, RESTAURANT_4, RESTAURANT_5};

    public static boolean BUZZER = false;

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
    }

}
