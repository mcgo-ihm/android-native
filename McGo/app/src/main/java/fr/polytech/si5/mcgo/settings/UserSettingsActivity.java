package fr.polytech.si5.mcgo.settings;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import fr.polytech.si5.mcgo.R;

public class UserSettingsActivity extends AppCompatActivity {

    public static final int REQUEST_PREFERENCE_SETTINGS = 1;

    private View toolbarView;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_activity);

        toolbarView = (View) findViewById(R.id.preferences_toolbar);
        toolbar = (Toolbar) toolbarView.findViewById(R.id.toolbar);
        initUI();
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new UserSettingsFragment()).commit();
    }

    void initUI() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
