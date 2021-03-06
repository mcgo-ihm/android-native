package fr.polytech.si5.mcgo.orders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.data.local.ItemsDataSource;
import fr.polytech.si5.mcgo.items.ItemsActivity;
import fr.polytech.si5.mcgo.quickorder.QuickOrderActivity;

public class OrdersActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private TabLayout mTabLayout;
    private RecyclerView mRecyclerView;

    private OrderAdapter mInProgressAdapter;
    private OrderAdapter mDeliveredAdapter;
    private OrderAdapter currentAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_activity);

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

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    mRecyclerView.setAdapter(mInProgressAdapter);
                    currentAdapter = mInProgressAdapter;
                } else {
                    mRecyclerView.setAdapter(mDeliveredAdapter);
                    currentAdapter = mDeliveredAdapter;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mInProgressAdapter = new OrderAdapter(this, ItemsDataSource.ORDERS_IN_PROGRESS);
        mDeliveredAdapter = new OrderAdapter(this, ItemsDataSource.ORDERS_DELIVERED);
        mRecyclerView.setAdapter(mInProgressAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        currentAdapter = mInProgressAdapter;
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //currentAdapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //currentAdapter.onRestoreInstanceState(savedInstanceState);
    }

    protected void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    Intent intent;

                    switch (menuItem.getItemId()) {
                        case R.id.food_navigation_menu_item:
                            intent = new Intent(OrdersActivity.this, ItemsActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case R.id.orders_navigation_menu_item:
                            // Do nothing, we're already on that screen
                            break;
                        case R.id.quick_order_navigation_menu_item:
                            intent = new Intent(OrdersActivity.this, QuickOrderActivity.class);
                            startActivity(intent);
                            finish();
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
