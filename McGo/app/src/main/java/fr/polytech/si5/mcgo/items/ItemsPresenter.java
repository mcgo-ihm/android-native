package fr.polytech.si5.mcgo.items;

import android.support.annotation.NonNull;

import java.util.List;

import fr.polytech.si5.mcgo.data.Item;
import fr.polytech.si5.mcgo.data.local.ItemsDataSource;

import static com.google.common.base.Preconditions.checkNotNull;
import static fr.polytech.si5.mcgo.data.local.ItemsDataSource.itemsToOrder;

/**
 * Listens to user actions from the UI ({@link ItemsFragment}), retrieves the data and updates the
 * UI as required.
 */
public class ItemsPresenter implements ItemsContract.Presenter {

    private final ItemsContract.View mItemsView;

    public ItemsPresenter(@NonNull ItemsContract.View itemsView) {
        mItemsView = checkNotNull(itemsView, "itemsView cannot be null!");
        mItemsView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadDataSource(@NonNull List<Item> requestedDataSource) {
        if (requestedDataSource.isEmpty()) {
            mItemsView.showNoItems();
        } else {
            mItemsView.showItems(requestedDataSource);
        }
    }

    @Override
    public void openItemDetails(@NonNull Item requestedItem) {
        // Do nothing atm.
    }

    @Override
    public void selectItemForQuickOrder(@NonNull Item requestedItem) {
        mItemsView.selectQuickOrderItem(requestedItem);
    }

    @Override
    public void enableQuickOrderSelection(@NonNull Item requestedItem) {
        mItemsView.enableQuickOrderSelection();
    }

    @Override
    public void addToCart(@NonNull Item requestedItem) {
        if (ItemsDataSource.itemsToOrder.containsKey(requestedItem)) {
            ItemsDataSource.itemsToOrder.put(requestedItem, ItemsDataSource.itemsToOrder.get(requestedItem) + 1);
        } else {
            ItemsDataSource.itemsToOrder.put(requestedItem, 1);
        }

        ItemsDataSource.cartSize++;
        requestedItem.setQuantity(itemsToOrder.get(requestedItem));
        mItemsView.addToCart();
    }
}
