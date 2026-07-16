## Restaurant Inventory Management System
## About the Project

This is a Java restaurant inventory program that uses a Swing GUI and an SQLite database.

The user can connect to the database, view inventory items, add new items, update items, remove items, and calculate the total inventory value.

The design uses Barrio Burrito Bar-inspired colors and branding.

## Main Features
Connect to an SQLite database
Display inventory records
Add new items
Update existing items
Remove items
Refresh the inventory table
Validate user input
Prevent duplicate item IDs
Show total items
Calculate total inventory value
Confirm before exiting
Inventory Information

## Each item includes:

Item ID
Item name
Category
Supplier
Quantity
Unit cost
Reorder level

The value of each item is calculated by multiplying the quantity by the unit cost.

## Technologies Used
Java 17
Java Swing
SQLite
JDBC
IntelliJ IDEA
Important Files
src
├── InventoryApplication.java
├── InventoryDatabase.java
├── InventoryGUI.java
├── InventoryItem.java
└── CreateInventoryDatabase.java

lib
└── sqlite-jdbc-3.53.2.0.jar

inventory.db
## How to Run
Open the project in IntelliJ.
Make sure Java 17 is selected.
Make sure the SQLite JDBC file is added as a library.
Run InventoryApplication.java.
Click Browse.
Select inventory.db.
Click Connect Database.
## How to Use

To add an item, enter all information and click Add Item.

To update an item, select a row, change the information, and click Update Item.

To remove an item, select a row and click Remove Item.

The program checks for blank fields, incorrect numbers, negative values, duplicate IDs, and invalid database files.

## Custom Feature

The custom feature calculates the total value of all inventory items stored in the database.
