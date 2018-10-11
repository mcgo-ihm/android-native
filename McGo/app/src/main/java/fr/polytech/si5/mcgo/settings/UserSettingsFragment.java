package fr.polytech.si5.mcgo.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.data.UserSettings;

public class UserSettingsFragment extends PreferenceFragment {

    private SharedPreferences prefs;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int xmlId = R.xml.user_settings_preferences;
        addPreferencesFromResource(xmlId);
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
        Toolbar toolbar = ((Toolbar) getActivity().findViewById(R.id.toolbar));

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
    }
}
