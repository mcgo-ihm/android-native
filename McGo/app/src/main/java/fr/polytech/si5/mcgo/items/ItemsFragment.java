package fr.polytech.si5.mcgo.items;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import fr.polytech.si5.mcgo.data.Item;
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

    public ItemsFragment() {
        // Requires empty public constructor.
    }

    public static ItemsFragment newInstance() {
        return new ItemsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new ItemsAdapter(new ArrayList<>(0), mItemListener);
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
        ListView listView = (ListView) root.findViewById(R.id.items_list);
        listView.setAdapter(mListAdapter);
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
                // Set quick order with the set of selected items
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
        menu.getItem(0).setEnabled(false);
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

    public interface ItemListener {

        void onItemClick(Item clickedItem);

        void onAddToCartItemClick(Item clickedItem);

        void onQuickOrderItemClick(Item clickedItem, boolean isChecked);
    }

    private static class ItemsAdapter extends BaseAdapter {

        private List<Item> mItems;
        private ItemListener mItemListener;

        public ItemsAdapter(List<Item> items, ItemListener itemListener) {
            setList(items);
            mItemListener = itemListener;
        }

        public void replaceData(List<Item> items) {
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
            View rowView = view;
            //view.setClipToOutline(true); // For round borders.

            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.food_item, viewGroup, false);
            }

            final Item item = getItem(i);

            CheckBox quickOrderCB = (CheckBox) rowView.findViewById(R.id.item_set_quick_order);
            ImageView itemImage = (ImageView) rowView.findViewById(R.id.item_image);
            TextView itemTitle = (TextView) rowView.findViewById(R.id.item_title);
            View priceCartView = rowView.findViewById(R.id.price_cart_layout);
            TextView itemPrice = priceCartView.findViewById(R.id.item_price);
            Button addToCart = (Button) priceCartView.findViewById(R.id.item_order_button);

            itemTitle.setText(item.getName());
            itemPrice.setText(String.format(Locale.ENGLISH, "%.2f$", item.getPrice()));

            quickOrderCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.onQuickOrderItemClick(item, quickOrderCB.isChecked());
                }
            });

            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.onAddToCartItemClick(item);
                }
            });

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onItemClick(item);
                }
            });

            return rowView;
        }
    }
}
