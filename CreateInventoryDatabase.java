/**
 * Creates and fills the sample SQLite inventory database.
 *
 * <p>This class creates the inventory table and inserts twenty sample
 * restaurant inventory records. It is mainly used to prepare the sample
 * database submitted with the project.</p>
 *
 * @author Melanie Pinheiro
 * @version 1.0
 */

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateInventoryDatabase {

    /**
     * Creates the inventory database and inserts the sample records.
     *
     * @param args command-line arguments that are not used
     */
    public static void main(String[] args) {

        // The database will be created in the main project folder.
        String databasePath = "inventory.db";

        // This creates the SQLite connection address.
        String databaseUrl = "jdbc:sqlite:" + databasePath;

        // This command creates the inventory table.
        String createTableSql =
                "CREATE TABLE IF NOT EXISTS inventory (" +
                        "item_id INTEGER PRIMARY KEY, " +
                        "item_name TEXT NOT NULL, " +
                        "category TEXT NOT NULL, " +
                        "supplier TEXT NOT NULL, " +
                        "quantity INTEGER NOT NULL CHECK(quantity >= 0), " +
                        "unit_cost REAL NOT NULL CHECK(unit_cost >= 0), " +
                        "reorder_level INTEGER NOT NULL CHECK(reorder_level >= 0)" +
                        ")";

        // This removes old records before adding the sample records.
        String deleteOldDataSql = "DELETE FROM inventory";

        // This command inserts one inventory record.
        String insertSql =
                "INSERT INTO inventory " +
                        "(item_id, item_name, category, supplier, " +
                        "quantity, unit_cost, reorder_level) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // These are the 20 sample inventory records.
        Object[][] inventoryData = {
                {1001, "Chicken Breast", "Protein", "Sysco", 35, 5.50, 10},
                {1002, "Ground Beef", "Protein", "Sysco", 15, 4.25, 8},
                {1003, "Lettuce", "Produce", "FreshPoint", 20, 2.00, 10},
                {1004, "Tomatoes", "Produce", "FreshPoint", 18, 2.30, 8},
                {1005, "Shredded Cheese", "Dairy", "Dairy Best", 20, 2.75, 8},
                {1006, "Olive Oil", "Pantry", "Good Supply Co.", 12, 6.80, 5},
                {1007, "Flour Tortillas", "Grains", "Tortilla King", 50, 0.45, 20},
                {1008, "Black Beans", "Pantry", "Sysco", 40, 1.20, 15},
                {1009, "Pinto Beans", "Pantry", "Sysco", 35, 1.15, 15},
                {1010, "White Rice", "Grains", "Sysco", 45, 1.10, 20},
                {1011, "Brown Rice", "Grains", "Sysco", 25, 1.25, 10},
                {1012, "Sour Cream", "Dairy", "Dairy Best", 16, 2.10, 7},
                {1013, "Guacamole", "Produce", "FreshPoint", 14, 4.50, 6},
                {1014, "Queso", "Dairy", "Dairy Best", 18, 3.80, 8},
                {1015, "Salsa", "Condiments", "Sysco", 22, 2.50, 10},
                {1016, "Corn", "Produce", "FreshPoint", 17, 1.75, 7},
                {1017, "Jalapenos", "Produce", "FreshPoint", 12, 1.40, 5},
                {1018, "Chips", "Grains", "Tortilla King", 30, 1.00, 12},
                {1019, "Steak", "Protein", "Sysco", 20, 7.25, 8},
                {1020, "Tofu", "Protein", "Fresh Foods", 10, 3.50, 5}
        };

        try (
                Connection connection =
                        DriverManager.getConnection(databaseUrl);

                Statement statement =
                        connection.createStatement()
        ) {
            // Create the table before preparing the INSERT command.
            statement.executeUpdate(createTableSql);

            // Remove any old sample records.
            statement.executeUpdate(deleteOldDataSql);

            /*
             * The INSERT statement is prepared after the table exists.
             * This fixes the "no such table: inventory" error.
             */
            try (
                    PreparedStatement insertStatement =
                            connection.prepareStatement(insertSql)
            ) {
                for (Object[] item : inventoryData) {

                    insertStatement.setInt(1, (Integer) item[0]);
                    insertStatement.setString(2, (String) item[1]);
                    insertStatement.setString(3, (String) item[2]);
                    insertStatement.setString(4, (String) item[3]);
                    insertStatement.setInt(5, (Integer) item[4]);
                    insertStatement.setDouble(6, (Double) item[5]);
                    insertStatement.setInt(7, (Integer) item[6]);

                    insertStatement.executeUpdate();
                }
            }

            System.out.println(
                    "Database created successfully with 20 inventory records."
            );

            System.out.println(
                    "Database location: "
                            + new File(databasePath).getAbsolutePath()
            );

        } catch (SQLException exception) {
            System.out.println(
                    "Database creation failed: "
                            + exception.getMessage()
            );

            exception.printStackTrace();
        }
    }
}
