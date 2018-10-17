package fr.polytech.si5.mcgo.data.local;

import java.util.ArrayList;
import java.util.List;

import fr.polytech.si5.mcgo.R;
import fr.polytech.si5.mcgo.data.Item;

public final class ItemsDataSource {

    public static final List<Item> itemsDataSource = new ArrayList<>();
    public static final List<Item> quickOrderItemsDataSource = new ArrayList<>();
    public static final List<Item> itemsToOrder = new ArrayList<>();

    static {
        int counter = 1;

        itemsDataSource.add(new Item(counter++, "Kung Pao Chicken", null, 10.50, R.drawable.kung_pao_chicken));
        itemsDataSource.add(new Item(counter++, "Ma Po Tofu", null, 9.0, R.drawable.ma_po_tofu));
        itemsDataSource.add(new Item(counter++, "Tacos", null, 6.75, R.drawable.tacos));
        itemsDataSource.add(new Item(counter++, "Quesadillas", null, 6.25, R.drawable.quesadillas));
        itemsDataSource.add(new Item(counter++, "Buldak", null, 8.0, R.drawable.buldak));
        itemsDataSource.add(new Item(counter++, "Abiko Curry", null, 12.0, null));
        itemsDataSource.add(new Item(counter++, "Margherita", null, 11.0, R.drawable.margherita));
        itemsDataSource.add(new Item(counter++, "Napoli", null, 10.75, R.drawable.napoli));
        itemsDataSource.add(new Item(counter++, "Gazpachuelo", null, 13.10, R.drawable.gazpachuelo));
        itemsDataSource.add(new Item(counter++, "Paella", null, 19.80, R.drawable.paella));
        itemsDataSource.add(new Item(counter++, "Watermelon and feta", null, 12.20, R.drawable.watermelon_and_feta));
        itemsDataSource.add(new Item(counter++, "Sushi salad", null, 14.50, R.drawable.sushi_salad));
        itemsDataSource.add(new Item(counter, "Sweet and Sour Pork", null, 15.0, R.drawable.sweet_and_sour_pork));
    }
}
