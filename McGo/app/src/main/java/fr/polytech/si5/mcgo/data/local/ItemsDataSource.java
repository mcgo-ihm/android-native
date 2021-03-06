package fr.polytech.si5.mcgo.data.local;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.data.Item;
import fr.polytech.si5.mcgo.data.Order;

@RequiresApi(api = Build.VERSION_CODES.O)
public final class ItemsDataSource {

    // Main
    public static final List<Item> ITEMS_DATA_SOURCE = new ArrayList<>();
    public static final List<Order> ORDERS_IN_PROGRESS = new LinkedList<>();
    public static final List<Order> ORDERS_DELIVERED = new LinkedList<>();

    // Orders
    public static int ORDER_ID = 0;
    // Cart
    public static Order cart;
    // Quick Orders
    public static Order quickOrder;

    static {
        quickOrder = new Order();
        cart = new Order();

        int counter = 1;

        ITEMS_DATA_SOURCE.add(new Item(counter++, "Kung Pao Chicken", null, 10.50, R.drawable.kung_pao_chicken_low_res));
        ITEMS_DATA_SOURCE.add(new Item(counter++, "Ma Po Tofu", null, 9.0, R.drawable.ma_po_tofu_low_res));
        ITEMS_DATA_SOURCE.add(new Item(counter++, "Tacos", null, 6.75, R.drawable.tacos_low_res));
        ITEMS_DATA_SOURCE.add(new Item(counter++, "Quesadillas", null, 6.25, R.drawable.quesadillas_low_res));
        ITEMS_DATA_SOURCE.add(new Item(counter++, "Buldak", null, 8.0, R.drawable.buldak_low_res));
        ITEMS_DATA_SOURCE.add(new Item(counter++, "Abiko Curry", null, 12.0, null));
        ITEMS_DATA_SOURCE.add(new Item(counter++, "Margherita", null, 11.0, R.drawable.margherita_low_res));
        ITEMS_DATA_SOURCE.add(new Item(counter++, "Napoli", null, 10.75, R.drawable.napoli_low_res));
        ITEMS_DATA_SOURCE.add(new Item(counter++, "Gazpachuelo", null, 13.10, R.drawable.gazpachuelo_low_res));
        ITEMS_DATA_SOURCE.add(new Item(counter++, "Paella", null, 19.80, R.drawable.paella_low_res));
        ITEMS_DATA_SOURCE.add(new Item(counter++, "Watermelon and feta", null, 12.20, R.drawable.watermelon_and_feta_low_res));
        ITEMS_DATA_SOURCE.add(new Item(counter++, "Sushi salad", null, 14.50, R.drawable.sushi_salad_low_res));
        ITEMS_DATA_SOURCE.add(new Item(counter, "Sweet and Sour Pork", null, 15.0, R.drawable.sweet_and_sour_pork_low_res));
    }
}
