import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainUserScreen extends JFrame {
    private JList<User> userList;
    private DefaultListModel<User> userListModel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField typeField;
    private UserController userController;
    private int currentIndex = 0; // Tracks the current user in the list

    public MainUserScreen(UserController userController) {
        this.userController = userController;

        setTitle("User Management System");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        add(new JScrollPane(userList), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        inputPanel.add(new JLabel("Type:"));
        typeField = new JTextField();
        inputPanel.add(typeField);

        add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add User");
        JButton editButton = new JButton("Edit User");
        JButton deleteButton = new JButton("Delete User");
        JButton previousButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        JButton backButton = new JButton("Back");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editUser();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPreviousUser();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextUser();
            }
        });

        // Add functionality to the Back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToLogin();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadUserList();
        setVisible(true);
    }

    private void loadUserList() {
        userListModel.clear();
        List<User> users = userController.getAllUsers();
        for (User user : users) {
            userListModel.addElement(user);
        }
        if (!users.isEmpty()) {
            currentIndex = 0; // Reset to the first user
            displayUser(users.get(currentIndex));
        }
    }

    private void addUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String type = typeField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || type.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userController.addUser(new User(username, password, type))) {
            loadUserList();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "User already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editUser() {
        User selectedUser = userList.getSelectedValue();
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "No user selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String password = new String(passwordField.getPassword()).trim();
        String type = typeField.getText().trim();

        if (password.isEmpty() || type.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password and Type fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        selectedUser.setPassword(password);
        selectedUser.setType(type);
        userController.updateUser(selectedUser);
        loadUserList();
        clearFields();
    }

    private void deleteUser() {
        User selectedUser = userList.getSelectedValue();
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "No user selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        userController.deleteUser(selectedUser.getUsername());
        loadUserList();
        clearFields();
    }

    private void showPreviousUser() {
        List<User> users = userController.getAllUsers();
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users available!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        currentIndex = (currentIndex - 1 + users.size()) % users.size(); // Cycle to the previous user
        displayUser(users.get(currentIndex));
    }

    private void showNextUser() {
        List<User> users = userController.getAllUsers();
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users available!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        currentIndex = (currentIndex + 1) % users.size(); // Cycle to the next user
        displayUser(users.get(currentIndex));
    }

    private void displayUser(User user) {
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        typeField.setText(user.getType());
    }

    private void goBackToLogin() {
        dispose(); // Close the current screen
        new LoginScreen(userController); // Open the login screen
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        typeField.setText("");
    }
}