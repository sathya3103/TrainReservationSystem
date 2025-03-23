package trainreservation;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AvailableTrainsFrame extends JFrame {

    public AvailableTrainsFrame() {
        setTitle("Available Trains");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        add(panel);
        
        panel.setLayout(new BorderLayout());
        
        JTextArea trainArea = new JTextArea(10, 40);
        JScrollPane scrollPane = new JScrollPane(trainArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton bookTicketButton = new JButton("Book Ticket");
        panel.add(bookTicketButton, BorderLayout.SOUTH);
        
        loadAvailableTrains(trainArea);
        
        bookTicketButton.addActionListener((var e) -> {
            new TicketBookingFrame(); // Assume the fare is retrieved in the booking frame
            dispose();
        });
        
        setVisible(true);
    }

    // Method to load available trains from the database
    private void loadAvailableTrains(JTextArea trainArea) {
        try (Connection connection = Database.getConnection()) {
            // Check if the connection is successful
            if (connection == null) {
                throw new SQLException("Failed to establish database connection.");
            }

            // Query to fetch train_id, train_name, source, destination, and seats_available
            String query = "SELECT train_id, train_name, source, destination, seats_available FROM trains";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            
            // StringBuilder to construct the output
            StringBuilder builder = new StringBuilder("Train Id | Train Name | Source | Destination | Available Seats\n");
            while (resultSet.next()) {
                // Append train details to the text area
                builder.append(resultSet.getString("train_id")).append(" | ")
                       .append(resultSet.getString("train_name")).append(" | ")
                       .append(resultSet.getString("source")).append(" | ")
                       .append(resultSet.getString("destination")).append(" | ")
                       .append(resultSet.getInt("seats_available")).append("\n");
            }
            
            // Set the fetched data to the JTextArea
            trainArea.setText(builder.toString());

        } catch (SQLException e) {
            // Print the stack trace for debugging
            e.printStackTrace();
            
            // Show error dialog to the user
            JOptionPane.showMessageDialog(this, 
                "Error fetching train data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AvailableTrainsFrame::new); // Start the GUI
    }
}

