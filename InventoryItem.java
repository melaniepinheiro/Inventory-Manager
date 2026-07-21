/**
 * Stores information about one restaurant inventory item.
 *
 * <p>Each item contains an ID, name, category, supplier,
 * quantity, unit cost, and reorder level. The class can also
 * calculate the total value of the item.</p>
 *
 * @author Melanie Pinheiro
 * @version 1.0
 */
public class InventoryItem {

    private int itemId;
    private String itemName;
    private String category;
    private String supplier;
    private int quantity;
    private double unitCost;
    private int reorderLevel;

    /**
     * Creates a new inventory item.
     *
     * @param itemId unique ID for the item
     * @param itemName name of the item
     * @param category category of the item
     * @param supplier company that supplies the item
     * @param quantity amount currently in inventory
     * @param unitCost cost of one unit
     * @param reorderLevel quantity where the item should be reordered
     */
    public InventoryItem(
            int itemId,
            String itemName,
            String category,
            String supplier,
            int quantity,
            double unitCost,
            int reorderLevel
    ) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.supplier = supplier;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.reorderLevel = reorderLevel;
    }

    /**
     * Returns the item ID.
     *
     * @return the item ID
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Returns the item name.
     *
     * @return the item name
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Returns the item category.
     *
     * @return the item category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the supplier name.
     *
     * @return the supplier
     */
    public String getSupplier() {
        return supplier;
    }

    /**
     * Returns the current quantity.
     *
     * @return the item quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns the cost of one unit.
     *
     * @return the unit cost
     */
    public double getUnitCost() {
        return unitCost;
    }

    /**
     * Returns the reorder level.
     *
     * @return the reorder level
     */
    public int getReorderLevel() {
        return reorderLevel;
    }

    /**
     * Changes the item name.
     *
     * @param itemName new item name
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * Changes the item category.
     *
     * @param category new item category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Changes the supplier name.
     *
     * @param supplier new supplier name
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    /**
     * Changes the item quantity.
     *
     * @param quantity new quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Changes the cost of one unit.
     *
     * @param unitCost new unit cost
     */
    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    /**
     * Changes the reorder level.
     *
     * @param reorderLevel new reorder level
     */
    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    /**
     * Calculates the total value of this item.
     *
     * <p>The quantity is multiplied by the unit cost.</p>
     *
     * @return the total value of the item
     */
    public double calculateItemValue() {
        return quantity * unitCost;
    }

    /**
     * Converts the item into a comma-separated text line.
     *
     * <p>This method was used during the earlier text-file phase
     * of the project.</p>
     *
     * @return the item information in text-file format
     */
    public String toFileString() {
        return itemId + ","
                + itemName + ","
                + category + ","
                + supplier + ","
                + quantity + ","
                + unitCost + ","
                + reorderLevel;
    }

    /**
     * Returns a readable description of the inventory item.
     *
     * @return formatted inventory item information
     */
    @Override
    public String toString() {
        return String.format(
                "ID: %d | Name: %s | Category: %s | Supplier: %s | "
                        + "Quantity: %d | Unit Cost: $%.2f | "
                        + "Reorder Level: %d | Total Value: $%.2f",
                itemId,
                itemName,
                category,
                supplier,
                quantity,
                unitCost,
                reorderLevel,
                calculateItemValue()
        );
    }
}
