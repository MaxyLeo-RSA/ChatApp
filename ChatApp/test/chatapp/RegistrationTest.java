package chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationTest {

    @Test
    void testUserNameCorrectlyFormatted() {
        Registration reg = new Registration();
        reg.username = "Kyl_1"; // valid username
        assertTrue(reg.checkUserName(), "Username should be correctly formatted");
    }

    @Test
    void testUserNameIncorrectlyFormatted() {
        Registration reg = new Registration();
        reg.username = "Kyle!!!!!"; // invalid username
        assertFalse(reg.checkUserName(), "Username should be incorrectly formatted");
    }

    @Test
    void testPasswordMeetsComplexity() {
        Registration reg = new Registration();
        reg.password = "C*h8&sce@ke99!"; // meets all requirements
        assertTrue(reg.checkPasswordComplexity(), "Password should meet complexity rules");
    }

    @Test
    void testPasswordDoesNotMeetComplexity() {
        Registration reg = new Registration();
        reg.password = "password"; // too weak
        assertFalse(reg.checkPasswordComplexity(), "Password should fail complexity rules");
    }

    @Test
    void testCellPhoneCorrectlyFormatted() {
        Registration reg = new Registration();
        reg.cellNumber = "27833669876"; // valid SA number
        assertTrue(reg.checkCellPhoneNumber(), "Cell number should be valid");
    }

    @Test
    void testCellPhoneIncorrectlyFormatted() {
        Registration reg = new Registration();
        reg.cellNumber = "08966553"; // invalid
        assertFalse(reg.checkCellPhoneNumber(), "Cell number should be invalid");
    }

    @Test
    void testRegisterUserSuccessMessage() {
        Registration reg = new Registration();
        reg.firstName = "Kyle";
        reg.lastName = "Smith";
        String expected = "Registration successful for Kyle Smith";
        assertEquals(expected, reg.registerUser());
    }
}
