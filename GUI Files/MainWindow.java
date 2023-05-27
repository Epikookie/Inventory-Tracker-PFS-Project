import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainWindow implements ActionListener {
  private JFrame frame;
  private JPanel panel;
  private JButton scanInOutButton;
  private JButton signOutButton;
  private JTextField textField1;
  private JTextField textField2;
  private JTextField textField3;
  private JButton searchButton;
  private JToggleButton toggleButton;
  private JScrollPane tableScrollPane;
  private JTable table;

  public MainWindow() {
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

        new ScanInOut();
        frame.dispose();
        break;
      case "Sign Out":
        System.out.println(Operation);
        new Login();
        frame.dispose();
        break;
    }
  }

  private void addComponents() {
    GridBagConstraints gbc = new GridBagConstraints();
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
    JLabel label1 = new JLabel("Label 1:");
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;

    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    panel.add(label1, gbc);

    textField1 = new JTextField(50);
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 2;

    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(textField1, gbc);

    JLabel label2 = new JLabel("Label 2:");
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;

    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(label2, gbc);

    textField2 = new JTextField(50);
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 2;

    gbc.anchor = GridBagConstraints.WEST;
    panel.add(textField2, gbc);

    JLabel label3 = new JLabel("Label 3:");
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;

    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(label3, gbc);

    textField3 = new JTextField(50);
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 2;

    gbc.anchor = GridBagConstraints.WEST;
    panel.add(textField3, gbc);

    searchButton = new JButton("Search");
    searchButton.addActionListener(this);

    gbc.gridx = 4;
    gbc.gridy = 1;
    gbc.gridheight = 3;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel.add(searchButton, gbc);

    // Final Row
    toggleButton = new JToggleButton("Show only Low Stock");
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
    SwingUtilities.invokeLater(MainWindow::new);
  }
}
