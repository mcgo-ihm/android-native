package fr.polytech.si5.mcgo.items;

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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.cart.CartActivity;
import fr.polytech.si5.mcgo.data.Constants;
import fr.polytech.si5.mcgo.data.Item;
import fr.polytech.si5.mcgo.data.local.ItemsDataSource;
import fr.polytech.si5.mcgo.quickorder.QuickOrderActivity;
import fr.polytech.si5.mcgo.settings.UserSettingsActivity;
import fr.polytech.si5.mcgo.utils.ActivityUtils;
import fr.polytech.si5.mcgo.utils.OrderUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a grid of {@link Item}s. User can choose to view all, active or completed tasks.
 */
public class ItemsFragment extends Fragment implements ItemsContract.View {

    private ItemsContract.Presenter mPresenter;

    /**
     * Listener for clicks on items in the ListView.
     */
    ItemListener mItemListener = new ItemListener() {

        @Override
        public void onItemClick(Item clickedItem) {
            mPresenter.openItemDetails(clickedItem);
        }

        @Override
        public void onItemLongClick(Item clickedItem) {
            mPresenter.enableQuickOrderSelection(clickedItem);
        }

        @Override
        public void onAddToCartItemClick(Item clickedItem) {
            mPresenter.addToCart(clickedItem);
        }

        @Override
        public void onQuickOrderItemClick(Item clickedItem, boolean isChecked) {
            mPresenter.selectItemForQuickOrder(clickedItem);
        }
    };

    private MenuItem mOnResumeMenuItem;
    private FetchCountTask mFetcher;
    private ItemsAdapter mListAdapter;
    private LinearLayout mItemsView;
    private LinearLayout mNoItemsView;
    private ImageView mNoItemImage;
    private TextView mNoItemTextView;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private LayerDrawable mIcon;

    private boolean mQuickOrderFragment = false;
    private boolean mEnableQuickOrderSelection = false;
    private int mQuickOrderSelectedCounter = 0;
    private List<Item> itemsForQuickOrder;

    public ItemsFragment() {
        // Requires empty public constructor.
    }

    public static ItemsFragment newInstance() {
        return new ItemsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        mQuickOrderFragment = args != null && args.getBoolean(Constants.FRAGMENT_BUNDLE_QUICK_ORDER_KEY, false);

        mListAdapter = new ItemsAdapter(new ArrayList<>(0), mItemListener, mQuickOrderFragment);
        itemsForQuickOrder = new ArrayList<>();

        // Run a task to fetch the notifications count.
        mFetcher = new FetchCountTask();
        mFetcher.execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();

        if (mOnResumeMenuItem != null) {
            mOnResumeMenuItem.setEnabled(true);
        }

        if (mIcon != null) {
            ActivityUtils.setBadgeCount(getContext(), mIcon, ItemsDataSource.cart.getTotalItemsNumber());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.items_frag, container, false);

        // Set up items view
        mRecyclerView = root.findViewById(R.id.items_list);
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

        setHasOptionsMenu(true);

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.items_fragment_menu, menu);

        // Get the notifications MenuItem and its LayerDrawable (layer-list).
        MenuItem item = menu.findItem(R.id.menu_cart);
        mIcon = (LayerDrawable) item.getIcon();

        if (mQuickOrderFragment) {
            menu.getItem(0).setEnabled(false);
        } else {
            menu.getItem(0).setVisible(false);
        }

