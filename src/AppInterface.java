public class AppInterface {

    public static AppFunctions func;

    public static void main(String[] args) {
        func = new AppFunctions();
        func.initialiseDatabase(true);
        // Security security = new Security();
        func.populateStaff();
        func.populateSupplier();
        func.populateStore();

        // Add Items to Database
        func.addItem(2, "Apple", "This is just an ordinary apple.");
        func.addItem(4, "Banana", "This is just an ordinary banana.");
        func.addItem(8, "Cherry", "This is no ordinary cherry.");

        // Add 10 bananas to inventory in store 3 Knives Out and set low stock to 3
        // bananas
        func.addInventory(2, 3, 10, 3);

        // func.addClass(1, "23/05/2022", null, 1, null);

        new Login(func);
    }

}
