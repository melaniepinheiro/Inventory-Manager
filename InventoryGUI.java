/*
 * InventoryGUI.java
 *
 * This class creates the graphical user interface for the
 * Restaurant Inventory Management System.
 *
 * The program connects to an SQLite database and allows the
 * user to display, add, update, remove, and calculate inventory data.
 *
 * Author: Melanie Pinheiro
 */

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.List;

public class InventoryGUI extends JFrame {

    /*
     * Barrio-inspired colors used throughout the GUI.
     */
    private static final Color AQUA =
            new Color(104, 194, 201);

    private static final Color DARK_AQUA =
            new Color(45, 145, 153);

    private static final Color CHARCOAL =
            new Color(30, 31, 34);

    private static final Color SOFT_CHARCOAL =
            new Color(48, 50, 55);

    private static final Color CREAM =
            new Color(248, 247, 242);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color TEXT_COLOR =
            new Color(38, 40, 44);

    private static final Color MUTED_TEXT =
            new Color(105, 110, 118);

    private static final Color BORDER_COLOR =
            new Color(218, 221, 225);

    private static final Color SUCCESS_GREEN =
            new Color(55, 145, 98);

    private static final Color REMOVE_RED =
            new Color(185, 63, 70);

    /*
     * This object handles all SQLite database actions.
     */
    private final InventoryDatabase database;

    /*
     * These variables hold the inventory table.
     */
    private final DefaultTableModel tableModel;
    private final JTable inventoryTable;

    /*
     * This field contains the path of the selected database.
     */
    private final JTextField databaseField;

    /*
     * These fields allow the user to enter item information.
     */
    private final JTextField idField;
    private final JTextField nameField;
    private final JTextField categoryField;
    private final JTextField supplierField;
    private final JTextField quantityField;
    private final JTextField unitCostField;
    private final JTextField reorderLevelField;

    /*
     * These labels display database information.
     */
    private final JLabel totalItemsLabel;
    private final JLabel totalValueLabel;
    private final JLabel connectionLabel;
    private final JLabel statusLabel;

    /*
     * This constructor prepares the GUI.
     */
    public InventoryGUI(InventoryDatabase database) {

        this.database = database;

        setTitle(
                "Barrio Restaurant Inventory Manager"
        );

        setDefaultCloseOperation(
                JFrame.DO_NOTHING_ON_CLOSE
        );

        setMinimumSize(
                new Dimension(1180, 760)
        );

        setSize(
                1320,
                830
        );

        setLocationRelativeTo(null);

        /*
         * Create the table before building the interface.
         */
        tableModel = createTableModel();
        inventoryTable = createInventoryTable();

        /*
         * Create the database path field.
         */
        databaseField = createTextField();
        databaseField.setText("inventory.db");

        /*
         * Create the item entry fields.
         */
        idField = createTextField();
        nameField = createTextField();
        categoryField = createTextField();
        supplierField = createTextField();
        quantityField = createTextField();
        unitCostField = createTextField();
        reorderLevelField = createTextField();

        /*
         * Create the information labels.
         */
        totalItemsLabel = createMetricValueLabel("0");
        totalValueLabel = createMetricValueLabel("$0.00");

        connectionLabel =
                new JLabel("Not Connected");

        connectionLabel.setForeground(REMOVE_RED);
        connectionLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        statusLabel =
                new JLabel(
                        "Select a database to begin."
                );

        statusLabel.setForeground(MUTED_TEXT);
        statusLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        buildInterface();
        connectEvents();
        updateMetricCards();
    }

    /*
     * This method builds the full window.
     */
    private void buildInterface() {

        JPanel rootPanel =
                new JPanel(new BorderLayout());

        rootPanel.setBackground(CREAM);

        rootPanel.add(
                createHeader(),
                BorderLayout.NORTH
        );

        rootPanel.add(
                createMainContent(),
                BorderLayout.CENTER
        );

        rootPanel.add(
                createStatusBar(),
                BorderLayout.SOUTH
        );

        setContentPane(rootPanel);
    }

