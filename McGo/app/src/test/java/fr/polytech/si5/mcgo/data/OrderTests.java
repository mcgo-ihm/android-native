package fr.polytech.si5.mcgo.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import fr.polytech.si5.mcgo.data.local.ItemsDataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderTests {

    private Order order;
    private List<Item> itemList;

    @Before
    public void setUp() {
        order = new Order();

        itemList = new ArrayList<>();
        itemList.add(new Item(ItemsDataSource.ITEMS_DATA_SOURCE.get(0)));
        itemList.add(new Item(ItemsDataSource.ITEMS_DATA_SOURCE.get(3)));
        itemList.add(new Item(ItemsDataSource.ITEMS_DATA_SOURCE.get(6)));
        itemList.add(new Item(ItemsDataSource.ITEMS_DATA_SOURCE.get(9)));

        itemList.get(0).setQuantity(3);
        itemList.get(1).setQuantity(5);
    }

    @After
    public void tearDown() {
        reset();
    }

    private void reset() {
        order.reset();
    }

    @Test
    public void setListOfItems() {
        reset();
        assertTrue(order.getListOfItems().isEmpty());
        order.setListOfItems(itemList);

        assertEquals(1, (int) order.getListOfItems().get(0).getQuantity());
        assertEquals(1, (int) order.getListOfItems().get(1).getQuantity());
        orderAssert(4, 4, 47.55f);
    }

    @Test
    public void addItem() {
        order.addItem(itemList.get(0));
        orderAssert(1, 1, 10.50f);
        ;
        assertEquals(1, (int) order.getListOfItems().get(0).getQuantity());

        for (Item i : itemList) {
            order.addItem(i);
        }

        orderAssert(5, 4, 58.05f);
        assertEquals(2, (int) order.getListOfItems().get(0).getQuantity());
    }

    @Test
    public void removeItem() {
        order.setListOfItems(itemList);
        order.removeItem(itemList.get(0));
        orderAssert(3, 3, 37.05f);

        order.removeItem(itemList.get(0));
        orderAssert(3, 3, 37.05f);

        order.addItem(itemList.get(3));
        order.removeItem(itemList.get(3));
        orderAssert(3, 3, 37.05f);
        assertTrue(order.getListOfItems().contains(itemList.get(3)));
    }

    private void orderAssert(int totalItemsNumber, int listOfItemsSize, float price) {
        assertEquals(totalItemsNumber, order.getTotalItemsNumber());
        assertEquals(listOfItemsSize, order.getListOfItems().size());
        assertEquals(price, order.getPrice(), 0.01);
    }

}
