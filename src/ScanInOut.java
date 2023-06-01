import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    String RFID = itemInput.getText();
    String storeVal = storeInput.getText();
    String quantityVal = Quantity.getText();
    String Operation = e.getActionCommand();
    switch (Operation) {
      case "Scan In":
        System.out.println(Operation);
        break;
      case "Scan Out":
        System.out.println(Operation);
        break;
      case "Back": // Handle back button action
        frame.dispose();
        new MainWindow(func);
        break;
    }

    if (RFID.equals("root")) {
      labelThree.setText("Scan successful. Loading...");
      frame.dispose();
      // Call your MainMenu() class or code here
    } else {
      labelThree.setText("Please check fields");
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
