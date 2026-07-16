/*
 * InventoryManager.java
 *
 * This class stores the inventory list and handles the main inventory actions.
 * It can add, remove, update, display, load, save, and calculate inventory value.
 *
 * Author: Melanie Pinheiro
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryManager {

    private final ArrayList<InventoryItem> inventory;

    /*
     * This constructor creates an empty inventory list.
     */
    public InventoryManager() {
        inventory = new ArrayList<>();
    }

    /*
     * This method checks whether an item ID already exists.
     */
    public boolean itemExists(int itemId) {
        return findItemById(itemId) != null;
    }

    /*
     * This method searches for an item using its ID.
     */
    public InventoryItem findItemById(int itemId) {
        for (InventoryItem item : inventory) {
            if (item.getItemId() == itemId) {
                return item;
            }
        }

        return null;
    }

    /*
     * This method adds a new item if its ID is not already being used.
     */
    public boolean addItem(InventoryItem item) {
        if (item == null) {
            return false;
        }

        if (itemExists(item.getItemId())) {
            return false;
        }

        inventory.add(item);
        return true;
    }

    /*
     * This method removes an inventory item using its ID.
     */
    public boolean removeItem(int itemId) {
        InventoryItem item = findItemById(itemId);

        if (item == null) {
            return false;
        }

        return inventory.remove(item);
    }

    /*
     * This method updates one field of an inventory item.
     *
     * Field choices:
     * 1 = Item name
     * 2 = Category
     * 3 = Supplier
     * 4 = Quantity
     * 5 = Unit cost
     * 6 = Reorder level
     */
    public boolean updateItem(
            int itemId,
            int fieldChoice,
            String newValue
    ) {
        InventoryItem item = findItemById(itemId);

        if (item == null || newValue == null) {
            return false;
        }

        try {
            switch (fieldChoice) {

                case 1:
                    if (newValue.trim().isEmpty()) {
                        return false;
                    }

                    item.setItemName(newValue.trim());
                    return true;

                case 2:
                    if (newValue.trim().isEmpty()) {
                        return false;
                    }

                    item.setCategory(newValue.trim());
                    return true;

                case 3:
                    if (newValue.trim().isEmpty()) {
                        return false;
                    }

                    item.setSupplier(newValue.trim());
                    return true;

                case 4:
                    int quantity = Integer.parseInt(
                            newValue.trim()
                    );

                    if (quantity < 0) {
                        return false;
                    }

                    item.setQuantity(quantity);
                    return true;

                case 5:
                    double unitCost = Double.parseDouble(
                            newValue.trim()
                    );

                    if (unitCost < 0) {
                        return false;
                    }

                    item.setUnitCost(unitCost);
                    return true;

                case 6:
                    int reorderLevel = Integer.parseInt(
                            newValue.trim()
                    );

                    if (reorderLevel < 0) {
                        return false;
                    }

                    item.setReorderLevel(reorderLevel);
                    return true;

                default:
                    return false;
            }

        } catch (NumberFormatException exception) {
            return false;
        }
    }

    /*
     * This method prints all inventory items to the console.
     * The GUI uses getInventory() instead.
     */
    public void displayItems() {
        if (inventory.isEmpty()) {
            System.out.println(
                    "No inventory items are currently stored."
            );

            return;
        }

        System.out.println(
                "\n========== Current Inventory =========="
        );

        for (InventoryItem item : inventory) {
            System.out.println(item);
        }
    }

    /*
     * This method calculates the total value of all inventory items.
     */
    public double calculateTotalInventoryValue() {
        double total = 0;

        for (InventoryItem item : inventory) {
            total += item.calculateItemValue();
        }

        return total;
    }

    /*
     * This method loads inventory items from a text file.
     *
     * The inventory is only replaced after the file is read successfully.
     * This prevents the current inventory from being erased if the file is bad.
     */
    public boolean loadInventory(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }

        ArrayList<InventoryItem> loadedItems =
                new ArrayList<>();

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new FileReader(
                                        fileName.trim()
                                )
                        )
        ) {
            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length != 7) {
                    return false;
                }

                int itemId = Integer.parseInt(
                        parts[0].trim()
                );

                String itemName = parts[1].trim();
                String category = parts[2].trim();
                String supplier = parts[3].trim();

                int quantity = Integer.parseInt(
                        parts[4].trim()
                );

                double unitCost = Double.parseDouble(
                        parts[5].trim()
                );

                int reorderLevel = Integer.parseInt(
                        parts[6].trim()
                );

                if (
                        itemName.isEmpty()
                                || category.isEmpty()
                                || supplier.isEmpty()
                                || quantity < 0
                                || unitCost < 0
                                || reorderLevel < 0
                                || containsId(
                                loadedItems,
                                itemId
                        )
                ) {
                    return false;
                }

                InventoryItem item =
                        new InventoryItem(
                                itemId,
                                itemName,
                                category,
                                supplier,
                                quantity,
                                unitCost,
                                reorderLevel
                        );

                loadedItems.add(item);
            }

            inventory.clear();
            inventory.addAll(loadedItems);

            return true;

        } catch (
                IOException
                | NumberFormatException exception
        ) {
            return false;
        }
    }

    /*
     * This method saves all inventory items to a text file.
     */
    public boolean saveInventory(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }

        try (
                PrintWriter writer =
                        new PrintWriter(
                                new FileWriter(
                                        fileName.trim()
                                )
                        )
        ) {
            for (InventoryItem item : inventory) {
                writer.println(
                        item.toFileString()
                );
            }

            return true;

        } catch (IOException exception) {
            return false;
        }
    }

    /*
     * This method returns the number of inventory items.
     */
    public int getInventorySize() {
        return inventory.size();
    }

    /*
     * This method returns a read-only inventory list.
     *
     * The GUI can view the items, but it cannot directly change
     * the ArrayList without using the manager methods.
     */
    public List<InventoryItem> getInventory() {
        return Collections.unmodifiableList(
                inventory
        );
    }

    /*
     * This helper method checks whether a temporary list
     * already contains an item ID.
     */
    private boolean containsId(
            List<InventoryItem> itemList,
            int itemId
    ) {
        for (InventoryItem item : itemList) {
            if (item.getItemId() == itemId) {
                return true;
            }
        }

        return false;
    }
}