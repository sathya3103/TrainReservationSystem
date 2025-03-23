package trainreservation;

import javax.swing.*;
import java.awt.*;

public class TicketDisplayFrame extends JFrame {
    private JLabel passengerNameLabel;
    private JLabel trainNameLabel;
    private JLabel trainIdLabel;
    private JLabel numTicketsLabel;
    private JLabel contactNumberLabel;
    private JLabel paymentOptionLabel;

    // Constructor that takes the ticket details as parameters
    public TicketDisplayFrame(String passengerName, String contactNumber, String trainName, String trainId, int numTickets, String paymentOption) {
        setTitle("Ticket Confirmation");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel, passengerName, contactNumber, trainName, trainId, numTickets, paymentOption);
        setVisible(true);
    }

    private void placeComponents(JPanel panel, String passengerName, String contactNumber, String trainName, String trainId, int numTickets, String paymentOption) {
        panel.setLayout(new GridLayout(6, 2, 10, 10)); // GridLayout with 6 rows and 2 columns

        JLabel passengerNameTextLabel = new JLabel("Passenger Name:");
        passengerNameLabel = new JLabel(passengerName);
        
        JLabel contactNumberTextLabel = new JLabel("Contact Number:");
        contactNumberLabel = new JLabel(contactNumber);

        JLabel trainNameTextLabel = new JLabel("Train Name:");
        trainNameLabel = new JLabel(trainName);

        JLabel trainIdTextLabel = new JLabel("Train ID:");
        trainIdLabel = new JLabel(trainId);

        JLabel numTicketsTextLabel = new JLabel("Number of Tickets:");
        numTicketsLabel = new JLabel(String.valueOf(numTickets));

        JLabel paymentOptionTextLabel = new JLabel("Payment Option:");
        paymentOptionLabel = new JLabel(paymentOption);

        panel.add(passengerNameTextLabel);
        panel.add(passengerNameLabel);

        panel.add(contactNumberTextLabel);
        panel.add(contactNumberLabel);

        panel.add(trainNameTextLabel);
        panel.add(trainNameLabel);

        panel.add(trainIdTextLabel);
        panel.add(trainIdLabel);

        panel.add(numTicketsTextLabel);
        panel.add(numTicketsLabel);

        panel.add(paymentOptionTextLabel);
        panel.add(paymentOptionLabel);
    }
}
