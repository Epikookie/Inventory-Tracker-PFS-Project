import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

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
  private AppFunctions func = new AppFunctions();

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
        System.out.println(Operation);
        new Login(func);
        frame.dispose();
        break;
      case "Search":
        System.out.println(Operation);
        runQuery();
        JTable resultTable;
        if (toggleButton.isSelected()) {
          resultTable = func.allSuppliers();
        } else {
          // resultTable = runQuery();
          resultTable = func.searchInventoryByItem("Apple");
        }
        refreshView(resultTable);
        break;
    }
  }

  private JTable runQuery() {
    JTable table = func.allInventory();
    String itemString = itemField.getText();
    String storeString = storeField.getText();
    String supplierString = supplierField.getText();
    boolean lowStock = toggleButton.isSelected();

    return table;

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

    String[] columnNames = { "Column 1", "Column 2", "Column 3", "Column 4", "Column 5", "Column 6", "Column 7",
        "Column 8" };
    Object[][] data = new Object[50][8];

    for (int i = 0; i < 50; i++) {
      for (int j = 0; j < 8; j++) {
        data[i][j] = "Data " + (i * 8 + j + 1);
      }
    }

    table = new JTable(data, columnNames);
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
