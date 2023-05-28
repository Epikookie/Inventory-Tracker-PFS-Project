public class AppInterface {

    public static AppFunctions func;

    public static void main(String[] args) {
        func = new AppFunctions();
        func.initialiseDatabase(true);
        func.addSupplier("ABC Store");

        // func.addClass(1, "23/05/2022", null, 1, null);

        // new Login();
    }

}
