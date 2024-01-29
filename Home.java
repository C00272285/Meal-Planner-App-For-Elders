import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Home {
    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost/mealplanner";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "12July98";

    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setSize(500, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel forenameLabel = new JLabel("Forename");
        panel.add(forenameLabel);
        JTextField forenameTextField = new JTextField(15);
        panel.add(forenameTextField);

        JLabel surnameLabel = new JLabel("Surname");
        panel.add(surnameLabel);
        JTextField surnameTextField = new JTextField(15);
        panel.add(surnameTextField);

        JLabel ageLabel = new JLabel("Age");
        panel.add(ageLabel);
        JTextField ageTextField = new JTextField(15);
        panel.add(ageTextField);

        JLabel genderLabel = new JLabel("Gender");
        panel.add(genderLabel);
        JTextField genderTextField = new JTextField(15);
        panel.add(genderTextField);

        JLabel weightLabel = new JLabel("Weight");
        panel.add(weightLabel);
        JTextField weightTextField = new JTextField(15);
        panel.add(weightTextField);

        mainPanel.add(panel, BorderLayout.CENTER);

        JButton submit = new JButton("Submit");
        submit.setPreferredSize(new Dimension(80, 30));

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform error handling before processing the input
                if (!isNumeric(ageTextField.getText())
                        || !isAlphabetic(forenameTextField.getText())
                        || !isAlphabetic(surnameTextField.getText())
                        || !isValidGender(genderTextField.getText())
                        || !isNumeric(weightTextField.getText())) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid data.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // If all input is valid, insert data into the database
                insertData(
                        forenameTextField.getText(),
                        surnameTextField.getText(),
                        Integer.parseInt(ageTextField.getText()),
                        genderTextField.getText(),
                        Double.parseDouble(weightTextField.getText())
                );

                // Display a success message or perform further processing
                JOptionPane.showMessageDialog(frame, "Data submitted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        mainPanel.add(submit, BorderLayout.LINE_END);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // Helper method to check if a string is numeric
    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Helper method to check if a string contains only alphabetic characters
    private static boolean isAlphabetic(String str) {
        return str.matches("[a-zA-Z]+");
    }

    // Helper method to check if a string is a valid gender
    private static boolean isValidGender(String str) {
        return str.equalsIgnoreCase("male") || str.equalsIgnoreCase("female");
    }

    private static void insertData(String forename, String surname, int age, String gender, double weight) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // Check if the connection is successful
            if (isDatabaseConnected(connection)) {
                System.out.println("Connected to the database.");
    
                String insertQuery = "INSERT INTO register (ForeName, SurName, Age, Gender, Weight) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, forename);
                    preparedStatement.setString(2, surname);
                    preparedStatement.setInt(3, age);
                    preparedStatement.setString(4, gender);
                    preparedStatement.setDouble(5, weight);
    
                    preparedStatement.executeUpdate();
                    System.out.println("Data inserted successfully.");
                }
            } else {
                System.err.println("Failed to connect to the database.");
            }
        } catch (SQLException ex) {
            // Handle the exception more gracefully (log, display a user-friendly message, etc.)
            ex.printStackTrace();
        }
    }
    
    // Helper method to check if the database connection is successful
    private static boolean isDatabaseConnected(Connection connection) {
        try {
            return connection.isValid(5); // 5 seconds timeout, adjust as needed
        } catch (SQLException e) {
            return false;
        }
    }
    
}
