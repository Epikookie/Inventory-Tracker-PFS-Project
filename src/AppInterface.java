public class AppInterface {

    public static AppFunctions func;

    public static void main(String[] args) {
        func = new AppFunctions();
        func.initialiseDatabase(true);
        func.populateStaff();
        func.populateSupplier();
        func.populateStore();
        func.populateItems();

        // Add 10 bananas to inventory in store 3 Knives Out and set low stock to 3
        // bananas
        func.addInventory(1, 1, 5, 3);
        func.addInventory(1, 1, 5, 3);
        func.addInventory(2, 3, 10, 6);
        func.addInventory(3, 5, 15, 9);
        func.addInventory(4, 4, 5, 2);
        func.addInventory(5, 2, 15, 2);
        func.addInventory(6, 1, 5, 9);
        func.addInventory(7, 2, 15, 9);
        func.addInventory(7, 3, 15, 9);
        func.addInventory(8, 4, 15, 9);
        func.addInventory(8, 3, 1, 3);
        func.addInventory(9, 1, 15, 5);
        func.addInventory(10, 5, 1, 9);
        func.addInventory(11, 5, 15, 9);
        func.addInventory(12, 2, 5, 9);
        func.addInventory(13, 1, 1, 9);

        new Login(func);
    }

}
