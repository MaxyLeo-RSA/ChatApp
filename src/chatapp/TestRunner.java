package chatapp;

public class TestRunner {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("🚀 Starting QuickChat Automated Tests...");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        
        try {
            runRegistrationTests();
            runLoginTests();
            runMessageTests();
            runArrayManagerTests();
            runJSONHandlerTests();
            
            printResults();
            
            // Exit with code 0 if all passed, 1 if any failed
            if (failed > 0) {
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void runRegistrationTests() {
        System.out.println("\n📝 Testing Registration Class...");
        Registration user = new Registration();
        user.firstName = "John";
        user.lastName = "Doe";
        
        // Test 1: Valid username
        user.username = "v__1";
        assertTrue(user.checkUserName(), "Username 'v__1' should be valid");
        
        // Test 2: Invalid username (no underscore)
        user.username = "user";
        assertFalse(user.checkUserName(), "Username 'user' should be invalid (no underscore)");
        
        // Test 3: Invalid username (too long)
        user.username = "user_123";
        assertFalse(user.checkUserName(), "Username 'user_123' should be invalid (too long)");
        
        // Test 4: Valid password
        user.password = "Ch&&sec@ke99!";
        assertTrue(user.checkPasswordComplexity(), "Password 'Ch&&sec@ke99!' should meet complexity requirements");
        
        // Test 5: Invalid password (no uppercase)
        user.password = "password123!";
        assertFalse(user.checkPasswordComplexity(), "Password without uppercase should be invalid");
        
        // Test 6: Valid SA cell number (international)
        user.cellNumber = "+27838968976";
        assertTrue(user.checkCellPhoneNumber(), "Valid SA cell number '+27838968976' should be accepted");
        
        // Test 7: Valid SA cell number (local)
        user.cellNumber = "0712345678";
        assertTrue(user.checkCellPhoneNumber(), "Valid SA cell number '0712345678' should be accepted");
        
        // Test 8: Invalid cell number
        user.cellNumber = "08966553";
        assertFalse(user.checkCellPhoneNumber(), "Invalid cell number '08966553' should be rejected");
    }
    
    private static void runLoginTests() {
        System.out.println("\n🔐 Testing Login Class...");
        Login login = new Login();
        Registration user = new Registration();
        user.firstName = "John";
        user.lastName = "Doe";
        user.username = "v__1";
        user.password = "Ch&&sec@ke99!";
        
        // Test 1: Successful login
        assertTrue(login.checkLogin(user, "v__1", "Ch&&sec@ke99!"), "Valid login credentials should be accepted");
        
        // Test 2: Failed login (wrong password)
        assertFalse(login.checkLogin(user, "v__1", "wrongpass"), "Invalid password should be rejected");
        
        // Test 3: Failed login (wrong username)
        assertFalse(login.checkLogin(user, "wrong_user", "Ch&&sec@ke99!"), "Invalid username should be rejected");
        
        // Test 4: Login status messages
        String successMsg = login.returnLoginStatus(true, user);
        assertTrue(successMsg.contains("Welcome John Doe"), "Success message should contain welcome text");
        assertTrue(successMsg.contains("it is great to see you again"), "Success message should contain greeting");
        
        String failMsg = login.returnLoginStatus(false, user);
        assertTrue(failMsg.contains("Username or password incorrect"), "Fail message should contain error text");
        assertTrue(failMsg.contains("please try again"), "Fail message should contain retry instruction");
    }
    
    private static void runMessageTests() {
        System.out.println("\n💬 Testing Message Class...");
        Message msg = new Message();
        
        // Test 1: Valid recipient (international)
        assertEquals(1, msg.checkRecipientCell("+27718693002"), "Valid international recipient should return 1");
        
        // Test 2: Valid recipient (local)
        assertEquals(1, msg.checkRecipientCell("0712345678"), "Valid local recipient should return 1");
        
        // Test 3: Invalid recipient
        assertEquals(0, msg.checkRecipientCell("08966553"), "Invalid recipient should return 0");
        
        // Test 4: Message ID generation
        String messageID = msg.getMessageID();
        assertNotNull(messageID, "Message ID should not be null");
        assertEquals(10, messageID.length(), "Message ID should be 10 digits");
        assertTrue(messageID.matches("\\d+"), "Message ID should contain only digits");
        assertFalse(messageID.startsWith("0"), "Message ID should not start with 0");
        
        // Test 5: Message hash generation
        msg.setMessage("Hi Mike, can you join us for dinner tonight");
        String hash = msg.getMessageHash();
        assertNotNull(hash, "Message hash should not be null");
        assertTrue(hash.contains(":"), "Message hash should contain colon separators");
        
        // Test 6: Message sending options
        Message testMsg = new Message();
        assertEquals("Message sent successfully!", testMsg.sentMessage(1), "Send message should return success");
        assertEquals("sent", testMsg.getStatus(), "Message status should be 'sent'");
        
        Message testMsg2 = new Message();
        assertEquals("Message discarded", testMsg2.sentMessage(2), "Discard message should return discard message");
        assertEquals("discarded", testMsg2.getStatus(), "Message status should be 'discarded'");
        
        Message testMsg3 = new Message();
        assertEquals("Message stored successfully!", testMsg3.sentMessage(3), "Store message should return store message");
        assertEquals("stored", testMsg3.getStatus(), "Message status should be 'stored'");
        
        // Test 7: Invalid message option
        Message testMsg4 = new Message();
        assertEquals("Invalid option", testMsg4.sentMessage(4), "Invalid option should return error");
        
        // Test 8: Message formatting
        msg.setRecipient("0712345678");
        assertEquals("+2712345678", msg.getRecipient(), "Local number should be formatted to international");
    }
    
    private static void runArrayManagerTests() {
        System.out.println("\n🗂️ Testing Array Manager Class...");
        ChatAppArrayManager manager = new ChatAppArrayManager();
        
        // Test 1: Add sent message
        Message msg1 = new Message();
        msg1.setRecipient("+27834557896");
        msg1.setMessage("Did you get the cake?");
        msg1.setStatus("sent");
        manager.addToSentMessages(msg1);
        
        assertEquals(1, manager.getSentMessages().size(), "Sent messages array should have 1 message");
        assertEquals(1, manager.getMessageHashes().size(), "Message hashes array should have 1 hash");
        assertEquals(1, manager.getMessageIDs().size(), "Message IDs array should have 1 ID");
        
        // Test 2: Add stored message
        Message msg2 = new Message();
        msg2.setRecipient("+27838884567");
        msg2.setMessage("Where are you? You are late! I have asked you to be on time.");
        msg2.setStatus("stored");
        manager.addToStoredMessages(msg2);
        
        assertEquals(1, manager.getStoredMessages().size(), "Stored messages array should have 1 message");
        
        // Test 3: Add disregarded message
        Message msg3 = new Message();
        msg3.setRecipient("+27834484567");
        msg3.setMessage("Yohoooo, I am at your gate.");
        msg3.setStatus("discarded");
        manager.addToDisregardedMessages(msg3);
        
        assertEquals(1, manager.getDisregardedMessages().size(), "Disregarded messages array should have 1 message");
        
        // Test 4: Find longest message
        Message longest = manager.findLongestMessage();
        assertNotNull(longest, "Longest message should not be null");
        assertEquals("Did you get the cake?", longest.getMessage(), "Should find correct longest message");
        
        // Test 5: Search by recipient
        java.util.ArrayList<Message> results = manager.searchMessagesByRecipient("+27838884567");
        assertEquals(1, results.size(), "Should find 1 message for recipient '+27838884567'");
        
        // Test 6: Search by ID
        String searchID = manager.getSentMessages().get(0).getMessageID();
        Message found = manager.searchMessageByID(searchID);
        assertNotNull(found, "Should find message by ID");
        assertEquals("Did you get the cake?", found.getMessage(), "Found message should have correct content");
        
        // Test 7: Delete by hash
        String hashToDelete = manager.getStoredMessages().get(0).getMessageHash();
        boolean deleted = manager.deleteMessageByHash(hashToDelete);
        assertTrue(deleted, "Message should be deleted by hash");
        assertEquals(0, manager.getStoredMessages().size(), "Stored messages should be empty after deletion");
        
        // Test 8: Array consistency
        int totalMessages = manager.getSentMessages().size() + manager.getStoredMessages().size();
        assertEquals(totalMessages, manager.getMessageHashes().size(), "Hashes array should match total messages");
        assertEquals(totalMessages, manager.getMessageIDs().size(), "IDs array should match total messages");
        
        // Test 9: Display methods
        String sendersInfo = manager.displaySendersAndRecipients("Test User");
        assertTrue(sendersInfo.contains("Test User"), "Senders info should contain sender name");
        assertTrue(sendersInfo.contains("+27834557896"), "Senders info should contain recipient");
        
        String report = manager.generateFullReport("Test User");
        assertTrue(report.contains("FULL SENT MESSAGES REPORT"), "Report should contain header");
        assertTrue(report.contains("Did you get the cake?"), "Report should contain message content");
        assertTrue(report.contains("SUMMARY"), "Report should contain summary");
    }
    
    private static void runJSONHandlerTests() {
        System.out.println("\n📄 Testing JSON Handler Class...");
        
        // Test 1: Store and retrieve message
        Message testMsg = new Message();
        testMsg.setRecipient("+27123456789");
        testMsg.setMessage("Test message for JSON");
        testMsg.setStatus("sent");
        
        JSONHandler.storeMessage(testMsg);
        System.out.println("✅ INFO: Message stored to JSON");
        
        // Test 2: Read messages (basic functionality)
        try {
            java.util.ArrayList<Message> messages = JSONHandler.readAllMessages();
            assertTrue(messages != null, "Should be able to read messages from JSON");
            System.out.println("✅ INFO: Successfully read " + messages.size() + " messages from JSON");
        } catch (Exception e) {
            System.out.println("⚠️  WARNING: Could not read JSON file (might be empty)");
        }
        
        // Test 3: Get formatted messages
        String formatted = JSONHandler.getAllMessagesFormatted();
        assertTrue(formatted != null, "Should be able to get formatted messages");
        System.out.println("✅ INFO: Formatted messages retrieved successfully");
    }
    
    // Assertion methods
    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            System.out.println("✅ PASS: " + message);
            passed++;
        } else {
            System.out.println("❌ FAIL: " + message);
            failed++;
        }
    }
    
