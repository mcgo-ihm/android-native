package fr.polytech.si5.mcgo.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.polytech.si5.mcgo.data.Item;
import fr.polytech.si5.mcgo.data.Order;
import fr.polytech.si5.mcgo.data.local.DataSource;
import fr.polytech.si5.mcgo.data.local.ItemsDataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class OrderUtilsTests {

    private List<Item> itemList;

    @Before
    public void setUp() {
        itemList = new ArrayList<>();
        itemList.add(new Item(ItemsDataSource.ITEMS_DATA_SOURCE.get(0)));
        itemList.add(new Item(ItemsDataSource.ITEMS_DATA_SOURCE.get(3)));
        itemList.add(new Item(ItemsDataSource.ITEMS_DATA_SOURCE.get(6)));
        itemList.add(new Item(ItemsDataSource.ITEMS_DATA_SOURCE.get(9)));
    }

    @After
    public void tearDown() {
        reset();
    }

    private void reset() {
        ItemsDataSource.quickOrder.reset();
        ItemsDataSource.cart.reset();
        ItemsDataSource.ORDERS_IN_PROGRESS.clear();
    }

    @Test
    public void performQuickOrderTest() {
        reset();
        assertNotEquals(DataSource.RESTAURANT_3, ItemsDataSource.quickOrder.getLocation());
        ItemsDataSource.quickOrder.setListOfItems(itemList);

        LocalDateTime fakeDate = LocalDateTime.of(2000, 1, 1, 0, 0);
        ItemsDataSource.quickOrder.setDate(fakeDate);
        OrderUtils.performQuickOrder();
        assertNotEquals(fakeDate, ItemsDataSource.quickOrder.getDate());
        assertEquals(DataSource.RESTAURANT_3, ItemsDataSource.quickOrder.getLocation());
        assertEquals(1, ItemsDataSource.ORDERS_IN_PROGRESS.size());
        assertEquals(ItemsDataSource.quickOrder, ItemsDataSource.ORDERS_IN_PROGRESS.get(0));
    }

    @Test
    public void confirmCart_MainCart() {
        reset();
        assertNotEquals(DataSource.RESTAURANT_3, ItemsDataSource.cart.getLocation());
        ItemsDataSource.cart.setListOfItems(itemList);

        LocalDateTime fakeDate = LocalDateTime.of(2000, 1, 1, 0, 0);
        ItemsDataSource.cart.setDate(fakeDate);
        OrderUtils.confirmCart();
        assertNotEquals(fakeDate, ItemsDataSource.cart.getDate());
        assertEquals(1, ItemsDataSource.ORDERS_IN_PROGRESS.size());
        assertNotEquals(ItemsDataSource.cart, ItemsDataSource.ORDERS_IN_PROGRESS.get(0));
    }

    @Test
    public void confirmCart_SpecificCart() {
        reset();
        Order order = new Order();
        order.setListOfItems(itemList);
        assertNotEquals(DataSource.RESTAURANT_3, order.getLocation());

        LocalDateTime fakeDate = LocalDateTime.of(2000, 1, 1, 0, 0);
        order.setDate(fakeDate);
        OrderUtils.confirmCart(order);
        assertNotEquals(fakeDate, order.getDate());
        assertEquals(DataSource.RESTAURANT_3, order.getLocation());
        assertEquals(1, ItemsDataSource.ORDERS_IN_PROGRESS.size());
        assertEquals(order, ItemsDataSource.ORDERS_IN_PROGRESS.get(0));
    }
}
