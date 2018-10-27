package fr.polytech.si5.mcgo.cart;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import fr.polytech.si5.mcgo.QuickOrderPropertyActivity;
import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.data.Constants;
import fr.polytech.si5.mcgo.data.Order;
import fr.polytech.si5.mcgo.data.local.ItemsDataSource;
import fr.polytech.si5.mcgo.utils.ActivityUtils;

public class CartActivity extends QuickOrderPropertyActivity {

    public static final int REQUEST_CART_OVERVIEW = 2;

    private Toolbar toolbar;
    private CartPresenter mItemsPresenter;

    private Button mConfirmCart;
    private Button mClearCart;

    private Order cart;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        View toolbarView = findViewById(R.id.cart_toolbar);
        toolbar = toolbarView.findViewById(R.id.toolbar);
        initUI();

        this.cart = ItemsDataSource.cart;

        // Set up items fragment.
        CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

        if (cartFragment == null) {
            // Create the fragment.
            cartFragment = CartFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.FRAGMENT_BUNDLE_CAN_SEE_CART_KEY, false);
            cartFragment.setArguments(bundle);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), cartFragment, R.id.content_frame);
        }

        // Create the presenter.
        mItemsPresenter = new CartPresenter(cartFragment);

        mConfirmCart = findViewById(R.id.validate_cart);
        mClearCart = findViewById(R.id.cleanup_cart);

        mConfirmCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemsPresenter.confirmCart();
            }
        });

        mClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemsPresenter.clearCart();
            }
        });
    }

    void initUI() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        mItemsPresenter.loadDataSource(cart);
    }
}
