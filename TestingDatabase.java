import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestingDatabase extends JFrame implements ActionListener
{

    // Variables
    private JLabel ForeName, SurName, Age, Weight, DietaryProblemLabel;
    private JTextField ForeNameField, SurNameField, AgeField, WeightField;
    private JRadioButton maleRadio, femaleRadio;
    private JButton addButton, cancelButton;
    private JComboBox<String> dietaryProblemComboBox;

    public TestingDatabase()
    {
        super("Create a User");

        // creating the Labels
        ForeName = new JLabel("First Name:");
        SurName = new JLabel("Last Name:");
        Age = new JLabel("Age:");
        Weight = new JLabel("Weight:");
        DietaryProblemLabel = new JLabel("Dietary Problem:");

        ForeNameField = new JTextField(10);
        SurNameField = new JTextField(10);
        AgeField = new JTextField(10);
        WeightField = new JTextField(10);

        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);

        // Define the dietary problems
        String[] dietaryProblems = {"Diabetes", "Lactose Intolerance", "Digestive Issues", "Malnutrition"};
        dietaryProblemComboBox = new JComboBox<>(dietaryProblems);

        addButton = new JButton("Create");
        cancelButton = new JButton("Dispose");

        // Creating the grid layout
        setLayout(new GridBagLayout());
        GridBagConstraints Screen = new GridBagConstraints();
        Screen.insets = new Insets(30, 30, 30, 30);
        Screen.anchor = GridBagConstraints.WEST;

        // moving the labels around
        Screen.gridx = 0;
        Screen.gridy = 1;
        add(ForeName, Screen);

        Screen.gridx = 1;
        Screen.gridy = 1;
        add(ForeNameField, Screen);

        Screen.gridx = 0;
        Screen.gridy = 2;
        add(SurName, Screen);

        Screen.gridx = 1;
        Screen.gridy = 2;
        add(SurNameField, Screen);

        Screen.gridx = 0;
        Screen.gridy = 3;
        add(Age, Screen);

        Screen.gridx = 1;
        Screen.gridy = 3;
        add(AgeField, Screen);

        Screen.gridx = 0;
        Screen.gridy = 4;
        add(Weight, Screen);

        Screen.gridx = 1;
        Screen.gridy = 4;
        add(WeightField, Screen);

        Screen.gridx = 0;
        Screen.gridy = 5;
        add(DietaryProblemLabel, Screen);

        Screen.gridx = 1;
        Screen.gridy = 5;
        add(dietaryProblemComboBox, Screen);

        Screen.gridx = 0;
        Screen.gridy = 6;
        add(new JLabel("Gender:"), Screen);

        Screen.gridx = 1;
        Screen.gridy = 6;
        JPanel genderPanel = new JPanel();
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        add(genderPanel, Screen);

        Screen.gridx = 0;
        Screen.gridy = 8;
        Screen.anchor = GridBagConstraints.CENTER;
        add(addButton, Screen);

        Screen.gridx = 1;
        Screen.gridy = 8;
        add(cancelButton, Screen);

        // Add button action listener
        addButton.addActionListener(this);
        cancelButton.addActionListener(this);

        // Set window properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == cancelButton)
        {
            dispose();
            return;
        }

        // Validation for Age and Weight
        String userAgeText = AgeField.getText();
        String weightText = WeightField.getText();

        if (!isValidAge(userAgeText) || !isValidWeight(weightText))
        {
            JOptionPane.showMessageDialog(this, "Invalid Age or Weight. Please enter valid values.");
            return;
        }

        // Convert validated values to the appropriate data types
        int userAge = Integer.parseInt(userAgeText);
        double weight = Double.parseDouble(weightText);

        // database URL
        final String DATABASE_URL = "jdbc:mysql://localhost/MealPlannerApp";
        Connection connection = null;
        PreparedStatement pstat = null;
        String ForeName = ForeNameField.getText();
        String SurName = SurNameField.getText();
        String GenderValue = maleRadio.isSelected() ? "Male" : "Female";
        String dietaryProblem = dietaryProblemComboBox.getSelectedItem().toString();

        int i = 0;
        try
        {
            // establish connection to database
            connection = DriverManager.getConnection(DATABASE_URL, "root", "12July98");
            // create Prepared Statement for inserting data into table
            pstat = connection.prepareStatement("INSERT INTO Register (FirstName, SurName, Age, Gender, Weight, DietaryProblem) VALUES (?,?,?,?,?,?)");
            pstat.setString(1, ForeName);
            pstat.setString(2, SurName);
            pstat.setInt(3, userAge);
            pstat.setString(4, GenderValue);
            pstat.setDouble(5, weight);
            pstat.setString(6,dietaryProblem);

            i = pstat.executeUpdate();
            JOptionPane.showMessageDialog(this, i + " record successfully added to the database table.");
        } catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + sqlException.getMessage());
        } finally
        {
            try
            {
                if (pstat != null)
                {
                    pstat.close();
                }
                if (connection != null)
                {
                    connection.close();
                }
            } catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private boolean isValidAge(String age)
    {
        try
        {
            int userAge = Integer.parseInt(age);
            return userAge >= 0;  // making sure that the user cant enter 0
        } catch (NumberFormatException e)
        {
            return false;  // Not a valid integer
        }
    }

    private boolean isValidWeight(String weight)
    {
        try
        {
            double userWeight = Double.parseDouble(weight);
            return userWeight >= 0;  // making sure that the user cant enter 0
        } catch (NumberFormatException e)
        {
            return false;  // Not a valid double
        }
    }

    public static void main(String[] args)
    {
        new TestingDatabase();
    }
}
