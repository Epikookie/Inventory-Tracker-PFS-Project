import java.sql.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

// used for password criteria checking
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Security {
    /**
     * Run main commmand
     * 
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Hello Security World");

        // Test the password validator and timing attack prevention
        testValidPassword();
        preventTimingAttack();
        testValidPassword();
    }

    /**
     * Generate a SHA-1 hash of item primary key for RFID
     * 
     * @param input
     * @return
     */
    public static String generateSHA1Hash(int inputInt) {
        try {
            String input = Integer.toString(inputInt);

            // Create MessageDigest instance for SHA-1
            MessageDigest digest = MessageDigest.getInstance("SHA-1");

            // Apply SHA-1 hash to the input string
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle exception if SHA-1 algorithm is not available
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generate a SHA-256 hash of the input string
     * 
     * @param input
     * @return
     */
    public static String generateSHA256Hash(String input) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Apply SHA-256 hash to the input string
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle exception if SHA-256 algorithm is not available
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generate a random 4 character salt to be used for password hashing
     * 
     * @return 4 character string
     */
    public static String generateSalt() {
        String SALT_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder salt = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            int randomIndex = random.nextInt(SALT_CHARACTERS.length());
            char randomChar = SALT_CHARACTERS.charAt(randomIndex);
            salt.append(randomChar);
        }

        return salt.toString();
    }

    /**
     * Check login credentials. Returns true if correct username and password
     * entered. False otherwise.
     * 
     * @param staffID
     * @param password
     * @return true if correct username and password entered. False otherwise.
     */
    public static boolean validStaffID(String staffID, Statement stmt) {
        int staffIDint;

        // Assert staffID is numerical
        try {
            staffIDint = Integer.parseInt(staffID);
        } catch (NumberFormatException e) {
            System.err.println("Staff ID must be numerical. Entered: " + staffID);
            return false;
        }

        // Assert staffID is positive integer
        if (staffIDint <= 0) {
            System.err.println("Staff ID must be positive integer. Entered: " + staffID);
            return false;
        }

        return true;

    }

    /**
     * Uses Regex expressions to verify that password is 12 characters minimum and
     * contains at least one:
     * <ul>
     * <li>lower-case letter
     * <li>upper-case letter
     * <li>number
     * <li>special character
     * </ul>
     * 
     * Returns true if password meets criteria, false otherwise
     * 
     * Regex expression is explained as follows:
     * (?=.*[a-z]): At least one lowercase letter
     * (?=.*[A-Z]): At least one uppercase letter
     * (?=.*\d): At least one digit
     * (?=.*[@#$%^&+=!]): At least one special character (e.g., @, #, $, %, ^, &, +,
     * * =, !)
     * (?=\S+$): No whitespace allowed
     * .{12,}: Minimum of 12 characters
     * 
     * * Credit to ChatGPT for helping with the Regex compilation
     * 
     * @param password
     * @return boolean
     */
    public static boolean validPassword(String password) {
        // Minimum 12 characters, at least 1 lowercase letter, 1 uppercase letter, 1
        // number, and 1 special character
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{12,}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    /**
     * Get password hash and salt from the database
     * 
     * @param stmt
     * @param staffID
     * @return String array containing password hash and salt in order
     */
    public static String[] getHashAndSalt(int staffID, Statement stmt) throws SQLException {
        String[] passAndSalt = new String[2];
        String sql = "SELECT passhash, salt FROM staff WHERE id = '" + staffID + "'";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String hash = rs.getString("passhash");
            String salt = rs.getString("salt");
            passAndSalt[0] = hash;
            passAndSalt[1] = salt;
            // System.out.println("Passhash: " + hash + "\nSalt: " + salt); // Used for
            // debugging
        }

        return passAndSalt;
    }

    /**
     * Check if the salted password matches the hash
     * 
     * @param password
     * @param salt
     * @param hash
     * @return true if password matches hash, false otherwise
     */
    public static boolean correctPassword(String password, String salt, String hash) {
        Security.preventTimingAttack();
        String hashedPassword = Security.generateSHA256Hash(password + salt);
        return hashedPassword.equals(hash);
    }

    /** Test that valid password function works */
    public static void testValidPassword() {
        String password1 = "abcdefghiJ1!"; // Valid password
        String password2 = "abcdefgh!J1"; // Invalid password - less than 12 characters
        String password3 = "abcdefghiJJ!"; // Invalid password - missing number
        String password4 = "abcdefghij1!"; // Invalid password - missing upper-case letter
        String password5 = "ABCDEFGHIJ1!"; // Invalid password - missing lower-case letter
        String password6 = "abcdefghiJ12"; // Invalid password - missing special letter

        System.out.println("Password 1: " + password1 + " , should be true: " + validPassword(password1));
        System.out.println("Password 2: " + password2 + " , should be false: " + validPassword(password2));
        System.out.println("Password 3: " + password3 + " , should be false: " + validPassword(password3));
        System.out.println("Password 4: " + password4 + " , should be false: " + validPassword(password4));
        System.out.println("Password 5: " + password5 + " , should be false: " + validPassword(password5));
        System.out.println("Password 6: " + password6 + " , should be false: " + validPassword(password6));

        return;
    }

    /**
     * Prevent timing attack by sleeping for a random amount of time
     *
     */
    public static void preventTimingAttack() {
        SecureRandom random = new SecureRandom();
        int randomInt = random.nextInt(1000);
        try {
            Thread.sleep(randomInt); // Sleep for up to 1000 milliseconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void Bruteforce() {
        System.out.println("Hello Bruteforce World");
        // TODO: Implement Bruteforce method
        // TODO: BEN to comment on if we need this
        // check that we dont too many attemps
    }

    /**
     * Make sure the server times out after a while after running a query or if no
     * new commands are issued
     */
    public static void preventInfiniteLoop() {
        // TODO: Implement preventInfiniteLoo method
        // TODO: BEN to comment on if we need this
        System.out.println("Hello Infinite Loop World");
    }

    /**
     * Limit amount of user requests to sever within a certain time frame
     */
    public static void preventDDOS() {
        // TODO: Implement preventDDOS method
        // TODO: BEN to comment on if we need this
        System.out.println("Hello DDOS World");
    }

}
