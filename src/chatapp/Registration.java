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

    // South African cell phone number validation
    public boolean checkCellPhoneNumber() {
        if (cellNumber == null || cellNumber.isEmpty()) {
            return false;
        }
        
        // Format 1: +27XXXXXXXXX (12 characters total)
        if (cellNumber.startsWith("+27") && cellNumber.length() == 12) {
            String numberPart = cellNumber.substring(3);
            if (numberPart.matches("\\d{9}")) {
                char firstDigit = numberPart.charAt(0);
                return firstDigit == '6' || firstDigit == '7' || firstDigit == '8';
            }
        }
        
        // Format 2: 07XXXXXXXX (10 characters - local format)
        if (cellNumber.startsWith("07") && cellNumber.length() == 10) {
            String numberPart = cellNumber.substring(2);
            return numberPart.matches("\\d{8}");
        }
        
        // Format 3: 27XXXXXXXXX (11 characters - without +)
        if (cellNumber.startsWith("27") && cellNumber.length() == 11) {
            String numberPart = cellNumber.substring(2);
            if (numberPart.matches("\\d{9}")) {
                char firstDigit = numberPart.charAt(0);
                return firstDigit == '6' || firstDigit == '7' || firstDigit == '8';
            }
        }
        
        // Format 4: 0XXXXXXXXX (10 characters - other local formats)
        if (cellNumber.startsWith("0") && cellNumber.length() == 10 && !cellNumber.startsWith("07")) {
            String numberPart = cellNumber.substring(1);
            return numberPart.matches("\\d{9}");
        }
        
        return false;
    }

    // NEW: Method to format cell number to international format
    public String formatCellNumber(String number) {
        if (number == null) return number;
        
        // If it's in 07 format, convert to +27
        if (number.startsWith("07") && number.length() == 10) {
            return "+27" + number.substring(2);
        }
        
        // If it's in 0 format (other local), convert to +27
        if (number.startsWith("0") && number.length() == 10 && !number.startsWith("07")) {
            return "+27" + number.substring(1);
        }
        
        // If it's in 27 format (without +), add the +
        if (number.startsWith("27") && number.length() == 11 && !number.startsWith("+")) {
            return "+" + number;
        }
        
        // If it's already in +27 format, return as is
        if (number.startsWith("+27") && number.length() == 12) {
            return number;
        }
        
        return number;
    }

    // NEW: Setter that automatically formats the cell number
    public void setCellNumber(String cellNumber) {
        if (!checkCellPhoneNumber(cellNumber)) {
            throw new IllegalArgumentException("Invalid South African cell number: " + cellNumber);
        }
        this.cellNumber = formatCellNumber(cellNumber);
    }

    // CHANGED: Getter for cellNumber
    public String getCellNumber() {
        return cellNumber;
    }

    public String registerUser() {
        return "Registration successful for " + firstName + " " + lastName;
    }
}
