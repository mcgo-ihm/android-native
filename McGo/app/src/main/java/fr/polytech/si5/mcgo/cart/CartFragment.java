package fr.polytech.si5.mcgo.cart;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.Utils.ActivityUtils;
import fr.polytech.si5.mcgo.data.Constants;
import fr.polytech.si5.mcgo.data.Item;
import fr.polytech.si5.mcgo.data.local.ItemsDataSource;
import fr.polytech.si5.mcgo.settings.UserSettingsActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class CartFragment extends Fragment implements CartContract.View {

    private CartContract.Presenter mPresenter;

    /**
     * Listener for clicks on items in the ListView.
     */
    CartFragment.ItemListener mItemListener = new CartFragment.ItemListener() {

        @Override
        public void onItemClick(Item clickedItem) {
            mPresenter.openItemDetails(clickedItem);
        }

        @Override
        public void onItemPlusClick(TextView itemCount, Item clickedItem) {
            mPresenter.addItemToCart(itemCount, clickedItem);
        }

        @Override
        public void onItemMinusClick(TextView itemCount, Item clickedItem) {
            mPresenter.removeItemFromCart(itemCount, clickedItem);
        }
    };

    private MenuItem mOnResumeMenuItem;
    private LayerDrawable mIcon;
    private boolean mCanSeeCart;

    private CartFragment.ItemsAdapter mListAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private LinearLayout mNoItemsView;
    private ImageView mNoItemImage;
    private TextView mNoItemTextView;
    private TextView mItemsQuantity;
    private TextView mTotalItemsPrice;

    public CartFragment() {
        // Requires empty public constructor.
    }

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        mCanSeeCart = args != null && args.getBoolean(Constants.FRAGMENT_BUNDLE_CAN_SEE_CART_KEY, false);

        mListAdapter = new CartFragment.ItemsAdapter(new ArrayList<>(0), mItemListener);

        // Run a task to fetch the notifications count.
        new CartFragment.FetchCountTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.items_cart_frag, container, false);

        // Set up items view
        mRecyclerView = root.findViewById(R.id.items_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // Set up no items view
        mNoItemsView = root.findViewById(R.id.no_items);
        mNoItemImage = root.findViewById(R.id.no_items_image);
        mNoItemTextView = root.findViewById(R.id.no_items_text);

        // Set up info view
        mItemsQuantity = root.findViewById(R.id.items_quantity);
        mTotalItemsPrice = root.findViewById(R.id.total_items_price);

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();

        if (mOnResumeMenuItem != null) {
            mOnResumeMenuItem.setEnabled(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cart_fragment_menu, menu);

        // Get the notifications MenuItem and its LayerDrawable (layer-list).
        MenuItem item = menu.findItem(R.id.menu_cart);
        mIcon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable.
        ActivityUtils.setBadgeCount(getContext(), mIcon, ItemsDataSource.cart.getTotalItemsNumber());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cart:
                if (mCanSeeCart) {
                    // Disable menu item to prevent double activity instantiation.
                    item.setEnabled(false);
                    mOnResumeMenuItem = item;

                    // Load cart activity.
                    Intent intent = new Intent(getContext(), CartActivity.class);
                    startActivityForResult(intent, CartActivity.REQUEST_CART_OVERVIEW);
                }
                // Do nothing, we are currently in this activity.
                break;
            case R.id.menu_preferences:
                // Load preferences activity.
                Intent intent = new Intent(getContext(), UserSettingsActivity.class);

                // Prevent double click on icon and thus double activity loading.
                item.setEnabled(false);
                mOnResumeMenuItem = item;
                startActivityForResult(intent, UserSettingsActivity.REQUEST_PREFERENCE_SETTINGS);
                break;
        }
        return true;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

    }

    @Override
    public void setPresenter(@NonNull CartContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showItems(List<Item> items) {
        mListAdapter.replaceData(items);

        mRecyclerView.setVisibility(View.VISIBLE);
        mItemsQuantity.setVisibility(View.VISIBLE);
        mTotalItemsPrice.setVisibility(View.VISIBLE);
        mNoItemsView.setVisibility(View.GONE);
    }

    @Override
    public void showNoItems() {
        mRecyclerView.setVisibility(View.GONE);
        mItemsQuantity.setVisibility(View.GONE);
        mTotalItemsPrice.setVisibility(View.GONE);
        mNoItemsView.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void addItemToCart(TextView quantityTextView, @NonNull Item requestedItem, int cartSize, float cartPrice) {
        // There's probably a better way to do that.
        // But I can't figure it out since I don't know how to get the view in the recycler view
        // associated with the parameter requestedItem.
        updateCartStatus(quantityTextView, requestedItem, cartSize, cartPrice);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void removeItemFromCart(TextView quantityTextView, @NonNull Item requestedItem, int cartSize, float cartPrice) {
        updateCartStatus(quantityTextView, requestedItem, cartSize, cartPrice);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void clearCart() {
        mListAdapter.replaceData(new ArrayList<>());
        ActivityUtils.setBadgeCount(getContext(), mIcon, ItemsDataSource.cart.getTotalItemsNumber());
        showNoItems();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void updateCartStatus(int cartSize, float cartPrice) {
        mListAdapter.notifyDataSetChanged();
        mItemsQuantity.setText(String.format(Locale.ENGLISH, "%s %d",
                getResources().getString(R.string.order_items_total_quantity), cartSize));
        mTotalItemsPrice.setText(String.format(Locale.ENGLISH, "%s %.2f$",
                getResources().getString(R.string.order_price), cartPrice));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateCartStatus(@NonNull TextView quantityTextView, @Nullable Item item, int cartSize, float cartPrice) {
        mListAdapter.notifyDataSetChanged();

        if (mListAdapter.mItems.contains(item)) {
            quantityTextView.setText(Integer.toString(item.getQuantity()));
        }

        mItemsQuantity.setText(String.format(Locale.ENGLISH, "%s %d",
                getResources().getString(R.string.order_items_total_quantity), cartSize));
        mTotalItemsPrice.setText(String.format(Locale.ENGLISH, "%s %.2f$",
                getResources().getString(R.string.order_price), cartPrice));

        ActivityUtils.setBadgeCount(getContext(), mIcon, ItemsDataSource.cart.getTotalItemsNumber());
    }

    public interface ItemListener {

        void onItemClick(Item clickedItem);

        void onItemPlusClick(TextView itemCount, Item clickedItem);

        void onItemMinusClick(TextView itemCount, Item clickedItem);

    }

    private static class ItemsAdapter extends RecyclerView.Adapter<CartFragment.ItemsAdapter.ViewHolder> {

        private List<Item> mItems;
        private CartFragment.ItemListener mItemListener;

        ItemsAdapter(List<Item> items, CartFragment.ItemListener itemListener) {
            setList(items);
            mItemListener = itemListener;
        }

        private void setList(List<Item> items) {
            mItems = checkNotNull(items);
        }

        void replaceData(List<Item> items) {
            setList(items);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public CartFragment.ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup view, int viewType) {
            CartFragment.ItemsAdapter.ViewHolder viewHolder;

            View foodView = LayoutInflater.from(view.getContext()).inflate(R.layout.item_cart, view, false);
            viewHolder = new CartFragment.ItemsAdapter.ViewHolder(foodView);

            return viewHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull CartFragment.ItemsAdapter.ViewHolder viewHolder, int position) {
            Item item = mItems.get(position);

            viewHolder.itemImage.setImageResource(item.getIconId());
            viewHolder.itemTitle.setText(item.getName());
            viewHolder.itemPrice.setText(String.format(Locale.ENGLISH, "%.2f$", item.getPrice()));

            if (mItems.contains(item)) {
                viewHolder.itemCount.setText(Integer.toString(item.getQuantity()));
            } else {
                viewHolder.itemCount.setText(0);
            }

            viewHolder.removeFromCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.onItemMinusClick(viewHolder.itemCount, item);

                    if (!mItems.contains(item)) {
                        mItems.remove(item);
                        notifyDataSetChanged();
                    }
                }
            });

            viewHolder.addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.onItemPlusClick(viewHolder.itemCount, item);
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onItemClick(item);
                }
            });
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            ImageView itemImage;
            TextView itemTitle;
            View priceCartView;
            View addDelView;
            TextView itemPrice;
            TextView itemCount;
            Button removeFromCart;
            Button addToCart;

            ViewHolder(View view) {
                super(view);
                //view.setClipToOutline(true); // For round borders.

                itemImage = view.findViewById(R.id.item_image);
                itemTitle = view.findViewById(R.id.item_title);
                priceCartView = view.findViewById(R.id.price_cart_layout);
                itemPrice = priceCartView.findViewById(R.id.item_price);
                addDelView = priceCartView.findViewById(R.id.add_del_layout);
                itemCount = addDelView.findViewById(R.id.item_count);
                removeFromCart = addDelView.findViewById(R.id.item_minus_button);
                addToCart = addDelView.findViewById(R.id.item_plus_button);
            }
        }
    }

    /**
     * Updates the count of notifications in the ActionBar.
     */
    private void updateNotificationsBadge(int count) {
        // Force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        getActivity().invalidateOptionsMenu();
    }

    /**
     * Sample AsyncTask to fetch the notifications count.
     */
    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Integer doInBackground(Void... params) {
            return ItemsDataSource.cart.getTotalItemsNumber();
        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }
}
