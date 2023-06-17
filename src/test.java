import java.util.ArrayList;
import java.util.HashSet;
import java.util.Arrays;

public class test {

    public static void main(String[] args) {

        ArrayList<Object[]> currentRows = new ArrayList<Object[]>();
        currentRows.add(new Object[] { "1", "2", "3", "4", "5", "6", "7", "8" });
        var newer = new Object[] { "1", "2", "3", "4", "5", "6", "7", "8" };
        System.out.println(currentRows.contains(newer));

        HashSet<Object[]> hashRows = new HashSet<Object[]>();
        hashRows.add(new Object[] { "1", "2", "3", "4", "5", "6", "7", "8" });
        var hashnew = new Object[] { "1", "2", "3", "4", "5", "6", "7", "8" };
        System.out.println(hashRows.contains(hashnew));

        System.out.println(Arrays.equals(newer, hashnew));
        System.out.println(Arrays.equals(newer, currentRows.get(0)));
        System.out.println(newer.equals(currentRows.get(0)));

        int quantity = 100;
        int lownum = Math.floorDiv(quantity, 10);
        System.out.println(lownum);

    }

}
