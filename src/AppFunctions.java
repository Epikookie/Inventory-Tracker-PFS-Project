import java.sql.*;
import javax.swing.*;
import java.time.LocalDateTime;

public class AppFunctions {

    // database connection variables
    Connection conn = null;
    Statement stmt;
    ResultSet rs;
    String databaseName = "glitchtracking"; // local database file name

    // Constant JTable column names
    String[] COL_NAMES = { "ItemID", "Item", "Store", "Quantity", "Summary", "Supplier" };

    // User session - populated when user logs in
    Session session;

    // ----------------------------------------------------------------------------------------------------
    // CONSTRUCTOR
    // ----------------------------------------------------------------------------------------------------

    /**
     * Constructs a new AppFunctions object
     */
    public AppFunctions() {
        initialiseConnection();
    }

    /**
     * Initialises the connection to the database if it hasn't already been done
     */
    private void initialiseConnection() {
        // check if connection already exists
        if (conn == null) {
            try {

                // initialise Driver
                try {
                    Class.forName("org.sqlite.JDBC");
                    System.out.println("SQLite JDBC Driver initialised");
                } catch (ClassNotFoundException e) {
                    // catch JDBC Driver initialisation error
                    e.printStackTrace();
                }

                // create database connection
                conn = DriverManager.getConnection("jdbc:sqlite:lib/" + databaseName + ".db", null, null);
                stmt = conn.createStatement();
                System.out.println("Connection to local SQLite database file has been established.");

            } catch (SQLException e) {
                // database connection failed
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Close database connection (if connected)
     */
    public void closeConnection() {
        // check that connection exists
        if (conn != null) {
            try {
                // try to close database connection
                conn.close();

            } catch (SQLException e) {
                // failed to close connection
                System.err.println(e.getMessage());
            }
        }
    }

    // ----------------------------------------------------------------------------------------------------
    // GENERIC DATABASE METHODS - PUBLIC
    // ----------------------------------------------------------------------------------------------------

    /**
     * Creates a new SQLite database and appropriate tables, returns true if
     * successful
     * 
     * @param overwrite boolean, whether or not to overwrite any existing tables
     * @return true if successful, false otherwise
     */
    public synchronized boolean initialiseDatabase(boolean overwrite) {
        try {

            if (overwrite) {
                // drop tables if they already exist
                System.out.println("Dropping existing tables");
                stmt.executeUpdate("DROP TABLE IF EXISTS inventorylog;\n" +
                        "DROP TABLE IF EXISTS inventory;\n" +
                        "DROP TABLE IF EXISTS item;\n" +
                        "DROP TABLE IF EXISTS staff;\n" +
                        "DROP TABLE IF EXISTS store;\n" +
                        "DROP TABLE IF EXISTS supplier;");
                System.out.println("Tables dropped");
            }

            createTables();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }

        // tables were successfully created
        return true;
    }

    // ----------------------------------------------------------------------------------------------------

    public void createTables() {

        try {
            // Batch supplier table creation
            stmt.addBatch("""
                    CREATE TABLE IF NOT EXISTS supplier (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name varchar(45) NOT NULL UNIQUE,
                    phone varchar(45) DEFAULT NULL,
                    email varchar(45) DEFAULT NULL,
                    contactName varchar(45) DEFAULT NULL,
                    street varchar(45) DEFAULT NULL,
                    suburb varchar(45) DEFAULT NULL,
                    state varchar(45) DEFAULT NULL,
                    postcode varchar(10) DEFAULT NULL)
                    """);

            // Batch store table creation
            stmt.addBatch("""
                    CREATE TABLE IF NOT EXISTS store (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name varchar(45) NOT NULL UNIQUE,
                        phone varchar(45) DEFAULT NULL,
                        email varchar(45) DEFAULT NULL,
                        manager varchar(45) DEFAULT NULL,
                        street varchar(45) DEFAULT NULL,
                        suburb varchar(45) DEFAULT NULL,
                        state varchar(45) DEFAULT NULL,
                        postcode varchar(10) DEFAULT NULL)
                    """);

            // Batch staff table creation
            stmt.addBatch("""
                    CREATE TABLE IF NOT EXISTS staff (
                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                      firstname varchar(45) NOT NULL,
                      lastname varchar(45) NOT NULL,
                      phone varchar(45) DEFAULT NULL,
                      email varchar(45) DEFAULT NULL,
                      street varchar(45) DEFAULT NULL,
                      suburb varchar(45) DEFAULT NULL,
                      state varchar(45) DEFAULT NULL,
                      postcode varchar(10) DEFAULT NULL,
                      passhash varchar(64) DEFAULT NULL,
                      salt varchar(4) DEFAULT NULL,
                      lastlogin datetime DEFAULT NULL,
                      admin INTEGER DEFAULT 0)
                    """);

            // Batch item table creation
            stmt.addBatch(
                    """
                            CREATE TABLE IF NOT EXISTS item (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            supplierid int NOT NULL,
                            name varchar(45) NOT NULL,
                            summary tinytext,
                            rfid varchar(40) DEFAULT NULL,
                            CONSTRAINT fk_supplierid FOREIGN KEY (supplierid) REFERENCES supplier (id) ON DELETE CASCADE ON UPDATE CASCADE)
                            """);

            // Batch inventory table creation
            stmt.addBatch(
                    """
                            CREATE TABLE IF NOT EXISTS inventory (
                                itemid INTEGER NOT NULL,
                                storeid INTEGER NOT NULL,
                                instock INTEGER DEFAULT NULL,
                                lownum INTEGER DEFAULT NULL,
                                PRIMARY KEY (storeid,itemid),
                                CONSTRAINT fk_itemid FOREIGN KEY (itemid) REFERENCES item (id),
                                CONSTRAINT fk_storeid FOREIGN KEY (storeid) REFERENCES store (id) ON DELETE CASCADE ON UPDATE CASCADE
                              )
                                """);

            // Batch inventory table creation
            stmt.addBatch(
                    """
                            CREATE TABLE IF NOT EXISTS inventorylog (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                staffid INTEGER NOT NULL,
                                itemid INTEGER NOT NULL,
                                storeid INTEGER NOT NULL,
                                operation VARCHAR(10) NOT NULL,
                                quantity INTEGER NOT NULL,
                                datetime datetime DEFAULT NULL)
                                """);

            stmt.executeBatch();

            System.out.println("Tables created");

        } catch (Exception e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }

    }

    /**
     * Populate staff table with dummy data
     */
    public void populateStaff() {

        String[] firstName = { "John", "Emma", "Michael", "Olivia", "William", "Ava", "James", "Sophia", "Benjamin",
                "Mia" };
        String[] lastName = { "Smith", "Johnson", "Brown", "Taylor", "Miller", "Anderson", "Clark", "Wilson", "Lee",
                "Martin" };
        String[] street = { "Main", "Oak", "Cedar", "Elm", "Maple", "Pine", "Spruce", "Birch", "Ash", "Willow" };
        String[] suburb = { "Downtown", "Central", "North", "South", "East", "West", "Hillside", "Riverside",
                "Sunnyvale", "Greenfield" };
        String[] state = { "NT", "TAS", "VIC", "SA", "NSW", "QLD", "WA", "ACT", "NSW", "NT" };
        String[] postcode = { "0870", "7004", "3001", "5690", "2795", "4737", "6532", "2603", "2648",
                "0871" };
        String[] phone = { "(08)83186989", "(07)49044635", "(03)99851311", "(07)45940823", "(03)53356152",
                "(08)94168098", "(08)90321422", "(07)40279091", "(07)40165282", "(07)45978132" };
        String email, password, firstDotLast, passhash, salt;
        LocalDateTime dateTime;

        try {
            for (int i = 0; i < 10; i++) {

                firstDotLast = firstName[i].toLowerCase() + "." + lastName[i].toLowerCase();
                email = firstDotLast + "@gmail.com";
                password = firstDotLast + state[i] + postcode[i];
                passhash = Security.generateSHA256Hash(password);
                salt = Security.generateSalt();
                dateTime = LocalDateTime.now();

                String query = "INSERT INTO staff (firstname, lastname, phone, email, street, suburb, state, postcode, passhash, salt, lastlogin) "
                        +
                        "VALUES ('" + firstName[i] + "', '" + lastName[i] + "', '" + phone[i] + "', '" + email + "', '"
                        + street[i]
                        + "', '" + suburb[i] + "', '" + state[i] + "', '" +
                        postcode[i] + "', '" + passhash + "', '" + salt + "', '" + dateTime + "')";

                stmt.addBatch(query);
                System.out.println("Staff " + firstName[i] + " " + lastName[i] + " added");
            }

            System.out.println("attempting to execute batch");
            stmt.executeBatch();
            System.out.println("batch executed");

        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
    }

    /**
     * Populate supplier table with dummy data
     */
    public void populateSupplier() {

        String[] name = { "ABC Supplier", "DEF Supplier", "GHI Supplier", "JKL Supplier", "MNO Supplier",
                "PQR Supplier", "STU Supplier", "VWX Supplier", "YZA Supplier",
                "BCD Supplier" };
        String[] contact = { "Sally", "Jimmy", "Bryce", "Terry", "Mike", "Ando", "Clarky", "Willy", "Stevo",
                "Trev" };
        String[] street = { "Main", "Oak", "Cedar", "Elm", "Maple", "Pine", "Spruce", "Birch", "Ash", "Willow" };
        String[] suburb = { "Downtown", "Central", "North", "South", "East", "West", "Hillside", "Riverside",
                "Sunnyvale", "Greenfield" };
        String[] state = { "NT", "TAS", "VIC", "SA", "NSW", "QLD", "WA", "ACT", "NSW", "NT" };
        String[] postcode = { "0870", "7004", "3001", "5690", "2795", "4737", "6532", "2603", "2648",
                "0871" };
        String[] phone = { "(07)56170183", "(07)40314341", "(03)53567182", "(03)53983663", "(02)40312432",
                "(03)53124851", "(02)40238930", "(03)53838987", "(07)30860234", "(08)83341006" };
        String email, nameNoSpace;

        try {
            for (int i = 0; i < 10; i++) {

                nameNoSpace = name[i].toLowerCase().replace(" ", "_");
                email = nameNoSpace + "@gmail.com";

                String query = "INSERT INTO supplier (name, phone, email, contactName, street, suburb, state, postcode) "
                        +
                        "VALUES ('" + name[i] + "', '" + phone[i] + "', '" + email + "', '" + contact[i] + "', '"
                        + street[i] + "', '" + suburb[i] + "', '" + state[i] + "', '" + postcode[i] + "')";

                stmt.addBatch(query);
                System.out.println("Supplier " + name[i] + " added");
            }

            System.out.println("attempting to execute batch");
            stmt.executeBatch();
            System.out.println("batch executed");

        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
    }

    /**
     * Populate supplier table with dummy data
     */
    public void populateStore() {

        String[] name = { "GST Stronghold", "Sorry Everyone", "Knives Out", "Grey Brigade", "Budgies-R-Us" };
        String[] manager = { "John", "Kevin", "Julia", "Malcom", "Tony" };
        String[] street = { "Main", "Oak", "Cedar", "Elm", "Maple" };
        String[] suburb = { "Downtown", "Central", "North", "South", "East" };
        String[] state = { "NT", "TAS", "VIC", "SA", "NSW" };
        String[] postcode = { "0870", "7004", "3001", "5690", "2795" };
        String[] phone = { "(07)56170183", "(07)40314341", "(03)53567182", "(03)53983663", "(02)40312432" };
        String email, nameNoSpace;

        try {
            for (int i = 0; i < 5; i++) {

                nameNoSpace = name[i].toLowerCase().replace(" ", "_");
                email = nameNoSpace + "@gmail.com";

                String query = "INSERT INTO store (name, phone, email, manager, street, suburb, state, postcode) "
                        +
                        "VALUES ('" + name[i] + "', '" + phone[i] + "', '" + email + "', '" + manager[i] + "', '"
                        + street[i] + "', '" + suburb[i] + "', '" + state[i] + "', '" + postcode[i] + "')";

                stmt.addBatch(query);
                System.out.println("Store " + name[i] + " added");
            }

            System.out.println("attempting to execute batch");
            stmt.executeBatch();
            System.out.println("batch executed");

        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
    }

    /**
     * Populate item table with dummy data
     */
    public void populateItems() {
        addItem(1, "Apple", "This is just an ordinary apple");
        addItem(2, "Banana", "This is just an ordinary banana");
        addItem(3, "Cherry", "This is no ordinary cherry");
        addItem(4, "D fruit", "Delicious fruit");
        addItem(5, "E fruit", "Energetic fruit");
        addItem(6, "Grapes", "So grape");
        addItem(6, "F fruit", "Feelgood fruit");
        addItem(7, "H fruit", "Healthy fruit");
        addItem(8, "I fruit", "Interesting fruit");
        addItem(8, "J fruit", "Just fruit");
        addItem(9, "Kiwifruit", "NZ fruit");
        addItem(10, "Lime", "In the coconut fruit");
        addItem(1, "Mango", "Kramer fruit");
        addItem(2, "N fruit", "Nice fruit");
        addItem(3, "Orange", "Colourful fruit");
        addItem(4, "Pineapple", "Hawaiian fruit");
        addItem(5, "Q fruit", "Questionable fruit");
        addItem(6, "R fruit", "Red fruit");
        addItem(7, "S fruit", "Succulent fruit");
        addItem(8, "Tangerine", "Tasty fruit");
        addItem(8, "Banana", "Longer shelf life");
        addItem(9, "Banana", "Shorter shelf life");
        addItem(10, "Apple", "Fuji - seconds");
        addItem(9, "Apple", "Fuji - Newer Stock");
        addItem(8, "Apple", "Small Red Delicious");
        addItem(7, "Apple", "Big Red Delicious");
    }

    /**
     * Adds an item to the inventory of a certain store
     * 
     * @param itemid  The itemid of the item to be added
     * @param storeid The storeid of the store to add the item to
     * @param inStock The amount of the item to add to the store
     * @param lowNum  The low number of the item to add to the store
     * @return True if the item was added successfully, false otherwise
     */
    public boolean addInventory(int itemid, int storeid, int inStock, int lowNum) {
        try {
            stmt.executeUpdate("INSERT INTO inventory(itemid, storeid, instock, lownum) " +
                    "VALUES (\'" + itemid + "', '" + storeid + "', '" + inStock + "', '" + lowNum +
                    "\');");
            System.out.println("Added " + inStock + " itemid " + itemid + " to store " + storeid);
            return true;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println(
                    "NOTE: If you are trying to add an item to a store that already has that item, " +
                            "use resetInventory() to overwrite existing values, or updateInventory() to " +
                            "modify existing stock on hand");
            return false;
        }
    }

    /**
     * Overwrites the item inventory of a certain store with new values
     * 
     * @param itemid  itemid of item to be added
     * @param storeid storeid of store to add item to
     * @param inStock new value of items in stock
     * @param lowNum  new value of low stock threshold
     * @return true if successful, false if not
     */
    public boolean resetInventory(int itemid, int storeid, int inStock, int lowNum) {
        try {
            stmt.executeUpdate("UPDATE inventory SET instock = " + inStock + ", lownum = " + lowNum +
                    " WHERE itemid = " + itemid + " AND storeid = " + storeid + ";");
            System.out.println("Updated " + inStock + " itemid " + itemid + " to store " + storeid);
            return true;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves the integer value of items in stock for a certain store, then adds
     * the new value to it. Value can be negative.
     * 
     * @param itemid      itemid of item to be added
     * @param storeid     storeid of store to add item to
     * @param adjustStock new value of items in stock
     * @return true if successful, false if not *
     */
    public boolean adjustInventory(int itemid, int storeid, int adjustStock) {
        try {
            int currentStock = 0, updatedStock = 0;
            ResultSet rs = stmt.executeQuery("SELECT instock FROM inventory WHERE itemid = " + itemid +
                    " AND storeid = " + storeid + ";");
            while (rs.next()) {
                currentStock = rs.getInt("instock");
            }

            // Make sure we don't have negative value for instock
            updatedStock = currentStock + adjustStock;
            if (updatedStock < 0) {
                System.err.println("ERROR: Cannot have negative stock on hand. Only " + currentStock
                        + " available in storeid " + storeid + ". Aborting.");
                return false;
            }

            // Update inventory
            stmt.executeUpdate("UPDATE inventory SET instock = " + updatedStock +
                    " WHERE itemid = " + itemid + " AND storeid = " + storeid + ";");
            System.out.println("Adjusted itemid " + itemid + " in store " + storeid + " by " + adjustStock +
                    " units. New stock on hand: " + updatedStock);
            return true;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Adds an item to the item table
     * 
     * @param supplier
     * @param itemName
     * @param itemSummary
     * @return true if successful, false if not
     */
    public boolean addItem(int supplier, String itemName, String itemSummary) {
        try {
            stmt.executeUpdate("INSERT INTO item(supplierid, name, summary, rfid) " +
                    "VALUES (\'" + supplier + "', '" + itemName + "', '" + itemSummary + "', '" +
                    "\');");

            ResultSet rs = stmt.executeQuery(
                    "SELECT id FROM Item WHERE name = '" + itemName + "' AND supplierid = " + supplier + ";");

            while (rs.next()) {
                int itemid = rs.getInt("id");
                String rfidHash = Security.generateSHA1Hash(itemid);

                stmt.executeUpdate("UPDATE item SET rfid = '" + rfidHash + "' WHERE name = '"
                        + itemName + "' AND supplierid = " + supplier + ";");
            }

            System.out.println("Item " + itemName + " added");
            return true;

        } catch (SQLException e) {
            System.err.print(e.getMessage());
            return false;
        }
    }

    /**
     * Add staff member to the database
     * 
     * @param firstName
     * @param lastName
     * @param phone
     * @param email
     * @param street
     * @param suburb
     * @param state
     * @param postcode
     * @param password
     * 
     * @return true if successful, false if not
     */
    public boolean addStaff(String firstName, String lastName, String phone, String email, String street,
            String suburb, String state, String postcode, String password) {

        String passhash, salt, saltedPassword;
        LocalDateTime dateTime;

        try {
            salt = Security.generateSalt();
            saltedPassword = password + salt;
            passhash = Security.generateSHA256Hash(saltedPassword);
            dateTime = LocalDateTime.now();
            stmt.executeUpdate(
                    "INSERT INTO staff (firstname, lastname, phone, email, street, suburb, state, postcode, passhash, salt, lastlogin) "
                            +
                            "VALUES ('" + firstName + "', '" + lastName + "', '" + phone + "', '" + email + "', '"
                            + street
                            + "', '" + suburb + "', '" + state + "', '" +
                            postcode + "', '" + passhash + "', '" + salt + "', '" + dateTime + "')");

            System.out.println("Staff member " + firstName + " " + lastName + " added");
            return true;

        } catch (SQLException e) {
            System.err.print(e.getMessage());
            return false;
        }
    }

    /**
     * Attempt log in with given username and password. Returns true if successful,
     * 
     * @param username
     * @param password
     * @return true if successful, false if not
     */
    public boolean attemptLogin(String staffID, String password) {

        if (!Security.validStaffID(staffID, stmt)) {
            System.err.println("Staff ID invalid");
            return false;
        }

        if (!Security.validPassword(password)) {
            System.err.println(
                    "Password must be minimum 12 characters, at least 1 lowerand uppercase letter, number, and special character, and no spaces");
            System.err.println("Password invalid");
            return false;
        }

        int staffIDint = Integer.parseInt(staffID);
        String[] hashAndSalt;

        // Get hash and salt from database
        try {
            hashAndSalt = Security.getHashAndSalt(staffIDint, stmt);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }

        if (hashAndSalt[0] == null) {
            System.err.println("Staff ID not found");
            return false;
        }

        String hash = hashAndSalt[0];
        String salt = hashAndSalt[1];

        if (Security.correctPassword(password, salt, hash)) {
            System.out.println("Authenticated");
            return true;
        } else {
            System.err.println("Incorrect password");
            return false;
        }

    }

    /**
     * Adds a supplier
     * 
     * @param supplierName
     * @return true if successful, false if not
     */
    public boolean addSupplier(String supplierName) {
        try {
            stmt.executeUpdate("INSERT INTO supplier(name) " +
                    "VALUES (\'" + supplierName + "\');");
            System.out.println("Supplier " + supplierName + " added");
            return true;

        } catch (SQLException e) {
            System.err.print(e.getMessage());
            return false;
        }
    }

    /**
     * Updates the inventory log
     * 
     * @param supplierName
     * @return true if successful, false if not
     */
    public boolean updateLog(int staffid, String rfid, String store, String operation, int quantity,
            LocalDateTime date) {
        try {

            ResultSet itemRS = stmt
                    .executeQuery("SELECT item.id FROM item WHERE item.rfid = '" + rfid + "';");
            ResultSet storeRS = stmt
                    .executeQuery("SELECT store.id FROM store WHERE store.name = '" + store + "';");

            int itemid, storeid;

            itemid = itemRS.getInt("id");
            storeid = storeRS.getInt("id");

            stmt.executeUpdate("INSERT INTO inventorylog(staffid, itemid, storeid, operation, quantity, datetime) " +
                    "VALUES (" + staffid + ", " + itemid + ", " + storeid + ", \'" + operation + "\', "
                    + quantity + ", \'" + date + "\');");

            System.out.println("InventoryLog updated");
            return true;

        } catch (SQLException e) {
            System.err.print(e.getMessage());
            return false;
        }
    }

    // -------------------------------------------------------
    public JTable searchInventoryByItem(String itemName, boolean lowStock) {
        Object[][] data;
        String[] col = { "ItemID", "Item", "Store", "Quantity", "Summary", "Supplier" };

        String sql = """
                SELECT itm.id, itm.name, S.name, Inv.instock, itm.summary, Sup.name
                FROM Inventory AS Inv
                JOIN Item AS itm ON Inv.itemid = itm.id
                JOIN Store AS S ON Inv.storeid = S.id
                JOIN Supplier AS Sup ON itm.supplierid = Sup.id
                WHERE itm.name LIKE ? || '%'
                """;
        if (lowStock) {
            sql += " AND Inv.instock < Inv.lownum;";
        } else {
            sql += ";";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemName);
            ResultSet rs = stmt.executeQuery();

            // Determine the number of rows in the ResultSet
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }

            // Reset the ResultSet to the start
            rs = stmt.executeQuery();

            // Resize the data array to accommodate all the records
            data = new Object[rowCount][col.length];

            int currentRow = 0;
            while (rs.next()) {
                for (int i = 1; i <= col.length; i++) {
                    data[currentRow][i - 1] = rs.getString(i);
                }
                currentRow++;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            data = new Object[0][0]; // Return an empty array in case of error
        }

        return new JTable(data, col);
    }

    public JTable searchInventoryByStore(String storeName, boolean lowStock) {
        Object[][] data;
        String[] col = { "ItemID", "Item", "Store", "Quantity", "Summary", "Supplier" };

        String sql = """
                SELECT itm.id, itm.name, S.name, Inv.instock, itm.summary, Sup.name
                FROM Inventory AS Inv
                JOIN Item AS itm ON Inv.itemid = itm.id
                JOIN Store AS S ON Inv.storeid = S.id
                JOIN Supplier AS Sup ON itm.supplierid = Sup.id
                WHERE S.name LIKE ? || '%'
                """;

        if (lowStock) {
            sql += " AND Inv.instock < Inv.lownum;";
        } else {
            sql += ";";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, storeName);
            ResultSet rs = stmt.executeQuery();

            // Determine the number of rows in the ResultSet
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }

            // Reset the ResultSet to the start
            rs = stmt.executeQuery();

            // Resize the data array to accommodate all the records
            data = new Object[rowCount][col.length];

            int currentRow = 0;
            while (rs.next()) {
                for (int i = 1; i <= col.length; i++) {
                    data[currentRow][i - 1] = rs.getString(i);
                }
                currentRow++;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            data = new Object[0][0]; // Return an empty array in case of error
        }

        return new JTable(data, col);
    }

    public JTable searchInventoryBySupplier(String supName, boolean lowStock) {
        Object[][] data;
        String[] col = { "ItemID", "Item", "Store", "Quantity", "Summary", "Supplier" };

        String sql = """
                SELECT itm.id, itm.name, S.name, Inv.instock, itm.summary, Sup.name
                FROM Inventory AS Inv
                JOIN Item AS itm ON Inv.itemid = itm.id
                JOIN Store AS S ON Inv.storeid = S.id
                JOIN Supplier AS Sup ON itm.supplierid = Sup.id
                WHERE Sup.name LIKE ? || '%'
                """;
        if (lowStock) {
            sql += " AND Inv.instock < Inv.lownum;";
        } else {
            sql += ";";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supName);
            ResultSet rs = stmt.executeQuery();

            // Determine the number of rows in the ResultSet
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }

            // Reset the ResultSet to the start
            rs = stmt.executeQuery();

            // Resize the data array to accommodate all the records
            data = new Object[rowCount][col.length];

            int currentRow = 0;
            while (rs.next()) {
                for (int i = 1; i <= col.length; i++) {
                    data[currentRow][i - 1] = rs.getString(i);
                }
                currentRow++;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            data = new Object[0][0]; // Return an empty array in case of error
        }

        return new JTable(data, col);
    }

    // -----------

    // ----------------------------------------------------

    public JTable allInventory(boolean lowStock) {
        String[] col = { "ItemID", "Item", "Store", "Quantity", "Summary", "Supplier" };
        Object[][] data = new Object[1][col.length];

        try {
            data = queryInventory(data, lowStock);
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }

        return new JTable(data, col);
    }

    private Object[][] queryInventory(Object[][] data, boolean lowStock) throws SQLException {
        // get inventory details from database
        String sql = """
                SELECT itm.id, itm.name, S.name, Inv.instock, itm.summary, Sup.name
                    FROM Inventory AS Inv
                    JOIN Item AS itm ON Inv.itemid = itm.id
                    JOIN Store AS S ON Inv.storeid = S.id
                    JOIN Supplier AS Sup ON itm.supplierid = Sup.id
                    """;

        if (lowStock) {
            sql += "WHERE Inv.instock < Inv.lownum;";
        } else {
            sql += ";";
        }

        rs = stmt.executeQuery(sql); // add inventory details to data

        // Determine the number of rows in the ResultSet
        int rowCount = 0;
        while (rs.next()) {
            rowCount++;
        }
        // Reset the ResultSet to the start
        rs = stmt.executeQuery(sql);
        // Resize the data array to accommodate all the records
        data = new Object[rowCount][COL_NAMES.length];
        int currentRow = 0;
        while (rs.next()) {
            for (int i = 1; i <= COL_NAMES.length; i++) {
                data[currentRow][i - 1] = rs.getString(i);
                System.out.println(i);
            }
            currentRow++;
        }

        return data;

    }

    public JTable viewSuppliers() {
        String[] col = { "Supplier ID", "Supplier Name", "Phone", "Email", "Contact Name", "Street", "Suburb", "State",
                "Postcode" };
        Object[][] data = {};
        try {
            data = querySupplier();
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
        return new JTable(data, col);
    }

    private Object[][] querySupplier() throws SQLException {
        // get inventory details from database
        rs = stmt.executeQuery("SELECT * FROM SUPPLIER;");

        // Determine the number of rows in the ResultSet
        int rowCount = 0;
        while (rs.next()) {
            rowCount++;
        }
        // Reset the ResultSet to the start
        rs = stmt.executeQuery("SELECT * FROM SUPPLIER;");
        // Resize the data array to accommodate all the records
        Object[][] data = new Object[rowCount][9];
        int currentRow = 0;
        while (rs.next()) {
            for (int i = 1; i <= 9; i++) {
                data[currentRow][i - 1] = rs.getString(i);
                System.out.println(i);
            }
            currentRow++;
        }

        return data;
    }

    /**
     * Add to stock based on RFID tag
     */
    public boolean scanIn(String RFID, int Quantity, String storeName) {
        try {
            // Get itemid from RFID
            PreparedStatement itemStatement = conn.prepareStatement("SELECT id FROM item WHERE rfid = ?");
            itemStatement.setString(1, RFID);
            ResultSet itemResult = itemStatement.executeQuery();

            int itemid;
            if (itemResult.next()) {
                itemid = itemResult.getInt(1);
            } else {
                // Handle case when item not found
                return false;
            }
            itemResult.close();
            itemStatement.close();

            // Get storeid from storeName
            PreparedStatement storeStatement = conn.prepareStatement("SELECT id FROM store WHERE name = ?");
            storeStatement.setString(1, storeName);
            ResultSet storeResult = storeStatement.executeQuery();

            int storeid;
            if (storeResult.next()) {
                storeid = storeResult.getInt(1);
            } else {
                // Handle case when store not found
                return false;
            }
            storeResult.close();
            storeStatement.close();

            // Add to stock
            PreparedStatement updateStatement = conn
                    .prepareStatement("UPDATE inventory SET instock = instock + ? WHERE itemid = ? AND storeid = ?");
            updateStatement.setInt(1, Quantity);
            updateStatement.setInt(2, itemid);
            updateStatement.setInt(3, storeid);
            updateStatement.executeUpdate();
            updateStatement.close();
            System.out.println("Scanned in Sucessfully");
            return true;
        } catch (SQLException e) {
            System.out.println("Error scanning in");
            System.err.print(e.getMessage());
            return false;
        }
    }

    /**
     * Remove from stock based on RDFID tag
     */
    public boolean scanOut(String RFID, int Quantity, String storeName) {
        try {
            // Get itemid from RFID
            PreparedStatement itemStatement = conn.prepareStatement("SELECT id FROM item WHERE rfid = ?");
            itemStatement.setString(1, RFID);
            ResultSet itemResult = itemStatement.executeQuery();

            int itemid;
            if (itemResult.next()) {
                itemid = itemResult.getInt(1);
            } else {
                // Handle case when item not found
                return false;
            }
            itemResult.close();
            itemStatement.close();

            // Get storeid from storeName
            PreparedStatement storeStatement = conn.prepareStatement("SELECT id FROM store WHERE name = ?");
            storeStatement.setString(1, storeName);
            ResultSet storeResult = storeStatement.executeQuery();

            int storeid;
            if (storeResult.next()) {
                storeid = storeResult.getInt(1);
            } else {
                // Handle case when store not found
                return false;
            }
            storeResult.close();
            storeStatement.close();

            // Check if instock is 0 or greater
            PreparedStatement checkStatement = conn
                    .prepareStatement("SELECT instock FROM inventory WHERE itemid = ? AND storeid = ?");
            checkStatement.setInt(1, itemid);
            checkStatement.setInt(2, storeid);
            ResultSet checkResult = checkStatement.executeQuery();

            if (checkResult.next()) {
                int instock = checkResult.getInt(1);
                if (instock >= Quantity) {
                    // Subtract from stock
                    PreparedStatement updateStatement = conn.prepareStatement(
                            "UPDATE inventory SET instock = instock - ? WHERE itemid = ? AND storeid = ?");
                    updateStatement.setInt(1, Quantity);
                    updateStatement.setInt(2, itemid);
                    updateStatement.setInt(3, storeid);
                    updateStatement.executeUpdate();
                    updateStatement.close();

                    System.out.println("Scanned out successfully");
                    return true;
                } else {
                    System.out.println("Not enough quantity in stock");
                    return false;
                }
            } else {
                System.out.println("Inventory record not found");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error scanning out");
            System.err.print(e.getMessage());
            return false;
        }
    }

    // ! Old methods
    // ----------------------------------------------------------------------------------------------------

    /**
     * Edit the information for the student with the given id in the database, any
     * parameters which
     * are null are not changed
     * 
     * @param id
     * @param firstName
     * @param lastName
     * @param dob
     * @param yearGroup
     * @param contactDetails
     * @return true if the edit was successful, false otherwise
     */
    public void editStudent(int id, String firstName, String lastName, String dob, String yearGroup,
            String contactDetails) {
        // initialise str query
        String str = "UPDATE STUDENTS SET ";
        boolean first = true;

        // add firstName update
        if (firstName != null) {
            str += "firstname=\'" + firstName + "\'";
            first = false;
        }

        // add lastName update
        if (lastName != null) {
            if (!first)
                str += ", ";
            str += "lastname=\'" + lastName + "\'";
            first = false;
        }

        // add dob update
        if (dob != null) {
            if (!first)
                str += ", ";
            str += "dob=\'" + dob + "\'";
            first = false;
        }

        // add yearGroup update
        if (yearGroup != null) {
            if (!first)
                str += ", ";
            str += "yearGroup=\'" + yearGroup + "\'";
            first = false;
        }

        // add contactDetails update
        if (contactDetails != null) {
            if (!first)
                str += ", ";
            str += "contactDetails=\'" + contactDetails + "\'";
        }

        try {
            stmt.executeUpdate(str + " WHERE id=" + id + ";");

        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * Deletes the student with the specified id from the database
     * 
     * @param id
     * @return true if the deletion was successful, false otherwise
     */
    public void deleteStudent(int id) {
        try {
            stmt.executeUpdate("DELETE FROM STUDENTS WHERE id=" + id + "\';");

        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
    }

}