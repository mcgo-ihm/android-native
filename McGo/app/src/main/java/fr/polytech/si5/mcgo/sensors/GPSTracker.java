package fr.polytech.si5.mcgo.sensors;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.data.local.DataSource;
import fr.polytech.si5.mcgo.items.ItemsActivity;
import fr.polytech.si5.mcgo.notification.NotificationActivity;
import fr.polytech.si5.mcgo.orders.OrdersActivity;

public class GPSTracker extends Service implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000;
    //private final Context mContext;
    protected LocationManager locationManager;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private Location location;
    private double latitude;
    private double longitude;

    /* Notifications */
    private static final String CHANNEL_ID = "buzzer";

    //private NotificationChannel buzzerChannel;
    private Intent mainIntent;
    private PendingIntent mainPendingIntent;
    private Intent orderIntent;
    private PendingIntent orderPendingIntent;
    private PendingIntent dismissIntent;
    //private NotificationManager notificationManager;
    private NotificationManagerCompat notificationManager;
    private Notification buzzerOnNotification;
    private Notification buzzerOffNotification;
    private static final int buzzerOnNotificationID = new Random().nextInt();
    private static final int buzzerOffNotificationID = new Random().nextInt();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        getLocation();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        createNotificationChannel();
        //notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager = NotificationManagerCompat.from(this);

        /*int importance = NotificationManager.IMPORTANCE_DEFAULT;
        buzzerChannel = new NotificationChannel(CHANNEL_ID, getString(R.string.notification_channel_name), importance);
        buzzerChannel.setDescription(getString(R.string.notification_channel_description));*/

        mainIntent = new Intent(this, ItemsActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);
        orderIntent = new Intent(this, OrdersActivity.class);
        orderIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        orderPendingIntent = PendingIntent.getActivity(this, 0, orderIntent, 0);
        dismissIntent = NotificationActivity.getDismissIntent(0, this);

        buzzerOnNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("McGo restaurant detected")
                .setContentText("")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(orderPendingIntent)
                .setAutoCancel(true).build();
        //.addAction(R.drawable.ic_see, "See", orderPendingIntent)
        //.addAction(R.drawable.ic_cross, "Dismiss", dismissIntent).build();

        buzzerOffNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Buzzer mode deactivated")
                .setContentText("You are to far from the McGo restaurant where your order is being prepared")
                .setSmallIcon(R.drawable.ic_logo)
                .setContentIntent(mainPendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).build();
    }

    @Override
    public void onDestroy() {
        stopUsingGPS();
    }

    public GPSTracker() {

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_name);
            String description = getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(getBaseContext(), "GPS or Internet turned off", Toast.LENGTH_LONG).show();
            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Shows settings alert dialog
     * On pressing Settings button will launch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.e(this.getClass().getName(), "GPS Location - lat=" + latitude + ", lon=" + longitude);

        if (!DataSource.BUZZER && checkProximity(location)) {
            notificationManager.notify(buzzerOnNotificationID, buzzerOnNotification);
            DataSource.BUZZER = true;
            Log.e(this.getClass().getName(), "Buzzer state=" + DataSource.BUZZER);
        } else if (DataSource.BUZZER && !checkProximity(location)) {
            //notificationManager.notify(buzzerOffNotificationID, buzzerOffNotification);
            DataSource.BUZZER = false;
            Log.e(this.getClass().getName(), "Buzzer state=" + DataSource.BUZZER);
        }
    }

    private boolean checkProximity(Location location) {
        for (Location l : DataSource.locations) {
            if (l.distanceTo(location) < 50) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getBaseContext(), "Gps turned off", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getBaseContext(), "Gps turned on", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
