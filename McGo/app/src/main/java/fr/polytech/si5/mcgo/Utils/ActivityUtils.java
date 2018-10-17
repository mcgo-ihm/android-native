package fr.polytech.si5.mcgo.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.data.Item;
import fr.polytech.si5.mcgo.data.local.ItemsDataSource;
import fr.polytech.si5.mcgo.orders.BadgeDrawable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This provides methods to help Activities load their UI.
 */
public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, int count) {
        BadgeDrawable badge;
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);

        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    public static int calculateCartSize() {
        int size = 0;

        for (Item i : ItemsDataSource.itemsToOrder.keySet()) {
            size += ItemsDataSource.itemsToOrder.get(i);
        }

        size = size < 0 ? 0 : size;
        ItemsDataSource.cartSize = size;
        return size;
    }

    /**
     * Calculates / Updates cart total price.
     *
     * @param item the last item added or removed.
     * @param add  add operation if true, remove operation otherwise.
     * @return The updated cart price
     */
    public static float calculatePrice(@Nullable Item item, boolean add) {
        float price = ItemsDataSource.cartPrice;

        if (item == null) {
            price = 0;

            for (Item i : ItemsDataSource.itemsToOrder.keySet()) {
                price += i.getPrice() * ItemsDataSource.itemsToOrder.get(i);
            }
        } else {
            if (add) {
                price += item.getPrice();
            } else {
                price -= item.getPrice();
            }
        }

        price = price < 0 ? 0 : price;
        ItemsDataSource.cartPrice = price;
        return price;
    }
}
