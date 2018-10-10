package fr.polytech.si5.mcgo.items;

import android.support.annotation.NonNull;

import java.util.List;

import fr.polytech.si5.mcgo.BasePresenter;
import fr.polytech.si5.mcgo.BaseView;
import fr.polytech.si5.mcgo.data.Item;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface ItemsContract {

    interface View extends BaseView<Presenter> {

        void showItems(List<Item> items);

    }

    interface Presenter extends BasePresenter {

        void loadDataSource(@NonNull List<Item> requestedDataSource);

        void openItemDetails(@NonNull Item requestedItem);

        void selectItemForQuickOrder(@NonNull Item requestedItem);

        void addToCart(@NonNull Item requestedItem);

    }
}
