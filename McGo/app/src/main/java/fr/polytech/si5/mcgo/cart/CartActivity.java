package fr.polytech.si5.mcgo.cart;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.Set;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.Utils.ActivityUtils;
import fr.polytech.si5.mcgo.data.Constants;
import fr.polytech.si5.mcgo.data.Item;
import fr.polytech.si5.mcgo.data.local.ItemsDataSource;
import fr.polytech.si5.mcgo.items.ItemsFragment;
import fr.polytech.si5.mcgo.items.ItemsPresenter;
import fr.polytech.si5.mcgo.settings.UserSettingsFragment;

public class CartActivity extends AppCompatActivity {

    public static final int REQUEST_CART_OVERVIEW = 2;

    private View toolbarView;
    private Toolbar toolbar;
    private CartPresenter mItemsPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        toolbarView = (View) findViewById(R.id.cart_toolbar);
        toolbar = (Toolbar) toolbarView.findViewById(R.id.toolbar);
        initUI();

        // Set up items fragment.
        CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

        if (cartFragment == null) {
            // Create the fragment.
            cartFragment = cartFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), cartFragment, R.id.content_frame);
        }

        // Create the presenter.
        mItemsPresenter = new CartPresenter(cartFragment);
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

    @Override
    protected void onStart() {
        super.onStart();
        mItemsPresenter.loadDataSource(new ArrayList<>(ItemsDataSource.itemsToOrder.keySet()));
    }

}
