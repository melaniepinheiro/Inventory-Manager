/*
 * InventoryApplication.java
 *
 * This class starts the Restaurant Inventory Manager program.
 * It creates the database object and opens the GUI.
 *
 * Author: Melanie Pinheiro
 */

import javax.swing.SwingUtilities;

public class InventoryApplication {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // Create the database helper object.
            InventoryDatabase database =
                    new InventoryDatabase();

            // Send the database object to the GUI.
            InventoryGUI gui =
                    new InventoryGUI(database);

            // Show the GUI window.
            gui.setVisible(true);
        });
    }
}