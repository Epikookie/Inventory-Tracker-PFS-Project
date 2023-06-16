import java.util.ArrayList;

public class test {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        String database = "database.db";
        System.out.println("Database: " + database);

        ArrayList<String> tables = new ArrayList<String>();

        tables.add("Staff");
        tables.add("Inventory");
        tables.add("Store");
        tables.add("Supplier");

        for (String table : tables) {
            System.out.println(table);

        }
    }

}
