import java.sql.*;
import javax.swing.*;
import java.util.ArrayList;

public class AppFunctions {

    // database connection variables
    Connection conn = null;
    Statement stmt;
    ResultSet rs;
    String databaseName = "glitchtracking"; // local database file name

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
                stmt.executeUpdate("DROP TABLE IF EXISTS inventory;\n" +
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
                    state varchar(3) DEFAULT NULL,
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
                        state varchar(3) DEFAULT NULL,
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
                      state varchar(3) DEFAULT NULL,
                      postcode varchar(10) DEFAULT NULL,
                      passhash varchar(300) DEFAULT NULL,
                      rfid varchar(300) DEFAULT NULL)
                    """);

            // Batch item table creation
            stmt.addBatch(
                    """
                            CREATE TABLE IF NOT EXISTS item (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            supplierid int NOT NULL,
                            name varchar(45) NOT NULL,
                            summary tinytext,
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

            stmt.executeBatch();

            System.out.println("Tables created");

        } catch (Exception e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }

    }

    /**
     * Adds a supplier
     * 
     * @param supplierName
     */
    public void addSupplier(String supplierName) {
        try {
            stmt.executeUpdate("INSERT INTO supplier(name) " +
                    "VALUES (\'" + supplierName + "\');");
            System.out.println("Supplier " + supplierName + " added");

        } catch (SQLException e) {
            System.err.print(e.getMessage());
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

    // !Old queries
    // ----------------------------------------------------------------------------------------------------

    /**
     * Adds a new student to the database with the appropriate details (including
     * group)
     * 
     * @param firstName
     * @param lastName
     * @param dob
     * @param contactDetails
     * @param group
     */
    public void addStudent(String firstName, String lastName, String dob, String yearGroup, String contactDetails) {
        try {
            stmt.executeUpdate("INSERT INTO STUDENTS(firstName, lastName, dob, yearGroup, contactDetails)" +
                    "VALUES(\'" + firstName + "\', \'" + lastName + "\', \'" + dob + "\', \'" +
                    yearGroup + "\', \'" + contactDetails + "\');");

        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * Searches for student with provided id and returns their personal details and
     * achievements if
     * they exist
     * 
     * @param id the students unique id in the database
     * @return JTable of personal details and achievements
     */
    public JTable searchStudent(int id) {
        // initialise return objects
        String[] col = { "ID", "First Name", "Last Name", "DOB", "Year Group", "Contact Details", "Badges", "Tests",
                "Topics" };
        Object[][] data = new Object[1][9];

        try {
            // add student details and achievements to data
            data = queryStudentDetails(data, id, 0);
            data = queryStudentBadges(data, id, 0);
            data = queryStudentTestsNTopics(data, id, 0);
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }

        // return student information
        return new JTable(data, col);
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * Searches for student with provided name and returns their personal details
     * and achievements if
     * they exist. At least one of the parameters must not be null
     * 
     * @param firstName the students first name in the database
     * @param lastName  the students last name in the database
     * @return JTable of personal details and achievements
     */
    public JTable searchStudent(String firstName, String lastName) {
        // initialise return objects
        String[] col = { "ID", "First Name", "Last Name", "DOB", "Year Group", "Contact Details", "Badges", "Tests",
                "Topics" };
        Object[][] data = {};

        try {
            // get student ids
            if (firstName != null && lastName != null) {
                rs = stmt.executeQuery("SELECT id FROM STUDENTS WHERE firstName=\'" + firstName + "\' AND lastname=\'"
                        + lastName + "\';");
            } else {
                if (firstName == null) {
                    rs = stmt.executeQuery("SELECT id FROM STUDENTS WHERE lastName=\'" + lastName + "\';");
                } else {
                    rs = stmt.executeQuery("SELECT id FROM STUDENTS WHERE firstName=\'" + firstName + "\';");
                }
            }

            // add student ids to temporary list
            ArrayList<Integer> studentIds = new ArrayList<Integer>();
            while (rs.next()) {
                studentIds.add(rs.getInt(1));
            }

            // correct size of data matrix
            data = new Object[studentIds.size()][9];

            // add student details and achievements to data
            for (int i = 0; i < studentIds.size(); i++) {
                data = queryStudentDetails(data, studentIds.get(i), i);
                data = queryStudentBadges(data, studentIds.get(i), i);
                data = queryStudentTestsNTopics(data, studentIds.get(i), i);
            }

        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }

        // return student information
        return new JTable(data, col);
    }

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

    // ----------------------------------------------------------------------------------------------------

    /**
     * Enrol the student with the specified student id into a microcredential with
     * the specified
     * badge. The elected tests and associated topics are specified inthe
     * electedTests and
     * associatedTopics paramters respectively.
     * 
     * @param studentId
     * @param badgeId
     * @param electedTests     an Integer[] with a size of 3, contains the id's for
     *                         each elected test
     * @param associatedTopics an Integer[][] with 3 rows and 3 columns, contains
     *                         the id's for each
     *                         associated topic such that associatedTopics[1][2]
     *                         will refer to the third associated topic of
     *                         the second elected test, some may contain null values
     */
    public void enrolMicroCredential(int studentId, int badgeId, int[] electedTests, int[][] associatedTopics) {
        try {
            stmt.executeUpdate(
                    "INSERT INTO MICROCREDENTIALS(student, badge, electiveTest1, electiveTest2, electiveTest3, " +
                            "test1top1, test1top2, test1top3, test2top1, test2top2, test2top3, test3top1, test3top2, test3top3)"
                            +
                            "VALUES(\'" + studentId + "\', \'" + badgeId + "\', \'" +
                            electedTests[0] + "\', \'" + electedTests[1] + "\', \'" + electedTests[2] + "\', \'" +
                            associatedTopics[0][0] + "\', \'" + associatedTopics[0][1] + "\', \'"
                            + associatedTopics[0][2] + "\', \'" +
                            associatedTopics[1][0] + "\', \'" + associatedTopics[1][1] + "\', \'"
                            + associatedTopics[1][2] + "\', \'" +
                            associatedTopics[2][0] + "\', \'" + associatedTopics[2][1] + "\', \'"
                            + associatedTopics[2][2] + "\');");

        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
    }

    // ----------------------------------------------------------------------------------------------------
    // STUDENT METHODS - PRIVATE
    // ----------------------------------------------------------------------------------------------------

    /**
     * Adds student details from database to the specified row of the data parameter
     * and returns it
     * 
     * @param data
     * @param studentId
     * @param row
     * @return updated data parameter
     * @throws SQLException
     */
    private Object[][] queryStudentDetails(Object[][] data, Integer studentId, Integer row) throws SQLException {
        // get student details from database
        rs = stmt.executeQuery("SELECT * FROM STUDENTS WHERE id=" + studentId + ";");

        // add student details to data
        rs.next();
        data[row][0] = rs.getInt(1);
        for (int i = 1; i <= 5; i++) {
            data[row][i] = rs.getString(i + 1);
        }

        return data;
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * Adds student badges from database to the specified row of the data parameter
     * and returns it
     * 
     * @param data
     * @param studentId
     * @param row
     * @return updated data parameter
     * @throws SQLException
     */
    private Object[][] queryStudentBadges(Object[][] data, Integer studentId, Integer row) throws SQLException {
        // get student badges from database
        rs = stmt.executeQuery("SELECT badge FROM MICROCREDENTIALS WHERE student=" + studentId + ";");

        // add student badges to data
        data[row][6] = "";
        boolean first = true;
        while (rs.next()) {
            if (first) {
                data[row][6] += String.valueOf(rs.getInt(1));
                first = false;
            } else {
                data[row][6] += ", " + rs.getInt(1);
            }
        }

        return data;
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * Adds student tests and topics from database to the specified row of the data
     * parameter and returns it
     * 
     * @param data
     * @param studentId
     * @param row
     * @return updated data parameter
     * @throws SQLException
     */
    private Object[][] queryStudentTestsNTopics(Object[][] data, Integer studentId, Integer row) throws SQLException {
        // get student classes attended from database
        rs = stmt.executeQuery("SELECT class FROM ATTENDANCE WHERE student=" + studentId + ";");

        // add student classes attended to temp list
        ArrayList<Integer> attendedClasses = new ArrayList<Integer>();
        while (rs.next()) {
            attendedClasses.add(rs.getInt(1));
        }

        // get students tests and topics from database
        for (Integer classId : attendedClasses) {
            rs = stmt.executeQuery("SELECT test, topic FROM CLASSES WHERE id=" + classId + ";");
            boolean testFirst = true;
            boolean topicFirst = true;
            while (rs.next()) {
                if (testFirst) {
                    data[row][7] += String.valueOf(rs.getInt(1));
                    testFirst = false;
                } else {
                    data[row][7] += ", " + rs.getInt(1);
                }

                if (topicFirst) {
                    data[row][8] += String.valueOf(rs.getInt(2));
                    topicFirst = false;
                } else {
                    data[row][8] += ", " + rs.getInt(2);
                }
            }
        }

        return data;
    }

    // ----------------------------------------------------------------------------------------------------
    // CLASS METHODS - PUBLIC
    // ----------------------------------------------------------------------------------------------------

    /**
     * Adds a new class to the database with the corresponding details, only one of
     * the parameters
     * test, topic, and part should not be null or all of the parameters should be
     * null
     * 
     * @param teacherId
     * @param date
     * @param test
     * @param topic
     * @param part
     */
    public void addClass(Integer teacherId, String date, Integer test, Integer topic, Integer part) {
        try {
            stmt.executeUpdate("INSERT INTO CLASSES(teacher, date, test, topic, part)" +
                    "VALUES(\'" + teacherId + "\', \'" + date + "\', \'" + test + "\', \'" +
                    topic + "\', \'" + part + "\');");

        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * Searches the database for the class with the provided id and returns the
     * details of that
     * class
     * 
     * @param classId
     * @return JTable containing details of class
     */
    public JTable searchClass(Integer classId) {
        // initialise return objects
        String[] col = { "ID", "Teacher ID", "Date", "Test ID", "Topic ID", "Part ID" };
        Object[][] data = new Object[1][6];

        try {
            // add class details to data
            data = queryClassDetails(data, classId, 0);
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }

        // return class information
        return new JTable(data, col);
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * Searches the database for classes on the provided date and returns the
     * details of those
     * classes
     * 
     * @param date
     * @return JTable containing details of class
     */
    public JTable searchClass(String date) {
        // initialise return objects
        String[] col = { "ID", "Teacher ID", "Date", "Test ID", "Topic ID", "Part ID" };
        Object[][] data = {};

        try {
            // get class ids
            rs = stmt.executeQuery("SELECT id FROM CLASSES WHERE date=\'" + date + "\';");

            // add class ids to temporary list
            ArrayList<Integer> classIds = new ArrayList<Integer>();
            while (rs.next()) {
                classIds.add(rs.getInt(1));
            }

            // correct size of data matrix
            data = new Object[classIds.size()][6];

            // add class details and achievements to data
            for (int i = 0; i < classIds.size(); i++) {
                data = queryClassDetails(data, classIds.get(i), i);
            }

        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }

        // return class information
        return new JTable(data, col);
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * Record a student's attendance at at class and the grade they recieved if a
     * test was done during
     * that class. testGrade parameter may be null
     * 
     * @param classId
     * @param studentId
     * @param testGrade
     * @param true      if the attendance was successfully recorded, false otherwise
     */
    public void recordAttendance(Integer classId, Integer studentId, String testGrade) {
        try {
            stmt.executeUpdate("INSERT INTO ATTENDANCE(class, student, testGrade)" +
                    "VALUES(\'" + classId + "\', \'" + studentId + "\', \'" + testGrade + "\');");

        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * Checks the database to see what class would be most efficient for students to
     * complete next
     * badge
     * ! Not implemented
     * 
     * @return JTable of classes with appropriate test, topic, or part
     */
    public JTable recommendClasses() {
        return null;
    }

    // ----------------------------------------------------------------------------------------------------
    // CLASS METHODS - PRIVATE
    // ----------------------------------------------------------------------------------------------------

    /**
     * Adds class details from database to the specified row of the data parameter
     * and returns it
     * 
     * @param data
     * @param classId
     * @param row
     * @return updated data parameter
     * @throws SQLException
     */
    private Object[][] queryClassDetails(Object[][] data, Integer classId, Integer row) throws SQLException {
        // get class details from database
        rs = stmt.executeQuery("SELECT * FROM CLASSES WHERE id=" + classId + ";");

        // add class details to data
        rs.next();
        for (int i = 0; i <= 5; i++) {
            data[row][i] = rs.getInt(i);
        }

        return data;
    }

    // ----------------------------------------------------------------------------------------------------
    // ACHIEVEMENT METHODS - PUBLIC
    // ----------------------------------------------------------------------------------------------------

    /**
     * Checks for appropriate updates to all student's achievements, returns a null
     * string if there are
     * no updates, otherwise returns a string explaining the update. There should
     * only be one update
     * between calls of this method
     * ! Not implemented
     * 
     * @return Null or string explaining update
     */
    public String updateAchievements() {
        return null;
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * Returns a leaderboard of students based on their achievements, table shows
     * student's id, name,
     * and what badges they have earned
     * ! Not implemented
     * 
     * @return JTable of leaderboard
     */
    public JTable viewLeaderboard() {
        return null;
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * Returns a schedule of class dates and teachers who are scheduled to teach on
     * that day.
     * ! Not implemented
     * 
     * @return JTable of schedule
     */

    public JTable viewSchedule() {
        return null;
    }

}