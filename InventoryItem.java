/*
 * InventoryItem.java
 * This class stores information about one restaurant inventory item.
 * It also calculates the value of one item by using quantity and unit cost.
 * Author: Melanie Pinheiro
 */

public class InventoryItem {
    private int itemId;
    private String itemName;
    private String category;
    private String supplier;
    private int quantity;
    private double unitCost;
    private int reorderLevel;

    /*
     * This constructor creates a new inventory item with all needed information.
     */
    public InventoryItem(int itemId, String itemName, String category, String supplier,
                         int quantity, double unitCost, int reorderLevel) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.supplier = supplier;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.reorderLevel = reorderLevel;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getCategory() {
        return category;
    }

    public String getSupplier() {
        return supplier;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    /*
     * This method calculates the total value of this one item.
     */
    public double calculateItemValue() {
        return quantity * unitCost;
    }

    /*
     * This method turns the item into a line that can be saved in a text file.
     */
    public String toFileString() {
        return itemId + "," + itemName + "," + category + "," + supplier + "," +
                quantity + "," + unitCost + "," + reorderLevel;
    }

    /*
     * This method controls how an item prints on the screen.
     */
    @Override
    public String toString() {
        return String.format(
                "ID: %d | Name: %s | Category: %s | Supplier: %s | Quantity: %d | Unit Cost: $%.2f | Reorder Level: %d | Total Value: $%.2f",
                itemId, itemName, category, supplier, quantity, unitCost, reorderLevel, calculateItemValue()
        );
    }
}

