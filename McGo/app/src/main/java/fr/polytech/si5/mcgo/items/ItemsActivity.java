package fr.polytech.si5.mcgo.items;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import fr.polytech.si5.mcgo.QuickOrderActivity;
import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.Utils.ActivityUtils;
import fr.polytech.si5.mcgo.data.local.ItemsDataSource;
import fr.polytech.si5.mcgo.favorite.FavoriteActivity;

public class ItemsActivity extends QuickOrderActivity {

    private DrawerLayout mDrawerLayout;
    private ItemsPresenter mItemsPresenter;

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
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), itemsFragment, R.id.content_frame);
        }

        // Create the presenter.
        mItemsPresenter = new ItemsPresenter(itemsFragment);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action",
                Snackbar.LENGTH_LONG).setAction("Action", null).show());
        fab.hide();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mItemsPresenter.loadDataSource(ItemsDataSource.itemsDataSource);
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

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.food_navigation_menu_item:
                            // Do nothing, we're already on that screen
                            break;
                        case R.id.favorite_navigation_menu_item:
                            Intent intent = new Intent(ItemsActivity.this, FavoriteActivity.class);
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