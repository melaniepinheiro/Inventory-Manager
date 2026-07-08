/*
 * InventoryApplication.java
 * This class runs the Restaurant Inventory Management System.
 * The program uses a menu so the user can choose inventory actions.
 * Author: Melanie Pinheiro
 */

import java.util.Scanner;

public class InventoryApplication {
    private InventoryManager manager;
    private Scanner scanner;

    /*
     * This constructor creates the manager and scanner for the program.
     */
    public InventoryApplication() {
        manager = new InventoryManager();
        scanner = new Scanner(System.in);
    }

    /*
     * This method starts the program.
     */
    public void runApplication() {
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1:
                    loadInventoryFile();
                    break;
                case 2:
                    manager.displayItems();
                    break;
                case 3:
                    addInventoryItem();
                    break;
                case 4:
                    removeInventoryItem();
                    break;
                case 5:
                    updateInventoryItem();
                    break;
                case 6:
                    calculateInventoryValue();
                    break;
                case 7:
                    saveInventoryFile();
                    break;
                case 8:
                    System.out.println("Exiting program. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid menu option. Please choose 1 through 8.");
            }
        }
    }

    /*
     * This method displays the menu options.
     */
    private void displayMenu() {
        System.out.println("\n===== Restaurant Inventory Manager =====");
        System.out.println("1. Load Inventory File");
        System.out.println("2. Display Inventory");
        System.out.println("3. Add Inventory Item");
        System.out.println("4. Remove Inventory Item");
        System.out.println("5. Update Inventory Item");
        System.out.println("6. Calculate Total Inventory Value");
        System.out.println("7. Save Inventory File");
        System.out.println("8. Exit");
    }

    /*
     * This method loads inventory from a text file.
     */
    private void loadInventoryFile() {
        System.out.print("Enter file name to load, such as inventory.txt: ");
        String fileName = scanner.nextLine();

        if (manager.loadInventory(fileName)) {
            System.out.println("Inventory file loaded successfully.");
        } else {
            System.out.println("File could not be loaded. Please check the file name and format.");
        }
    }

    /*
     * This method saves inventory to a text file.
     */
    private void saveInventoryFile() {
        System.out.print("Enter file name to save, such as inventory.txt: ");
        String fileName = scanner.nextLine();

        if (manager.saveInventory(fileName)) {
            System.out.println("Inventory saved successfully.");
        } else {
            System.out.println("Inventory could not be saved.");
        }
    }

    /*
     * This method lets the user manually add an inventory item.
     */
    private void addInventoryItem() {
        System.out.println("\nAdd New Inventory Item");

        int itemId;

        while (true) {
            itemId = getPositiveIntInput("Enter item ID: ");

            if (!manager.itemExists(itemId)) {
                break;
            }

            System.out.println("That item ID already exists. Please enter a different ID.");
        }

        String itemName = getStringInput("Enter item name: ");
        String category = getStringInput("Enter category: ");
        String supplier = getStringInput("Enter supplier: ");
        int quantity = getPositiveIntInput("Enter quantity: ");
        double unitCost = getPositiveDoubleInput("Enter unit cost: ");
        int reorderLevel = getPositiveIntInput("Enter reorder level: ");

        InventoryItem item = new InventoryItem(
                itemId,
                itemName,
                category,
                supplier,
                quantity,
                unitCost,
                reorderLevel
        );

        if (manager.addItem(item)) {
            System.out.println("Inventory item added successfully.");
            System.out.println(item);
        } else {
            System.out.println("Inventory item could not be added.");
        }
    }

    /*
     * This method removes an item by ID.
     */
    private void removeInventoryItem() {

        while (true) {
            int itemId = getPositiveIntInput("Enter item ID to remove: ");

            if (manager.removeItem(itemId)) {
                System.out.println("Inventory item removed successfully.");
                return;
            }

            System.out.println("Item not found. Please enter another item ID.");
        }
    }
    /*
     * This method allows the user to update one field of an item.
     */
    private void updateInventoryItem() {

        int itemId;

        while (true) {
            itemId = getPositiveIntInput("Enter item ID to update: ");

            if (manager.itemExists(itemId)) {
                break;
            }

            System.out.println("Item not found. Please enter another item ID.");
        }

        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Item Name");
        System.out.println("2. Category");
        System.out.println("3. Supplier");
        System.out.println("4. Quantity");
        System.out.println("5. Unit Cost");
        System.out.println("6. Reorder Level");

        int fieldChoice = getIntInput("Choose a field: ");

        String newValue;

        if (fieldChoice == 4 || fieldChoice == 6) {
            int numberValue = getPositiveIntInput("Enter the new value: ");
            newValue = String.valueOf(numberValue);
        } else if (fieldChoice == 5) {
            double doubleValue = getPositiveDoubleInput("Enter the new value: ");
            newValue = String.valueOf(doubleValue);
        } else if (fieldChoice >= 1 && fieldChoice <= 3) {
            newValue = getStringInput("Enter the new value: ");
        } else {
            System.out.println("Invalid field choice.");
            return;
        }

        if (manager.updateItem(itemId, fieldChoice, newValue)) {
            System.out.println("Inventory item updated successfully.");
        } else {
            System.out.println("Update failed. Please check your input.");
        }
    }

    /*
     * This method prints the total inventory value.
     */
    private void calculateInventoryValue() {
        double total = manager.calculateTotalInventoryValue();
        System.out.printf("Total inventory value: $%.2f%n", total);
    }

    /*
     * This method safely gets an integer from the user.
     */
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    /*
     * This method safely gets a zero or positive integer from the user.
     */
    private int getPositiveIntInput(String prompt) {
        while (true) {
            int value = getIntInput(prompt);

            if (value >= 0) {
                return value;
            }

            System.out.println("Invalid input. Number cannot be negative.");
        }
    }

    /*
     * This method safely gets a zero or positive decimal number from the user.
     */
    private double getPositiveDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());

                if (value >= 0) {
                    return value;
                }

                System.out.println("Invalid input. Number cannot be negative.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid decimal number.");
            }
        }
    }

    /*
     * This method safely gets text from the user and does not allow blank answers.
     */
    private String getStringInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("Input cannot be blank.");
        }
    }

    /*
     * Main method that starts the program.
     */
    public static void main(String[] args) {
        InventoryApplication app = new InventoryApplication();
        app.runApplication();
    }
}
