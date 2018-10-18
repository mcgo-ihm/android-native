package fr.polytech.si5.mcgo.orders;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.data.Order;

public class OrderViewHolder extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;

    private View mViewHolderContent;
    private TextView mOrderPrice;
    private TextView mOrderTotalItems;
    private TextView mOrderDate;
    private ImageView mExpandCollapseArrow;

    public OrderViewHolder(View itemView) {
        super(itemView);

        mViewHolderContent = (View) itemView.findViewById(R.id.view_holder_content);
        mOrderPrice = (TextView) mViewHolderContent.findViewById(R.id.order_price);
        mOrderTotalItems = (TextView) mViewHolderContent.findViewById(R.id.order_total_items);
        mOrderDate = (TextView) mViewHolderContent.findViewById(R.id.order_date);
        mExpandCollapseArrow = (ImageView) mViewHolderContent.findViewById(R.id.ic_arrow_expand);
    }

    public void bind(Context context, Order order) {
        mOrderPrice.setText(String.format(Locale.ENGLISH, "%s %.2f$",
                context.getResources().getString(R.string.order_price), order.getTotal()));
        mOrderTotalItems.setText(String.format(Locale.ENGLISH, "%s %d",
                context.getResources().getString(R.string.order_items_number), order.getTotalItems()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mOrderDate.setText(order.getDate().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")));
        } else {
            mOrderDate.setText(order.getDate().toString());
        }
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);

        if (expanded) {
            mExpandCollapseArrow.setRotation(ROTATED_POSITION);
        } else {
            mExpandCollapseArrow.setRotation(INITIAL_POSITION);
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);

        RotateAnimation rotateAnimation;
        if (expanded) { // rotate clockwise
            rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        } else { // rotate counterclockwise
            rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        }

        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);
        mExpandCollapseArrow.startAnimation(rotateAnimation);
    }
}
