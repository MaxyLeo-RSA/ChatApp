package chatapp;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class RegistrationTest {
    
    private Registration user;
    
    @Before
    public void setUp() {
        user = new Registration();
        user.firstName = "John";
        user.lastName = "Doe";
    }
    
    // Username tests with specific test data
    @Test
    public void testUsernameCorrectlyFormatted() {
        user.username = "v__1";
        assertTrue("Username 'v__1' with underscore and ≤5 chars should be valid", 
                   user.checkUserName());
    }
    
    @Test
    public void testUsernameIncorrectlyFormattedNoUnderscore() {
        user.username = "vell!!!!!";
        assertFalse("Username 'vell!!!!!' without underscore should be invalid", 
                    user.checkUserName());
    }
    
    @Test
    public void testUsernameIncorrectlyFormattedTooLong() {
        user.username = "user_123";
        assertFalse("Username longer than 5 chars should be invalid", 
                    user.checkUserName());
    }
    
    @Test
    public void testUsernameIncorrectlyFormattedShortNoUnderscore() {
        user.username = "user";
        assertFalse("Username 'user' without underscore should be invalid", 
                    user.checkUserName());
    }
    
    // Password tests with specific test data
    @Test
    public void testPasswordMeetsComplexityRequirements() {
        user.password = "Ch&&sec@ke991";
        assertTrue("Password 'Ch&&sec@ke991' should meet complexity requirements", 
                   user.checkPasswordComplexity());
    }
    
    @Test
    public void testPasswordDoesNotMeetComplexityRequirements() {
        user.password = "password";
        assertFalse("Password 'password' should not meet complexity requirements", 
                    user.checkPasswordComplexity());
    }
    
    @Test
    public void testPasswordNoUppercase() {
        user.password = "password123!";
        assertFalse("Password without uppercase should be invalid", 
                    user.checkPasswordComplexity());
    }
    
    @Test
    public void testPasswordNoDigit() {
        user.password = "Password!";
        assertFalse("Password without digit should be invalid", 
                    user.checkPasswordComplexity());
    }
    
    @Test
    public void testPasswordNoSpecialChar() {
        user.password = "Password123";
        assertFalse("Password without special character should be invalid", 
                    user.checkPasswordComplexity());
    }
    
    @Test
    public void testPasswordTooShort() {
        user.password = "Pass1!";
        assertFalse("Password shorter than 8 chars should be invalid", 
                    user.checkPasswordComplexity());
    }
    
    // Cell phone tests with specific test data
    @Test
    public void testCellPhoneCorrectlyFormatted() {
        user.cellNumber = "+27838968976";
        assertTrue("Cell number '+27838968976' should be valid", 
                   user.checkCellPhoneNumber());
    }
    
    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        user.cellNumber = "08966553";
        assertFalse("Cell number '08966553' should be invalid", 
                    user.checkCellPhoneNumber());
    }
    
    @Test
    public void testCellPhoneLocalFormat() {
        user.cellNumber = "0712345678";
        assertTrue("Cell number '0712345678' should be valid", 
                   user.checkCellPhoneNumber());
    }
    
    @Test
    public void testCellPhoneInvalidInternational() {
        user.cellNumber = "+44896655321"; // UK number, not SA
        assertFalse("Non-SA international number should be invalid", 
                    user.checkCellPhoneNumber());
    }
    
    // First name and last name tests
    @Test
    public void testFirstNameValidation() {
        user.firstName = "John";
        assertTrue("Non-empty first name should be valid", user.checkFirstName());
        
        user.firstName = "";
        assertFalse("Empty first name should be invalid", user.checkFirstName());
        
        user.firstName = "   ";
        assertFalse("Whitespace first name should be invalid", user.checkFirstName());
    }
    
    @Test
    public void testLastNameValidation() {
        user.lastName = "Doe";
        assertTrue("Non-empty last name should be valid", user.checkLastName());
        
        user.lastName = "";
        assertFalse("Empty last name should be invalid", user.checkLastName());
        
        user.lastName = "   ";
        assertFalse("Whitespace last name should be invalid", user.checkLastName());
    }
    
    // Test registration success message
    @Test
    public void testRegisterUser() {
        user.firstName = "John";
        user.lastName = "Doe";
        String result = user.registerUser();
        assertEquals("Registration should return success message", 
                     "Registration successful for John Doe", result);
    }
    
    // Test instruction messages
    @Test
    public void testFirstNameInstructions() {
        String instructions = user.firstNameInstructions();
        assertEquals("First name instructions should be correct", 
                     "First name cannot be empty.", instructions);
    }
    
    @Test
    public void testLastNameInstructions() {
        String instructions = user.lastNameInstructions();
        assertEquals("Last name instructions should be correct", 
                     "Last name cannot be empty.", instructions);
    }
}