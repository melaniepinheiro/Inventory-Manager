/*
 * InventoryApplication.java
 * Starts the Phase 3 Java Swing interface.
 * Author: Melanie Pinheiro
 */

import javax.swing.*;

public class InventoryApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // The program continues with Java's default appearance.
            }

            InventoryManager manager = new InventoryManager();
            InventoryGUI gui = new InventoryGUI(manager);
            gui.setVisible(true);
        });
    }
}
