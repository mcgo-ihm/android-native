package fr.polytech.si5.mcgo.cart;

import android.support.annotation.NonNull;
import android.widget.TextView;

import java.util.List;

import fr.polytech.si5.mcgo.BasePresenter;
import fr.polytech.si5.mcgo.BaseView;
import fr.polytech.si5.mcgo.data.Item;

public interface CartContract {

    interface View extends BaseView<CartContract.Presenter> {

        void showItems(List<Item> items);

        void showNoItems();

        void addItemToCart(TextView itemCount, @NonNull Item requestedItem);

        void removeItemFromCart(TextView itemCount, @NonNull Item requestedItem);

        void clearCart();

    }

    interface Presenter extends BasePresenter {

        void loadDataSource(@NonNull List<Item> requestedDataSource);

        void openItemDetails(@NonNull Item requestedItem);

        void addItemToCart(TextView itemCount, @NonNull Item requestedItem);

        void removeItemFromCart(TextView itemCount, @NonNull Item requestedItem);

        void confirmOrder();

        void clearCart();

    }
}
