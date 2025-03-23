package trainreservation;

import javax.swing.*;
import java.awt.event.ActionEvent;
public class MainFrame extends JFrame {
 private JTextField userField;
 private JPasswordField passField;
 public MainFrame() {
 setTitle("Train Reservation System - Login");
 setSize(300, 150);
 
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setLocationRelativeTo(null);
 JPanel panel = new JPanel();
 add(panel);
 placeComponents(panel);
 setVisible(true);
 }
 private void placeComponents(JPanel panel) {
 panel.setLayout(null);
 JLabel userLabel = new JLabel("Username:");
 userLabel.setBounds(10, 20, 80, 25);
 panel.add(userLabel);
 userField = new JTextField(20);
 userField.setBounds(100, 20, 165, 25);
 panel.add(userField);
 JLabel passwordLabel = new JLabel("Password:");
 passwordLabel.setBounds(10, 50, 80, 25);
 panel.add(passwordLabel);
 passField = new JPasswordField(20);
 passField.setBounds(100, 50, 165, 25);
 panel.add(passField);
 JButton loginButton = new JButton("Login");
 loginButton.setBounds(10, 80, 80, 25);
 panel.add(loginButton);
 loginButton.addActionListener((ActionEvent e) -> {
     String username = userField.getText().trim();
     String password = new String(passField.getPassword()).trim();
     if (username.equals("SET YOUR USERNAME") && password.equals("SET YOUR PASSWORD")) {
         new AvailableTrainsFrame();
         dispose();
     } else {
         JOptionPane.showMessageDialog(null, "Invalid Credentials", "Error",
                 JOptionPane.ERROR_MESSAGE);
     }
 });}
 public static void main(String[] args) {
 new MainFrame();
 }
}
