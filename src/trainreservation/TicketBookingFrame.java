package trainreservation;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TicketBookingFrame extends JFrame {
    private JTextField passengerNameField;
    private JTextField contactNumberField;
    private JComboBox<String> trainSelectionBox;
    private JTextField numTicketsField;
    private JComboBox<String> paymentOptionBox;

    public TicketBookingFrame() {
        setTitle("Train Reservation System - Book a Ticket");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);
        JLabel passengerNameLabel = new JLabel("Passenger Name:");
        passengerNameLabel.setBounds(10, 20, 120, 25);
        panel.add(passengerNameLabel);
        passengerNameField = new JTextField(20);
        passengerNameField.setBounds(150, 20, 200, 25);
        panel.add(passengerNameField);

        JLabel contactNumberLabel = new JLabel("Contact Number:");
        contactNumberLabel.setBounds(10, 60, 120, 25);
        panel.add(contactNumberLabel);
        contactNumberField = new JTextField(20);
        contactNumberField.setBounds(150, 60, 200, 25);
        panel.add(contactNumberField);

        JLabel train_id = new JLabel("Train Id:");
        train_id.setBounds(10, 100, 120, 25);
        panel.add(train_id);
        trainSelectionBox = new JComboBox<>();
        loadtrain_name();
        trainSelectionBox.setBounds(150, 100, 200, 25);
        panel.add(trainSelectionBox);

        JLabel numTicketsLabel = new JLabel("Number of Tickets:");
        numTicketsLabel.setBounds(10, 140, 120, 25);
        panel.add(numTicketsLabel);
        numTicketsField = new JTextField(20);
        numTicketsField.setBounds(150, 140, 200, 25);
        panel.add(numTicketsField);

        JLabel paymentOptionLabel = new JLabel("Payment Option:");
        paymentOptionLabel.setBounds(10, 180, 120, 25);
        panel.add(paymentOptionLabel);
        paymentOptionBox = new JComboBox<>(new String[]{"Credit Card", "Debit Card"});
        paymentOptionBox.setBounds(150, 180, 200, 25);
        panel.add(paymentOptionBox);

        JButton bookButton = new JButton("Book Ticket");
        bookButton.setBounds(10, 220, 120, 25);
        panel.add(bookButton);
        bookButton.addActionListener(e -> {
            try {
                bookTicket();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void loadtrain_name() {
        try (Connection connection = Database.getConnection()) {
            String query = "SELECT train_name FROM trains";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                trainSelectionBox.addItem(resultSet.getString("train_name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching train_name.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void bookTicket() throws SQLException {
        String passengerName = passengerNameField.getText().trim();
        String contactNumber = contactNumberField.getText().trim();
        String trainId = (String) trainSelectionBox.getSelectedItem();
        String numTicketsStr = numTicketsField.getText().trim();
        String paymentOption = (String) paymentOptionBox.getSelectedItem();

        if (passengerName.isEmpty() || contactNumber.isEmpty() || trainId.isEmpty() || numTicketsStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int numTickets;
        try {
            numTickets = Integer.parseInt(numTicketsStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of tickets.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int seatsAvailable = getAvailableSeats(trainId);
        if (numTickets > seatsAvailable) {
            JOptionPane.showMessageDialog(this, "Not enough available seats.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection connection = Database.getConnection();
        if (connection != null) {
            String sql = "INSERT INTO reservations (passenger_name, contact_number, train_id) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, passengerName);
                preparedStatement.setString(2, contactNumber);
                preparedStatement.setString(3, trainId);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Ticket booked successfully!");
                    updateAvailableSeats(trainId, seatsAvailable - numTickets);

                    // After successful booking, show the ticket (without modifying bookTicket() directly)
                    showTicket(passengerName, contactNumber, trainId, numTickets, paymentOption);
                    dispose();  // Close the booking frame
                } else {
                    JOptionPane.showMessageDialog(this, "Ticket booking failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } finally {
                Database.closeConnection(connection);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showTicket(String passengerName, String contactNumber, String trainName, int numTickets, String paymentOption) {
        // Open TicketDisplayFrame with the ticket details
        new TicketDisplayFrame(passengerName, contactNumber, trainName, trainName, numTickets, paymentOption);
    }

    private int getAvailableSeats(String trainId) throws SQLException {
        int seatsAvailable = 0;
        try (Connection connection = Database.getConnection()) {
            String query = "SELECT seats_available FROM trains WHERE train_name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, trainId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    seatsAvailable = resultSet.getInt("seats_available");
                }
            }
        }
        return seatsAvailable;
    }

    private void updateAvailableSeats(String trainId, int newSeatsAvailable) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            String query = "UPDATE trains SET seats_available = ? WHERE train_name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, newSeatsAvailable);
                preparedStatement.setString(2, trainId);
                preparedStatement.executeUpdate();
            }
        }
    }
}
