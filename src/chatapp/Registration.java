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

    // FIXED: Proper South African cell phone number validation
    public boolean checkCellPhoneNumber() {
        if (cellNumber == null || cellNumber.isEmpty()) {
            return false;
        }
        
        // Format 1: +27XXXXXXXXX (12 characters total)
        if (cellNumber.startsWith("+27") && cellNumber.length() == 12) {
            String numberPart = cellNumber.substring(3); // Get the part after +27
            if (numberPart.matches("\\d{9}")) {
                // Check if it starts with 6, 7, or 8 (common SA prefixes)
                char firstDigit = numberPart.charAt(0);
                return firstDigit == '6' || firstDigit == '7' || firstDigit == '8';
            }
        }
        
        // Format 2: 07XXXXXXXX (10 characters - local format)
        if (cellNumber.startsWith("07") && cellNumber.length() == 10) {
            String numberPart = cellNumber.substring(2); // Get the part after 07
            return numberPart.matches("\\d{8}");
        }
        
        // Format 3: 27XXXXXXXXX (11 characters - without +)
        if (cellNumber.startsWith("27") && cellNumber.length() == 11) {
            String numberPart = cellNumber.substring(2); // Get the part after 27
            if (numberPart.matches("\\d{9}")) {
                char firstDigit = numberPart.charAt(0);
                return firstDigit == '6' || firstDigit == '7' || firstDigit == '8';
            }
        }
        
        // Format 4: 0XXXXXXXXX (10 characters - other local formats)
        if (cellNumber.startsWith("0") && cellNumber.length() == 10 && !cellNumber.startsWith("07")) {
            String numberPart = cellNumber.substring(1); // Get the part after 0
            return numberPart.matches("\\d{9}");
        }
        
        return false;
    }

    public String registerUser() {
        return "Registration successful for " + firstName + " " + lastName;
    }
}
