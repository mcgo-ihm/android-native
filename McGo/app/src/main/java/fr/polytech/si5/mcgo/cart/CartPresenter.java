package fr.polytech.si5.mcgo.cart;

import android.support.annotation.NonNull;
import android.widget.TextView;

import java.util.List;

import fr.polytech.si5.mcgo.data.Item;
import fr.polytech.si5.mcgo.data.local.ItemsDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

public class CartPresenter implements CartContract.Presenter {

    private final CartContract.View mItemsView;

    public CartPresenter(@NonNull CartContract.View itemsView) {
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
    public void addItemToCart(TextView itemCount, @NonNull Item requestedItem) {
        if (ItemsDataSource.itemsToOrder.containsKey(requestedItem)) {
            ItemsDataSource.itemsToOrder.put(requestedItem, ItemsDataSource.itemsToOrder.get(requestedItem) + 1);
        } else {
            ItemsDataSource.itemsToOrder.put(requestedItem, 1);
        }

        mItemsView.addItemToCart(itemCount, requestedItem);
    }

    @Override
    public void removeItemFromCart(TextView itemCount, @NonNull Item requestedItem) {
        if (ItemsDataSource.itemsToOrder.containsKey(requestedItem)) {
            if (ItemsDataSource.itemsToOrder.get(requestedItem) == 1) {
                ItemsDataSource.itemsToOrder.remove(requestedItem);
            } else {
                ItemsDataSource.itemsToOrder.put(requestedItem, ItemsDataSource.itemsToOrder.get(requestedItem) - 1);
            }
        }

        mItemsView.removeItemFromCart(itemCount, requestedItem);

        if (ItemsDataSource.cartSize == 0) {
            mItemsView.showNoItems();
        }
    }

    @Override
    public void confirmOrder() {

    }

    @Override
    public void clearCart() {
        ItemsDataSource.itemsToOrder.clear();
        ItemsDataSource.cartSize = 0;
        ItemsDataSource.cartPrice = 0f;
        mItemsView.clearCart();
    }
}
