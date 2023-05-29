import java.sql.*;
import javax.swing.*;
import java.util.ArrayList;

public class AppFunc {

    // database connection variables
    Connection conn = null;
    Statement stmt;
    ResultSet rs;

    // ----------------------------------------------------------------------------------------------------
    // CONSTRUCTOR
    // ----------------------------------------------------------------------------------------------------

    /**
     * Constructs a new AppFunctions object
     */
    public AppFunc() {
        initialiseConnection();
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
    public synchronized boolean initialiseDatabase(boolean overwrite) throws SQLException {
        // if overwriting existing tables
        if (overwrite) {
            // drop tables if they already exist
            // stmt.executeUpdate("DROP TABLE IF EXISTS TEACHERS;\n");
        }

        // tables were successfully created
        return true;
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * Close the connection to the database if it exists
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
    // GENERIC DATABASE METHODS - PRIVATE
    // ----------------------------------------------------------------------------------------------------

    /**
     * Initialises the connection to the database if it hasn't already been done
     */
    private void initialiseConnection() {
        // check if connection already exists
        if (conn == null) {
            try {
                // load JDBC driver class
                Class.forName("org.sqlite.JDBC");
                // create database connection
                String dbPath = System.getProperty("user.dir") + "/lib/glitchtracking.db";
                conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath, null, null);
                stmt = conn.createStatement();
                System.out.println("Connection successful");
            } catch (SQLException | ClassNotFoundException e) {
                // database connection failed
                System.out.println("Connection failed");
                System.err.println(e.getMessage());
            }
        }

    }

    public JTable allInventory() {
        String[] col = { "itemid", "storeid", "quantity", "instock", "lownum" };
        Object[][] data = new Object[1][5];

        try {
            data = queryInventory(data, 0);
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }

        return new JTable(data, col);
    }

    private Object[][] queryInventory(Object[][] data, Integer row) throws SQLException {
        // get inventory details from database
        rs = stmt.executeQuery("SELECT * FROM INVENTORY;"); // add inventory details to data
        rs.next();
        data[row][0] = rs.getInt(1);
        for (int i = 1; i <= 4; i++) {
            data[row][i] = rs.getString(i + 1);
            System.out.println(i);
        }
        return data;
    }

    private Object[][] querySupplier(Object[][] data, Integer row) throws SQLException {
        // get inventory details from database
        rs = stmt.executeQuery("SELECT * FROM SUPPLIER;"); // add inventory details to data
        rs.next();
        data[row][0] = rs.getInt(1);
        for (int i = 1; i <= 4; i++) {
            data[row][i] = rs.getString(i + 1);
            System.out.println(i);
        }
        return data;
    }

}