/*
 * InventoryGUI.java
 *
 * This class creates the graphical user interface for the
 * Restaurant Inventory Management System.
 *
 * The user can load, display, add, update, remove, save,
 * and calculate the total value of inventory items.
 *
 * Author: Melanie Pinheiro
 */

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.List;

public class InventoryGUI extends JFrame {

    /*
     * Barrio-inspired colors.
     */
    private static final Color AQUA = new Color(104, 194, 201);
    private static final Color DARK_AQUA = new Color(45, 145, 153);
    private static final Color CHARCOAL = new Color(30, 31, 34);
    private static final Color SOFT_CHARCOAL = new Color(48, 50, 55);
    private static final Color CREAM = new Color(248, 247, 242);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(38, 40, 44);
    private static final Color MUTED_TEXT = new Color(105, 110, 118);
    private static final Color BORDER_COLOR = new Color(218, 221, 225);
    private static final Color SUCCESS_GREEN = new Color(55, 145, 98);
    private static final Color REMOVE_RED = new Color(185, 63, 70);

    private final InventoryManager manager;

    private final DefaultTableModel tableModel;
    private final JTable inventoryTable;

    private final JTextField fileField;
    private final JTextField idField;
    private final JTextField nameField;
    private final JTextField categoryField;
    private final JTextField supplierField;
    private final JTextField quantityField;
    private final JTextField unitCostField;
    private final JTextField reorderLevelField;

    private final JLabel totalItemsLabel;
    private final JLabel totalValueLabel;
    private final JLabel statusLabel;

