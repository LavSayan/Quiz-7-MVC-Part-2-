import java.util.ArrayList;
import java.util.List;

public class UserController {
    private List<User> users = new ArrayList<>();

    public UserController() {
        // Add predefined users
        users.add(new User("admin", "admin123", "Admin"));
        users.add(new User("user1", "password1", "User"));
        users.add(new User("user2", "password2", "User"));
    }

    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user; // Return the matching user if credentials are correct
            }
        }
        return null; // Return null if no match is found
    }

    public boolean addUser(User user) {
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername())) {
                return false; // User already exists
            }
        }
        users.add(user);
        return true;
    }

    public void updateUser(User user) {
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername())) {
                existingUser.setPassword(user.getPassword());
                existingUser.setType(user.getType());
                return;
            }
        }
    }

    public void deleteUser(String username) {
        users.removeIf(user -> user.getUsername().equals(username));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    
}