package fr.polytech.si5.mcgo.settings;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.data.Constants;
import fr.polytech.si5.mcgo.data.Constants.UserSettings;

import static fr.polytech.si5.mcgo.data.local.DataSource.CHANNELS;
import static fr.polytech.si5.mcgo.data.local.DataSource.CURRENT_NOTIFICATION_CHANNEL;

public class UserSettingsFragment extends PreferenceFragment {

    private SharedPreferences prefs;
    private Activity activity;
    private List<CheckBoxPreference> checkBoxPreferences;
    private Preference.OnPreferenceClickListener clickListener;
    private Preference.OnPreferenceChangeListener changeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int xmlPref = R.xml.user_settings_preferences;
        addPreferencesFromResource(xmlPref);
        checkBoxPreferences = new ArrayList<>();

        clickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                for (CheckBoxPreference cbp : checkBoxPreferences) {
                    if (!cbp.getKey().equals(preference.getKey()) && cbp.isChecked()) {
                        cbp.setChecked(false);
                    }

                    if (cbp.getKey().equals(preference.getKey()) && !cbp.isChecked()) {
                        cbp.setChecked(true);
                    }
                }
                return true;
            }
        };

        changeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((Boolean) newValue) {
                    prefs.edit().putString(UserSettings.NOTIFICATION_PRIORITY_VALUE, preference.getKey()).apply();
                    NotificationManager notificationManager = null;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        notificationManager = activity.getSystemService(NotificationManager.class);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            int importance = Constants.UserSettings.Priority.match(
                                    prefs.getString(Constants.UserSettings.NOTIFICATION_PRIORITY_VALUE, "High"));
                            notificationManager.deleteNotificationChannel(CHANNELS[importance].getId());
                            createNotificationChannel();
                        }
                    }
                }

                return true;
            }
        };
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        prefs = activity.getSharedPreferences(UserSettings.USER_PREFERENCES, Context.MODE_MULTI_PROCESS);
        setTitle();
    }

    private void setTitle() {
        String title = getString(R.string.preferences_activity_label);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Quick Order enable
        final SwitchPreference quickOrderSwitchPreference = (SwitchPreference) findPreference("main");
        if (quickOrderSwitchPreference != null) {
            quickOrderSwitchPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                prefs.edit().putBoolean(UserSettings.QUICK_ORDER_ENABLE, (Boolean) newValue).apply();
                return true;
            });
        }

        // Vibration feedback preference
        final CheckBoxPreference vibrationCheckBoxPreference = (CheckBoxPreference) findPreference("firstDependent");
        if (vibrationCheckBoxPreference != null) {
            vibrationCheckBoxPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                prefs.edit().putBoolean(UserSettings.QUICK_ORDER_VIBRATION_FEEDBACK, (Boolean) newValue).apply();
                return true;
            });
        }

        // Audio feedback preference
        final CheckBoxPreference audioCheckBoxPreference = (CheckBoxPreference) findPreference("secondDependent");
        if (audioCheckBoxPreference != null) {
            audioCheckBoxPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                prefs.edit().putBoolean(UserSettings.QUICK_ORDER_AUDIO_FEEDBACK, (Boolean) newValue).apply();
                return true;
            });
        }

        // Notification Priorities

        final CheckBoxPreference highPriority = (CheckBoxPreference) findPreference("High");
        final CheckBoxPreference defaultPriority = (CheckBoxPreference) findPreference("Default");
        final CheckBoxPreference lowPriority = (CheckBoxPreference) findPreference("Low");
        final CheckBoxPreference minPriority = (CheckBoxPreference) findPreference("Min");
        final CheckBoxPreference nonePriority = (CheckBoxPreference) findPreference("None");

        if (checkBoxPreferences.isEmpty()) {
            checkBoxPreferences.add(highPriority);
            checkBoxPreferences.add(defaultPriority);
            checkBoxPreferences.add(lowPriority);
            checkBoxPreferences.add(minPriority);
            checkBoxPreferences.add(nonePriority);
        }

        for (CheckBoxPreference cbp : checkBoxPreferences) {
            cbp.setOnPreferenceChangeListener(changeListener);
            cbp.setOnPreferenceClickListener(clickListener);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = Constants.UserSettings.Priority.match(
                    prefs.getString(Constants.UserSettings.NOTIFICATION_PRIORITY_VALUE, "High"));
            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            CURRENT_NOTIFICATION_CHANNEL = CHANNELS[importance].getId();
            notificationManager.createNotificationChannel(CHANNELS[importance]);
        }
    }
}