    /*
     * This constructor prepares the GUI and its main components.
     */
    public InventoryGUI(InventoryManager manager) {
        this.manager = manager;

        setTitle("Barrio Restaurant Inventory Manager");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 760));
        setSize(1320, 830);
        setLocationRelativeTo(null);

        tableModel = createTableModel();
        inventoryTable = createInventoryTable();

        fileField = createTextField();
        fileField.setText("inventory.txt");

        idField = createTextField();
        nameField = createTextField();
        categoryField = createTextField();
        supplierField = createTextField();
        quantityField = createTextField();
        unitCostField = createTextField();
        reorderLevelField = createTextField();

        totalItemsLabel = createMetricValueLabel("0");
        totalValueLabel = createMetricValueLabel("$0.00");

        statusLabel = new JLabel("Ready");
        statusLabel.setForeground(MUTED_TEXT);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        buildInterface();
        connectEvents();
        refreshTable();
    }

    /*
     * This method builds the full window.
     */
    private void buildInterface() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(CREAM);

        rootPanel.add(createHeader(), BorderLayout.NORTH);
        rootPanel.add(createMainContent(), BorderLayout.CENTER);
        rootPanel.add(createStatusBar(), BorderLayout.SOUTH);

        setContentPane(rootPanel);
    }

    /*
     * This method creates the black header at the top.
     */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(CHARCOAL);
        header.setBorder(new EmptyBorder(14, 22, 14, 22));

        JLabel logoLabel = createLogoLabel();

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("RESTAURANT INVENTORY MANAGER");
        titleLabel.setForeground(WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 25));

        JLabel subtitleLabel = new JLabel(
                "Track stock, control costs, and keep Barrio running smoothly."
        );
        subtitleLabel.setForeground(AQUA);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitleLabel);

        JLabel phaseLabel = new JLabel("PHASE 3");
        phaseLabel.setOpaque(true);
        phaseLabel.setBackground(AQUA);
        phaseLabel.setForeground(CHARCOAL);
        phaseLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        phaseLabel.setBorder(new EmptyBorder(8, 13, 8, 13));

        header.add(logoLabel, BorderLayout.WEST);
        header.add(titlePanel, BorderLayout.CENTER);
        header.add(phaseLabel, BorderLayout.EAST);

        return header;
    }

    /*
     * This method loads the Barrio logo.
     * If the image cannot be found, the word BARRIO is shown instead.
     */
    private JLabel createLogoLabel() {
        URL logoUrl = getClass().getResource("/assets/BarrioLogo.jpg");

        if (logoUrl != null) {
            ImageIcon originalIcon = new ImageIcon(logoUrl);

            Image scaledImage = originalIcon.getImage().getScaledInstance(
                    145,
                    62,
                    Image.SCALE_SMOOTH
            );

            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setPreferredSize(new Dimension(150, 62));

            return logoLabel;
        }

        JLabel fallbackLabel = new JLabel("BARRIO");
        fallbackLabel.setForeground(AQUA);
        fallbackLabel.setFont(new Font("Serif", Font.BOLD, 30));

        return fallbackLabel;
    }

    /*
     * This method creates the center section of the window.
     */
    private JPanel createMainContent() {
        JPanel contentPanel = new JPanel(new BorderLayout(16, 16));
        contentPanel.setBackground(CREAM);
        contentPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        contentPanel.add(createSidebar(), BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new BorderLayout(14, 14));
        centerPanel.setOpaque(false);

        centerPanel.add(createFilePanel(), BorderLayout.NORTH);
        centerPanel.add(createInventoryPanel(), BorderLayout.CENTER);
        centerPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        contentPanel.add(centerPanel, BorderLayout.CENTER);

        return contentPanel;
    }

    /*
     * This method creates the dark menu on the left side.
     */
    /*
     * This method creates the sidebar menu.
     * Only useful actions are included so the layout does not feel repetitive.
     */
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();

        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(CHARCOAL);
        sidebar.setBorder(new EmptyBorder(18, 16, 18, 16));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel toolsLabel = new JLabel("INVENTORY TOOLS");
        toolsLabel.setForeground(AQUA);
        toolsLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );
        toolsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveButton =
                createSidebarButton("Save Inventory");

        saveButton.addActionListener(
                event -> saveInventory()
        );

        JButton clearButton =
                createSidebarButton("Clear Form");

        clearButton.addActionListener(
                event -> clearFields()
        );

        JButton exitButton =
                createSidebarButton("Exit");

        exitButton.addActionListener(
                event -> exitApplication()
        );

        sidebar.add(toolsLabel);
        sidebar.add(Box.createVerticalStrut(16));

        sidebar.add(saveButton);
        sidebar.add(Box.createVerticalStrut(9));

        sidebar.add(clearButton);

        sidebar.add(Box.createVerticalGlue());

        sidebar.add(exitButton);

        return sidebar;
    }

    /*
     * This method creates the inventory file section.
     */
    private JPanel createFilePanel() {
        JPanel filePanel = createCardPanel(new BorderLayout(12, 0));

        JLabel fileLabel = new JLabel("Inventory File");
        fileLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        fileLabel.setForeground(TEXT_COLOR);

        JButton browseButton = createOutlineButton("Browse");
        browseButton.addActionListener(event -> browseForFile());

        JButton loadButton = createPrimaryButton("Load Inventory");
        loadButton.addActionListener(event -> loadInventory());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(browseButton);
        buttonPanel.add(loadButton);

        filePanel.add(fileLabel, BorderLayout.WEST);
        filePanel.add(fileField, BorderLayout.CENTER);
        filePanel.add(buttonPanel, BorderLayout.EAST);

        return filePanel;
    }

    /*
     * This method creates the inventory table section.
     */
    private JPanel createInventoryPanel() {
        JPanel inventoryPanel = createCardPanel(new BorderLayout(0, 12));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("CURRENT INVENTORY");
        titleLabel.setForeground(CHARCOAL);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel helpLabel = new JLabel("Click a row to update or remove an item");
        helpLabel.setForeground(MUTED_TEXT);
        helpLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(helpLabel, BorderLayout.EAST);

        JScrollPane tableScrollPane = new JScrollPane(inventoryTable);
        tableScrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER_COLOR)
        );
        tableScrollPane.getViewport().setBackground(WHITE);

        inventoryPanel.add(titlePanel, BorderLayout.NORTH);
        inventoryPanel.add(tableScrollPane, BorderLayout.CENTER);

        return inventoryPanel;
    }

    /*
     * This method creates the item form and total cards.
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(14, 14));
        bottomPanel.setOpaque(false);

        bottomPanel.add(createItemForm(), BorderLayout.CENTER);
        bottomPanel.add(createMetricsPanel(), BorderLayout.EAST);

        return bottomPanel;
    }

    /*
     * This method creates the form for entering item information.
     */
    private JPanel createItemForm() {
        JPanel formPanel = createCardPanel(new BorderLayout(0, 12));

        JLabel titleLabel = new JLabel("ITEM DETAILS");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        titleLabel.setForeground(CHARCOAL);

        JPanel aquaLine = new JPanel();
        aquaLine.setBackground(AQUA);
        aquaLine.setPreferredSize(new Dimension(70, 4));

        JPanel headingPanel = new JPanel(new BorderLayout());
        headingPanel.setOpaque(false);
        headingPanel.add(titleLabel, BorderLayout.WEST);
        headingPanel.add(aquaLine, BorderLayout.SOUTH);

        JPanel fieldPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        fieldPanel.setOpaque(false);

        fieldPanel.add(createLabeledField("Item ID", idField));
        fieldPanel.add(createLabeledField("Item Name", nameField));
        fieldPanel.add(createLabeledField("Category", categoryField));
        fieldPanel.add(createLabeledField("Supplier", supplierField));
        fieldPanel.add(createLabeledField("Quantity", quantityField));
        fieldPanel.add(createLabeledField("Unit Cost", unitCostField));
        fieldPanel.add(createLabeledField("Reorder Level", reorderLevelField));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.setOpaque(false);

        JButton addButton = createActionButton(
                "Add Item",
                SUCCESS_GREEN
        );
        addButton.addActionListener(event -> addItem());

        JButton updateButton = createActionButton(
                "Update Item",
                DARK_AQUA
        );
        updateButton.addActionListener(event -> updateItem());

        JButton removeButton = createActionButton(
                "Remove Item",
                REMOVE_RED
        );
        removeButton.addActionListener(event -> removeItem());

        JButton clearButton = createOutlineButton("Clear");
        clearButton.addActionListener(event -> clearFields());

        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(removeButton);
        actionPanel.add(clearButton);

        formPanel.add(headingPanel, BorderLayout.NORTH);
        formPanel.add(fieldPanel, BorderLayout.CENTER);
        formPanel.add(actionPanel, BorderLayout.SOUTH);

        return formPanel;
    }

    /*
     * This method creates the total items and total value cards.
     */
    private JPanel createMetricsPanel() {
        JPanel metricsPanel = new JPanel(new GridLayout(2, 1, 0, 12));
        metricsPanel.setOpaque(false);
        metricsPanel.setPreferredSize(new Dimension(255, 0));

        metricsPanel.add(
                createMetricCard("TOTAL ITEMS", totalItemsLabel)
        );

        metricsPanel.add(
                createMetricCard(
                        "TOTAL INVENTORY VALUE",
                        totalValueLabel
                )
        );

        return metricsPanel;
    }

    /*
     * This method creates one information card.
     */
    private JPanel createMetricCard(
            String title,
            JLabel valueLabel
    ) {
        JPanel metricCard = createCardPanel(new BorderLayout());

        JPanel topLine = new JPanel();
        topLine.setBackground(AQUA);
        topLine.setPreferredSize(new Dimension(0, 5));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        titleLabel.setForeground(MUTED_TEXT);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(valueLabel, BorderLayout.CENTER);

        metricCard.add(topLine, BorderLayout.NORTH);
        metricCard.add(textPanel, BorderLayout.CENTER);

        return metricCard;
    }

    /*
     * This method creates the message bar at the bottom.
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(WHITE);
        statusBar.setBorder(
                new MatteBorder(1, 0, 0, 0, BORDER_COLOR)
        );

        JPanel insidePanel = new JPanel(new BorderLayout());
        insidePanel.setOpaque(false);
        insidePanel.setBorder(new EmptyBorder(8, 18, 8, 18));

        JLabel phaseMessage = new JLabel(
                "GUI only • No database connection yet"
        );
        phaseMessage.setForeground(MUTED_TEXT);
        phaseMessage.setFont(new Font("SansSerif", Font.PLAIN, 12));

        insidePanel.add(statusLabel, BorderLayout.WEST);
        insidePanel.add(phaseMessage, BorderLayout.EAST);

        statusBar.add(insidePanel, BorderLayout.CENTER);

        return statusBar;
    }

    /*
     * This method connects table and window events.
     */
    private void connectEvents() {
        inventoryTable.getSelectionModel()
                .addListSelectionListener(event -> {

                    if (!event.getValueIsAdjusting()) {
                        fillFieldsFromSelectedRow();
                    }
                });

        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(
                    java.awt.event.WindowEvent event
            ) {
                exitApplication();
            }
        });
    }

    /*
     * This method creates the table columns.
     */
    private DefaultTableModel createTableModel() {
        String[] columnNames = {
                "Item ID",
                "Item Name",
                "Category",
                "Supplier",
                "Quantity",
                "Unit Cost",
                "Reorder Level",
                "Total Value"
        };

        return new DefaultTableModel(columnNames, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {

                if (columnIndex == 0
                        || columnIndex == 4
                        || columnIndex == 6) {

                    return Integer.class;
                }

                if (columnIndex == 5
                        || columnIndex == 7) {

                    return Double.class;
                }

                return String.class;
            }
        };
    }

    /*
     * This method styles the inventory table.
     */
    private JTable createInventoryTable() {
        JTable table = new JTable(tableModel);

        table.setRowHeight(32);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setForeground(TEXT_COLOR);
        table.setBackground(WHITE);
        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        table.setSelectionBackground(
                new Color(215, 242, 244)
        );

        table.setSelectionForeground(TEXT_COLOR);
        table.setGridColor(BORDER_COLOR);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        styleTableHeader(table);
        styleMoneyColumns(table);

        return table;
    }

    /*
     * This method creates a dark and readable table header.
     */
    private void styleTableHeader(JTable table) {
        JTableHeader header = table.getTableHeader();

        header.setPreferredSize(new Dimension(0, 42));
        header.setReorderingAllowed(false);

        header.setDefaultRenderer(
                new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean isSelected,
                            boolean hasFocus,
                            int row,
                            int column
                    ) {
                        JLabel label = new JLabel(
                                String.valueOf(value)
                        );

                        label.setOpaque(true);
                        label.setBackground(SOFT_CHARCOAL);
                        label.setForeground(WHITE);
                        label.setFont(
                                new Font(
                                        "SansSerif",
                                        Font.BOLD,
                                        12
                                )
                        );

                        label.setHorizontalAlignment(
                                SwingConstants.CENTER
                        );

                        label.setBorder(
                                BorderFactory.createCompoundBorder(
                                        new MatteBorder(
                                                0,
                                                0,
                                                3,
                                                1,
                                                AQUA
                                        ),
                                        new EmptyBorder(
                                                8,
                                                6,
                                                8,
                                                6
                                        )
                                )
                        );

                        return label;
                    }
                }
        );
    }

    /*
     * This method formats prices as dollar amounts.
     */
    private void styleMoneyColumns(JTable table) {
        DefaultTableCellRenderer moneyRenderer =
                new DefaultTableCellRenderer() {

                    @Override
                    protected void setValue(Object value) {

                        if (value instanceof Number) {
                            setText(
                                    String.format(
                                            "$%.2f",
                                            ((Number) value)
                                                    .doubleValue()
                                    )
                            );
                        } else {
                            setText("");
                        }
                    }
                };

        moneyRenderer.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        table.getColumnModel()
                .getColumn(5)
                .setCellRenderer(moneyRenderer);

        table.getColumnModel()
                .getColumn(7)
                .setCellRenderer(moneyRenderer);
    }

    /*
     * This method displays all inventory items in the table.
     */
    private void refreshTable() {
        tableModel.setRowCount(0);

        List<InventoryItem> inventoryItems =
                manager.getInventory();

        for (InventoryItem item : inventoryItems) {

            tableModel.addRow(new Object[]{
                    item.getItemId(),
                    item.getItemName(),
                    item.getCategory(),
                    item.getSupplier(),
                    item.getQuantity(),
                    item.getUnitCost(),
                    item.getReorderLevel(),
                    item.calculateItemValue()
            });
        }

        updateMetricCards();
        setStatus("Inventory display refreshed.");
    }

    /*
     * This method adds a new inventory item.
     */
    private void addItem() {
        try {
            InventoryItem newItem = readItemFromFields();

            if (manager.addItem(newItem)) {
                refreshTable();
                clearFields();

                showSuccessMessage(
                        "Inventory item added successfully."
                );
            } else {
                showErrorMessage(
                        "That item ID already exists."
                                + " Please enter another ID."
                );

                idField.requestFocusInWindow();
            }

        } catch (IllegalArgumentException exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    /*
     * This method updates the selected inventory item.
     */
    private void updateItem() {
        try {
            int itemId = parseNonNegativeInteger(
                    idField.getText(),
                    "Item ID"
            );

            if (!manager.itemExists(itemId)) {
                showErrorMessage(
                        "The item ID was not found."
                );

                return;
            }

            String itemName = requireText(
                    nameField.getText(),
                    "Item name"
            );

            String category = requireText(
                    categoryField.getText(),
                    "Category"
            );

            String supplier = requireText(
                    supplierField.getText(),
                    "Supplier"
            );

            int quantity = parseNonNegativeInteger(
                    quantityField.getText(),
                    "Quantity"
            );

            double unitCost = parseNonNegativeDouble(
                    unitCostField.getText(),
                    "Unit cost"
            );

            int reorderLevel = parseNonNegativeInteger(
                    reorderLevelField.getText(),
                    "Reorder level"
            );

            boolean itemUpdated =
                    manager.updateItem(
                            itemId,
                            1,
                            itemName
                    )
                            && manager.updateItem(
                            itemId,
                            2,
                            category
                    )
                            && manager.updateItem(
                            itemId,
                            3,
                            supplier
                    )
                            && manager.updateItem(
                            itemId,
                            4,
                            String.valueOf(quantity)
                    )
                            && manager.updateItem(
                            itemId,
                            5,
                            String.valueOf(unitCost)
                    )
                            && manager.updateItem(
                            itemId,
                            6,
                            String.valueOf(reorderLevel)
                    );

            if (itemUpdated) {
                refreshTable();
                selectItemInTable(itemId);

                showSuccessMessage(
                        "Inventory item updated successfully."
                );
            } else {
                showErrorMessage(
                        "The inventory item could not be updated."
                );
            }

        } catch (IllegalArgumentException exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    /*
     * This method removes an inventory item.
     */
    private void removeItem() {
        try {
            int itemId = parseNonNegativeInteger(
                    idField.getText(),
                    "Item ID"
            );

            if (!manager.itemExists(itemId)) {
                showErrorMessage(
                        "The item ID was not found."
                );

                return;
            }

            int userChoice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove item "
                            + itemId
                            + "?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (userChoice == JOptionPane.YES_OPTION) {

                if (manager.removeItem(itemId)) {
                    refreshTable();
                    clearFields();

                    showSuccessMessage(
                            "Inventory item removed successfully."
                    );
                }
            }

        } catch (IllegalArgumentException exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    /*
     * This method reads and validates all item form fields.
     */
    private InventoryItem readItemFromFields() {
        int itemId = parseNonNegativeInteger(
                idField.getText(),
                "Item ID"
        );

        String itemName = requireText(
                nameField.getText(),
                "Item name"
        );

        String category = requireText(
                categoryField.getText(),
                "Category"
        );

        String supplier = requireText(
                supplierField.getText(),
                "Supplier"
        );

        int quantity = parseNonNegativeInteger(
                quantityField.getText(),
                "Quantity"
        );

        double unitCost = parseNonNegativeDouble(
                unitCostField.getText(),
                "Unit cost"
        );

        int reorderLevel = parseNonNegativeInteger(
                reorderLevelField.getText(),
                "Reorder level"
        );

        return new InventoryItem(
                itemId,
                itemName,
                category,
                supplier,
                quantity,
                unitCost,
                reorderLevel
        );
    }

    /*
     * This method loads inventory from the selected file.
     */
    private void loadInventory() {
        String fileName = fileField.getText().trim();

        if (fileName.isEmpty()) {
            showErrorMessage(
                    "Please enter or select a file name."
            );

            return;
        }

        if (manager.loadInventory(fileName)) {
            refreshTable();
            clearFields();

            showSuccessMessage(
                    manager.getInventorySize()
                            + " inventory items were loaded."
            );
        } else {
            showErrorMessage(
                    "The file could not be loaded."
                            + " Check the file path and format."
            );
        }
    }

    /*
     * This method saves inventory to the selected text file.
     */
    private void saveInventory() {
        String fileName = fileField.getText().trim();

        if (fileName.isEmpty()) {
            showErrorMessage(
                    "Please enter or select a file name."
            );

            return;
        }

        if (manager.saveInventory(fileName)) {
            showSuccessMessage(
                    "Inventory saved successfully."
            );
        } else {
            showErrorMessage(
                    "The inventory could not be saved."
            );
        }
    }

    /*
     * This method opens a window where the user can select a file.
     */
    private void browseForFile() {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle(
                "Choose Inventory Text File"
        );

        fileChooser.setFileSelectionMode(
                JFileChooser.FILES_ONLY
        );

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile =
                    fileChooser.getSelectedFile();

            fileField.setText(
                    selectedFile.getAbsolutePath()
            );

            setStatus(
                    "Selected file: "
                            + selectedFile.getName()
            );
        }
    }

    /*
     * This method displays the total value of inventory.
     */
    private void showInventoryValue() {
        double inventoryValue =
                manager.calculateTotalInventoryValue();

        updateMetricCards();

        JOptionPane.showMessageDialog(
                this,
                String.format(
                        "The current inventory value is $%,.2f",
                        inventoryValue
                ),
                "Inventory Value",
                JOptionPane.INFORMATION_MESSAGE
        );

        setStatus(
                "Total inventory value calculated."
        );
    }

    /*
     * This method copies a selected table row into the form.
     */
    private void fillFieldsFromSelectedRow() {
        int selectedViewRow =
                inventoryTable.getSelectedRow();

        if (selectedViewRow < 0) {
            return;
        }

        int selectedModelRow =
                inventoryTable.convertRowIndexToModel(
                        selectedViewRow
                );

        idField.setText(
                String.valueOf(
                        tableModel.getValueAt(
                                selectedModelRow,
                                0
                        )
                )
        );

        nameField.setText(
                String.valueOf(
                        tableModel.getValueAt(
                                selectedModelRow,
                                1
                        )
                )
        );

        categoryField.setText(
                String.valueOf(
                        tableModel.getValueAt(
                                selectedModelRow,
                                2
                        )
                )
        );

        supplierField.setText(
                String.valueOf(
                        tableModel.getValueAt(
                                selectedModelRow,
                                3
                        )
                )
        );

        quantityField.setText(
                String.valueOf(
                        tableModel.getValueAt(
                                selectedModelRow,
                                4
                        )
                )
        );

        unitCostField.setText(
                String.valueOf(
                        tableModel.getValueAt(
                                selectedModelRow,
                                5
                        )
                )
        );

        reorderLevelField.setText(
                String.valueOf(
                        tableModel.getValueAt(
                                selectedModelRow,
                                6
                        )
                )
        );

        idField.setEditable(false);

        setStatus(
                "Selected item "
                        + idField.getText()
                        + " for editing."
        );
    }

    /*
     * This method selects an item in the table by its ID.
     */
    private void selectItemInTable(int itemId) {
        for (
                int modelRow = 0;
                modelRow < tableModel.getRowCount();
                modelRow++
        ) {
            int currentId =
                    (Integer) tableModel.getValueAt(
                            modelRow,
                            0
                    );

            if (currentId == itemId) {
                int viewRow =
                        inventoryTable.convertRowIndexToView(
                                modelRow
                        );

                inventoryTable.setRowSelectionInterval(
                        viewRow,
                        viewRow
                );

                inventoryTable.scrollRectToVisible(
                        inventoryTable.getCellRect(
                                viewRow,
                                0,
                                true
                        )
                );

                return;
            }
        }
    }

    /*
     * This method clears the item form.
     */
    private void clearFields() {
        idField.setEditable(true);

        idField.setText("");
        nameField.setText("");
        categoryField.setText("");
        supplierField.setText("");
        quantityField.setText("");
        unitCostField.setText("");
        reorderLevelField.setText("");

        inventoryTable.clearSelection();
        idField.requestFocusInWindow();

        setStatus("Form cleared.");
    }

    /*
     * This method updates the total item and value cards.
     */
    private void updateMetricCards() {
        totalItemsLabel.setText(
                String.valueOf(
                        manager.getInventorySize()
                )
        );

        totalValueLabel.setText(
                String.format(
                        "$%,.2f",
                        manager.calculateTotalInventoryValue()
                )
        );
    }

    /*
     * This method asks the user before closing the program.
     */
    private void exitApplication() {
        int userChoice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit Restaurant Inventory Manager",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (userChoice == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    /*
     * This method validates whole numbers.
     */
    private int parseNonNegativeInteger(
            String value,
            String fieldName
    ) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    fieldName + " is required."
            );
        }

        try {
            int number = Integer.parseInt(
                    value.trim()
            );

            if (number < 0) {
                throw new IllegalArgumentException(
                        fieldName + " cannot be negative."
                );
            }

            return number;

        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    fieldName + " must be a whole number."
            );
        }
    }

    /*
     * This method validates decimal numbers.
     */
    private double parseNonNegativeDouble(
            String value,
            String fieldName
    ) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    fieldName + " is required."
            );
        }

        try {
            double number = Double.parseDouble(
                    value.trim()
            );

            if (number < 0) {
                throw new IllegalArgumentException(
                        fieldName + " cannot be negative."
                );
            }

            return number;

        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    fieldName + " must be a valid number."
            );
        }
    }

    /*
     * This method makes sure a text field is not blank.
     */
    private String requireText(
            String value,
            String fieldName
    ) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be blank."
            );
        }

        return value.trim();
    }

    /*
     * This method displays a success message.
     */
    private void showSuccessMessage(String message) {
        setStatus(message);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /*
     * This method displays an error message.
     */
    private void showErrorMessage(String message) {
        setStatus(message);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Input Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /*
     * This method changes the message at the bottom.
     */
    private void setStatus(String message) {
        statusLabel.setText(message);
    }

    /*
     * This method creates a white card panel.
     */
    private JPanel createCardPanel(
            LayoutManager layout
    ) {
        JPanel cardPanel = new JPanel(layout);

        cardPanel.setBackground(WHITE);

        cardPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        new SoftCardBorder(),
                        new EmptyBorder(
                                14,
                                14,
                                14,
                                14
                        )
                )
        );

        return cardPanel;
    }

    /*
     * This method places a label above a text field.
     */
    private JPanel createLabeledField(
            String labelText,
            JTextField textField
    ) {
        JPanel fieldPanel =
                new JPanel(new BorderLayout(0, 5));

        fieldPanel.setOpaque(false);

        JLabel fieldLabel = new JLabel(labelText);
        fieldLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        fieldLabel.setForeground(TEXT_COLOR);

        fieldPanel.add(fieldLabel, BorderLayout.NORTH);
        fieldPanel.add(textField, BorderLayout.CENTER);

        return fieldPanel;
    }

    /*
     * This method creates a modern text field.
     */
    private JTextField createTextField() {
        JTextField textField = new JTextField();

        textField.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        textField.setForeground(TEXT_COLOR);
        textField.setBackground(WHITE);
        textField.setCaretColor(DARK_AQUA);

        textField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(190, 196, 202),
                                1
                        ),
                        new EmptyBorder(
                                8,
                                10,
                                8,
                                10
                        )
                )
        );

        return textField;
    }

    /*
     * This method creates the main aqua button.
     */
    private JButton createPrimaryButton(String text) {
        return createActionButton(text, DARK_AQUA);
    }

    /*
     * This method creates a white button with an aqua border.
     */
    private JButton createOutlineButton(String text) {
        JButton button = new JButton(text);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(CHARCOAL);
        button.setBackground(
                new Color(225, 244, 245)
        );

        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                DARK_AQUA,
                                2
                        ),
                        new EmptyBorder(
                                8,
                                14,
                                8,
                                14
                        )
                )
        );

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    /*
     * This method creates a solid colored button.
     */
    private JButton createActionButton(
            String text,
            Color backgroundColor
    ) {
        JButton button = new JButton(text);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(WHITE);
        button.setBackground(backgroundColor);

        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.setBorder(
                new EmptyBorder(
                        11,
                        17,
                        11,
                        17
                )
        );

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    /*
     * This method creates a dark sidebar button.
     */
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        44
                )
        );

        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(WHITE);
        button.setBackground(SOFT_CHARCOAL);

        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        new MatteBorder(
                                0,
                                5,
                                0,
                                0,
                                AQUA
                        ),
                        new EmptyBorder(
                                11,
                                13,
                                11,
                                13
                        )
                )
        );

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    /*
     * This method creates the large number labels.
     */
    private JLabel createMetricValueLabel(String value) {
        JLabel valueLabel = new JLabel(value);

        valueLabel.setForeground(CHARCOAL);

        valueLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        25
                )
        );

        return valueLabel;
    }

    /*
     * This border gives panels a soft and simple shadow.
     */
    private static class SoftCardBorder
            extends AbstractBorder {

        private final Insets borderInsets =
                new Insets(1, 1, 3, 3);

        @Override
        public Insets getBorderInsets(
                Component component
        ) {
            return borderInsets;
        }

        @Override
        public Insets getBorderInsets(
                Component component,
                Insets insets
        ) {
            insets.top = borderInsets.top;
            insets.left = borderInsets.left;
            insets.bottom = borderInsets.bottom;
            insets.right = borderInsets.right;

            return insets;
        }

        @Override
        public void paintBorder(
                Component component,
                Graphics graphics,
                int x,
                int y,
                int width,
                int height
        ) {
            graphics.setColor(
                    new Color(211, 213, 215)
            );

            graphics.drawRect(
                    x,
                    y,
                    width - 3,
                    height - 3
            );

            graphics.setColor(
                    new Color(229, 230, 228)
            );

            graphics.drawLine(
                    x + 3,
                    y + height - 2,
                    x + width - 2,
                    y + height - 2
            );

            graphics.drawLine(
                    x + width - 2,
                    y + 3,
                    x + width - 2,
                    y + height - 2
            );
        }
    }
}