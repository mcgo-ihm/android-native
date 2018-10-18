package fr.polytech.si5.mcgo.cart;

import android.support.annotation.NonNull;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.polytech.si5.mcgo.Utils.ActivityUtils;
import fr.polytech.si5.mcgo.data.Item;
import fr.polytech.si5.mcgo.data.Order;

import static com.google.common.base.Preconditions.checkNotNull;
import static fr.polytech.si5.mcgo.data.local.ItemsDataSource.ORDER_ID;
import static fr.polytech.si5.mcgo.data.local.ItemsDataSource.cartPrice;
import static fr.polytech.si5.mcgo.data.local.ItemsDataSource.cartSize;
import static fr.polytech.si5.mcgo.data.local.ItemsDataSource.itemsToOrder;
import static fr.polytech.si5.mcgo.data.local.ItemsDataSource.ordersInProgress;

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
        if (itemsToOrder.containsKey(requestedItem)) {
            itemsToOrder.put(requestedItem, itemsToOrder.get(requestedItem) + 1);
        } else {
            itemsToOrder.put(requestedItem, 1);
        }

        requestedItem.setQuantity(itemsToOrder.get(requestedItem));
        mItemsView.addItemToCart(itemCount, requestedItem);
    }

    @Override
    public void removeItemFromCart(TextView itemCount, @NonNull Item requestedItem) {
        if (itemsToOrder.containsKey(requestedItem)) {
            if (itemsToOrder.get(requestedItem) == 1) {
                itemsToOrder.remove(requestedItem);
            } else {
                itemsToOrder.put(requestedItem, itemsToOrder.get(requestedItem) - 1);
            }
        }

        requestedItem.setQuantity(itemsToOrder.get(requestedItem));
        mItemsView.removeItemFromCart(itemCount, requestedItem);

        if (cartSize == 0) {
            mItemsView.showNoItems();
        }
    }

    @Override
    public void confirmOrder() {
        ActivityUtils.confirmOrder();
        clearCart();
    }

    @Override
    public void clearCart() {
        mItemsView.clearCart();
    }
}
