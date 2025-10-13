
package chatapp;

/**
 *
 * @author DELL
 */
import java.util.Scanner;

public class Login {
    public boolean checkLogin(Registration user, String enteredUser, String enteredPass) {
    return user.username.equals(enteredUser) && user.password.equals(enteredPass);
}

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Registration user = new Registration();

        // --- REGISTRATION ---
        System.out.println("=== USER REGISTRATION ===");

        // First name (To make it not empty )
        System.out.print("Enter first name: ");
        user.firstName = input.nextLine();
        while (user.firstName.equals("")) {
            System.out.println("First name cannot be empty.");
            System.out.print("Enter first name: ");
            user.firstName = input.nextLine();
        }

        // Last name  (To make it not empty )
        System.out.print("Enter last name: ");
        user.lastName = input.nextLine();
        while (user.lastName.equals("")) {
            System.out.println("Last name cannot be empty.");
            System.out.print("Enter last name: ");
            user.lastName = input.nextLine();
        }

        // Username (must contain underscore and be <= 5 characters)
        System.out.print("Enter username(must have a '_' and is no more than 5 characters ): ");
        user.username = input.nextLine();
        while (!user.checkUserName()) {
            System.out.println("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.");
            System.out.print("Enter username: ");
            user.username = input.nextLine();
        }
        System.out.println("Username successfully captured.");

        // Password (must be >= 8 characters, 1 uppercase, 1 digit, 1 special char)
        System.out.print("Enter password (must be >= 8 characters, 1 uppercase, 1 digit, 1 special char): ");
        user.password = input.nextLine();
        while (!user.checkPasswordComplexity()) {
            System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
            System.out.print("Enter password: ");
            user.password = input.nextLine();
        }
        System.out.println("Password successfully captured.");

        // Cellphone number (must start with "27", be 11 digits, and have 3rd digit = 6,7,8)
        System.out.print("Enter cell phone number (must start with \"27\", be 11 digits, and have 3rd digit = 6,7,8): ");
        user.cellNumber = input.nextLine();
        while (!user.checkCellPhoneNumber()) {
            System.out.println("Cell phone number is not correctly formatted; please ensure that your number contains 11 digits, starts with 27, and the third digit is 6, 7, or 8.");
            System.out.print("Enter cell phone number: ");
            user.cellNumber = input.nextLine();
        }
        System.out.println("Cell phone number successfully captured.");

        // Show registration result
        System.out.println("\n" + user.registerUser());

        // --- LOGIN ---
        System.out.println("\n=== USER LOGIN ===");
        System.out.print("Enter username: ");
        String enteredUser = input.nextLine();
        System.out.print("Enter password: ");
        String enteredPass = input.nextLine();

        // Check login credentials
        if (user.username.equals(enteredUser) && user.password.equals(enteredPass)) {
            System.out.println("Welcome " + user.firstName + " " + user.lastName + ", it is great to see you.");
        } else {
            System.out.println("Login failed. Username or password incorrect.");
        }

        input.close();
    }
}
