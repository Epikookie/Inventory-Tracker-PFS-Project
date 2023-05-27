import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Main Structure of Application
 */
public class Login implements ActionListener {
  /**
   * Border constants for GUI panel
   */
  private int dimensionX = 400, dimensionY = 250;
  private static int TOP = 10, BOTTOM = 10, LEFT = 10, RIGHT = 10;
  private JFrame frame;
  private JPanel panel;
  private GridBagConstraints gbc = new GridBagConstraints();

  private JLabel labelOne, labelTwo, labelThree;
  private JTextField userInput;
  private JPasswordField passwordInput;
  private JButton buttonOne;

  /**
   * Constructor, initializes frame and panel objects
   */
  public Login() {
    // Initialize Main Window
    Window();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new Login());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String teacherID = userInput.getText();
    String password = String.valueOf(passwordInput.getPassword());
    if (teacherID.equals("root") && password.equals("toor")) {
      labelThree.setText("Login successful. Loading...");
      new MainWindow();
      frame.dispose();
    } else {
      labelThree.setText("ID or Password is incorrect");
    }
  }

  public void Window() {
    frame = new JFrame();
    panel = new JPanel(new GridBagLayout());

    // First Column
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.insets = new Insets(5, 5, 5, 5); // Add padding
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Add Labels
    labelOne = new JLabel("ID:");
    panel.add(labelOne, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    labelTwo = new JLabel("Password:");
    panel.add(labelTwo, gbc);

    // Second Column
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    userInput = new JTextField(10);
    panel.add(userInput, gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    passwordInput = new JPasswordField(10);
    panel.add(passwordInput, gbc);

    // Final Row
    gbc.weighty = 1.0;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.NONE;
    buttonOne = new JButton("Sign In");
    buttonOne.addActionListener(this);
    panel.add(buttonOne, gbc);

    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = GridBagConstraints.REMAINDER; // Span across all remaining columns
    labelThree = new JLabel();
    panel.add(labelThree, gbc);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Authentication");
    frame.add(panel, BorderLayout.CENTER);
    frame.setSize(dimensionX, dimensionY);
    frame.setLocationRelativeTo(null); // Center the frame on the screen
    frame.setVisible(true);
  }
}