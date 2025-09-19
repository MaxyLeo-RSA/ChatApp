
package chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    private Registration reg;
    private Login login;


    @Test
    void testSuccessfulLogin() {
        assertTrue(login.checkLogin(reg, "al_ce", "Passw0rd!"));
    }

    @Test
    void testFailedLogin_WrongUsername() {
        assertFalse(login.checkLogin(reg, "wrong", "Passw0rd!"));
    }

    @Test
    void testFailedLogin_WrongPassword() {
        assertFalse(login.checkLogin(reg, "al_ce", "wrongPass"));
    }

    @Test
    void testFailedLogin_WrongBoth() {
        assertFalse(login.checkLogin(reg, "wrong", "wrong"));
    }
}

