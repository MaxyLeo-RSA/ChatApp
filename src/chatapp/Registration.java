package chatapp;

public class Registration {
    public String firstName;
    public String lastName;
    public String username;
    public String password;
    public String cellNumber;

    public boolean checkUserName() {
        if (username == null) return false;
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkFirstName() {
        return firstName != null && !firstName.trim().isEmpty();
    }

    public String firstNameInstructions() {
        return "First name cannot be empty.";
    }

    public boolean checkLastName() {
        return lastName != null && !lastName.trim().isEmpty();
    }

    public String lastNameInstructions() {
        return "Last name cannot be empty.";
    }

    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

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

    public boolean checkCellPhoneNumber() {
        if (cellNumber == null || cellNumber.isEmpty()) {
            return false;
        }
        
        if (cellNumber.startsWith("+27") && cellNumber.length() == 12) {
            String digits = cellNumber.substring(1);
            return digits.matches("\\d+");
        }
        return false;
    }

    public String registerUser() {
        return "Registration successful for " + firstName + " " + lastName;
    }
}