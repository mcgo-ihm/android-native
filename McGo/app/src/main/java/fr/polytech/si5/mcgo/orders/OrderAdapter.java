package fr.polytech.si5.mcgo.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.data.Item;
import fr.polytech.si5.mcgo.data.Order;

public class OrderAdapter extends ExpandableRecyclerAdapter<OrderViewHolder, ItemsViewHolder> {

    private Context context;
    private LayoutInflater mInflater;

    public OrderAdapter(Context context, List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public OrderViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View orderView = mInflater.inflate(R.layout.order_parent_view, parentViewGroup, false);
        return new OrderViewHolder(orderView);
    }

    @Override
    public ItemsViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View itemsView = mInflater.inflate(R.layout.order_expand_view, childViewGroup, false);
        return new ItemsViewHolder(itemsView);
    }

    @Override
    public void onBindParentViewHolder(OrderViewHolder orderViewHolder, int position, ParentListItem parentListItem) {
        Order order = (Order) parentListItem;
        orderViewHolder.bind(context, order);
    }

    @Override
    public void onBindChildViewHolder(ItemsViewHolder itemsViewHolder, int position, Object childListItem) {
        Item item = (Item) childListItem;
        itemsViewHolder.bind(context, item);
    }

    public void refreshData() {
        notifyDataSetChanged();
    }
}
