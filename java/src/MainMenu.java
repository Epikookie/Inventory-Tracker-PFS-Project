import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Main Structure of Application
 */
public class MainMenu implements ActionListener {
  /**
   * Border constants for GUI panel
   */
  private final int TOP = 10, LEFT = 10, BOTTOM = 10, RIGHT = 10;

  private JFrame frame;
  private JPanel panel;
  private GridBagConstraints gbc = new GridBagConstraints();
  private JButton buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven;

  /**
   * Constructor, initilaises frame and panel objects
   */
  public MainMenu() {
    // Initilaise Main Window
    Window();
  }

  public static void main(String[] args) {
    new MainMenu();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String Operation = e.getActionCommand();
    switch (Operation) {
      case "Record Attendance":
        System.out.println(Operation);
        new RecordAttendance();
        frame.dispose();
        break;
      case "Recommend Class":
        System.out.println(Operation);
        new RecClass();
        frame.dispose();
        break;
      case "Student Management":
        System.out.println(Operation);
        new StudentManagement();
        frame.dispose();
        break;
      case "Class Management":
        System.out.println(Operation);
        new ClassManagement();
        frame.dispose();
        break;
      case "View Leaderboard":
        System.out.println(Operation);
        new LdrBoard();
        frame.dispose();
        break;
      case "Sign Out":
        System.out.println(Operation);
        new Login();
        frame.dispose();
        break;
      case "Schedule":
        System.out.println(Operation);
        new Login();
        frame.dispose();
        break;
    }
  }

  public void Window() {
    // Initilaise Main Window
    frame = new JFrame();
    panel = new JPanel(new GridBagLayout());
    gbc.fill = GridBagConstraints.BOTH;
    // Add Buttons
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0.5; //How much space allocated to the cell
    gbc.weighty = 0.5;
    buttonOne = new JButton("Record Attendance");
    buttonOne.addActionListener(this);
    panel.add(buttonOne, gbc);

    // Add Buttons
    gbc.gridx = 0;
    gbc.gridy = 2;
    buttonTwo = new JButton("Recommend Class");
    buttonTwo.addActionListener(this);
    panel.add(buttonTwo, gbc);
    // Add Buttons
    buttonThree = new JButton("Class Management");
    gbc.gridx = 0;
    gbc.gridy = 1;
    buttonThree.addActionListener(this);
    panel.add(buttonThree, gbc);
    // Add Buttons
    buttonFour = new JButton("Student Management");
    gbc.gridx = 1;
    gbc.gridy = 1;
    buttonFour.addActionListener(this);
    panel.add(buttonFour, gbc);
    // Add Buttons
    buttonFive = new JButton("View Leaderboard");
    gbc.gridx = 1;
    gbc.gridy = 2;
    buttonFive.addActionListener(this);
    panel.add(buttonFive, gbc);
    // Add Buttons
    buttonSix = new JButton("Sign Out");
    gbc.gridx = 1;
    gbc.gridy = 0;
    buttonSix.addActionListener(this);
    panel.add(buttonSix, gbc);

    buttonSeven = new JButton("Schedule");
    gbc.gridx = 1;
    gbc.gridy = 3;
    buttonSeven.addActionListener(this);
    panel.add(buttonSeven, gbc);

    frame.setSize(500, 500);
    frame.add(panel, BorderLayout.CENTER);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Main Menu");
    panel.setBorder(BorderFactory.createEmptyBorder(TOP, LEFT, BOTTOM, RIGHT));
    frame.setVisible(true);
  }
}
