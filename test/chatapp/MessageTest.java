package chatapp;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class MessageTest {
    
    private Message message;
    
    @Before
    public void setUp() {
        message = new Message();
    }
    
    // Test 1: Message should not be more than 250 characters
    @Test
    public void testMessageLengthSuccess() {
        String shortMessage = "This is a short message";
        message.setMessage(shortMessage);
        assertTrue("Message should be valid when under 250 characters", 
                   shortMessage.length() <= 250);
    }
    
    @Test
    public void testMessageLengthFailure() {
        String longMessage = "A".repeat(251);
        message.setMessage(longMessage);
        assertTrue("Message should be invalid when over 250 characters", 
                   longMessage.length() > 250);
    }
    
    // Test 2: Recipient number is correctly formatted (South African)
    @Test
    public void testRecipientNumberSuccessInternational() {
        String validNumber = "+27718693002";
        int result = message.checkRecipientCell(validNumber);
        assertEquals("Valid international SA number should return 1", 1, result);
    }
    
    @Test
    public void testRecipientNumberSuccessLocal() {
        String validNumber = "0712345678";
        int result = message.checkRecipientCell(validNumber);
        assertEquals("Valid local SA number should return 1", 1, result);
    }
    
    @Test
    public void testRecipientNumberFailure() {
        String invalidNumber = "1234567890"; // No country code
        int result = message.checkRecipientCell(invalidNumber);
        assertEquals("Invalid number should return 0", 0, result);
    }
    
    // Test 3: Message hash is correct
    @Test
    public void testMessageHashGeneration() {
        message.setMessageID("1234567890");
        message.setMessage("Hi Mike, can you join us for dinner tonight");
        
        String expectedHashStart = "12:1:HI"; // First two digits of ID + count + first word
        String actualHash = message.getMessageHash();
        
        assertTrue("Message hash should start with correct format", 
                   actualHash.startsWith(expectedHashStart));
        assertTrue("Message hash should contain TONIGHT", 
                   actualHash.contains("TONIGHT"));
    }
    
    @Test
    public void testMessageHashWithTestData1() {
        // Test with specific data from requirements
        Message testMessage = new Message();
        testMessage.setMessageID("1234567890");
        testMessage.setMessage("Hi Mike, can you join us for dinner tonight");
        
        // Hash format: firstTwoID:count:firstWordLastWord
        String hash = testMessage.getMessageHash();
        assertNotNull("Message hash should not be null", hash);
        assertTrue("Hash should contain colon separators", hash.contains(":"));
    }
    
    // Test 4: Message ID is created and valid
    @Test
    public void testMessageIDCreation() {
        String messageID = message.getMessageID();
        assertNotNull("Message ID should not be null", messageID);
        assertEquals("Message ID should be 10 digits long", 10, messageID.length());
        assertTrue("Message ID should contain only digits", messageID.matches("\\d+"));
        assertFalse("Message ID should not start with 0", messageID.startsWith("0"));
    }
    
    // Test 5: Message sent with different options
    @Test
    public void testSendMessageOption1() {
        String result = message.sentMessage(1);
        assertEquals("Send option should return success message", 
                     "Message sent successfully!", result);
        assertEquals("Status should be 'sent'", "sent", message.getStatus());
    }
    
    @Test
    public void testSendMessageOption2() {
        String result = message.sentMessage(2);
        assertEquals("Discard option should return discard message", 
                     "Message discarded", result);
        assertEquals("Status should be 'discarded'", "discarded", message.getStatus());
    }
    
    @Test
    public void testSendMessageOption3() {
        String result = message.sentMessage(3);
        assertEquals("Store option should return store message", 
                     "Message stored successfully!", result);
        assertEquals("Status should be 'stored'", "stored", message.getStatus());
    }
    
    @Test
    public void testSendMessageInvalidOption() {
        String result = message.sentMessage(4);
        assertEquals("Invalid option should return error", 
                     "Invalid option", result);
    }
    
    // Test 6: Message count increments
    @Test
    public void testMessageCountIncrement() {
        Message firstMessage = new Message();
        Message secondMessage = new Message();
        
        assertTrue("Second message should have higher count", 
                   secondMessage.getMessageCount() > firstMessage.getMessageCount());
    }
    
    // Test 7: Format South African numbers
    @Test
    public void testFormatSouthAfricanNumber() {
        Message testMessage = new Message();
        
        // Test local to international conversion
        testMessage.setRecipient("0712345678");
        assertEquals("Local number should be formatted to international", 
                     "+2712345678", testMessage.getRecipient());
        
        // Test national to international conversion
        testMessage.setRecipient("27123456789");
        assertEquals("National number should be formatted to international", 
                     "+27123456789", testMessage.getRecipient());
    }
    
    // Test 8: Check message ID validation
    @Test
    public void testMessageIDValidation() {
        assertTrue("Valid 10-digit ID should pass", message.checkMessageID());
        
        // Test with invalid ID
        message.setMessageID("123456789"); // 9 digits
        assertFalse("9-digit ID should fail", message.checkMessageID());
        
        message.setMessageID("0123456789"); // Starts with 0
        assertFalse("ID starting with 0 should fail", message.checkMessageID());
    }
}