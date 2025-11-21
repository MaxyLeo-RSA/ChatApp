package chatapp;

public class Login {
    public boolean checkLogin(Registration user, String enteredUser, String enteredPass) {
        return user.username.equals(enteredUser) && user.password.equals(enteredPass);
    }

    public String returnLoginStatus(boolean isSuccessful, Registration user) {
        if (isSuccessful) {
            return "Welcome " + user.firstName + " " + user.lastName + ", it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }
}