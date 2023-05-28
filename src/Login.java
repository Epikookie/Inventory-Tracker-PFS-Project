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
  private int dimensionX = 500, dimensionY = 500;
  private static int TOP = 10, BOTTOM = 10, LEFT = 10, RIGHT = 10;
  private JFrame frame;
  private JPanel panel;
  private GridBagConstraints gbc = new GridBagConstraints();

  private JLabel labelOne, labelTwo, labelThree;
  private JTextField userInput;
  private JPasswordField passwordInput;
  private JButton buttonOne;


  /**
   * Constructor, initilaises frame and panel objects
   */
  public Login() {
    // Initilaise Main Window
    Window();
  }

  public static void main(String[] args) {
    new Login();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String teacherID = userInput.getText();
    String password = String.valueOf(passwordInput.getPassword());
    if (teacherID.equals("root") && password.equals("toor")) {
      labelThree.setText("Login sucessful. Loading...");
      frame.dispose();
      new MainMenu();
    } else {
      labelThree.setText("ID or Password is incorrect");
    }
  }

  public void Window() {
    frame = new JFrame();
    panel = new JPanel(new GridBagLayout());
    //First Column

    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0.25; //How much space allocated to the cell
    gbc.weighty = 0.25;
    // Add Labels
    labelOne = new JLabel("Teacher ID:");
    panel.add(labelOne, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    labelTwo = new JLabel("Password:");
    panel.add(labelTwo, gbc);

    //Second Column

    // Add input fields
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    userInput = new JTextField(10);
    gbc.gridx = 1;
    gbc.gridy = 0;
    panel.add(userInput, gbc);

    passwordInput = new JPasswordField(10);
    gbc.gridx = 1;
    gbc.gridy = 1;
    panel.add(passwordInput, gbc);

    //Final Row
    gbc.weighty = 5;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    buttonOne = new JButton("Sign In");
    gbc.gridx = 1;
    gbc.gridy = 2;
    buttonOne.addActionListener(this);
    panel.add(buttonOne, gbc);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.gridx = 0;
    gbc.gridy = 3;
    labelThree = new JLabel();
    panel.add(labelThree, gbc);

    frame.setSize(dimensionX, dimensionY);
    frame.add(panel, BorderLayout.CENTER);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Authentication");
    panel.setBorder(BorderFactory.createEmptyBorder(TOP, LEFT, BOTTOM, RIGHT));
    frame.setVisible(true);
  }
}
