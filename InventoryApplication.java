import javax.swing.SwingUtilities;

/**
 * Starts the Restaurant Inventory Management System.
 *
 * <p>This class creates the database object and opens the
 * graphical user interface for the program.</p>
 *
 * @author Melanie Pinheiro
 * @version 1.0
 */
public class InventoryApplication {

    /**
     * Starts the inventory management application.
     *
     * @param args command-line arguments that are not used
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            InventoryDatabase database =
                    new InventoryDatabase();

            InventoryGUI gui =
                    new InventoryGUI(database);

            gui.setVisible(true);
        });
    }
}