import java.sql.SQLException;

public class API {

    public static AppFunc func;

    public static void main(String[] args) {
        func = new AppFunc();
        try {
            func.initialiseDatabase(true);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        new Login();
    }

}
