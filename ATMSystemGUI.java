import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

interface ATMInterface {
    boolean authenticate(String enteredId, String enteredPin);
    void deposit(double amount);
    void withdraw(double amount);
    void transfer(ATM recipient, double amount);
    void printTransactionHistory();
    double getBalance();
}

class ATM implements ATMInterface {
    private double balance;
    private ArrayList<String> transactionHistory;
    private String userId;
    private String userPin;

    public ATM(String userId, String userPin) {
        this.userId = userId;
        this.userPin = userPin;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    public boolean authenticate(String enteredId, String enteredPin) {
        return userId.equals(enteredId) && userPin.equals(enteredPin);
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactionHistory.add("Deposited: $" + amount);
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            transactionHistory.add("Withdrew: $" + amount);
        }
    }

    public void transfer(ATM recipient, double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            recipient.balance += amount;
            transactionHistory.add("Transferred: $" + amount + " to " + recipient.userId);
        }
    }

    public void printTransactionHistory() {
        for (String transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }

    public double getBalance() {
        return balance;
    }
}

public class ATMSystemGUI extends JFrame implements ActionListener {
    private ATM currentUserATM;
    private ATM user1, user2;
    private JTextField userIdField;
    private JPasswordField pinField;
    private JTextArea displayArea;

    public ATMSystemGUI() {
        // Set up users
        user1 = new ATM("user1", "1234");
        user2 = new ATM("user2", "4321");

        // Set up GUI components
        setTitle("ATM System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        add(new JLabel("User ID:"));
        userIdField = new JTextField(10);
        add(userIdField);

        add(new JLabel("PIN:"));
        pinField = new JPasswordField(10);
        add(pinField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        add(loginButton);

        displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea));

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String userId = userIdField.getText();
        String userPin = new String(pinField.getPassword());

        if (user1.authenticate(userId, userPin)) {
            currentUserATM = user1;
        } else if (user2.authenticate(userId, userPin)) {
            currentUserATM = user2;
        } else {
            displayArea.setText("Invalid credentials.");
            return;
        }

        showATMMenu();
    }

    private void showATMMenu() {
        String[] options = {"Transaction History", "Withdraw", "Deposit", "Transfer", "View Balance", "Quit"};
        int choice = JOptionPane.showOptionDialog(null, "Select an option", "ATM Menu", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0:
                displayTransactionHistory();
                break;
            case 1:
                withdraw();
                break;
            case 2:
                deposit();
                break;
            case 3:
                transfer();
                break;
            case 4:
                viewBalance();
                break;
            case 5:
                System.exit(0);
                break;
            default:
                displayArea.setText("Invalid option.");
        }
    }

    private void deposit() {
        String amountStr = JOptionPane.showInputDialog("Enter amount to deposit:");
        double amount = Double.parseDouble(amountStr);
        currentUserATM.deposit(amount);
        displayArea.setText("Deposited $" + amount);
    }

    private void withdraw() {
        String amountStr = JOptionPane.showInputDialog("Enter amount to withdraw:");
        double amount = Double.parseDouble(amountStr);
        currentUserATM.withdraw(amount);
        displayArea.setText("Withdrew $" + amount);
    }

    private void transfer() {
        String recipientId = JOptionPane.showInputDialog("Enter recipient user ID (user1 or user2):");
        ATM recipient = recipientId.equals("user1") ? user1 : user2;
        String amountStr = JOptionPane.showInputDialog("Enter amount to transfer:");
        double amount = Double.parseDouble(amountStr);
        currentUserATM.transfer(recipient, amount);
        displayArea.setText("Transferred $" + amount + " to " + recipientId);
    }

    private void displayTransactionHistory() {
        displayArea.setText("Transaction History:\n");
        currentUserATM.printTransactionHistory();
    }

    private void viewBalance() {
        displayArea.setText("Current balance: $" + currentUserATM.getBalance());
    }

    public static void main(String[] args) {
        new ATMSystemGUI();
    }
}
