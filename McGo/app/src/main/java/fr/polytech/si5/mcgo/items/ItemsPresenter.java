package fr.polytech.si5.mcgo.items;

import android.support.annotation.NonNull;

import java.util.List;

import fr.polytech.si5.mcgo.data.Item;

import static com.google.common.base.Preconditions.checkNotNull;

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
        mItemsView.showItems(requestedDataSource);
    }

    @Override
    public void openItemDetails(@NonNull Item requestedItem) {
        // Do nothing atm.
    }

    @Override
    public void selectItemForQuickOrder(@NonNull Item requestedItem) {

    }

    @Override
    public void addToCart(@NonNull Item requestedItem) {

    }
}