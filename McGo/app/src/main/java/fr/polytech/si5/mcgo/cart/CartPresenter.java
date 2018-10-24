package fr.polytech.si5.mcgo.cart;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

import fr.polytech.si5.mcgo.Utils.OrderUtils;
import fr.polytech.si5.mcgo.data.Item;
import fr.polytech.si5.mcgo.data.Order;

import static com.google.common.base.Preconditions.checkNotNull;

public class CartPresenter implements CartContract.Presenter {

    private final CartContract.View mItemsView;
    private Order cart;

    public CartPresenter(@NonNull CartContract.View itemsView) {
        mItemsView = checkNotNull(itemsView, "itemsView cannot be null!");
        mItemsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadDataSource(cart);
        mItemsView.updateCartStatus(cart.getTotalItemsNumber(), cart.getPrice());
    }

    @Override
    public void loadDataSource(@NonNull Order order) {
        cart = order;

        if (cart.getListOfItems().isEmpty()) {
            mItemsView.showNoItems();
        } else {
            mItemsView.showItems(cart.getListOfItems());
        }
    }

    @Override
    public void openItemDetails(@NonNull Item requestedItem) {
        // Do nothing atm.
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void addItemToCart(TextView itemCount, @NonNull Item requestedItem) {
        cart.addItem(requestedItem);
        mItemsView.addItemToCart(itemCount, requestedItem, cart.getTotalItemsNumber(), cart.getPrice());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void removeItemFromCart(TextView itemQuantityView, @NonNull Item requestedItem) {
        if (cart.getListOfItems().contains(requestedItem)) {
            cart.removeItem(requestedItem);
        }

        mItemsView.removeItemFromCart(itemQuantityView, requestedItem, cart.getTotalItemsNumber(), cart.getPrice());

        if (cart.getTotalItemsNumber() == 0) {
            mItemsView.showNoItems();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void confirmCart() {
        OrderUtils.confirmCart(cart);
        clearCart();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void clearCart() {
        cart.reset();
        mItemsView.clearCart();
    }
}