    /*
     * This method creates the header at the top.
     */
    private JPanel createHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout(20, 0)
                );

        header.setBackground(CHARCOAL);

        header.setBorder(
                new EmptyBorder(
                        14,
                        22,
                        14,
                        22
                )
        );

        JLabel logoLabel =
                createLogoLabel();

        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                new JLabel(
                        "RESTAURANT INVENTORY MANAGER"
                );

        titleLabel.setForeground(WHITE);

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        25
                )
        );

        JLabel subtitleLabel =
                new JLabel(
                        "SQLite database inventory management system"
                );

        subtitleLabel.setForeground(AQUA);

        subtitleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        titlePanel.add(titleLabel);
        titlePanel.add(
                Box.createVerticalStrut(4)
        );
        titlePanel.add(subtitleLabel);

        JLabel phaseLabel =
                new JLabel("PHASE 4");

        phaseLabel.setOpaque(true);
        phaseLabel.setBackground(AQUA);
        phaseLabel.setForeground(CHARCOAL);

        phaseLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        phaseLabel.setBorder(
                new EmptyBorder(
                        8,
                        13,
                        8,
                        13
                )
        );

        header.add(
                logoLabel,
                BorderLayout.WEST
        );

        header.add(
                titlePanel,
                BorderLayout.CENTER
        );

        header.add(
                phaseLabel,
                BorderLayout.EAST
        );

        return header;
    }

    /*
     * This method loads the Barrio logo.
     */
    private JLabel createLogoLabel() {

        URL logoUrl =
                getClass().getResource(
                        "/assets/BarrioLogo.jpg"
                );

        if (logoUrl != null) {

            ImageIcon originalIcon =
                    new ImageIcon(logoUrl);

            Image scaledImage =
                    originalIcon.getImage()
                            .getScaledInstance(
                                    145,
                                    62,
                                    Image.SCALE_SMOOTH
                            );

            JLabel logoLabel =
                    new JLabel(
                            new ImageIcon(
                                    scaledImage
                            )
                    );

            logoLabel.setPreferredSize(
                    new Dimension(
                            150,
                            62
                    )
            );

            return logoLabel;
        }

        JLabel fallbackLabel =
                new JLabel("BARRIO");

        fallbackLabel.setForeground(AQUA);

        fallbackLabel.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        30
                )
        );

        return fallbackLabel;
    }

    /*
     * This method creates the main center section.
     */
    private JPanel createMainContent() {

        JPanel contentPanel =
                new JPanel(
                        new BorderLayout(16, 16)
                );

        contentPanel.setBackground(CREAM);

        contentPanel.setBorder(
                new EmptyBorder(
                        16,
                        16,
                        16,
                        16
                )
        );

        contentPanel.add(
                createSidebar(),
                BorderLayout.WEST
        );

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout(14, 14)
                );

        centerPanel.setOpaque(false);

        centerPanel.add(
                createDatabasePanel(),
                BorderLayout.NORTH
        );

        centerPanel.add(
                createInventoryPanel(),
                BorderLayout.CENTER
        );

        centerPanel.add(
                createBottomPanel(),
                BorderLayout.SOUTH
        );

        contentPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        return contentPanel;
    }

    /*
     * This method creates the sidebar menu.
     */
    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();

        sidebar.setPreferredSize(
                new Dimension(220, 0)
        );

        sidebar.setBackground(CHARCOAL);

        sidebar.setBorder(
                new EmptyBorder(
                        18,
                        16,
                        18,
                        16
                )
        );

        sidebar.setLayout(
                new BoxLayout(
                        sidebar,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel toolsLabel =
                new JLabel("DATABASE TOOLS");

        toolsLabel.setForeground(AQUA);

        toolsLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        toolsLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JButton refreshButton =
                createSidebarButton(
                        "Refresh Inventory"
                );

        refreshButton.addActionListener(
                event -> refreshTable()
        );

        JButton clearButton =
                createSidebarButton(
                        "Clear Form"
                );

        clearButton.addActionListener(
                event -> clearFields()
        );

        JButton exitButton =
                createSidebarButton(
                        "Exit"
                );

        exitButton.addActionListener(
                event -> exitApplication()
        );

        sidebar.add(toolsLabel);

        sidebar.add(
                Box.createVerticalStrut(16)
        );

        sidebar.add(refreshButton);

        sidebar.add(
                Box.createVerticalStrut(9)
        );

        sidebar.add(clearButton);

        sidebar.add(
                Box.createVerticalGlue()
        );

        sidebar.add(exitButton);

        return sidebar;
    }

    /*
     * This method creates the database connection section.
     */
    private JPanel createDatabasePanel() {

        JPanel databasePanel =
                createCardPanel(
                        new BorderLayout(12, 0)
                );

        JLabel databaseLabel =
                new JLabel("SQLite Database");

        databaseLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        databaseLabel.setForeground(TEXT_COLOR);

        JButton browseButton =
                createOutlineButton("Browse");

        browseButton.addActionListener(
                event -> browseForDatabase()
        );

        JButton connectButton =
                createPrimaryButton(
                        "Connect Database"
                );

        connectButton.addActionListener(
                event -> connectDatabase()
        );

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        buttonPanel.setOpaque(false);
        buttonPanel.add(browseButton);
        buttonPanel.add(connectButton);

        databasePanel.add(
                databaseLabel,
                BorderLayout.WEST
        );

        databasePanel.add(
                databaseField,
                BorderLayout.CENTER
        );

        databasePanel.add(
                buttonPanel,
                BorderLayout.EAST
        );

        return databasePanel;
    }

    /*
     * This method creates the inventory table section.
     */
    private JPanel createInventoryPanel() {

        JPanel inventoryPanel =
                createCardPanel(
                        new BorderLayout(0, 12)
                );

        JPanel titlePanel =
                new JPanel(
                        new BorderLayout()
                );

        titlePanel.setOpaque(false);

        JLabel titleLabel =
                new JLabel(
                        "CURRENT INVENTORY"
                );

        titleLabel.setForeground(CHARCOAL);

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        JLabel helpLabel =
                new JLabel(
                        "Click a row to update or remove an item"
                );

        helpLabel.setForeground(MUTED_TEXT);

        helpLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        titlePanel.add(
                titleLabel,
                BorderLayout.WEST
        );

        titlePanel.add(
                helpLabel,
                BorderLayout.EAST
        );

        JScrollPane tableScrollPane =
                new JScrollPane(
                        inventoryTable
                );

        tableScrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER_COLOR
                )
        );

        tableScrollPane
                .getViewport()
                .setBackground(WHITE);

        inventoryPanel.add(
                titlePanel,
                BorderLayout.NORTH
        );

        inventoryPanel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        return inventoryPanel;
    }

    /*
     * This method creates the bottom section.
     */
    private JPanel createBottomPanel() {

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout(14, 14)
                );

        bottomPanel.setOpaque(false);

        bottomPanel.add(
                createItemForm(),
                BorderLayout.CENTER
        );

        bottomPanel.add(
                createMetricsPanel(),
                BorderLayout.EAST
        );

        return bottomPanel;
    }

    /*
     * This method creates the item form.
     */
    private JPanel createItemForm() {

        JPanel formPanel =
                createCardPanel(
                        new BorderLayout(0, 12)
                );

        JLabel titleLabel =
                new JLabel("ITEM DETAILS");

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        titleLabel.setForeground(CHARCOAL);

        JPanel aquaLine = new JPanel();
        aquaLine.setBackground(AQUA);

        aquaLine.setPreferredSize(
                new Dimension(
                        70,
                        4
                )
        );

        JPanel headingPanel =
                new JPanel(
                        new BorderLayout()
                );

        headingPanel.setOpaque(false);

        headingPanel.add(
                titleLabel,
                BorderLayout.WEST
        );

        headingPanel.add(
                aquaLine,
                BorderLayout.SOUTH
        );

        JPanel fieldPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                4,
                                10,
                                10
                        )
                );

        fieldPanel.setOpaque(false);

        fieldPanel.add(
                createLabeledField(
                        "Item ID",
                        idField
                )
        );

        fieldPanel.add(
                createLabeledField(
                        "Item Name",
                        nameField
                )
        );

        fieldPanel.add(
                createLabeledField(
                        "Category",
                        categoryField
                )
        );

        fieldPanel.add(
                createLabeledField(
                        "Supplier",
                        supplierField
                )
        );

        fieldPanel.add(
                createLabeledField(
                        "Quantity",
                        quantityField
                )
        );

        fieldPanel.add(
                createLabeledField(
                        "Unit Cost",
                        unitCostField
                )
        );

        fieldPanel.add(
                createLabeledField(
                        "Reorder Level",
                        reorderLevelField
                )
        );

        JPanel actionPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        actionPanel.setOpaque(false);

        JButton addButton =
                createActionButton(
                        "Add Item",
                        SUCCESS_GREEN
                );

        addButton.addActionListener(
                event -> addItem()
        );

        JButton updateButton =
                createActionButton(
                        "Update Item",
                        DARK_AQUA
                );

        updateButton.addActionListener(
                event -> updateItem()
        );

        JButton removeButton =
                createActionButton(
                        "Remove Item",
                        REMOVE_RED
                );

        removeButton.addActionListener(
                event -> removeItem()
        );

        JButton clearButton =
                createOutlineButton("Clear");

        clearButton.addActionListener(
                event -> clearFields()
        );

        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(removeButton);
        actionPanel.add(clearButton);

        formPanel.add(
                headingPanel,
                BorderLayout.NORTH
        );

        formPanel.add(
                fieldPanel,
                BorderLayout.CENTER
        );

        formPanel.add(
                actionPanel,
                BorderLayout.SOUTH
        );

        return formPanel;
    }

    /*
     * This method creates the metric cards.
     */
    private JPanel createMetricsPanel() {

        JPanel metricsPanel =
                new JPanel(
                        new GridLayout(
                                3,
                                1,
                                0,
                                12
                        )
                );

        metricsPanel.setOpaque(false);

        metricsPanel.setPreferredSize(
                new Dimension(255, 0)
        );

        metricsPanel.add(
                createMetricCard(
                        "DATABASE STATUS",
                        connectionLabel
                )
        );

        metricsPanel.add(
                createMetricCard(
                        "TOTAL ITEMS",
                        totalItemsLabel
                )
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
     * This method creates one metric card.
     */
    private JPanel createMetricCard(
            String title,
            JLabel valueLabel
    ) {

        JPanel metricCard =
                createCardPanel(
                        new BorderLayout()
                );

        JPanel topLine =
                new JPanel();

        topLine.setBackground(AQUA);

        topLine.setPreferredSize(
                new Dimension(
                        0,
                        5
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        titleLabel.setForeground(MUTED_TEXT);

        JPanel textPanel =
                new JPanel(
                        new BorderLayout()
                );

        textPanel.setOpaque(false);

        textPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        textPanel.add(
                valueLabel,
                BorderLayout.CENTER
        );

        metricCard.add(
                topLine,
                BorderLayout.NORTH
        );

        metricCard.add(
                textPanel,
                BorderLayout.CENTER
        );

        return metricCard;
    }

    /*
     * This method creates the message bar.
     */
    private JPanel createStatusBar() {

        JPanel statusBar =
                new JPanel(
                        new BorderLayout()
                );

        statusBar.setBackground(WHITE);

        statusBar.setBorder(
                new MatteBorder(
                        1,
                        0,
                        0,
                        0,
                        BORDER_COLOR
                )
        );

        JPanel insidePanel =
                new JPanel(
                        new BorderLayout()
                );

        insidePanel.setOpaque(false);

        insidePanel.setBorder(
                new EmptyBorder(
                        8,
                        18,
                        8,
                        18
                )
        );

        JLabel phaseMessage =
                new JLabel(
                        "GUI and SQLite database connected"
                );

        phaseMessage.setForeground(MUTED_TEXT);

        phaseMessage.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        insidePanel.add(
                statusLabel,
                BorderLayout.WEST
        );

        insidePanel.add(
                phaseMessage,
                BorderLayout.EAST
        );

        statusBar.add(
                insidePanel,
                BorderLayout.CENTER
        );

        return statusBar;
    }

    /*
     * This method connects table and window events.
     */
    private void connectEvents() {

        inventoryTable
                .getSelectionModel()
                .addListSelectionListener(
                        event -> {

                            if (!event.getValueIsAdjusting()) {
                                fillFieldsFromSelectedRow();
                            }
                        }
                );

        addWindowListener(
                new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(
                            java.awt.event.WindowEvent event
                    ) {
                        exitApplication();
                    }
                }
        );
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

        return new DefaultTableModel(
                columnNames,
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(
                    int columnIndex
            ) {

                if (
                        columnIndex == 0
                                || columnIndex == 4
                                || columnIndex == 6
                ) {
                    return Integer.class;
                }

                if (
                        columnIndex == 5
                                || columnIndex == 7
                ) {
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

        JTable table =
                new JTable(tableModel);

        table.setRowHeight(32);

        table.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        table.setForeground(TEXT_COLOR);
        table.setBackground(WHITE);

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        table.setSelectionBackground(
                new Color(
                        215,
                        242,
                        244
                )
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
     * This method styles the table header.
     */
    private void styleTableHeader(
            JTable table
    ) {

        JTableHeader header =
                table.getTableHeader();

        header.setPreferredSize(
                new Dimension(
                        0,
                        42
                )
        );

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

                        JLabel label =
                                new JLabel(
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
     * This method formats money values.
     */
    private void styleMoneyColumns(
            JTable table
    ) {

        DefaultTableCellRenderer moneyRenderer =
                new DefaultTableCellRenderer() {

                    @Override
                    protected void setValue(
                            Object value
                    ) {

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
                .setCellRenderer(
                        moneyRenderer
                );

        table.getColumnModel()
                .getColumn(7)
                .setCellRenderer(
                        moneyRenderer
                );
    }

    /*
     * This method allows the user to select a database file.
     */
    private void browseForDatabase() {

        JFileChooser fileChooser =
                new JFileChooser();

        fileChooser.setDialogTitle(
                "Choose SQLite Database"
        );

        fileChooser.setFileFilter(
                new FileNameExtensionFilter(
                        "SQLite Database Files",
                        "db",
                        "sqlite",
                        "sqlite3"
                )
        );

        int result =
                fileChooser.showOpenDialog(this);

        if (
                result
                        == JFileChooser.APPROVE_OPTION
        ) {

            File selectedFile =
                    fileChooser.getSelectedFile();

            databaseField.setText(
                    selectedFile.getAbsolutePath()
            );

            setStatus(
                    "Selected database: "
                            + selectedFile.getName()
            );
        }
    }

    /*
     * This method connects to the selected SQLite database.
     */
    private void connectDatabase() {

        String databasePath =
                databaseField.getText().trim();

        if (databasePath.isEmpty()) {

            showErrorMessage(
                    "Please enter or select a database file."
            );

            return;
        }

        File databaseFile =
                new File(databasePath);

        if (!databaseFile.exists()) {

            showErrorMessage(
                    "The selected database file does not exist."
            );

            return;
        }

        if (database.connect(databasePath)) {

            connectionLabel.setText(
                    "Connected"
            );

            connectionLabel.setForeground(
                    SUCCESS_GREEN
            );

            refreshTable();

            showSuccessMessage(
                    "Database connected successfully."
            );

        } else {

            connectionLabel.setText(
                    "Not Connected"
            );

            connectionLabel.setForeground(
                    REMOVE_RED
            );

            clearTable();

            showErrorMessage(
                    "The database could not be connected. "
                            + "Make sure it contains the inventory table."
            );
        }
    }

    /*
     * This method displays database records in the table.
     */
    private void refreshTable() {

        if (!database.isConnected()) {

            showErrorMessage(
                    "Please connect to the database first."
            );

            return;
        }

        tableModel.setRowCount(0);

        List<InventoryItem> inventoryItems =
                database.getAllItems();

        for (InventoryItem item : inventoryItems) {

            tableModel.addRow(
                    new Object[]{
                            item.getItemId(),
                            item.getItemName(),
                            item.getCategory(),
                            item.getSupplier(),
                            item.getQuantity(),
                            item.getUnitCost(),
                            item.getReorderLevel(),
                            item.calculateItemValue()
                    }
            );
        }

        updateMetricCards();

        setStatus(
                "Inventory refreshed from the database."
        );
    }

    /*
     * This method clears the table.
     */
    private void clearTable() {

        tableModel.setRowCount(0);
        updateMetricCards();
    }

    /*
     * This method adds an item to the database.
     */
    private void addItem() {

        if (!checkDatabaseConnection()) {
            return;
        }

        try {

            InventoryItem newItem =
                    readItemFromFields();

            if (
                    database.itemExists(
                            newItem.getItemId()
                    )
            ) {

                showErrorMessage(
                        "That item ID already exists."
                );

                idField.requestFocusInWindow();
                return;
            }

            if (database.addItem(newItem)) {

                refreshTable();
                clearFields();

                showSuccessMessage(
                        "Inventory item added to the database."
                );

            } else {

                showErrorMessage(
                        "The item could not be added."
                );
            }

        } catch (
                IllegalArgumentException exception
        ) {

            showErrorMessage(
                    exception.getMessage()
            );
        }
    }

    /*
     * This method updates an item in the database.
     */
    private void updateItem() {

        if (!checkDatabaseConnection()) {
            return;
        }

        try {

            InventoryItem updatedItem =
                    readItemFromFields();

            if (
                    !database.itemExists(
                            updatedItem.getItemId()
                    )
            ) {

                showErrorMessage(
                        "The item ID was not found."
                );

                return;
            }

            if (
                    database.updateItem(
                            updatedItem
                    )
            ) {

                refreshTable();

                selectItemInTable(
                        updatedItem.getItemId()
                );

                showSuccessMessage(
                        "Inventory item updated in the database."
                );

            } else {

                showErrorMessage(
                        "The item could not be updated."
                );
            }

        } catch (
                IllegalArgumentException exception
        ) {

            showErrorMessage(
                    exception.getMessage()
            );
        }
    }

    /*
     * This method removes an item from the database.
     */
    private void removeItem() {

        if (!checkDatabaseConnection()) {
            return;
        }

        try {

            int itemId =
                    parseNonNegativeInteger(
                            idField.getText(),
                            "Item ID"
                    );

            if (
                    !database.itemExists(itemId)
            ) {

                showErrorMessage(
                        "The item ID was not found."
                );

                return;
            }

            int userChoice =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to remove item "
                                    + itemId
                                    + "?",
                            "Confirm Removal",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (
                    userChoice
                            == JOptionPane.YES_OPTION
            ) {

                if (
                        database.removeItem(itemId)
                ) {

                    refreshTable();
                    clearFields();

                    showSuccessMessage(
                            "Inventory item removed from the database."
                    );

                } else {

                    showErrorMessage(
                            "The item could not be removed."
                    );
                }
            }

        } catch (
                IllegalArgumentException exception
        ) {

            showErrorMessage(
                    exception.getMessage()
            );
        }
    }

    /*
     * This method reads and validates all form fields.
     */
    private InventoryItem readItemFromFields() {

        int itemId =
                parsePositiveInteger(
                        idField.getText(),
                        "Item ID"
                );

        String itemName =
                requireText(
                        nameField.getText(),
                        "Item name"
                );

        String category =
                requireText(
                        categoryField.getText(),
                        "Category"
                );

        String supplier =
                requireText(
                        supplierField.getText(),
                        "Supplier"
                );

        int quantity =
                parseNonNegativeInteger(
                        quantityField.getText(),
                        "Quantity"
                );

        double unitCost =
                parseNonNegativeDouble(
                        unitCostField.getText(),
                        "Unit cost"
                );

        int reorderLevel =
                parseNonNegativeInteger(
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
     * This method checks that the database is connected.
     */
    private boolean checkDatabaseConnection() {

        if (!database.isConnected()) {

            showErrorMessage(
                    "Please connect to the database first."
            );

            return false;
        }

        return true;
    }

    /*
     * This method fills the form when a row is selected.
     */
    private void fillFieldsFromSelectedRow() {

        int selectedViewRow =
                inventoryTable.getSelectedRow();

        if (selectedViewRow < 0) {
            return;
        }

        int selectedModelRow =
                inventoryTable
                        .convertRowIndexToModel(
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

        /*
         * The primary key should not change while updating.
         */
        idField.setEditable(false);

        setStatus(
                "Selected item "
                        + idField.getText()
                        + " for editing."
        );
    }

    /*
     * This method selects a table row by item ID.
     */
    private void selectItemInTable(
            int itemId
    ) {

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
                        inventoryTable
                                .convertRowIndexToView(
                                        modelRow
                                );

                inventoryTable
                        .setRowSelectionInterval(
                                viewRow,
                                viewRow
                        );

                inventoryTable
                        .scrollRectToVisible(
                                inventoryTable
                                        .getCellRect(
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
     * This method updates the total cards.
     */
    private void updateMetricCards() {

        totalItemsLabel.setText(
                String.valueOf(
                        database.getInventorySize()
                )
        );

        totalValueLabel.setText(
                String.format(
                        "$%,.2f",
                        database
                                .calculateTotalInventoryValue()
                )
        );
    }

    /*
     * This method safely exits the program.
     */
    private void exitApplication() {

        int userChoice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to exit?",
                        "Exit Restaurant Inventory Manager",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (
                userChoice
                        == JOptionPane.YES_OPTION
        ) {

            database.closeConnection();
            dispose();
        }
    }

    /*
     * This method validates a positive whole number.
     */
    private int parsePositiveInteger(
            String value,
            String fieldName
    ) {

        int number =
                parseNonNegativeInteger(
                        value,
                        fieldName
                );

        if (number == 0) {

            throw new IllegalArgumentException(
                    fieldName
                            + " must be greater than zero."
            );
        }

        return number;
    }

    /*
     * This method validates a nonnegative whole number.
     */
    private int parseNonNegativeInteger(
            String value,
            String fieldName
    ) {

        if (
                value == null
                        || value.trim().isEmpty()
        ) {

            throw new IllegalArgumentException(
                    fieldName + " is required."
            );
        }

        try {

            int number =
                    Integer.parseInt(
                            value.trim()
                    );

            if (number < 0) {

                throw new IllegalArgumentException(
                        fieldName
                                + " cannot be negative."
                );
            }

            return number;

        } catch (
                NumberFormatException exception
        ) {

            throw new IllegalArgumentException(
                    fieldName
                            + " must be a whole number."
            );
        }
    }

    /*
     * This method validates a nonnegative decimal number.
     */
    private double parseNonNegativeDouble(
            String value,
            String fieldName
    ) {

        if (
                value == null
                        || value.trim().isEmpty()
        ) {

            throw new IllegalArgumentException(
                    fieldName + " is required."
            );
        }

        try {

            double number =
                    Double.parseDouble(
                            value.trim()
                    );

            if (
                    number < 0
                            || Double.isNaN(number)
                            || Double.isInfinite(number)
            ) {

                throw new IllegalArgumentException(
                        fieldName
                                + " must be a valid nonnegative number."
                );
            }

            return number;

        } catch (
                NumberFormatException exception
        ) {

            throw new IllegalArgumentException(
                    fieldName
                            + " must be a valid number."
            );
        }
    }

    /*
     * This method checks that text was entered.
     */
    private String requireText(
            String value,
            String fieldName
    ) {

        if (
                value == null
                        || value.trim().isEmpty()
        ) {

            throw new IllegalArgumentException(
                    fieldName
                            + " cannot be blank."
            );
        }

        return value.trim();
    }

    /*
     * This method displays a success message.
     */
    private void showSuccessMessage(
            String message
    ) {

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
    private void showErrorMessage(
            String message
    ) {

        setStatus(message);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Input Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /*
     * This method changes the bottom status message.
     */
    private void setStatus(
            String message
    ) {

        statusLabel.setText(message);
    }

    /*
     * This method creates a white card panel.
     */
    private JPanel createCardPanel(
            LayoutManager layout
    ) {

        JPanel cardPanel =
                new JPanel(layout);

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
     * This method creates a labeled field.
     */
    private JPanel createLabeledField(
            String labelText,
            JTextField textField
    ) {

        JPanel fieldPanel =
                new JPanel(
                        new BorderLayout(0, 5)
                );

        fieldPanel.setOpaque(false);

        JLabel fieldLabel =
                new JLabel(labelText);

        fieldLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        fieldLabel.setForeground(TEXT_COLOR);

        fieldPanel.add(
                fieldLabel,
                BorderLayout.NORTH
        );

        fieldPanel.add(
                textField,
                BorderLayout.CENTER
        );

        return fieldPanel;
    }

    /*
     * This method creates a modern text field.
     */
    private JTextField createTextField() {

        JTextField textField =
                new JTextField();

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
                                new Color(
                                        190,
                                        196,
                                        202
                                ),
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
    private JButton createPrimaryButton(
            String text
    ) {

        return createActionButton(
                text,
                DARK_AQUA
        );
    }

    /*
     * This method creates an outlined button.
     */
    private JButton createOutlineButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(CHARCOAL);

        button.setBackground(
                new Color(
                        225,
                        244,
                        245
                )
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

        JButton button =
                new JButton(text);

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
     * This method creates a sidebar button.
     */
    private JButton createSidebarButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        44
                )
        );

        button.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

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
     * This method creates the large metric labels.
     */
    private JLabel createMetricValueLabel(
            String value
    ) {

        JLabel valueLabel =
                new JLabel(value);

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
     * This border creates a soft card shadow.
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
                    new Color(
                            211,
                            213,
                            215
                    )
            );

            graphics.drawRect(
                    x,
                    y,
                    width - 3,
                    height - 3
            );

            graphics.setColor(
                    new Color(
                            229,
                            230,
                            228
                    )
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