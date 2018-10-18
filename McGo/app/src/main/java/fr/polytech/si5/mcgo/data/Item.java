package fr.polytech.si5.mcgo.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;

import fr.polytech.si5.mcgo.R;

public final class Item {

    private final int mId;
    private final String mName;
    private final String mDescription;
    private final double mPrice;
    private final int mIconId;

    /**
     * Item quantity for items in order.
     */
    private int mQuantity;

    private boolean mQuickOrderSelect;

    /**
     * Use this constructor to create a new Item.
     *
     * @param id          id of the item
     * @param name        name of the item
     * @param description description of the item
     * @param price       price of the item
     * @param iconId      id of the icon
     */
    public Item(@NonNull Integer id, @NonNull String name, @Nullable String description, @NonNull Double price, @Nullable Integer iconId) {
        mId = id;
        mName = name;
        mDescription = description;
        mPrice = price;

        if (iconId != null) {
            mIconId = iconId;
        } else {
            mIconId = R.drawable.ic_default_food_item;
        }

        mQuickOrderSelect = false;
    }

    @NonNull
    public Integer getId() {
        return mId;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    @NonNull
    public Double getPrice() {
        return mPrice;
    }

    @NonNull
    public Boolean getQuickOrderSelect() {
        return mQuickOrderSelect;
    }

    @NonNull
    public Integer getIconId() {
        return mIconId;
    }

    @NonNull
    public Integer getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public void quickOrderSelect() {
        mQuickOrderSelect = !mQuickOrderSelect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equal(mId, item.mId) &&
                Objects.equal(mName, item.mName) &&
                Objects.equal(mDescription, item.mDescription) &&
                Objects.equal(mPrice, item.mPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mName, mDescription, mPrice);
    }

    @Override
    public String toString() {
        return "Item named " + mName;
    }
}
