import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Calculator implements ActionListener {

    private JFrame frame;
    private JTextField displayField;
    private double currentValue = 0.0;
    private boolean startNewNumber = true;
    private String lastOperator = "";

    public Calculator() {
        frame = new JFrame("Calculator");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        displayField = new JTextField();
        displayField.setFont(new Font("Arial", Font.PLAIN, 24));
        displayField.setEditable(false);
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        mainPanel.add(displayField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 10, 10));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        String[] buttonLabels = {
                "C", "<-", "/", "*", "7", "8", "9", "-", "4", "5", "6",
                "+", "1", "2", "3", "=", "0", ".", "SI"
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
                    || buttonLabel.equals("=") || buttonLabel.equals("SI")) {
                button.setBackground(Color.GREEN);
                button.setForeground(Color.WHITE);
            }

            button.setFont(new Font("Arial", Font.PLAIN, 18));
            buttonPanel.add(button);
        }

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonText = e.getActionCommand();

        if (buttonText.equals("C")) {
            currentValue = 0.0;
            startNewNumber = true;
            lastOperator = "";
            displayField.setText("");
            return;
        }

        if (buttonText.equals("<-")) {
            String text = displayField.getText();
            if (text.length() > 0) {
                displayField.setText(text.substring(0, text.length() - 1));
            }
            return;
        }

        if (buttonText.equals("/") || buttonText.equals("*")
                || buttonText.equals("-") || buttonText.equals("+")) {
            if (!lastOperator.equals("")) {
                return;
            }

            lastOperator = buttonText;

            if (startNewNumber) {
                if (buttonText.equals("-")) {
                    displayField.setText(buttonText);
                    startNewNumber = false;
                }
                return;
            }

            double newValue = Double.parseDouble(displayField.getText());

            if (lastOperator.equals("+")) {
                currentValue += newValue;
            } else if (lastOperator.equals("-")) {
                currentValue -= newValue;
            } else if (lastOperator.equals("*")) {
                currentValue *= newValue;
            } else if (lastOperator.equals("/")) {
                currentValue /= newValue;
            }

            displayField.setText("");
            startNewNumber = true;

            return;
        }

        if (buttonText.equals("=")) {
            if (lastOperator.equals("")) {
                return;
            }

            double newValue = Double.parseDouble(displayField.getText());

            if (lastOperator.equals("+")) {
                currentValue += newValue;
            } else if (lastOperator.equals("-")) {
                currentValue -= newValue;
            } else if (lastOperator.equals("*")) {
                currentValue *= newValue;
            } else if (lastOperator.equals("/")) {
                currentValue /= newValue;
            }

            displayField.setText(Double.toString(currentValue));
            startNewNumber = true;
            lastOperator = "";

            return;
        }

        if (buttonText.equals("SI")) {
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

        String displayText = displayField.getText();
        displayText += buttonText;
        displayField.setText(displayText);
        startNewNumber = false;
    }

    public static void main(String[] args) {
        new Calculator();
    }
}

// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.*;

// public class Calculator extends JFrame implements ActionListener {

// private JTextField displayField;
// private JButton[] digitButtons;
// private JButton[] operatorButtons;
// private JButton clearButton, equalButton, siButton;

// private double currentValue = 0;
// private String currentOperator = "";
// private boolean startNewNumber = true;

// public Calculator() {
// setTitle("Calculator");
// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// setSize(300, 400);
// setLocationRelativeTo(null);

// JPanel mainPanel = new JPanel(new BorderLayout());

// // create the display field and add it to the main panel
// displayField = new JTextField();
// displayField.setEditable(false);
// displayField.setHorizontalAlignment(JTextField.RIGHT);
// displayField.setFont(new Font("Arial", Font.PLAIN, 32));
// mainPanel.add(displayField, BorderLayout.NORTH);

// // create the button panel and add it to the main panel
// JPanel buttonPanel = new JPanel(new GridLayout(6, 3, 5, 5));
// mainPanel.add(buttonPanel, BorderLayout.CENTER);

// clearButton = new JButton("C");
// clearButton.addActionListener(this);
// buttonPanel.add(clearButton);

// siButton = new JButton("SI");
// siButton.addActionListener(this);
// buttonPanel.add(siButton);

// // create digit buttons

// digitButtons = new JButton[10];
// for (int i = 0; i < 10; i++) {
// digitButtons[i] = new JButton(String.valueOf(i));
// digitButtons[i].addActionListener(this);
// buttonPanel.add(digitButtons[i]);
// }

// // create operator buttons
// operatorButtons = new JButton[4];
// operatorButtons[0] = new JButton("+");
// operatorButtons[1] = new JButton("-");
// operatorButtons[2] = new JButton("*");
// operatorButtons[3] = new JButton("/");
// for (int i = 0; i < 4; i++) {
// operatorButtons[i].addActionListener(this);
// buttonPanel.add(operatorButtons[i]);
// }

// // create special buttons

// equalButton = new JButton("=");
// equalButton.addActionListener(this);
// buttonPanel.add(equalButton);

// // set main panel as the content pane
// setContentPane(mainPanel);

// setVisible(true);
// }

// public static void main(String[] args) {
// Calculator calculator = new Calculator();
// }

