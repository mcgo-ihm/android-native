package fr.polytech.si5.mcgo.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.data.UserSettings;

public class UserSettingsActivity extends PreferenceActivity implements PreferenceChangeListener {

    public static final int REQUEST_PREFERENCE_SETTINGS = 1;

    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.user_settings_preferences);
    }

    private void test() {
        System.out.println("hello");
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent evt) {
        String key = evt.getKey();
        Preference pref = findPreference(key);

        switch (key) {
            case "main":
                test();
                UserSettings.mQuickOrderEnable = pref.isEnabled();
                break;
            case "firstDependent":
                UserSettings.mQuickOrderVibrationFeedback = pref.isEnabled();
                break;
            case "secondDependent":
                UserSettings.mQuickOrderAudioFeedback = pref.isEnabled();
                break;
        }
    }
}
