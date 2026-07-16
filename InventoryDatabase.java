/*
 * InventoryDatabase.java
 *
 * This class connects the Java program to the SQLite database.
 * It handles reading, adding, updating, removing, and calculating
 * inventory data.
 *
 * Author: Melanie Pinheiro
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

    /*
     * This method connects to the SQLite database selected by the user.
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

    /*
     * This method checks whether the database is currently connected.
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

    /*
     * This method retrieves all inventory records from the database.
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

    /*
     * This method adds one new inventory item to the database.
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

    /*
     * This method updates an existing inventory item.
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

    /*
     * This method removes an inventory item using its ID.
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

    /*
     * This method checks whether an item ID already exists.
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

    /*
     * This custom feature calculates the total inventory value.
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

    /*
     * This method returns the total number of database records.
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

    /*
     * This method safely closes the database connection.
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