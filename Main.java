public class Main {
    public static void main(String[] args) {
        UserController userController = new UserController();
        LoginScreen loginScreen = new LoginScreen(userController);
        loginScreen.setVisible(true);
    }
}