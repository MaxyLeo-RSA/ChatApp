package chatapp;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class ChatAppArrayManagerTest {
    
    private ChatAppArrayManager arrayManager;
    
    @Before
    public void setUp() {
        arrayManager = new ChatAppArrayManager();
        setupTestData();
    }
    
    private void setupTestData() {
        // Test Data Message 1: Sent
        Message msg1 = new Message();
        msg1.setRecipient("+27834557896");
        msg1.setMessage("Did you get the cake?");
        msg1.setStatus("sent");
        arrayManager.addToSentMessages(msg1);
        
        // Test Data Message 2: Stored
        Message msg2 = new Message();
        msg2.setRecipient("+27838884567");
        msg2.setMessage("Where are you? You are late! I have asked you to be on time.");
        msg2.setStatus("stored");
        arrayManager.addToStoredMessages(msg2);
        
        // Test Data Message 3: Disregarded
        Message msg3 = new Message();
        msg3.setRecipient("+27834484567");
        msg3.setMessage("Yohoooo, I am at your gate.");
        msg3.setStatus("discarded");
        arrayManager.addToDisregardedMessages(msg3);
        
        // Test Data Message 4: Sent
        Message msg4 = new Message();
        msg4.setRecipient("0838884567");
        msg4.setMessage("It is dinner time !");
        msg4.setStatus("sent");
        arrayManager.addToSentMessages(msg4);
        
        // Test Data Message 5: Stored
        Message msg5 = new Message();
        msg5.setRecipient("+27838884567");
        msg5.setMessage("Ok, I am leaving without you.");
        msg5.setStatus("stored");
        arrayManager.addToStoredMessages(msg5);
    }
    
    // Test 1: Arrays correctly populated
    @Test
    public void testSentMessagesArrayPopulated() {
        ArrayList<Message> sentMessages = arrayManager.getSentMessages();
        assertEquals("Sent messages array should contain 2 messages", 2, sentMessages.size());
        
        // Check specific messages
        boolean foundMessage1 = false;
        boolean foundMessage4 = false;
        
        for (Message msg : sentMessages) {
            if ("Did you get the cake?".equals(msg.getMessage())) {
                foundMessage1 = true;
            }
            if ("It is dinner time !".equals(msg.getMessage())) {
                foundMessage4 = true;
            }
        }
        
        assertTrue("Should find 'Did you get the cake?' in sent messages", foundMessage1);
        assertTrue("Should find 'It is dinner time!' in sent messages", foundMessage4);
    }
    
    // Test 2: Display the longest message
    @Test
    public void testDisplayLongestMessage() {
        Message longest = arrayManager.findLongestMessage();
        assertNotNull("Longest message should not be null", longest);
        
        // The longest message in sent array should be "It is dinner time !"
        assertEquals("Longest message should be correct", 
                     "It is dinner time !", longest.getMessage());
    }
    
    // Test 3: Search for message by recipient
    @Test
    public void testSearchMessagesByRecipient() {
        String searchRecipient = "+27838884567";
        ArrayList<Message> results = arrayManager.searchMessagesByRecipient(searchRecipient);
        
        assertEquals("Should find 2 messages for recipient +27838884567", 2, results.size());
        
        boolean foundMessage2 = false;
        boolean foundMessage5 = false;
        
        for (Message msg : results) {
            if ("Where are you? You are late! I have asked you to be on time.".equals(msg.getMessage())) {
                foundMessage2 = true;
            }
            if ("Ok, I am leaving without you.".equals(msg.getMessage())) {
                foundMessage5 = true;
            }
        }
        
        assertTrue("Should find message 2", foundMessage2);
        assertTrue("Should find message 5", foundMessage5);
    }
    
    // Test 4: Delete message by hash
    @Test
    public void testDeleteMessageByHash() {
        // Get hash of message 2 to delete
        String hashToDelete = "";
        for (Message msg : arrayManager.getStoredMessages()) {
            if ("Where are you? You are late! I have asked you to be on time.".equals(msg.getMessage())) {
                hashToDelete = msg.getMessageHash();
                break;
            }
        }
        
        boolean deleted = arrayManager.deleteMessageByHash(hashToDelete);
        
        assertTrue("Message should be deleted", deleted);
        assertEquals("Stored messages should have 1 item after deletion", 
                     1, arrayManager.getStoredMessages().size());
        assertFalse("Message hash should be removed from hashes array", 
                    arrayManager.getMessageHashes().contains(hashToDelete));
    }
    
    // Test 5: Message ID search
    @Test
    public void testSearchMessageByID() {
        // Get ID of message 4
        String searchID = "";
        for (Message msg : arrayManager.getSentMessages()) {
            if ("It is dinner time !".equals(msg.getMessage())) {
                searchID = msg.getMessageID();
                break;
            }
        }
        
        Message foundMessage = arrayManager.searchMessageByID(searchID);
        
        assertNotNull("Should find message by ID", foundMessage);
        assertEquals("Found message should have correct recipient", 
                     "0838884567", foundMessage.getRecipient());
        assertEquals("Found message should have correct content", 
                     "It is dinner time !", foundMessage.getMessage());
    }
    
    // Test 6: Array sizes consistency
    @Test
    public void testArraySizesConsistency() {
        int totalMessagesInArrays = arrayManager.getSentMessages().size() + arrayManager.getStoredMessages().size();
        assertEquals("Message hashes array size should match total messages", 
                     totalMessagesInArrays, arrayManager.getMessageHashes().size());
        assertEquals("Message IDs array size should match total messages", 
                     totalMessagesInArrays, arrayManager.getMessageIDs().size());
    }
    
    // Test 7: Test message statuses
    @Test
    public void testMessageStatuses() {
        for (Message msg : arrayManager.getSentMessages()) {
            assertEquals("Sent messages should have 'sent' status", "sent", msg.getStatus());
        }
        
        for (Message msg : arrayManager.getStoredMessages()) {
            assertEquals("Stored messages should have 'stored' status", "stored", msg.getStatus());
        }
        
        for (Message msg : arrayManager.getDisregardedMessages()) {
            assertEquals("Disregarded messages should have 'discarded' status", "discarded", msg.getStatus());
        }
    }
    
    // Test 8: Test senders and recipients display
    @Test
    public void testDisplaySendersAndRecipients() {
        String result = arrayManager.displaySendersAndRecipients("Test User");
        assertNotNull("Result should not be null", result);
        assertTrue("Result should contain sender information", result.contains("Test User"));
        assertTrue("Result should contain recipient information", result.contains("+27834557896"));
        assertTrue("Result should contain recipient information", result.contains("0838884567"));
    }
    
    // Test 9: Test full report generation
    @Test
    public void testGenerateFullReport() {
        String report = arrayManager.generateFullReport("Test User");
        assertNotNull("Report should not be null", report);
        assertTrue("Report should contain message details", report.contains("Did you get the cake?"));
        assertTrue("Report should contain summary", report.contains("SUMMARY"));
        assertTrue("Report should contain total count", report.contains("Total Sent Messages"));
    }
    
    // Test 10: Test empty arrays
    @Test
    public void testEmptyArrays() {
        ChatAppArrayManager emptyManager = new ChatAppArrayManager();
        
        Message longest = emptyManager.findLongestMessage();
        assertNull("Longest message should be null for empty arrays", longest);
        
        ArrayList<Message> results = emptyManager.searchMessagesByRecipient("+27838884567");
        assertTrue("Search results should be empty for empty arrays", results.isEmpty());
        
        String sendersInfo = emptyManager.displaySendersAndRecipients("Test User");
        assertTrue("Should indicate no sent messages", sendersInfo.contains("No sent messages"));
        
        String report = emptyManager.generateFullReport("Test User");
        assertTrue("Should indicate no sent messages for report", report.contains("No sent messages"));
    }
    
    // Test 11: Test array addition methods
    @Test
    public void testArrayAdditionMethods() {
        ChatAppArrayManager testManager = new ChatAppArrayManager();
        
        Message testMessage = new Message();
        testMessage.setRecipient("+27123456789");
        testMessage.setMessage("Test message");
        testMessage.setStatus("sent");
        
        // Test adding to sent messages
        testManager.addToSentMessages(testMessage);
        assertEquals("Sent messages should have 1 message", 1, testManager.getSentMessages().size());
        assertTrue("Message hash should be added", testManager.getMessageHashes().contains(testMessage.getMessageHash()));
        assertTrue("Message ID should be added", testManager.getMessageIDs().contains(testMessage.getMessageID()));
        
        // Test adding to stored messages
        Message storedMessage = new Message();
        storedMessage.setRecipient("+27123456789");
        storedMessage.setMessage("Stored message");
        storedMessage.setStatus("stored");
        
        testManager.addToStoredMessages(storedMessage);
        assertEquals("Stored messages should have 1 message", 1, testManager.getStoredMessages().size());
        
        // Test adding to disregarded messages
        Message disregardedMessage = new Message();
        disregardedMessage.setRecipient("+27123456789");
        disregardedMessage.setMessage("Discarded message");
        disregardedMessage.setStatus("discarded");
        
        testManager.addToDisregardedMessages(disregardedMessage);
        assertEquals("Disregarded messages should have 1 message", 1, testManager.getDisregardedMessages().size());
    }
}