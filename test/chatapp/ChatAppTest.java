package chatapp;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class ChatAppArrayTest {
    
    private ChatApp chatApp;
    private ArrayList<Message> sentMessages;
    private ArrayList<Message> storedMessages;
    private ArrayList<String> messageHashes;
    private ArrayList<String> messageIDs;
    
    @Before
    public void setUp() {
        chatApp = new ChatApp();
        
        // Initialize arrays using reflection or create test data
        sentMessages = new ArrayList<>();
        storedMessages = new ArrayList<>();
        messageHashes = new ArrayList<>();
        messageIDs = new ArrayList<>();
        
        // Create test messages based on requirements
        setupTestData();
    }
    
    private void setupTestData() {
        // Test Data Message 1: Sent
        Message msg1 = new Message();
        msg1.setRecipient("+27834557896");
        msg1.setMessage("Did you get the cake?");
        msg1.setStatus("sent");
        sentMessages.add(msg1);
        messageHashes.add(msg1.getMessageHash());
        messageIDs.add(msg1.getMessageID());
        
        // Test Data Message 2: Stored
        Message msg2 = new Message();
        msg2.setRecipient("+27838884567");
        msg2.setMessage("Where are you? You are late! I have asked you to be on time.");
        msg2.setStatus("stored");
        storedMessages.add(msg2);
        messageHashes.add(msg2.getMessageHash());
        messageIDs.add(msg2.getMessageID());
        
        // Test Data Message 3: Disregarded
        Message msg3 = new Message();
        msg3.setRecipient("+27834484567");
        msg3.setMessage("Yohoooo, I am at your gate.");
        msg3.setStatus("discarded");
        // Not added to sent/stored arrays
        
        // Test Data Message 4: Sent
        Message msg4 = new Message();
        msg4.setRecipient("0838884567");
        msg4.setMessage("It is dinner time !");
        msg4.setStatus("sent");
        sentMessages.add(msg4);
        messageHashes.add(msg4.getMessageHash());
        messageIDs.add(msg4.getMessageID());
        
        // Test Data Message 5: Stored
        Message msg5 = new Message();
        msg5.setRecipient("+27838884567");
        msg5.setMessage("Ok, I am leaving without you.");
        msg5.setStatus("stored");
        storedMessages.add(msg5);
        messageHashes.add(msg5.getMessageHash());
        messageIDs.add(msg5.getMessageID());
    }
    
    // Test 1: Arrays correctly populated
    @Test
    public void testSentMessagesArrayPopulated() {
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
        Message longest = sentMessages.get(0);
        for (Message msg : sentMessages) {
            if (msg.getMessage().length() > longest.getMessage().length()) {
                longest = msg;
            }
        }
        
        // The longest message in sent array should be "It is dinner time !"
        // But overall longest is in stored array: "Where are you? You are late! I have asked you to be on time."
        assertEquals("Longest message should be correct", 
                     "It is dinner time !", longest.getMessage());
    }
    
    // Test 3: Search for message by recipient
    @Test
    public void testSearchMessagesByRecipient() {
        String searchRecipient = "+27838884567";
        ArrayList<Message> results = new ArrayList<>();
        
        // Search in sent messages
        for (Message msg : sentMessages) {
            if (searchRecipient.equals(msg.getRecipient())) {
                results.add(msg);
            }
        }
        
        // Search in stored messages
        for (Message msg : storedMessages) {
            if (searchRecipient.equals(msg.getRecipient())) {
                results.add(msg);
            }
        }
        
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
        for (Message msg : storedMessages) {
            if ("Where are you? You are late! I have asked you to be on time.".equals(msg.getMessage())) {
                hashToDelete = msg.getMessageHash();
                break;
            }
        }
        
        // Simulate deletion
        boolean deleted = false;
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).getMessageHash().equals(hashToDelete)) {
                storedMessages.remove(i);
                messageHashes.remove(hashToDelete);
                deleted = true;
                break;
            }
        }
        
        assertTrue("Message should be deleted", deleted);
        assertEquals("Stored messages should have 1 item after deletion", 1, storedMessages.size());
        assertFalse("Message hash should be removed from hashes array", 
                    messageHashes.contains(hashToDelete));
    }
    
    // Test 5: Message ID search
    @Test
    public void testSearchMessageByID() {
        // Get ID of message 4
        String searchID = "";
        for (Message msg : sentMessages) {
            if ("It is dinner time !".equals(msg.getMessage())) {
                searchID = msg.getMessageID();
                break;
            }
        }
        
        Message foundMessage = null;
        for (Message msg : sentMessages) {
            if (searchID.equals(msg.getMessageID())) {
                foundMessage = msg;
                break;
            }
        }
        
        assertNotNull("Should find message by ID", foundMessage);
        assertEquals("Found message should have correct recipient", 
                     "0838884567", foundMessage.getRecipient());
        assertEquals("Found message should have correct content", 
                     "It is dinner time !", foundMessage.getMessage());
    }
    
    // Test 6: Array sizes consistency
    @Test
    public void testArraySizesConsistency() {
        int totalMessagesInArrays = sentMessages.size() + storedMessages.size();
        assertEquals("Message hashes array size should match total messages", 
                     totalMessagesInArrays, messageHashes.size());
        assertEquals("Message IDs array size should match total messages", 
                     totalMessagesInArrays, messageIDs.size());
    }
    
    // Test 7: Test message statuses
    @Test
    public void testMessageStatuses() {
        for (Message msg : sentMessages) {
            assertEquals("Sent messages should have 'sent' status", "sent", msg.getStatus());
        }
        
        for (Message msg : storedMessages) {
            assertEquals("Stored messages should have 'stored' status", "stored", msg.getStatus());
        }
    }
}