# Library Management System
The Library Management System is designed to help librarians manage a library catalog and allow patrons to search, borrow easily, and track books. The system uses a MySQL database to store all data, including books, patrons, wishlists, and transactions.

## Features
* Librarians can add, create, and delete books.
* Patrons can create accounts, search books, borrow/return books, and manage wishlists.
* The MySQL database stores all library information and tracks transactions.

## Installation
1. Import LibraryDB.sql
2. Open MySQL Workbench, connect to your MySQL server, and select LibraryDB.sql to create the database
3. Open DatabaseConnection.java and update the following with your MySQL credentials

```java
private static final String URL = "jdbc:mysql://localhost:3306/LibraryDB";
private static final String USERNAME = "USERNAME";
private static final String PASSWORD = "PASSWORD";
```
4. Unzip and open LibraryManagementSystemUI in your preferred IDE
5. Compile the project and run LibraryApp.java
6. Create a new patron account or login with sample accounts:

**Librarian Account**
* Email: bettyjones@email.com
* Password: bettyPassword

**Patron Account**
* Email: jacksmith@email.com
* Password: jackPassword

## Project Structure
* **LibraryDB.sql**: creates the system database.
* **DatabaseConnection.java**: Manages the connection to the MySQL database.
* **DAO Classes**: Manages CRUD operations for Books, Patrons, Transactions, and Wishlist.
* **Controller Classes**: Handles user interactions in the UI. Connects the frontend to the database.

## Troubleshooting
* Verify that MySQL credentials in DatabaseConnection.java are correct and that the MySQL server is running.
