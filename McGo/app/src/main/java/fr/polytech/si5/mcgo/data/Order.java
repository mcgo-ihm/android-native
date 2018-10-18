package fr.polytech.si5.mcgo.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;

import java.time.LocalDateTime;
import java.util.List;

import fr.polytech.si5.mcgo.orders.ParentListItem;

public final class Order implements ParentListItem {

    // Useless
    public enum OrderState {IN_PROGRESS, DELIVERED}

    private final int mId;
    private final LocalDateTime mDate;
    private final List<Item> mListOfItems;
    private final double mTotal;
    private OrderState state;

    /**
     * Use this constructor to create a new Order.
     *
     * @param id          id of the order
     * @param date        date whose order has been validated
     * @param listOfItems list of items to order
     * @param total       sum of all items in {@see listOfItems}
     */
    public Order(@NonNull Integer id, @NonNull LocalDateTime date, @Nullable List<Item> listOfItems, @NonNull Double total) {
        mId = id;
        mDate = date;
        mListOfItems = listOfItems;
        mTotal = total;
        state = OrderState.IN_PROGRESS;
    }

    @NonNull
    public Integer getId() {
        return mId;
    }

    @NonNull
    public LocalDateTime getDate() {
        return mDate;
    }

    @NonNull
    public Double getTotal() {
        return mTotal;
    }

    @NonNull
    public Integer getTotalItems() {
        int total = 0;

        for (Item i : mListOfItems) {
            total += i.getQuantity();
        }

        return total;
    }

    @NonNull
    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    @Override
    public List<?> getChildItemList() {
        return mListOfItems;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equal(mId, order.mId) &&
                Objects.equal(mDate, order.mDate) &&
                Objects.equal(mListOfItems, order.mListOfItems) &&
                Objects.equal(mTotal, order.mTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mDate, mListOfItems, mTotal);
    }

    @Override
    public String toString() {
        return "Order n°" + mId;
    }
}