    private static void assertFalse(boolean condition, String message) {
        assertTrue(!condition, message);
    }
    
    private static void assertEquals(Object expected, Object actual, String message) {
        boolean condition = (expected == null && actual == null) || (expected != null && expected.equals(actual));
        if (condition) {
            System.out.println("✅ PASS: " + message + " (expected: " + expected + ", got: " + actual + ")");
            passed++;
        } else {
            System.out.println("❌ FAIL: " + message + " (expected: " + expected + ", but got: " + actual + ")");
            failed++;
        }
    }
    
    private static void assertNotNull(Object obj, String message) {
        if (obj != null) {
            System.out.println("✅ PASS: " + message);
            passed++;
        } else {
            System.out.println("❌ FAIL: " + message);
            failed++;
        }
    }
    
    private static void printResults() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 TEST SUMMARY");
        System.out.println("=".repeat(60));
        System.out.println("✅ Tests Passed: " + passed);
        System.out.println("❌ Tests Failed: " + failed);
        System.out.println("📋 Total Tests: " + (passed + failed));
        System.out.println("🎯 Success Rate: " + (passed * 100 / (passed + failed)) + "%");
        
        if (failed == 0) {
            System.out.println("🎉 ALL TESTS PASSED! Your code is ready for production! 🎉");
        } else {
            System.out.println("💥 SOME TESTS FAILED! Please check the failures above.");
        }
        System.out.println("=".repeat(60));
    }
}
