package fr.polytech.si5.mcgo.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;

public final class Item {

    private final int mId;
    private final String mName;
    private final String mDescription;
    private final double mPrice;

    /**
     * Use this constructor to create a new Item.
     *
     * @param id          id of the item
     * @param name        name of the item
     * @param description name of the item
     * @param price       name of the item
     */
    public Item(@NonNull Integer id, @NonNull String name, @Nullable String description, @NonNull Double price) {
        mId = id;
        mName = name;
        mDescription = description;
        mPrice = price;
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
