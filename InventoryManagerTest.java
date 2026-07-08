import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryManagerTest {

    /*
     * Test adding an inventory item.
     */
    @Test
    public void testAddItem() {

        InventoryManager manager = new InventoryManager();

        InventoryItem item = new InventoryItem(
                100,
                "Chicken",
                "Protein",
                "Sysco",
                20,
                5.99,
                10
        );

        assertTrue(manager.addItem(item));
        assertEquals(1, manager.getInventorySize());
        assertTrue(manager.itemExists(100));
    }

    /*
     * Test removing an inventory item.
     */
    @Test
    public void testRemoveItem() {

        InventoryManager manager = new InventoryManager();

        InventoryItem item = new InventoryItem(
                101,
                "Rice",
                "Grains",
                "Sysco",
                30,
                1.25,
                10
        );

        manager.addItem(item);

        assertTrue(manager.removeItem(101));
        assertFalse(manager.itemExists(101));
        assertEquals(0, manager.getInventorySize());
    }

    /*
     * Test updating an inventory item.
     */
    @Test
    public void testUpdateItem() {

        InventoryManager manager = new InventoryManager();

        InventoryItem item = new InventoryItem(
                102,
                "Beans",
                "Pantry",
                "Sysco",
                15,
                2.00,
                5
        );

        manager.addItem(item);

        assertTrue(manager.updateItem(102, 4, "25"));

        InventoryItem updatedItem = manager.findItemById(102);

        assertNotNull(updatedItem);
        assertEquals(25, updatedItem.getQuantity());
    }

    /*
     * Test loading inventory from a file.
     */
    @Test
    public void testLoadInventory() {

        InventoryManager manager = new InventoryManager();

        assertTrue(manager.loadInventory("inventory.txt"));

        assertEquals(20, manager.getInventorySize());
    }

    /*
     * Test calculating total inventory value.
     */
    @Test
    public void testCalculateInventoryValue() {

        InventoryManager manager = new InventoryManager();

        InventoryItem item1 = new InventoryItem(
                201,
                "Chicken",
                "Protein",
                "Sysco",
                10,
                5.00,
                5
        );

        InventoryItem item2 = new InventoryItem(
                202,
                "Rice",
                "Grains",
                "Sysco",
                20,
                2.00,
                10
        );

        manager.addItem(item1);
        manager.addItem(item2);

        double expected = (10 * 5.00) + (20 * 2.00);

        assertEquals(expected,
                manager.calculateTotalInventoryValue(),
                0.001);
    }
}