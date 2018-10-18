package fr.polytech.si5.mcgo.orders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.data.Item;

public class ItemsViewHolder extends ChildViewHolder {

    private ImageView mItemImage;
    private TextView mItemTitle;
    private View mPriceCartLayout;
    private TextView mItemQty;
    private TextView mItemUnitPrice;

    public ItemsViewHolder(View itemView) {
        super(itemView);

        mItemImage = (ImageView) itemView.findViewById(R.id.item_image);
        mItemTitle = (TextView) itemView.findViewById(R.id.item_title);
        mPriceCartLayout = (View) itemView.findViewById(R.id.price_cart_layout);
        mItemQty = (TextView) mPriceCartLayout.findViewById(R.id.item_qty);
        mItemUnitPrice = (TextView) mPriceCartLayout.findViewById(R.id.item_unit_price);
    }

    public void bind(Context context, Item item) {
        mItemImage.setImageResource(item.getIconId());
        mItemTitle.setText(item.getName());
        mItemQty.setText(String.format(Locale.ENGLISH, "%s %d",
                context.getResources().getString(R.string.item_qty), item.getQuantity()));
        mItemUnitPrice.setText(String.format(Locale.ENGLISH, "%s %f",
                context.getResources().getString(R.string.item_unit_price), item.getPrice()));
    }
}
