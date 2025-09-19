
package chatapp;

/**
 *
 * @author ST10361951
 */

public class Registration {
    String firstName;
    String lastName;
    String username;
    String password;
    String cellNumber;

    // Check if username has an underscore and is no longer than 5 characters
    public boolean checkUserName() {
        return username.contains("_") && username.length() <= 5;
    }
      // Validate first name (must not be null)
    public boolean checkFirstName() {
        return firstName != null;
    }

    public String firstNameInstructions() {
        return "First name cannot be null.";
    }

    // Validate last name (must not be null)
    public boolean checkLastName() {
        return lastName != null;
    }

    public String lastNameInstructions() {
        return "Last name cannot be null.";
    }

    // Check if password meets complexity rules
    public boolean checkPasswordComplexity() {
        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        // Password must be at least 8 characters long
        if (password.length() < 8) {
            return false;
        }

        // Check each character in the password
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }

        return hasUppercase && hasDigit && hasSpecialChar;
    }

    // Check if the cellphone number is a valid South African mobile number
  /*
 * Reference:
 * OpenAI (2025) ChatGPT [AI language model]. 
 * Available at: https://chat.openai.com/ (Accessed: 19 September 2025)
 */
    public boolean checkCellPhoneNumber() {
        if (cellNumber.length() == 11 && cellNumber.startsWith("27")) {
            char thirdDigit = cellNumber.charAt(2); // get the 3rd digit
            return thirdDigit == '6' || thirdDigit == '7' || thirdDigit == '8';
        }
        return false;
    }

    // Return a registration success message
    public String registerUser() {
        return "Registration successful for " + firstName + " " + lastName;
    }
}