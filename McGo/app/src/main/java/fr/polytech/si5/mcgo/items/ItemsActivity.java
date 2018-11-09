package fr.polytech.si5.mcgo.items;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import fr.polytech.si5.mcgo.QuickOrderPropertyActivity;
import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.data.Constants;
import fr.polytech.si5.mcgo.data.local.ItemsDataSource;
import fr.polytech.si5.mcgo.orders.OrdersActivity;
import fr.polytech.si5.mcgo.quickorder.QuickOrderActivity;
import fr.polytech.si5.mcgo.sensors.GPSTracker;
import fr.polytech.si5.mcgo.utils.ActivityUtils;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class ItemsActivity extends QuickOrderPropertyActivity {

    private DrawerLayout mDrawerLayout;
    private ItemsPresenter mItemsPresenter;

    /* Permissions */
    private static final String[] PERMISSIONS = {
            Manifest.permission.INTERNET, Manifest.permission.VIBRATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int PERMISSION_REQUEST = 100;

    /* Location Attributes */
    private GPSTracker gpsTracker;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_activity);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        // Set up items fragment.
        ItemsFragment itemsFragment = (ItemsFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

        if (itemsFragment == null) {
            // Create the fragment.
            itemsFragment = itemsFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.FRAGMENT_BUNDLE_QUICK_ORDER_KEY, true);
            itemsFragment.setArguments(bundle);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), itemsFragment, R.id.content_frame);
        }

        // Create the presenter.
        mItemsPresenter = new ItemsPresenter(itemsFragment);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action",
                Snackbar.LENGTH_LONG).setAction("Action", null).show());
        fab.hide();*/

        checkPermissions();
        //gpsTracker = new GPSTracker(this);
        startService(new Intent(this, GPSTracker.class));
    }

    private void checkPermissions() {
        // Checks the Android version of the device.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean canUseInternet = checkIfAlreadyHavePermission(PERMISSIONS[0]);
            boolean canReadExternalStorage = checkIfAlreadyHavePermission(PERMISSIONS[1]);
            boolean canReadFineLocation = checkIfAlreadyHavePermission(PERMISSIONS[2]);
            boolean canReadCoarseLocation = checkIfAlreadyHavePermission(PERMISSIONS[3]);

            if (!canUseInternet || !canReadExternalStorage || !canReadFineLocation || !canReadCoarseLocation) {
                requestPermissions(PERMISSIONS, PERMISSION_REQUEST);
            }
        }
    }

    private boolean checkIfAlreadyHavePermission(String permission) {
        int result = ContextCompat.checkSelfPermission(this, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        mItemsPresenter.loadDataSource(ItemsDataSource.ITEMS_DATA_SOURCE);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    Intent intent;

                    switch (menuItem.getItemId()) {
                        case R.id.food_navigation_menu_item:
                            // Do nothing, we're already on that screen
                            break;
                        case R.id.orders_navigation_menu_item:
                            intent = new Intent(ItemsActivity.this, OrdersActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.quick_order_navigation_menu_item:
                            intent = new Intent(ItemsActivity.this, QuickOrderActivity.class);
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }

                    // Close the navigation drawer when an item is selected.
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                });
    }
}