        // Update LayerDrawable's BadgeDrawable.
        ActivityUtils.setBadgeCount(getContext(), mIcon, ItemsDataSource.cart.getTotalItemsNumber());
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) { // Called after invalidateOptionsMenu call.
        if (mEnableQuickOrderSelection && mQuickOrderSelectedCounter > 0) {
            menu.getItem(0).setEnabled(true);
        } else {
            menu.getItem(0).setEnabled(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.menu_set_quick_order:
                // Set quick order.
                OrderUtils.setQuickOrder(itemsForQuickOrder);
                resetQuickOrderSelection();

                // Start activity to view the new quick order set.
                intent = new Intent(getContext(), QuickOrderActivity.class);
                startActivityForResult(intent, QuickOrderActivity.REQUEST_QUICK_ORDER_OVERVIEW);
                break;
            case R.id.menu_cart:
                // Disable menu item to prevent double activity instantiation.
                item.setEnabled(false);
                mOnResumeMenuItem = item;

                // Load cart activity.
                intent = new Intent(getContext(), CartActivity.class);
                startActivityForResult(intent, CartActivity.REQUEST_CART_OVERVIEW);
                break;
            case R.id.menu_preferences:
                // Disable menu item to prevent double activity instantiation.
                item.setEnabled(false);
                mOnResumeMenuItem = item;

                // Load preferences activity.
                intent = new Intent(getContext(), UserSettingsActivity.class);
                startActivityForResult(intent, UserSettingsActivity.REQUEST_PREFERENCE_SETTINGS);
                break;
        }
        return true;
    }

    @Override
    public void setPresenter(@NonNull ItemsContract.Presenter presenter) {
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
    public void enableQuickOrderSelection() {
        if (!mEnableQuickOrderSelection) {
            mEnableQuickOrderSelection = true;
            mListAdapter.setEnableQuickOrderSelection(true);
            updateViewsQuickOrder();
        } else if (mQuickOrderSelectedCounter == 0) {
            mEnableQuickOrderSelection = false;
            mListAdapter.setEnableQuickOrderSelection(false);
            updateViewsQuickOrder();

            // Safe clear
            itemsForQuickOrder.clear();
        }
    }

    private void updateViewsQuickOrder() {
        for (View v : mListAdapter.getUglyFix()) {
            CheckBox quickOrderCB = v.findViewById(R.id.item_set_quick_order);

            if (mEnableQuickOrderSelection) {
                quickOrderCB.setVisibility(View.VISIBLE);
            } else {
                quickOrderCB.setVisibility(View.GONE);
                quickOrderCB.setChecked(false);
            }
        }
    }

    @Override
    public void selectQuickOrderItem(Item item) {
        item.quickOrderSelect();

        if (item.getQuickOrderSelect() && mQuickOrderSelectedCounter == 0) {
            mQuickOrderSelectedCounter++;
            itemsForQuickOrder.add(item);
        } else if (item.getQuickOrderSelect()) {
            mQuickOrderSelectedCounter++;
            itemsForQuickOrder.add(item);
        } else {
            mQuickOrderSelectedCounter--;

            if (itemsForQuickOrder.contains(item)) {
                itemsForQuickOrder.remove(item);
            }

            if (mQuickOrderSelectedCounter == 0) {
                enableQuickOrderSelection();
            }
        }

        getActivity().invalidateOptionsMenu();
    }

    private void resetQuickOrderSelection() {
        for (Item i : itemsForQuickOrder) {
            i.quickOrderSelect();
        }

        mQuickOrderSelectedCounter = 0;
        enableQuickOrderSelection();
        getActivity().invalidateOptionsMenu();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void addToCart() {
        // Update LayerDrawable's BadgeDrawable.
        ActivityUtils.setBadgeCount(getContext(), mIcon, ItemsDataSource.cart.getTotalItemsNumber());
    }

    public interface ItemListener {

        void onItemClick(Item clickedItem);

        void onItemLongClick(Item clickedItem);

        void onAddToCartItemClick(Item clickedItem);

        void onQuickOrderItemClick(Item clickedItem, boolean isChecked);
    }

    private static class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

        private List<Item> mItems;
        private List<View> mUglyFix;

        private ItemListener mItemListener;
        private boolean mQuickOrderAdapter;

        private boolean mEnableQuickOrderSelection;

        ItemsAdapter(List<Item> items, ItemListener itemListener, boolean isQuickOrderAdapter) {
            setList(items);
            mUglyFix = new ArrayList<>();
            mItemListener = itemListener;
            mQuickOrderAdapter = isQuickOrderAdapter;
            mEnableQuickOrderSelection = false;
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
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup view, int viewType) {
            ViewHolder viewHolder;

            View foodView = LayoutInflater.from(view.getContext()).inflate(R.layout.item, view, false);
            viewHolder = new ViewHolder(foodView);

            if (!mUglyFix.contains(foodView)) {
                mUglyFix.add(foodView);
            }

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            Item item = mItems.get(position);

            viewHolder.itemImage.setImageResource(item.getIconId());
            viewHolder.itemTitle.setText(item.getName());
            viewHolder.itemPrice.setText(String.format(Locale.ENGLISH, "%.2f$", item.getPrice()));

            viewHolder.quickOrderCB.setChecked(item.getQuickOrderSelect());
            viewHolder.quickOrderCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.onQuickOrderItemClick(item, viewHolder.quickOrderCB.isChecked());
                }
            });

            viewHolder.addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.onAddToCartItemClick(item);
                }
            });

            if (mEnableQuickOrderSelection) {
                viewHolder.quickOrderCB.setVisibility(View.VISIBLE);
            } else {
                viewHolder.quickOrderCB.setVisibility(View.GONE);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onItemClick(item);
                }
            });

            if (mQuickOrderAdapter) {
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mItemListener.onItemLongClick(item);
                        return true;
                    }
                });
            }
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public List<View> getUglyFix() {
            return mUglyFix;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            CheckBox quickOrderCB;
            ImageView itemImage;
            TextView itemTitle;
            View priceCartView;
            TextView itemPrice;
            Button addToCart;

            ViewHolder(View view) {
                super(view);
                //view.setClipToOutline(true); // For round borders.

                quickOrderCB = view.findViewById(R.id.item_set_quick_order);
                itemImage = view.findViewById(R.id.item_image);
                itemTitle = view.findViewById(R.id.item_title);
                priceCartView = view.findViewById(R.id.price_cart_layout);
                itemPrice = priceCartView.findViewById(R.id.item_price);
                addToCart = priceCartView.findViewById(R.id.item_order_button);
            }
        }

        void setEnableQuickOrderSelection(boolean enable) {
            mEnableQuickOrderSelection = enable;
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
