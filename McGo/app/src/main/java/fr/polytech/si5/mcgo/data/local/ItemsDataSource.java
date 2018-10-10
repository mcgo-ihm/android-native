package fr.polytech.si5.mcgo.data.local;

import java.util.ArrayList;
import java.util.List;

import fr.polytech.si5.mcgo.data.Item;

public final class ItemsDataSource {

    public static final List<Item> itemsDataSource = new ArrayList<>();
    public static final List<Item> quickOrderItemsDataSource = new ArrayList<>();

    static {
        int counter = 1;

        itemsDataSource.add(new Item(counter++, "Kung Pao Chicken", null, 10.50));
        itemsDataSource.add(new Item(counter++, "Ma Po Tofu", null, 9.0));
        itemsDataSource.add(new Item(counter++, "Tacos", null, 6.75));
        itemsDataSource.add(new Item(counter++, "Quesadillas", null, 6.25));
        itemsDataSource.add(new Item(counter++, "Buldak", null, 8.0));
        itemsDataSource.add(new Item(counter++, "Abiko Curry", null, 12.0));
        itemsDataSource.add(new Item(counter++, "Margherita", null, 11.0));
        itemsDataSource.add(new Item(counter++, "Napoli", null, 10.75));
        itemsDataSource.add(new Item(counter++, "Gazpachuelo", null, 13.10));
        itemsDataSource.add(new Item(counter++, "Paella", null, 19.80));
        itemsDataSource.add(new Item(counter++, "Watermelon and feta", null, 12.20));
        itemsDataSource.add(new Item(counter++, "Sushi salad", null, 14.50));
        itemsDataSource.add(new Item(counter, "Sweat and Sour Pork", null, 15.0));
    }
}
