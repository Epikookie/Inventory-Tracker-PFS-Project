
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Security {
    public static void main(String[] args) {
        System.out.println("Hello Security World");

    }

    /**
     * Generate a SHA-256 hash of the input string
     * 
     * @param input
     * @return
     */
    public String generateSHA256Hash(String input) {
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
    public String generateSalt() {
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

    public Security() {
        System.out.println("Hello Security World");
    }

    public void Bruteforce() {
        System.out.println("Hello Bruteforce World");
        // TODO: Implement Bruteforce method
        // check that we dont too many attemps
    }

}
