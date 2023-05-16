import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class Data {
    String id;
    String history;

    public Data(String id, String history) {
        this.id = id;
        this.history = history;
    }
}

public class App1 implements ActionListener {

    private JFrame frame;
    private JTextField displayField;
    // private JTextField displayFieldHistory;
    private JTextArea area;
    private double currentValue = 0.0;
    private boolean startNewNumber = true;
    private String lastOperator = "";
    ArrayList<Data> dataList = new ArrayList<>();
    Connection connection = null;
    String databaseName = "Calculations";
    String tableName = "history";

    public App1() {
        frame = new JFrame("Calculator");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        JPanel Calpanel = new JPanel(new BorderLayout());
        // JPanel CalpanelHold = new JPanel(new GridLayout(1, 2));

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        displayField = new JTextField();
        displayField.setFont(new Font("Arial", Font.PLAIN, 24));
        displayField.setEditable(false);
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        Calpanel.add(displayField, BorderLayout.NORTH);

        area = new JTextArea();
        area.setBounds(90, 0, 200, 400);
        area.setEditable(false);
        area.setFont(new Font("Arial", Font.PLAIN, 24));
        area.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(area);
        mainPanel.add(scrollPane);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 10, 10));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        String[] buttonLabels = {
                "C", "<-", "/", "*", "7", "8", "9", "-", "4", "5", "6",
                "+", "1", "2", "3", "=", "0", ".", "SI", "MOD"
        };

        for (String buttonLabel : buttonLabels) {
            JButton button = new JButton(buttonLabel);
            button.addActionListener(this);

            if (buttonLabel.equals("C")) {
                button.setBackground(Color.RED);
                button.setForeground(Color.WHITE);
            } else if (buttonLabel.equals("<-")) {
                button.setBackground(Color.BLUE);
                button.setForeground(Color.WHITE);
            } else if (buttonLabel.equals("/") || buttonLabel.equals("*")
                    || buttonLabel.equals("-") || buttonLabel.equals("+")
                    || buttonLabel.equals("=") || buttonLabel.equals("SI") || buttonLabel.equals("MOD")) {
                button.setBackground(Color.GREEN);
                button.setForeground(Color.WHITE);
            }

            button.setFont(new Font("Arial", Font.PLAIN, 18));
            buttonPanel.add(button);
        }
        Calpanel.add(buttonPanel);
        // CalpanelHold.add(Calpanel);
        mainPanel.add(Calpanel);
        // mainPanel.add(buttonPanel, BorderLayout.CENTER);
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonText = e.getActionCommand();
        String displayText = displayField.getText();

        if (buttonText.equals("C")) {
            currentValue = 0.0;
            lastOperator = "";
            displayField.setText("");
        } else if (buttonText.equals("<-")) {
            if (displayText.length() > 0) {
                displayField.setText(displayText.substring(0, displayText.length() - 1));
            }
        } else if (buttonText.matches("\\d") || buttonText.equals(".")) {
            if (startNewNumber) {
                displayField.setText("");
                startNewNumber = false;
            }
            displayField.setText(displayField.getText() + buttonText);
        } else if (buttonText.matches("[+\\-*/]")) {
            if (!startNewNumber) {
                calculate();
            }
            lastOperator = buttonText;
            startNewNumber = true;
        } else if (buttonText.equals("=")) {
            calculate();
            startNewNumber = true;
            lastOperator = "";
        } else if (buttonText.equals("SI")) {
            SI();
        }
    }

    private void calculate() {
        double newValue = Double.parseDouble(displayField.getText());

        switch (lastOperator) {
            case "":
                currentValue = newValue;
                break;
            case "+":
                currentValue += newValue;
                break;
            case "-":
                currentValue -= newValue;
                break;
            case "*":
                currentValue *= newValue;
                break;
            case "/":
                currentValue /= newValue;
                break;
        }
        jdbc();
        displayField.setText(Double.toString(currentValue));

    }

    private void jdbc() {

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
            System.out.println("Conneted");
            String createDatabaseSql = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(createDatabaseSql);
            System.out.println("Db Created");
            // Use database
            String selectDatabaseSql = "USE Calculations";
            stmt.execute(selectDatabaseSql);

            // Create table
            String createTableSql = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "histories INT NOT NULL"
                    + ")";
            stmt.executeUpdate(createTableSql);
            System.out.println("Table Created");
            // Insert record
            String insertSql = "INSERT INTO " + tableName + " (histories) VALUES (?)";
            PreparedStatement pstmt = connection.prepareStatement(insertSql);
            pstmt.setDouble(1, currentValue);
            pstmt.executeUpdate();
            System.out.println("InsertRecord");

            String selectSql = "SELECT * FROM " + tableName;
            ResultSet rs = stmt.executeQuery(selectSql);
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                int id = rs.getInt("id");
                Double histories = rs.getDouble("histories");
                dataList.add(new Data(String.valueOf(id), histories.toString()));

            }

            for (Data data : dataList) {
                sb.append("ID: " + data.id + ", Histories: " + data.history + "\n");
            }
            area.setText(sb.toString());

        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void SI() {
        String principal = JOptionPane.showInputDialog(frame, "Enter principal amount:");
        String rate = JOptionPane.showInputDialog(frame, "Enter rate of interest:");
        String time = JOptionPane.showInputDialog(frame, "Enter time period in years:");

        double p = Double.parseDouble(principal);
        double r = Double.parseDouble(rate);
        double t = Double.parseDouble(time);

        double si = (p * r * t) / 100.0;

        displayField.setText(Double.toString(si));
        startNewNumber = true;
        lastOperator = "";

        return;
    }

    public static void main(String[] args) {
        new App1();
    }
}