// public void actionPerformed(ActionEvent e) {
// for (int i = 0; i < 10; i++) {
// if (e.getSource() == digitButtons[i]) {
// if (startNewNumber) {
// displayField.setText(String.valueOf(i));
// startNewNumber = false;
// } else {
// displayField.setText(displayField.getText() + i);
// }
// return;
// }
// }

// if (e.getSource() == clearButton) {
// displayField.setText("");
// currentValue = 0;
// currentOperator = "";
// startNewNumber = true;
// return;
// }

// for (int i = 0; i < 4; i++) {
// if (e.getSource() == operatorButtons[i]) {
// currentOperator = operatorButtons[i].getText();
// currentValue = Double.parseDouble(displayField.getText());
// displayField.setText("");
// startNewNumber = true;
// return;
// }
// }

// if (e.getSource() == equalButton) {
// double secondValue = Double.parseDouble(displayField.getText());
// double result = 0;
// switch (currentOperator) {
// case "+":
// result = currentValue + secondValue;
// break;
// case "-":
// result = currentValue * secondValue;
// break;
// case "*":
// result = currentValue * secondValue;
// break;
// case "/":
// result = currentValue / secondValue;
// break;
// }
// displayField.setText(String.valueOf(result));
// currentValue = result;
// startNewNumber = true;
// return;
// }

// if (e.getSource() == siButton) {
// double principal = Double.parseDouble(displayField.getText());
// String rateString = JOptionPane.showInputDialog("Enter the interest rate:");
// double rate = Double.parseDouble(rateString);
// String timeString = JOptionPane.showInputDialog("Enter the time in years:");
// double time = Double.parseDouble(timeString);
// double si = (principal * rate * time) / 100;
// double amount = principal + si;
// String output = "Principal: ₹" + principal + "\nInterest Rate: " + rate +
// "%\nTime: " + time + " years"+ "\nSimple Interest: ₹" + si + "";

// JOptionPane.showMessageDialog(null, output, "Simple Interest Calculation
// Result", JOptionPane.INFORMATION_MESSAGE);
// displayField.setText(String.valueOf(principal));
// displayField.setText(String.valueOf(si));
// currentValue = si;
// startNewNumber = true;
// }
// }
// }

// public Calculator() {
// setTitle

// setSize(300, 400);

// JPanel mainPanel = new JPanel(new BorderLayout());

// // create the display field and add it

// displayField.setEditable(false);
// displayField.setHorizontalAlignment(JTextField.RIGHT);
// displayField.setFont(new F

// // create the button panel and add it to the main panel
// JPanel buttonPanel = new JPanel(new GridLayout(6, 3, 5, 5));
// mainPanel.add(buttonPanel, BorderLayout.CENTER);

// for (int i = 0; i < 10; i++) {

// digitButtons[i].addActionListener(this);
// digitButtons[i].setBackground(Color.GREEN);
// buttonPanel.add(digitButtons[i]);
// }

// operatorButtons = new JButton[4];

// operatorButtons[1] = new JButton("-");
// operatorButtons[2] = new JButton("*");
// operatorButtons[3] = new JButton("/");

// operatorButtons[i].addActionListener(this);
// operatorButtons[i].setBackground(Color.RED);
// buttonPanel.add(operatorButtons[i]);
// }

// clearButton = new JButton("C");
//

// buttonPanel.add(clearButton);

// equalButton = new JButton("=");
// equalButton.addActionListener(this);
// buttonPanel.add(equalButton);

// siButton = new JButton("SI");
// siButton.addActionListener(this);
// buttonPanel.add(siButton);

// setVisible(true);
// }

// public static void main(Strin

// }

// public void actionPerformed(A

// if (e.getSource() == digitButtons[i]) {
// if (startNewNumber) {
//

// } else {
//

// return;
//

// if (e.getSource() == clearButton) {
//

// currentOperator = "";
// startNewNumber = true;
// return;
// }

// (int i = 0; i < 4; i++) {
// if (e.getSource() == operatorButtons[i]) {
// currentOperator = operatorButtons[i].getText();
// currentValue = Double.parseDouble(displayField.getText());
// displayField.setText("");
//
//

// }

// if (e.getSource() == equalButton) {
// double secondValue = Double.parseDouble(displayField.getText());
// double result = 0;
// switch (currentOperator) {
//

// break;
// case "-":
// result = currentValue - secondValue;
// break;
// case "*":
// result = currentValue * secondValue;
// break;
//
//

// }
// displayField.setText(String.valueOf(result));
// currentValue = result;
// startNewNumber = true;
// }

// e.getSource() == siButton) {
// double principal = Double.parseDouble(displayField.getText());
// String rateString = JOptionPane.showInputDialog("Enter the interest rate:");
// double rate = Double.parseDouble(rateString);
// imeString = JOptionPane.showInputDialog("Enter the time in years:");
// double time = Double.parseDouble(timeString);
// double si = (principal * rate * time) / 100;
// double amount = principal + si;
// String output = "Principal: ₹" + principal + "\nInterest Rate: " + rate +
// "%\nTime: " + time + " years\n"
//
// JOptionPane.showMessageDialog(null, output, "Simple Interest Calculation
// Result", JOptionPane.INFORMATION_MESSAGE);
// displayField.setText(String.valueOf(principal));
//

// }
//
//

//
//