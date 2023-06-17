import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

public class MainWindow implements ActionListener {
  private JFrame frame;
  private JPanel panel;
  private JButton scanInOutButton;
  private JButton signOutButton;
  private JTextField itemField;
  private JTextField storeField;
  private JTextField supplierField;
  private JButton searchButton;
  private JToggleButton toggleButton;
  private JScrollPane tableScrollPane;
  private JTable table;
  private GridBagConstraints gbc = new GridBagConstraints();
  public AppFunctions func = new AppFunctions();

  public MainWindow(AppFunctions func) {
    this.func = func;
    createWindow();
    addComponents();
    frame.setVisible(true);
  }

  private void createWindow() {
    frame = new JFrame("GUI Window");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 400);
    frame.setLocationRelativeTo(null);

    panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    frame.add(panel);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String Operation = e.getActionCommand();
    switch (Operation) {
      case "Scan In/Out":
        System.out.println(Operation);
        new ScanInOut(func);
        frame.dispose();
        break;
      case "Sign Out":
        signOut();
        break;
      case "Search":
        search();
        break;
    }
  }

  /**
   * Search for inventory items based on user inputs
   */
  public void search() {
    System.out.println("Search");
    JTable resultTable;
    resultTable = runQuery();
    refreshView(resultTable);
  }

  /**
   * Sign out of the application
   *
   * @return
   */
  public void signOut() {
    System.out.println("Sign Out");
    new Login(func);
    frame.dispose();
  }

  /**
   * Returns a JTable containing the results of the search query
   * 
   * @return
   */
  private JTable runQuery() {
    String itemString = itemField.getText();
    String storeString = storeField.getText();
    String supplierString = supplierField.getText();
    boolean lowStock = toggleButton.isSelected();

    // If no search parameters are entered, return all inventory
    if (itemString.isBlank() && storeString.isBlank() && supplierString.isBlank()) {
      return func.allInventory(lowStock);
    }

    ArrayList<JTable> results = new ArrayList<JTable>();

    // Gather the JTable results of each search field individually
    if (!itemString.isBlank()) {
      results.add(func.searchInventoryByItem(itemString, lowStock));
    }
    if (!storeString.isBlank()) {
      results.add(func.searchInventoryByStore(storeString, lowStock));
    }
    if (!supplierString.isBlank()) {
      results.add(func.searchInventoryBySupplier(supplierString, lowStock));
    }

    if (results.size() == 1) {
      return results.get(0);
    } else {
      return getCommonRows(results);
    }
  }

  /**
   * Accepts an ArrayList of JTables and returns a JTable containing the common
   * rows of all tables
   * 
   * @return
   */
  private JTable getCommonRows(ArrayList<JTable> tables) {

    String[] col = { "ItemID", "Item", "Store", "Quantity", "Summary", "Supplier" };

    // ArrayList of ArrayLists to keep track of common rows
    ArrayList<ArrayList<Object[]>> results = new ArrayList<ArrayList<Object[]>>(tables.size());

    for (int i = 0; i < tables.size(); i++) {
      JTable currentTable = tables.get(i);

      // Create a ArrayList to add every row's data from the current table
      ArrayList<Object[]> currentRows = new ArrayList<Object[]>();

      /*
       * Add each row of currentTable to the currentRows ArrayList<Object[]>
       * If it's the first JTable, add them all
       * If it's not the first JTable, only add the rows that are in the previous
       * This has a cascading effect, so the final ArrayList will only contain
       * rows found in every JTable checked
       */
      for (int j = 0; j < currentTable.getRowCount(); j++) {
        Object[] row = new Object[currentTable.getColumnCount()];
        for (int k = 0; k < currentTable.getColumnCount(); k++) {
          row[k] = currentTable.getValueAt(j, k);
        }

        // Always add first JTable data to the HashSet array
        if (i == 0) {
          currentRows.add(row);
        } else {

          // If the current row exists in the previous ArrayList,
          // add it to the current ArrayList (because it's a common row)
          ArrayList<Object[]> previousRows = results.get(i - 1);
          if (rowInArray(previousRows, row)) {
            currentRows.add(row);
          }
        }

      }

      results.add(currentRows);
    }

    // Convert the final HashSet<Object[]> to an Object[][]
    Object[][] commonRows = results.get(tables.size() - 1).toArray(new Object[0][]);

    return new JTable(commonRows, col);

  }

  /**
   * Returns true if the row is in the array
   * 
   * @param array
   * @param row
   * @return boolean
   */
  private boolean rowInArray(ArrayList<Object[]> array, Object[] row) {
    for (int i = 0; i < array.size(); i++) {
      if (Arrays.equals(array.get(i), row)) {
        return true;
      }
    }
    return false;
  }

  private void refreshView(JTable resultTable) {
    int rowCount = resultTable.getRowCount();
    System.out.println("Number of rows: " + rowCount);
    tableScrollPane.setViewportView(resultTable);
    tableScrollPane.repaint();
    tableScrollPane.revalidate();
  }

  private void addComponents() {
    gbc.insets = new Insets(5, 5, 5, 5);

    // First Row
    scanInOutButton = new JButton("Scan In/Out");
    scanInOutButton.addActionListener(this);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    panel.add(scanInOutButton, gbc);

    signOutButton = new JButton("Sign Out");
    signOutButton.addActionListener(this);

    gbc.gridx = 4;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.EAST;
    panel.add(signOutButton, gbc);

    // Second Row
    JLabel label1 = new JLabel("Item Name:");
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;

    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    panel.add(label1, gbc);

    itemField = new JTextField(50);
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 2;

    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(itemField, gbc);

    JLabel label2 = new JLabel("Store Name:");
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;

    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(label2, gbc);

    storeField = new JTextField(50);
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 2;

    gbc.anchor = GridBagConstraints.WEST;
    panel.add(storeField, gbc);

    JLabel label3 = new JLabel("Supplier Name:");
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;

    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(label3, gbc);

    supplierField = new JTextField(50);
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 2;

    gbc.anchor = GridBagConstraints.WEST;
    panel.add(supplierField, gbc);

    searchButton = new JButton("Search");
    searchButton.addActionListener(this);

    // Create a common KeyAdapter for text fields
    KeyAdapter enterKeyListener = new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          search();
        }
      }
    };

    // Add the KeyAdapter to the text fields
    itemField.addKeyListener(enterKeyListener);
    storeField.addKeyListener(enterKeyListener);
    supplierField.addKeyListener(enterKeyListener);

    gbc.gridx = 4;
    gbc.gridy = 1;
    gbc.gridheight = 3;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel.add(searchButton, gbc);

    // Final Row
    toggleButton = new JCheckBox("Show only Low Stock");
    toggleButton.addActionListener(this);
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    panel.add(toggleButton, gbc);

    // Table
    tableScrollPane = new JScrollPane(table);
    gbc.gridx = 0;
    gbc.gridy = 10;
    gbc.gridwidth = 3;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.fill = GridBagConstraints.BOTH;
    panel.add(tableScrollPane, gbc);
  }

  public static void main(String[] args) {
    AppFunctions func = new AppFunctions();
    SwingUtilities.invokeLater(() -> {
      new MainWindow(func);
    });

  }
}
