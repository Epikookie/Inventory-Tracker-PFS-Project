import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.swing.*;

public class ScanInOut implements ActionListener {
  private int dimensionX = 500, dimensionY = 400;
  private static int TOP = 10, BOTTOM = 10, LEFT = 10, RIGHT = 10;
  private JFrame frame;
  private JPanel panel;
  private GridBagConstraints gbc = new GridBagConstraints();

  private JLabel labelOne, labelTwo, labelThree, storeLabel;
  private JTextField itemInput, Quantity;
  private JTextField storeInput;
  private JButton buttonOne;
  private JButton backButton; // Added back button
  private AppFunctions func;

  public ScanInOut(AppFunctions func) {
    this.func = func;
    Window();
  }

  public static void main(String[] args) {
    AppFunctions func = new AppFunctions();
    SwingUtilities.invokeLater(() -> {
      new ScanInOut(func);
    });
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String Operation = e.getActionCommand();
    // ! Get staff ID from login
    int staffID = 1;
    switch (Operation) {
      case "Scan In":
        if (validateInputs()) {
          String RFID = itemInput.getText();
          String storeVal = storeInput.getText();
          String quantityVal = Quantity.getText();
          int quantity = Integer.parseInt(quantityVal);
          if (func.scanIn(RFID, quantity, storeVal)) {
            labelThree.setText("Item scanned in successfully");
          } else {
            labelThree.setText("Item not scanned in successfully");
          }
        }
        System.out.println(Operation);
        break;
      case "Scan Out":
        if (validateInputs()) {
          String RFID = itemInput.getText();
          String storeVal = storeInput.getText();
          String quantityVal = Quantity.getText();
          int quantity = Integer.parseInt(quantityVal);
          if (func.scanOut(RFID, quantity, storeVal)) {
            labelThree.setText("Item scanned out successfully");
            func.updateLog(staffID, RFID, storeVal, Operation, quantity, LocalDateTime.now());
          } else {
            labelThree.setText("Item not scanned out successfully");
            func.updateLog(staffID, RFID, storeVal, Operation, quantity, LocalDateTime.now());

          }
        }
        System.out.println(Operation);
        break;
      case "Back": // Handle back button action
        frame.dispose();
        new MainWindow(func);
        break;
    }

  }

  public boolean validateInputs() {
    String RFID = itemInput.getText();
    String storeVal = storeInput.getText();
    String quantityVal = Quantity.getText();
    if (RFID.equals("") || storeVal.equals("") || quantityVal.equals("")) {
      labelThree.setText("Please fill in all fields");
      return false;
    }
    // Check RFID is valid
    if (sanitiseRFID(RFID).equals("")) {
      labelThree.setText("Please enter a valid RFID");
      return false;
    }
    // Check store is valid
    if (sanitiseStore(storeVal).equals("")) {
      labelThree.setText("Please enter a valid store");
      return false;
    }
    storeVal = sanitiseStore(storeVal);
    if (!validateQuantity(quantityVal)) {
      labelThree.setText("Please enter a valid quantity");
      return false;
    }
    return true;
  }

  public String sanitiseRFID(String RFID) {
    // Ensure RFID is alphanumeric and 40 characters long
    if (RFID.matches("[a-zA-Z0-9]+") && RFID.length() == 40) {
      return RFID;
    }
    return "";

  }

  public String sanitiseStore(String storeVal) {
    // Ensure store is alphanumeric
    if (storeVal.matches("[a-zA-Z0-9]+")) {
      return storeVal;
    }
    return "";
  }

  public boolean validateQuantity(String quantityVal) {
    // Check string is numeric
    if (quantityVal.matches("[0-9]+")) {
      int quantity = Integer.parseInt(quantityVal);
      // Check quantity is greater than 0
      if (quantity > 0) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public void Window() {
    frame = new JFrame();
    panel = new JPanel(new GridBagLayout());

    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.insets = new Insets(TOP, LEFT, BOTTOM, RIGHT);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    labelOne = new JLabel("RFID:");
    panel.add(labelOne, gbc);
    gbc.gridx = 0;
    gbc.gridy = 1;
    labelTwo = new JLabel("Store:");
    panel.add(labelTwo, gbc);
    gbc.gridx = 0;
    gbc.gridy = 2;
    storeLabel = new JLabel("Quantity:");
    panel.add(storeLabel, gbc);

    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    itemInput = new JTextField(10);
    panel.add(itemInput, gbc);
    gbc.gridx = 1;
    gbc.gridy = 1;
    storeInput = new JTextField(10);
    panel.add(storeInput, gbc);
    gbc.gridx = 1;
    gbc.gridy = 2;
    Quantity = new JTextField(10);
    panel.add(Quantity, gbc);

    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.weighty = 1.0;
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.fill = GridBagConstraints.NONE;
    buttonOne = new JButton("Scan In");
    buttonOne.addActionListener(this);
    panel.add(buttonOne, gbc);

    gbc.gridx = 2;
    gbc.gridy = 4;
    gbc.fill = GridBagConstraints.NONE;
    buttonOne = new JButton("Scan Out");
    buttonOne.addActionListener(this);
    panel.add(buttonOne, gbc);

    gbc.gridx = 0; // Reset gridx value for the back button
    gbc.gridy = 5;
    gbc.anchor = GridBagConstraints.LINE_END;
    backButton = new JButton("Back");
    backButton.addActionListener(this);
    panel.add(backButton, gbc);

    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.gridx = 1;
    gbc.gridy = 5;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    labelThree = new JLabel();
    panel.add(labelThree, gbc);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Scan In/Out");
    frame.add(panel, BorderLayout.CENTER);
    frame.setSize(dimensionX, dimensionY);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
