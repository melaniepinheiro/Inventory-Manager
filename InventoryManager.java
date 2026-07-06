/*
 * InventoryManager.java
 * This class stores the inventory list and handles the main inventory actions.
 * It can add, remove, update, display, load, save, and calculate inventory value.
 * Author: Melanie Pinheiro
 */

import java.io.*;
import java.util.ArrayList;

public class InventoryManager {
    private ArrayList<InventoryItem> inventory;

    /*
     * This constructor creates an empty inventory list.
     */
    public InventoryManager() {
        inventory = new ArrayList<>();
    }

    /*
     * This method checks if an item ID already exists.
     */
    public boolean itemExists(int itemId) {
        return findItemById(itemId) != null;
    }

    /*
     * This method searches for an item by ID.
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
     * This method adds a new item if the ID is not already used.
     */
    public boolean addItem(InventoryItem item) {
        if (itemExists(item.getItemId())) {
            return false;
        }
        inventory.add(item);
        return true;
    }

    /*
     * This method removes an item by ID.
     */
    public boolean removeItem(int itemId) {
        InventoryItem item = findItemById(itemId);
        if (item == null) {
            return false;
        }
        inventory.remove(item);
        return true;
    }

    /*
     * This method updates one field of an inventory item.
     */
    public boolean updateItem(int itemId, int fieldChoice, String newValue) {
        InventoryItem item = findItemById(itemId);

        if (item == null) {
            return false;
        }

        try {
            switch (fieldChoice) {
                case 1:
                    if (newValue.trim().isEmpty()) return false;
                    item.setItemName(newValue);
                    return true;
                case 2:
                    if (newValue.trim().isEmpty()) return false;
                    item.setCategory(newValue);
                    return true;
                case 3:
                    if (newValue.trim().isEmpty()) return false;
                    item.setSupplier(newValue);
                    return true;
                case 4:
                    int quantity = Integer.parseInt(newValue);
                    if (quantity < 0) return false;
                    item.setQuantity(quantity);
                    return true;
                case 5:
                    double cost = Double.parseDouble(newValue);
                    if (cost < 0) return false;
                    item.setUnitCost(cost);
                    return true;
                case 6:
                    int reorderLevel = Integer.parseInt(newValue);
                    if (reorderLevel < 0) return false;
                    item.setReorderLevel(reorderLevel);
                    return true;
                default:
                    return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*
     * This method prints all inventory items.
     */
    public void displayItems() {
        if (inventory.isEmpty()) {
            System.out.println("No inventory items are currently stored.");
            return;
        }

        System.out.println("\n========== Current Inventory ==========");
        for (InventoryItem item : inventory) {
            System.out.println(item);
        }
    }

    /*
     * This method calculates the total value of all inventory.
     */
    public double calculateTotalInventoryValue() {
        double total = 0;

        for (InventoryItem item : inventory) {
            total += item.calculateItemValue();
        }

        return total;
    }

    /*
     * This method loads inventory from a text file.
     */
    public boolean loadInventory(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int loadedCount = 0;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 7) {
                    int itemId = Integer.parseInt(parts[0].trim());
                    String itemName = parts[1].trim();
                    String category = parts[2].trim();
                    String supplier = parts[3].trim();
                    int quantity = Integer.parseInt(parts[4].trim());
                    double unitCost = Double.parseDouble(parts[5].trim());
                    int reorderLevel = Integer.parseInt(parts[6].trim());

                    if (!itemExists(itemId) && quantity >= 0 && unitCost >= 0 && reorderLevel >= 0
                            && !itemName.isEmpty() && !category.isEmpty() && !supplier.isEmpty()) {
                        InventoryItem item = new InventoryItem(itemId, itemName, category, supplier,
                                quantity, unitCost, reorderLevel);
                        inventory.add(item);
                        loadedCount++;
                    }
                }
            }

            System.out.println(loadedCount + " inventory items loaded.");
            return true;

        } catch (IOException | NumberFormatException e) {
            return false;
        }
    }

    /*
     * This method saves inventory to a text file.
     */
    public boolean saveInventory(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (InventoryItem item : inventory) {
                writer.println(item.toFileString());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
