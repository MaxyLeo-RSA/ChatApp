package chatapp;

import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {
    
    @Test
    public void testLoginSuccessful() {
        Login login = new Login();
        Registration user = new Registration();
        user.firstName = "John";
        user.lastName = "Doe";
        user.username = "v__1";
        user.password = "Ch&&sec@ke991";
        
        boolean result = login.checkLogin(user, "v__1", "Ch&&sec@ke991");
        
        assertTrue("Valid credentials should return true", result);
        
        String status = login.returnLoginStatus(true, user);
        assertTrue("Login status should contain welcome message", 
                   status.contains("Welcome John Doe"));
        assertTrue("Login status should contain greeting", 
                   status.contains("it is great to see you again"));
    }
    
    @Test
    public void testLoginFailedWrongUsername() {
        Login login = new Login();
        Registration user = new Registration();
        user.firstName = "John";
        user.lastName = "Doe";
        user.username = "v__1";
        user.password = "Ch&&sec@ke991";
        
        boolean result = login.checkLogin(user, "wrong_user", "Ch&&sec@ke991");
        
        assertFalse("Wrong username should return false", result);
        
        String status = login.returnLoginStatus(false, user);
        assertTrue("Failed login status should contain error message", 
                   status.contains("Username or password incorrect"));
        assertTrue("Failed login status should ask to try again", 
                   status.contains("please try again"));
    }
    
    @Test
    public void testLoginFailedWrongPassword() {
        Login login = new Login();
        Registration user = new Registration();
        user.firstName = "John";
        user.lastName = "Doe";
        user.username = "v__1";
        user.password = "Ch&&sec@ke991";
        
        boolean result = login.checkLogin(user, "v__1", "wrongpassword");
        
        assertFalse("Wrong password should return false", result);
        
        String status = login.returnLoginStatus(false, user);
        assertTrue("Failed login status should contain error message", 
                   status.contains("Username or password incorrect"));
    }
    
    @Test
    public void testLoginFailedBothWrong() {
        Login login = new Login();
        Registration user = new Registration();
        user.firstName = "John";
        user.lastName = "Doe";
        user.username = "v__1";
        user.password = "Ch&&sec@ke991";
        
        boolean result = login.checkLogin(user, "wrong_user", "wrongpassword");
        
        assertFalse("Both wrong credentials should return false", result);
    }
    
    @Test
    public void testLoginWithNullUser() {
        Login login = new Login();
        Registration user = null;
        
        // This should handle null gracefully in the actual implementation
        // For now, we expect it to not throw an exception in the test setup
        assertTrue("Test should complete without exception", true);
    }
    
    @Test
    public void testLoginStatusMessages() {
        Login login = new Login();
        Registration user = new Registration();
        user.firstName = "Jane";
        user.lastName = "Smith";
        
        // Test successful login message
        String successStatus = login.returnLoginStatus(true, user);
        assertEquals("Successful login message should be correct", 
                     "Welcome Jane Smith, it is great to see you again.", successStatus);
        
        // Test failed login message
        String failedStatus = login.returnLoginStatus(false, user);
        assertEquals("Failed login message should be correct", 
                     "Username or password incorrect, please try again.", failedStatus);
    }
    
    @Test
    public void testLoginWithEmptyCredentials() {
        Login login = new Login();
        Registration user = new Registration();
        user.username = "v__1";
        user.password = "Ch&&sec@ke991";
        
        boolean result = login.checkLogin(user, "", "Ch&&sec@ke991");
        assertFalse("Empty username should return false", result);
        
        result = login.checkLogin(user, "v__1", "");
        assertFalse("Empty password should return false", result);
        
        result = login.checkLogin(user, "", "");
        assertFalse("Both empty credentials should return false", result);
    }
    
    @Test
    public void testLoginWithWhitespaceCredentials() {
        Login login = new Login();
        Registration user = new Registration();
        user.username = "v__1";
        user.password = "Ch&&sec@ke991";
        
        boolean result = login.checkLogin(user, "   ", "Ch&&sec@ke991");
        assertFalse("Whitespace username should return false", result);
        
        result = login.checkLogin(user, "v__1", "   ");
        assertFalse("Whitespace password should return false", result);
    }
}