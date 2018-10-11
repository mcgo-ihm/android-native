package fr.polytech.si5.mcgo.items;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.data.Constants;
import fr.polytech.si5.mcgo.data.Item;
import fr.polytech.si5.mcgo.data.local.ItemsDataSource;
import fr.polytech.si5.mcgo.settings.UserSettingsActivity;

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

    private ItemsAdapter mListAdapter;
    private LinearLayout mItemsView;
    private LinearLayout mNoItemsView;
    private ImageView mNoItemImage;
    private TextView mNoItemTextView;
    private ListView mListView;

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

        if (args != null) {
            mQuickOrderFragment = args.getBoolean(Constants.FRAGMENT_BUNDLE_QUICK_ORDER_KEY, false);
        }

        mListAdapter = new ItemsAdapter(new ArrayList<>(0), mItemListener, mQuickOrderFragment);
        itemsForQuickOrder = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.items_frag, container, false);

        // Set up items view
        mListView = (ListView) root.findViewById(R.id.items_list);
        mListView.setAdapter(mListAdapter);
        mItemsView = (LinearLayout) root.findViewById(R.id.items_linear_layout);

        // Set up no items view
        mNoItemsView = root.findViewById(R.id.no_items);
        mNoItemImage = (ImageView) root.findViewById(R.id.no_items_image);
        mNoItemTextView = (TextView) root.findViewById(R.id.no_items_text);

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_set_quick_order:
                // Set quick order
                ItemsDataSource.quickOrderItemsDataSource.clear();
                ItemsDataSource.quickOrderItemsDataSource.addAll(itemsForQuickOrder);
                resetQuickOrderSelection();
                Snackbar.make(((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0),
                        "Quick Order Food Set saved", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.menu_preferences:
                // Load preferences activity
                Intent intent = new Intent(getContext(), UserSettingsActivity.class);
                startActivityForResult(intent, UserSettingsActivity.REQUEST_PREFERENCE_SETTINGS);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.items_fragment_menu, menu);

        if (mQuickOrderFragment) {
            menu.getItem(0).setEnabled(false);
        } else {
            menu.getItem(0).setVisible(false);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mEnableQuickOrderSelection && mQuickOrderSelectedCounter > 0) {
            menu.getItem(0).setEnabled(true);
        } else {
            menu.getItem(0).setEnabled(false);
        }
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
        for (int i = 0; i < mListView.getCount(); ++i) {
            View itemView = mListView.getChildAt(i);

            if (itemView != null) {
                CheckBox quickOrderCB = (CheckBox) itemView.findViewById(R.id.item_set_quick_order);

                if (mEnableQuickOrderSelection) {
                    quickOrderCB.setVisibility(View.VISIBLE);
                } else {
                    quickOrderCB.setVisibility(View.GONE);
                    quickOrderCB.setChecked(false);
                }
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

    public interface ItemListener {

        void onItemClick(Item clickedItem);

        void onItemLongClick(Item clickedItem);

        void onAddToCartItemClick(Item clickedItem);

        void onQuickOrderItemClick(Item clickedItem, boolean isChecked);
    }

    private static class ItemsAdapter extends BaseAdapter {

        private List<Item> mItems;
        private ItemListener mItemListener;

        private boolean mQuickOrderAdapter;
        private boolean mEnableQuickOrderSelection;

        ItemsAdapter(List<Item> items, ItemListener itemListener, boolean isQuickOrderAdapter) {
            setList(items);
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

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Item getItem(int i) {
            return mItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;

            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                view = inflater.inflate(R.layout.food_item, viewGroup, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            final Item item = getItem(i);

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

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onItemClick(item);
                }
            });

            if (mQuickOrderAdapter) {
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mItemListener.onItemLongClick(item);
                        return true;
                    }
                });
            }

            if (mEnableQuickOrderSelection) {
                viewHolder.quickOrderCB.setVisibility(View.VISIBLE);
            } else {
                viewHolder.quickOrderCB.setVisibility(View.GONE);
            }

            return view;
        }

        void setEnableQuickOrderSelection(boolean enable) {
            mEnableQuickOrderSelection = enable;
        }

        private class ViewHolder {

            CheckBox quickOrderCB;
            ImageView itemImage;
            TextView itemTitle;
            View priceCartView;
            TextView itemPrice;
            Button addToCart;

            ViewHolder(View view) {
                //view.setClipToOutline(true); // For round borders.

                quickOrderCB = (CheckBox) view.findViewById(R.id.item_set_quick_order);
                itemImage = (ImageView) view.findViewById(R.id.item_image);
                itemTitle = (TextView) view.findViewById(R.id.item_title);
                priceCartView = view.findViewById(R.id.price_cart_layout);
                itemPrice = priceCartView.findViewById(R.id.item_price);
                addToCart = (Button) priceCartView.findViewById(R.id.item_order_button);
            }
        }
    }
}
