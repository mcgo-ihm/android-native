package fr.polytech.si5.mcgo.data.local;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.data.Item;

public final class ItemsDataSource {

    public static final List<Item> itemsDataSource = new ArrayList<>();
    public static final List<Item> quickOrderItemsDataSource = new ArrayList<>();
    public static final Map<Item, Integer> itemsToOrder = new LinkedHashMap<>();
    public static int cartSize = 0;
    public static float cartPrice = 0f;

    static {
        int counter = 1;

        itemsDataSource.add(new Item(counter++, "Kung Pao Chicken", null, 10.50, R.drawable.kung_pao_chicken_low_res));
        itemsDataSource.add(new Item(counter++, "Ma Po Tofu", null, 9.0, R.drawable.ma_po_tofu_low_res));
        itemsDataSource.add(new Item(counter++, "Tacos", null, 6.75, R.drawable.tacos_low_res));
        itemsDataSource.add(new Item(counter++, "Quesadillas", null, 6.25, R.drawable.quesadillas_low_res));
        itemsDataSource.add(new Item(counter++, "Buldak", null, 8.0, R.drawable.buldak_low_res));
        itemsDataSource.add(new Item(counter++, "Abiko Curry", null, 12.0, null));
        itemsDataSource.add(new Item(counter++, "Margherita", null, 11.0, R.drawable.margherita_low_res));
        itemsDataSource.add(new Item(counter++, "Napoli", null, 10.75, R.drawable.napoli_low_res));
        itemsDataSource.add(new Item(counter++, "Gazpachuelo", null, 13.10, R.drawable.gazpachuelo_low_res));
        itemsDataSource.add(new Item(counter++, "Paella", null, 19.80, R.drawable.paella_low_res));
        itemsDataSource.add(new Item(counter++, "Watermelon and feta", null, 12.20, R.drawable.watermelon_and_feta_low_res));
        itemsDataSource.add(new Item(counter++, "Sushi salad", null, 14.50, R.drawable.sushi_salad_low_res));
        itemsDataSource.add(new Item(counter, "Sweet and Sour Pork", null, 15.0, R.drawable.sweet_and_sour_pork_low_res));
    }
}
