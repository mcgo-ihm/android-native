package fr.polytech.si5.mcgo.utils;

import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import fr.polytech.si5.mcgo.data.Item;
import fr.polytech.si5.mcgo.data.Order;
import fr.polytech.si5.mcgo.data.local.DataSource;
import fr.polytech.si5.mcgo.data.local.ItemsDataSource;

public class OrderUtils {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void confirmCart() {
        ItemsDataSource.cart.setDate(LocalDateTime.now());
        // For testing purpose I'll just set a known location
        //ItemsDataSource.cart.setLocation(getRandomRestaurantLocation());
        ItemsDataSource.cart.setLocation(DataSource.RESTAURANT_3);
        ItemsDataSource.ORDERS_IN_PROGRESS.add(new Order(ItemsDataSource.cart));
        ItemsDataSource.cart = new Order();
    }

    private static Location getRandomRestaurantLocation() {
        return DataSource.locations[new Random().nextInt(DataSource.locations.length)];
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void confirmCart(Order order) {
        order.setDate(LocalDateTime.now());
        order.startTimer(estimateOrderTime(order));
        // For testing purpose I'll just set a known location
        //order.setLocation(getRandomRestaurantLocation());
        order.setLocation(DataSource.RESTAURANT_3);
        ItemsDataSource.ORDERS_IN_PROGRESS.add(new Order(order));
        order = new Order();
    }

    private static int estimateOrderTime(Order order) {
        // Mock
        return 60000;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void setQuickOrder(List<Item> itemsForQuickOrder) {
        ItemsDataSource.quickOrder.setListOfItems(itemsForQuickOrder);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void performQuickOrder() {
        ItemsDataSource.quickOrder.setDate(LocalDateTime.now());
        ItemsDataSource.ORDERS_IN_PROGRESS.add(new Order(ItemsDataSource.quickOrder));
    }
}
