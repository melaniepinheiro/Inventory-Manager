/**
 * Handles the connection between the application and the SQLite database.
 *
 * <p>This class performs all database operations, including connecting,
 * reading records, adding items, updating items, removing items, counting
 * items, and calculating the total inventory value.</p>
 *
 * @author Melanie Pinheiro
 * @version 1.0
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDatabase {

    private Connection connection;

    /**
     * Connects to an SQLite database selected by the user.
     *
     * @param databasePath path to the SQLite database file
     * @return true when the database connects and contains the inventory table;
     *         false otherwise
     */
    public boolean connect(String databasePath) {

        if (databasePath == null || databasePath.trim().isEmpty()) {
            return false;
        }

        closeConnection();

        String databaseUrl = "jdbc:sqlite:" + databasePath.trim();

        try {
            connection = DriverManager.getConnection(databaseUrl);

            if (!databaseHasInventoryTable()) {
                closeConnection();
                return false;
            }

            return true;

        } catch (SQLException exception) {
            connection = null;
            return false;
        }
    }

    /**
     * Checks whether the database connection is currently open.
     *
     * @return true when connected; false otherwise
     */
    public boolean isConnected() {

        try {
            return connection != null && !connection.isClosed();

        } catch (SQLException exception) {
            return false;
        }
    }

    /*
     * This method checks that the selected database contains
     * the correct inventory table.
     */
    private boolean databaseHasInventoryTable() {

        String sql =
                "SELECT name FROM sqlite_master " +
                        "WHERE type = 'table' AND name = 'inventory'";

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet results =
                        statement.executeQuery()
        ) {
            return results.next();

        } catch (SQLException exception) {
            return false;
        }
    }

    /**
     * Retrieves all inventory records from the database.
     *
     * @return a list containing every inventory item
     */
    public List<InventoryItem> getAllItems() {

        List<InventoryItem> items = new ArrayList<>();

        if (!isConnected()) {
            return items;
        }

        String sql =
                "SELECT item_id, item_name, category, supplier, " +
                        "quantity, unit_cost, reorder_level " +
                        "FROM inventory ORDER BY item_id";

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet results =
                        statement.executeQuery()
        ) {
            while (results.next()) {

                InventoryItem item = new InventoryItem(
                        results.getInt("item_id"),
                        results.getString("item_name"),
                        results.getString("category"),
                        results.getString("supplier"),
                        results.getInt("quantity"),
                        results.getDouble("unit_cost"),
                        results.getInt("reorder_level")
                );

                items.add(item);
            }

        } catch (SQLException exception) {
            System.out.println(
                    "Error reading inventory: "
                            + exception.getMessage()
            );
        }

        return items;
    }

    /**
     * Adds a new inventory item to the database.
     *
     * @param item inventory item to add
     * @return true when the record is added; false otherwise
     */
    public boolean addItem(InventoryItem item) {

        if (!isConnected() || item == null) {
            return false;
        }

        String sql =
                "INSERT INTO inventory " +
                        "(item_id, item_name, category, supplier, " +
                        "quantity, unit_cost, reorder_level) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setInt(1, item.getItemId());
            statement.setString(2, item.getItemName());
            statement.setString(3, item.getCategory());
            statement.setString(4, item.getSupplier());
            statement.setInt(5, item.getQuantity());
            statement.setDouble(6, item.getUnitCost());
            statement.setInt(7, item.getReorderLevel());

            return statement.executeUpdate() == 1;

        } catch (SQLException exception) {
            System.out.println(
                    "Error adding item: "
                            + exception.getMessage()
            );

            return false;
        }
    }

    /**
     * Updates an existing inventory item in the database.
     *
     * @param item item containing the updated information
     * @return true when the record is updated; false otherwise
     */
    public boolean updateItem(InventoryItem item) {

        if (!isConnected() || item == null) {
            return false;
        }

        String sql =
                "UPDATE inventory SET " +
                        "item_name = ?, " +
                        "category = ?, " +
                        "supplier = ?, " +
                        "quantity = ?, " +
                        "unit_cost = ?, " +
                        "reorder_level = ? " +
                        "WHERE item_id = ?";

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getCategory());
            statement.setString(3, item.getSupplier());
            statement.setInt(4, item.getQuantity());
            statement.setDouble(5, item.getUnitCost());
            statement.setInt(6, item.getReorderLevel());
            statement.setInt(7, item.getItemId());

            return statement.executeUpdate() == 1;

        } catch (SQLException exception) {
            System.out.println(
                    "Error updating item: "
                            + exception.getMessage()
            );

            return false;
        }
    }

    /**
     * Removes an inventory item from the database.
     *
     * @param itemId ID of the item to remove
     * @return true when the record is removed; false otherwise
     */
    public boolean removeItem(int itemId) {

        if (!isConnected()) {
            return false;
        }

        String sql =
                "DELETE FROM inventory WHERE item_id = ?";

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setInt(1, itemId);

            return statement.executeUpdate() == 1;

        } catch (SQLException exception) {
            System.out.println(
                    "Error removing item: "
                            + exception.getMessage()
            );

            return false;
        }
    }

    /**
     * Checks whether an item ID already exists in the database.
     *
     * @param itemId item ID to search for
     * @return true when the ID exists; false otherwise
     */
    public boolean itemExists(int itemId) {

        if (!isConnected()) {
            return false;
        }

        String sql =
                "SELECT item_id FROM inventory WHERE item_id = ?";

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setInt(1, itemId);

            try (
                    ResultSet results =
                            statement.executeQuery()
            ) {
                return results.next();
            }

        } catch (SQLException exception) {
            return false;
        }
    }

    /**
     * Calculates the total value of all inventory records.
     *
     * <p>The calculation multiplies each item's quantity by its unit cost
     * and adds all item values together.</p>
     *
     * @return the total inventory value
     */
    public double calculateTotalInventoryValue() {

        if (!isConnected()) {
            return 0;
        }

        String sql =
                "SELECT COALESCE(SUM(quantity * unit_cost), 0) " +
                        "AS total_value FROM inventory";

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet results =
                        statement.executeQuery()
        ) {
            if (results.next()) {
                return results.getDouble("total_value");
            }

        } catch (SQLException exception) {
            System.out.println(
                    "Error calculating inventory value: "
                            + exception.getMessage()
            );
        }

        return 0;
    }

    /**
     * Returns the number of inventory records in the database.
     *
     * @return total number of records
     */
    public int getInventorySize() {

        if (!isConnected()) {
            return 0;
        }

        String sql =
                "SELECT COUNT(*) AS item_count FROM inventory";

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet results =
                        statement.executeQuery()
        ) {
            if (results.next()) {
                return results.getInt("item_count");
            }

        } catch (SQLException exception) {
            System.out.println(
                    "Error counting inventory: "
                            + exception.getMessage()
            );
        }

        return 0;
    }

    /**
     * Safely closes the current database connection.
     */
    public void closeConnection() {

        if (connection != null) {

            try {
                connection.close();

            } catch (SQLException exception) {
                System.out.println(
                        "Database connection could not close correctly."
                );
            }

            connection = null;
        }
    }
}