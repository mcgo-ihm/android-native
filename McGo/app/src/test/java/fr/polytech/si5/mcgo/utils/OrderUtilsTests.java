package fr.polytech.si5.mcgo.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.polytech.si5.mcgo.data.Item;
import fr.polytech.si5.mcgo.data.local.ItemsDataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class OrderUtilsTests {

    private List<Item> quickOrderItemList;

    @Before
    public void setUp() {
        quickOrderItemList = new ArrayList<>();
        quickOrderItemList.add(new Item(ItemsDataSource.ITEMS_DATA_SOURCE.get(0)));
        quickOrderItemList.add(new Item(ItemsDataSource.ITEMS_DATA_SOURCE.get(3)));
        quickOrderItemList.add(new Item(ItemsDataSource.ITEMS_DATA_SOURCE.get(6)));
        quickOrderItemList.add(new Item(ItemsDataSource.ITEMS_DATA_SOURCE.get(9)));
    }

    @After
    public void tearDown() {
        reset();
    }

    private void reset() {
        ItemsDataSource.quickOrder.reset();
        ItemsDataSource.ORDERS_IN_PROGRESS.clear();
    }

    @Test
    public void performQuickOrderTest() {
        reset();
        ItemsDataSource.quickOrder.setListOfItems(quickOrderItemList);

        LocalDateTime fakeDate = LocalDateTime.of(2000, 1, 1, 0, 0);
        ItemsDataSource.quickOrder.setDate(fakeDate);
        OrderUtils.performQuickOrder();
        assertNotEquals(fakeDate, ItemsDataSource.quickOrder.getDate());
        assertEquals(1, ItemsDataSource.ORDERS_IN_PROGRESS.size());
    }
}
