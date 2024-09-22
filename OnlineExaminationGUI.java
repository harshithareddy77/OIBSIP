import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class OnlineExaminationGUI {
    private String username = "student1";
    private String password = "password123";
    private boolean isLoggedIn = false;
    private int score = 0;
    private int currentQuestionIndex = 0;
    private Timer timer;
    private JFrame frame;
    private JTextField usernameField, passwordField, newPasswordField;
    private JTextArea questionArea;
    private JRadioButton[] answerOptions;
    private ButtonGroup answersGroup;

    private String[] questions = {
        "Q1: What is 2 + 2?\n1. 3\n2. 4\n3. 5",
        "Q2: What is the capital of France?\n1. Berlin\n2. Madrid\n3. Paris"
    };
    private int[] correctAnswers = {2, 3};

    public OnlineExaminationGUI() {
        frame = new JFrame("Online Examination System");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));
        loginPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        loginPanel.add(passwordField);
        
        JButton loginButton = new JButton("Login");
        loginPanel.add(loginButton);

        frame.add(loginPanel, BorderLayout.NORTH);
        loginButton.addActionListener(e -> login());

        frame.setVisible(true);
    }

    private void login() {
        String inputUsername = usernameField.getText();
        String inputPassword = passwordField.getText();

        if (inputUsername.equals(username) && inputPassword.equals(password)) {
            isLoggedIn = true;
            JOptionPane.showMessageDialog(frame, "Login successful!");
            frame.remove(frame.getContentPane());
            showMainMenu();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid username or password.");
        }
    }

    private void showMainMenu() {
        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new GridLayout(3, 1));
        
        JButton updatePasswordButton = new JButton("Update Password");
        JButton startExamButton = new JButton("Start Exam");
        JButton logoutButton = new JButton("Logout");

        mainMenuPanel.add(updatePasswordButton);
        mainMenuPanel.add(startExamButton);
        mainMenuPanel.add(logoutButton);
        
        frame.add(mainMenuPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();

        updatePasswordButton.addActionListener(e -> updatePassword());
        startExamButton.addActionListener(e -> startExam());
        logoutButton.addActionListener(e -> logout());
    }

    private void updatePassword() {
        if (!isLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Please log in first.");
            return;
        }

        newPasswordField = new JTextField();
        int result = JOptionPane.showConfirmDialog(frame, newPasswordField, "Enter new password", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            password = newPasswordField.getText();
            JOptionPane.showMessageDialog(frame, "Password updated successfully.");
        }
    }

    private void startExam() {
        if (!isLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Please log in first.");
            return;
        }

        currentQuestionIndex = 0;
        score = 0;

        JFrame examFrame = new JFrame("Exam");
        examFrame.setSize(400, 400);
        examFrame.setLayout(new BorderLayout());

        questionArea = new JTextArea();
        questionArea.setEditable(false);
        examFrame.add(questionArea, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4, 1)); // Adjusted for 3 answers + Submit button
        answerOptions = new JRadioButton[3];
        answersGroup = new ButtonGroup();

        for (int i = 0; i < 3; i++) {
            answerOptions[i] = new JRadioButton();
            optionsPanel.add(answerOptions[i]);
            answersGroup.add(answerOptions[i]);
        }

        JButton submitButton = new JButton("Submit");
        optionsPanel.add(submitButton);
        examFrame.add(optionsPanel, BorderLayout.CENTER);
        
        submitButton.addActionListener(e -> {
            checkAnswer();
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.length) {
                displayNextQuestion();
            } else {
                JOptionPane.showMessageDialog(examFrame, "Exam ended. Your score is: " + score);
                examFrame.dispose();
                endSession();
            }
        });

        displayNextQuestion();
        
        examFrame.setVisible(true);
    }

    private void displayNextQuestion() {
        questionArea.setText(questions[currentQuestionIndex]);
        for (int i = 0; i < answerOptions.length; i++) {
            answerOptions[i].setSelected(false);
            answerOptions[i].setText("Option " + (i + 1)); 
        }

        answerOptions[0].setText("Option 1");
        answerOptions[1].setText("Option 2");
        answerOptions[2].setText("Option 3");
    }

    private void checkAnswer() {
        for (int i = 0; i < answerOptions.length; i++) {
            if (answerOptions[i].isSelected() && correctAnswers[currentQuestionIndex] == (i + 1)) {
                score++;
            }
        }
    }

    private void endSession() {
        isLoggedIn = false;
        JOptionPane.showMessageDialog(frame, "Logged out successfully.");
        frame.dispose();
    }

    private void logout() {
        isLoggedIn = false;
        JOptionPane.showMessageDialog(frame, "Logged out successfully.");
        frame.dispose();
        new OnlineExaminationGUI(); 
    }

    public static void main(String[] args) {
        new OnlineExaminationGUI();
    }
}
