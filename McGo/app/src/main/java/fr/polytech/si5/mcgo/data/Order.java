package fr.polytech.si5.mcgo.data;

import android.location.Location;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.google.common.base.Objects;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.polytech.si5.mcgo.data.local.ItemsDataSource;
import fr.polytech.si5.mcgo.orders.ParentListItem;

public final class Order implements ParentListItem {

    // Useless
    public enum OrderState {
        IN_PROGRESS, DELIVERED
    }

    private int mId;
    private LocalDateTime mDate;
    private List<Item> mListOfItems;
    private float mPrice;
    private int mTotalItemsNumber;
    private Location location;

    private CountDownTimer cdt;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Order() {
        mId = ItemsDataSource.ORDER_ID++;
        mDate = LocalDateTime.now();
        mListOfItems = new ArrayList<>();
        mPrice = 0;
        mTotalItemsNumber = 0;
        state = OrderState.IN_PROGRESS;
    }

    private OrderState state;

    // Default constructor

    public Order(Order order) {
        this.mId = order.getId();
        this.mDate = order.getDate();
        this.mListOfItems = new ArrayList<>(order.getListOfItems());
        this.mPrice = order.getPrice();
        this.mTotalItemsNumber = order.getTotalItemsNumber();
        this.state = order.getState();
        this.location = order.getLocation();
        this.cdt = order.getCdt();
    }

    public void setDate(LocalDateTime date) {
        mDate = date;
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
    public List<Item> getListOfItems() {
        return mListOfItems;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setListOfItems(List<Item> listOfItems) {
        reset();

        for (Item i : listOfItems) {
            Item item = new Item(i);
            item.setQuantity(1);
            mPrice += item.getPrice();
            mTotalItemsNumber++;
            mListOfItems.add(item);
        }
    }

    @NonNull
    public Float getPrice() {
        return mPrice;
    }

    public void setPrice(float total) {
        mPrice = total;
    }

    public int getTotalItemsNumber() {
        return mTotalItemsNumber;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addItem(Item item) {
        mTotalItemsNumber++;
        mPrice += item.getPrice();

        if (mListOfItems.contains(item)) {
            Optional<Item> oItem = getItem(item);
            oItem.ifPresent(Item::increaseQuantity);
        } else {
            Item newItem = new Item(item);
            newItem.setQuantity(1);
            mListOfItems.add(newItem);
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public CountDownTimer getCdt() {
        return cdt;
    }

    // Business

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeItem(Item item) {
        if (mListOfItems.contains(item)) {
            Optional<Item> oItem = getItem(item);

            if (oItem.isPresent()) {
                oItem.get().decreaseQuantity();
                mPrice -= oItem.get().getPrice();
                mTotalItemsNumber--;

                if (oItem.get().getQuantity() <= 0) {
                    mListOfItems.remove(oItem.get());
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Optional<Item> getItem(Item item) {
        for (Item i : mListOfItems) {
            if (i.equals(item)) {
                return Optional.of(i);
            }
        }

        return Optional.empty();
    }

    @NonNull
    public Float calculatePrice() {
        float price = 0f;

        for (Item i : mListOfItems) {
            price += i.getPrice() * i.getQuantity();
        }

        // Safe update
        mPrice = price;

        return price;
    }

    @NonNull
    public Integer calculateTotalItemsQuantity() {
        int total = 0;

        for (Item i : mListOfItems) {
            total += i.getQuantity();
        }

        // Safe update
        mTotalItemsNumber = total;

        return total;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void reset() {
        mDate = LocalDateTime.now();
        mListOfItems.clear();
        mTotalItemsNumber = 0;
        mPrice = 0f;
        state = OrderState.IN_PROGRESS;
    }

    public void startTimer(int millis) {
        cdt = new CountDownTimer(millis, millis / 2) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        };

        cdt.start();
    }

    // Useless

    @NonNull
    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    // ParentListItem overrides

    @Override
    public List<?> getChildItemList() {
        return mListOfItems;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    // Equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equal(mId, order.mId) &&
                Objects.equal(mDate, order.mDate) &&
                Objects.equal(mListOfItems, order.mListOfItems) &&
                Objects.equal(mPrice, order.mPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mDate, mListOfItems, mPrice);
    }

    @Override
    public String toString() {
        return "Order n°" + mId;
    }
}
