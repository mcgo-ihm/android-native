package fr.polytech.si5.mcgo.cart;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

        @Override
        public void onValidateCart() {
            if (ItemsDataSource.cartSize != 0) {
                mPresenter.confirmOrder();
            }
        }

        @Override
        public void onCleanupCart() {
            if (ItemsDataSource.cartSize != 0) {
                mPresenter.clearCart();
            }
        }
    };

    private MenuItem mOnResumeMenuItem;
    private CartFragment.ItemsAdapter mListAdapter;
    private LinearLayout mItemsView;
    private LinearLayout mNoItemsView;
    private ImageView mNoItemImage;
    private TextView mNoItemTextView;
    private TextView mTotalItemsPriceTextView;
    private Button mValidateCart;
    private Button mCleanupCart;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private LayerDrawable mIcon;

    public CartFragment() {
        // Requires empty public constructor.
    }

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListAdapter = new CartFragment.ItemsAdapter(new ArrayList<>(0), mItemListener);

        // Run a task to fetch the notifications count.
        new CartFragment.FetchCountTask().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();

        if (mOnResumeMenuItem != null) {
            mOnResumeMenuItem.setEnabled(false);
        }

        updateCartStatus(null, null, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.items_frag, container, false);

        // Set up items view
        mRecyclerView = (RecyclerView) root.findViewById(R.id.items_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        mItemsView = (LinearLayout) root.findViewById(R.id.items_linear_layout);

        // Set up no items view
        mNoItemsView = root.findViewById(R.id.no_items);
        mNoItemImage = (ImageView) root.findViewById(R.id.no_items_image);
        mNoItemTextView = (TextView) root.findViewById(R.id.no_items_text);
        mTotalItemsPriceTextView = (TextView) getActivity().findViewById(R.id.total_items_price);
        mValidateCart = (Button) getActivity().findViewById(R.id.validate_cart);
        mCleanupCart = (Button) getActivity().findViewById(R.id.cleanup_cart);

        mValidateCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onValidateCart();
            }
        });

        mCleanupCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onCleanupCart();
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cart:
                // We are currently in this activity
                break;
            case R.id.menu_preferences:
                // Load preferences activity
                Intent intent = new Intent(getContext(), UserSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                item.setEnabled(false);
                mOnResumeMenuItem = item;
                startActivityForResult(intent, UserSettingsActivity.REQUEST_PREFERENCE_SETTINGS);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cart_fragment_menu, menu);

        // Get the notifications MenuItem and its LayerDrawable (layer-list).
        MenuItem item = menu.findItem(R.id.menu_cart);
        mIcon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable.
        ActivityUtils.setBadgeCount(getContext(), mIcon, ItemsDataSource.cartSize);
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

        mItemsView.setVisibility(View.VISIBLE);
        mNoItemsView.setVisibility(View.GONE);
    }

    @Override
    public void showNoItems() {
        mItemsView.setVisibility(View.GONE);
        mNoItemsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void addItemToCart(TextView view, @NonNull Item requestedItem) {
        // There's probably a better way to do that
        updateCartStatus(view, requestedItem, true);
    }

    @Override
    public void removeItemFromCart(TextView view, @NonNull Item requestedItem) {
        updateCartStatus(view, requestedItem, false);
    }

    @Override
    public void clearCart() {
        mListAdapter.replaceData(new ArrayList<>());
        updateCartStatus(null, null, false);
        showNoItems();
    }

    private void updateCartStatus(@Nullable TextView itemCountView, @Nullable Item item, boolean add) {
        if (itemCountView != null && item != null) {
            if (ItemsDataSource.itemsToOrder.containsKey(item)) {
                itemCountView.setText(Integer.toString(ItemsDataSource.itemsToOrder.get(item)));
            } else {
                itemCountView.setText(Integer.toString(0));
            }
        }

        mTotalItemsPriceTextView.setText(String.format(Locale.ENGLISH, "%s %d item(s) for %.2f$",
                getResources().getString(R.string.total_item), ActivityUtils.calculateCartSize(),
                ActivityUtils.calculatePrice(item, add)));

        if (mIcon != null) {
            ActivityUtils.setBadgeCount(getContext(), mIcon, ItemsDataSource.cartSize);
        }
    }

    public interface ItemListener {

        void onItemClick(Item clickedItem);

        void onItemPlusClick(TextView itemCount, Item clickedItem);

        void onItemMinusClick(TextView itemCount, Item clickedItem);

        void onValidateCart();

        void onCleanupCart();

    }

    private static class ItemsAdapter extends RecyclerView.Adapter<CartFragment.ItemsAdapter.ViewHolder> {

        private List<Item> mItems;
        private CartFragment.ItemListener mItemListener;

        ItemsAdapter(List<Item> items, CartFragment.ItemListener itemListener) {
            setList(items);
            mItemListener = itemListener;
        }

        void replaceData(List<Item> items) {
            setList(items);
            notifyDataSetChanged();
        }

        private void setList(List<Item> items) {
            mItems = checkNotNull(items);
        }

        @NonNull
        @Override
        public CartFragment.ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup view, int viewType) {
            CartFragment.ItemsAdapter.ViewHolder viewHolder;

            View foodView = LayoutInflater.from(view.getContext()).inflate(R.layout.food_item_in_cart, view, false);
            viewHolder = new CartFragment.ItemsAdapter.ViewHolder(foodView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CartFragment.ItemsAdapter.ViewHolder viewHolder, int position) {
            Item item = mItems.get(position);

            viewHolder.itemImage.setImageResource(item.getIconId());
            viewHolder.itemTitle.setText(item.getName());
            viewHolder.itemPrice.setText(String.format(Locale.ENGLISH, "%.2f$", item.getPrice()));

            if (ItemsDataSource.itemsToOrder.containsKey(item)) {
                viewHolder.itemCount.setText(Integer.toString(ItemsDataSource.itemsToOrder.get(item)));
            } else {
                viewHolder.itemCount.setText(0);
            }

            viewHolder.removeFromCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.onItemMinusClick(viewHolder.itemCount, item);

                    if (!ItemsDataSource.itemsToOrder.containsKey(item)) {
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
        ItemsDataSource.cartSize = count;

        // Force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        getActivity().invalidateOptionsMenu();
    }

    /**
     * Sample AsyncTask to fetch the notifications count.
     */
    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            return ActivityUtils.calculateCartSize();
        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }
}
