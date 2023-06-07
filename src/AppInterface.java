public class AppInterface {

    public static AppFunctions func;

    /**
     * Main method
     * 
     * @param args
     */
    public static void main(String[] args) {
        func = new AppFunctions();
        func.initialiseDatabase(true); // drops and recreates tables if true
        func.populateStaff();
        func.populateSupplier();
        func.populateStore();
        func.populateItems();

        // Add some inventory
        func.addInventory(1, 1, 5, 3);
        func.addInventory(1, 1, 5, 3); // should fail as already exists
        func.resetInventory(1, 1, 10, 2);
        func.adjustInventory(1, 1, 10);
        func.adjustInventory(1, 1, -21); // should fail as not enough stock
        func.adjustInventory(1, 1, -20);
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
        func.addInventory(15, 3, 0, 5);
        func.addInventory(16, 2, 20, 10);
        func.addInventory(22, 1, 40, 10);
        func.addInventory(22, 4, 6, 5);
        func.addInventory(21, 1, 77, 7);
        func.addInventory(23, 1, 79, 25);
        func.addInventory(24, 3, 4, 5);
        func.addInventory(25, 5, 804, 100);
        func.addInventory(26, 2, 60, 9);
        func.addInventory(26, 5, 15, 20);

        new Login(func);
    }

}
